package com.example.crop.SupportLibrary;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.custom.FirebaseCustomLocalModel;
import com.google.firebase.ml.custom.FirebaseModelDataType;
import com.google.firebase.ml.custom.FirebaseModelInputOutputOptions;
import com.google.firebase.ml.custom.FirebaseModelInputs;
import com.google.firebase.ml.custom.FirebaseModelInterpreter;
import com.google.firebase.ml.custom.FirebaseModelInterpreterOptions;
import com.google.firebase.ml.custom.FirebaseModelOutputs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import im.delight.android.location.SimpleLocation;

public class HeavyLifter {
    private final int inputImageSize = 299;
    private final int classifications = 9;
    private final int imageChannels = 3;
    private final int batchSize = 1;
    private Context context;
    private AlertDialog alertDialog;
    private SimpleLocation location;
    private JSONObject labels;
    private FirebaseModelInterpreter interpreter;
    private FirebaseModelInputOutputOptions inputOutputOptions;
    private byte[] imageData;
    private int rotation;
    private String outputResult;
    private CompletionListener completionListener;
    private float[][][][] input;

    public HeavyLifter(Context context, CompletionListener listener, byte[] imageData, int rotation) {
        this.context = context;
        this.completionListener = listener;
        this.imageData = imageData;
        this.rotation = rotation;
        new SaveFile().execute();
        new HandleModel().execute();
    }

    void startLocation() {
        location = new SimpleLocation(context);
        if (!location.hasLocationEnabled()) {
            Toast.makeText(context, "Turn on the Location !!", Toast.LENGTH_SHORT).show();
            SimpleLocation.openSettings(context);
        }
        location.beginUpdates();
    }

    void getLabels() {
        String line = "";
        String results = "";
        try {
            InputStream inputStream = context.getAssets().open("crop_labels.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            while (line != null) {
                results += line;
                line = reader.readLine();
            }
            labels = new JSONObject(results);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    void initializeModel() {
        FirebaseCustomLocalModel localModel = new FirebaseCustomLocalModel.Builder()
                .setAssetFilePath("crop_model.tflite")
                .build();
        FirebaseModelInterpreterOptions options =
                new FirebaseModelInterpreterOptions.Builder(localModel).build();
        try {
            interpreter = FirebaseModelInterpreter.getInstance(options);
            inputOutputOptions =
                    new FirebaseModelInputOutputOptions.Builder()
                            .setInputFormat(0, FirebaseModelDataType.FLOAT32, new int[]{batchSize, inputImageSize, inputImageSize, imageChannels})
                            .setOutputFormat(0, FirebaseModelDataType.FLOAT32, new int[]{batchSize, classifications})
                            .build();
        } catch (FirebaseMLException e) {
            e.printStackTrace();
        }
    }

    void normalizeInput() {
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        bitmap = Bitmap.createScaledBitmap(bitmap, inputImageSize, inputImageSize, true);
        int batchNum = 0; //single image index
        input = new float[batchSize][inputImageSize][inputImageSize][imageChannels];
        for (int x = 0; x < inputImageSize; x++) {
            for (int y = 0; y < inputImageSize; y++) {
                int pixel = bitmap.getPixel(x, y);
                // Normalize channel values to [0.0f, 1.0f]
                input[batchNum][x][y][0] = (Color.red(pixel)) / 255.0f;
                input[batchNum][x][y][1] = (Color.green(pixel)) / 255.0f;
                input[batchNum][x][y][2] = (Color.blue(pixel)) / 255.0f;
            }
        }
    }

    void startInference(float[][][][] input) {
        final File textFile = new File(context.getExternalFilesDir(null), "INFORMATION.txt");
        FirebaseModelInputs inputs = null;
        try {
            inputs = new FirebaseModelInputs.Builder()
                    .add(input)  // add() as many input arrays as your model requires
                    .build();

        } catch (FirebaseMLException e) {
            e.printStackTrace();
        }
        interpreter.run(inputs, inputOutputOptions)
                .addOnSuccessListener(
                        new OnSuccessListener<FirebaseModelOutputs>() {
                            @Override
                            public void onSuccess(FirebaseModelOutputs result) {
                                float[][] output = result.getOutput(0);
                                int maxAt = 0;
                                for (int i = 1; i < classifications; i++) {
                                    maxAt = output[0][i] > output[0][maxAt] ? i : maxAt;
                                }
                                try {
                                    outputResult = labels.getString(String.valueOf(maxAt));
                                    if (output[0][maxAt] < 0.3) {
                                        outputResult = "Low Confidence";
                                        Toast.makeText(context, "Confidence is too low on this inference :(", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "Confidence > " + (int) (output[0][maxAt] * 100) + "% for this inference ;)", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.d("HeavyLifter", "onSuccess: Inferences were successful !!");
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                outputResult = "Unknown";
                                Toast.makeText(context, "Inference was unsuccessful !!", Toast.LENGTH_SHORT).show();
                                Log.d("HeavyLifter", "onFailure: Inferences failed !!");
                            }
                        })
                .addOnCompleteListener(
                        new OnCompleteListener<FirebaseModelOutputs>() {
                            @Override
                            public void onComplete(@NonNull Task<FirebaseModelOutputs> task) {
                                try {
                                    OutputStream outputStream = new FileOutputStream(textFile, true);
                                    OutputStreamWriter writer = new OutputStreamWriter(outputStream);
                                    writer.write(outputResult + "\n\n");
                                    writer.flush();
                                    writer.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                alertDialog.dismiss();
                                completionListener.onCompletion(outputResult);
                            }
                        }
                );
    }

    public interface CompletionListener {
        void onCompletion(String outputResult);
    }

    public class SaveFile extends AsyncTask<Void, Void, File> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startLocation();
        }

        @Override
        protected File doInBackground(Void... voids) {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());

            File imageFile = null;
            try {
                List<Address> list = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

            String imageName = "PIC_" + Calendar.getInstance().getTimeInMillis() + ".PNG";
            /*String textFileData = "ID: " + imageName +
                    "\n" + Calendar.getInstance().getTime().toString() +
                    "\nLocation: " + list.get(0).getSubLocality() + "\n";*/
            imageFile = new File(context.getExternalFilesDir(null), imageName);
                File textFile = new File(context.getExternalFilesDir(null), "INFORMATION.TXT");

                OutputStream outputStream = new FileOutputStream(textFile, true);
                OutputStreamWriter writer = new OutputStreamWriter(outputStream);
                //writer.write(textFileData);
                writer.flush();
                writer.close();

                Matrix matrix = new Matrix();
                matrix.preRotate(rotation);
                Bitmap bitmap = decodeByteArray(imageData);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return imageFile;
        }

        private Bitmap decodeByteArray(byte[] data) {
            Bitmap b = null;

            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(data, 0, data.length, o);

            int scale = 1;
            if (o.outHeight > 400 || o.outWidth > 600) {
                scale = (int) Math.pow(2, (int) Math.ceil(Math.log(600 /
                        (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return b = BitmapFactory.decodeByteArray(data, 0, data.length, o2);
        }
    }

    public class HandleModel extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog = new SpotsDialog.Builder()
                    .setContext(context)
                    .setMessage("Processing...")
                    .setCancelable(false)
                    .build();
            alertDialog.show();
            getLabels();
            initializeModel();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            normalizeInput();
            startInference(input);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
