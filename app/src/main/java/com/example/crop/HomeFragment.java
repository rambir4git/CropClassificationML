package com.example.crop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crop.POJOs.HomeCardsAdapter;
import com.example.crop.POJOs.SavedImages;
import com.example.crop.SupportLibrary.ReverseLineInputStream;
import com.google.android.gms.common.util.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private static RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private List<SavedImages> savedImagesList;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.fragment_home_recyclerview);
        recyclerView.setHasFixedSize(true);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        savedImagesList = new ArrayList<>();

        final HomeCardsAdapter.ButtonClickListener bListener = new HomeCardsAdapter.ButtonClickListener() {
            @Override
            public void onButtonClicked(int id) {
                if (id == HomeCardsAdapter.ButtonClickListener.CAMERABUTTON) {
                    Intent intent = new Intent(getActivity(), CameraActivity.class);
                    startActivity(intent);
                }
                if (id == HomeCardsAdapter.ButtonClickListener.GALLERYBUTTON) {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    intent.putExtra("return-data", true);
                    startActivityForResult(intent, 1);
                }
            }
        };
        HomeCardsAdapter.ClickListener mlistener = new HomeCardsAdapter.ClickListener() {

            @Override
            public void onItemClicked(SavedImages savedImage) {
                Bundle args = new Bundle();
                args.putString("CROPTYPE", savedImage.getCropType());
                changeFragment(args);
            }
        };
        adapter = new HomeCardsAdapter(savedImagesList, mlistener, bListener);
        recyclerView.setAdapter(adapter);

        File directory = new File(getActivity().getExternalFilesDir(null).toString());
        File[] filesList = directory.listFiles();
        if (filesList.length >= 2) {
            new loadSavedImages().execute(filesList);
        } else {
            Toast.makeText(getActivity(), "No images yet !!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(data.getData());
                byte[] imageData = IOUtils.toByteArray(inputStream);
                Bundle args = new Bundle();
                args.putByteArray("IMAGE", imageData);
                changeFragment(args);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {

            }
        }
    }

    void changeFragment(Bundle args) {
        CropInfoFragment cropInfoFragment = new CropInfoFragment();
        cropInfoFragment.setArguments(args);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_home, cropInfoFragment).addToBackStack(null);
        fragmentTransaction.commit();
    }

    private Bitmap decodeFile(File f) throws IOException {
        Bitmap b = null;

        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        FileInputStream fis = new FileInputStream(f);
        BitmapFactory.decodeStream(fis, null, o);
        fis.close();

        int scale = 1;
        if (o.outHeight > 400 || o.outWidth > 600) {
            scale = (int) Math.pow(2, (int) Math.ceil(Math.log(600 /
                    (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
        }

        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        fis = new FileInputStream(f);
        b = BitmapFactory.decodeStream(fis, null, o2);
        fis.close();

        return b;
    }

    private class loadSavedImages extends AsyncTask<File[], Void, Void> {

        @Override
        protected Void doInBackground(File[]... files) {
            BufferedReader reader = null;
            File informationFile = files[0][0];
            try {
                reader = new BufferedReader(new InputStreamReader(new ReverseLineInputStream(informationFile)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            for (int i = files[0].length - 1; i > 0; i--) {
                if (!files[0][i].getName().equals("EXPORT.ZIP")) {
                    try {
                        reader.readLine();//skip blank line
                        String crop = reader.readLine();
                        String location = reader.readLine();
                        String date = reader.readLine();
                        reader.readLine();//skip pic id
                        Bitmap bitmap = decodeFile(files[0][i]);
                        savedImagesList.add(new SavedImages(location, crop, date, bitmap));
                        publishProgress();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            adapter.notifyDataSetChanged();
        }
    }

}
