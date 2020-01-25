package com.example.crop.SupportLibrary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.crop.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import dmax.dialog.SpotsDialog;

public class ExportToEmail extends AsyncTask<Void, Void, File> {
    AlertDialog alertDialog;
    private Context context;

    public ExportToEmail(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        alertDialog = new SpotsDialog.Builder()
                .setContext(context)
                .setMessage("Processing file to export...")
                .setCancelable(false)
                .build();
        alertDialog.show();
    }

    @Override
    protected File doInBackground(Void... voids) {
        File exportFile = new File(context.getExternalFilesDir(null), "EXPORT.ZIP");
        try {
            FileOutputStream fos = new FileOutputStream(exportFile);
            ZipOutputStream zos = new ZipOutputStream(fos);
            File[] srcFiles = new File(context.getExternalFilesDir(null).toString()).listFiles();
            for (int i = 0; i < srcFiles.length; i++) {
                byte[] buffer = new byte[1024];
                if (!srcFiles[i].getName().equals("EXPORT.ZIP")) {
                    FileInputStream fis = new FileInputStream(srcFiles[i]);
                    zos.putNextEntry(new ZipEntry(srcFiles[i].getName()));
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }
                    zos.closeEntry();
                    fis.close();
                }
            }
            zos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exportFile;
    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        alertDialog.dismiss();
        Intent email = new Intent(Intent.ACTION_SEND)
                .setType("vnd.android.cursor.dir/email")
                .putExtra(Intent.EXTRA_EMAIL, new String[]{context.getResources().getString(R.string.app_name) + "@gmail.com"})
                .putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file))
                .putExtra(Intent.EXTRA_SUBJECT, "EXPORT FILES");
        context.startActivity(Intent.createChooser(email, "pick your email provider"));
    }
}
