package com.example.crop;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.crop.POJOs.GalleryAdapter;
import com.example.crop.POJOs.SavedImages;
import com.example.crop.SupportLibrary.ReverseLineInputStream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends Fragment {

    private static RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private List<SavedImages> savedImagesList;

    public GalleryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        savedImagesList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.galleryRecyclerView);
        recyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        GalleryAdapter.ClickListener mlistener = new GalleryAdapter.ClickListener() {
            @Override
            public void onItemClicked(SavedImages savedImage) {
                Bundle args = new Bundle();
                args.putString("CROPTYPE", savedImage.getCropType());
                CropInfoFragment cropInfoFragment = new CropInfoFragment();
                cropInfoFragment.setArguments(args);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.gallery_fragment, cropInfoFragment).addToBackStack(null);
                fragmentTransaction.commit();
            }
        };
        adapter = new GalleryAdapter(savedImagesList, mlistener);
        recyclerView.setAdapter(adapter);


        File directory = new File(getActivity().getExternalFilesDir(null).toString());
        File[] filesList = directory.listFiles();
        if (filesList.length >= 2) {
            new GalleryFragment.loadSavedImages().execute(filesList);
        } else {
            Toast.makeText(getActivity(), "No images yet !!", Toast.LENGTH_SHORT).show();
        }
        return view;
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
        if (o.outHeight > 1000 || o.outWidth > 1000) {
            scale = (int) Math.pow(2, (int) Math.ceil(Math.log(1000 /
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
