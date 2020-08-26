package com.sociosoftware.myapplication.fragment;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sociosoftware.myapplication.R;
import com.sociosoftware.myapplication.adapter.RecyclerViewAdapter;
import com.sociosoftware.myapplication.model.ImageModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import io.realm.Realm;

import static android.bluetooth.BluetoothGattDescriptor.PERMISSION_WRITE;
import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ImageListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageListFragment extends Fragment implements RecyclerViewAdapter.OnItemClickListener {

    Realm realm;
    long nextId;
    String currentDate;
    String currentTime;

    ImageModel clickedImage;

    static final int MINIMUM_NUMBER = 3;
    static final int MAXIMUM_NUMBER = 200;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<ImageModel> imageList;
    private RequestQueue requestQueue;

    private static String[] animals = { "dog", "cat", "bird", "cow", "horse", "sheep", "monkey"};
    private static String[] colors = { "green", "white", "black", "red", "yellow", "blue", "brown" };

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ImageListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ImageListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ImageListFragment newInstance(String param1, String param2) {
        ImageListFragment fragment = new ImageListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Realm.init(getContext());
        realm = Realm.getDefaultInstance();

        Context context = getActivity();
        CharSequence text = "ImageList!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_image_list, container, false);

        recyclerView = view.findViewById(R.id.image_list_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        imageList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(view.getContext());
        parseJson();

        // Inflate the layout for this fragment
        return view;
    }

    private void parseJson() {

        String randomAnimals = animals[(int) (Math.random() * animals.length)];
        String randomColors = colors[(int) (Math.random() * colors.length)];

        String url = "https://pixabay.com/api/?key=18029780-76f13c70d48da493923640ff9&q=" + randomAnimals + " " + randomColors + "&image_type=photo&pretty=true&per_page=200";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("hits");

                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject hit = jsonArray.getJSONObject(i);
                        String imageURL = hit.getString("webformatURL");
                        imageList.add(new ImageModel(imageURL));
                    }

                    recyclerViewAdapter = new RecyclerViewAdapter(getContext(), imageList, ImageListFragment.this);
                    recyclerView.setAdapter(recyclerViewAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);
    }

    //runtime storage permission
    public boolean checkPermission() {
        int READ_EXTERNAL_PERMISSION = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if((READ_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_WRITE);
            return false;
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==PERMISSION_WRITE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //do somethings
        }
    }

    @Override
    public void onItemClick(int position) {

        clickedImage = imageList.get(position);

        Log.d(TAG, "onItemClick: " + clickedImage.getImageUrl());

        currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        currentTime = new SimpleDateFormat("hh:mm aa", Locale.getDefault()).format(new Date());

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                Number current_id = realm.where(ImageModel.class).max("id");
                if (current_id == null) {
                    nextId = 1;
                }
                else {
                    nextId = current_id.intValue() + 1;
                }

                ImageModel imageModel = realm.createObject(ImageModel.class, nextId);
                imageModel.setCurrentDate(currentDate);
                imageModel.setCurrentTime(currentTime);
                imageModel.setImageUrl(clickedImage.getImageUrl());

            }
        }, new Realm.Transaction.OnSuccess() {

            @Override
            public void onSuccess() {
                Context context = getActivity();
                CharSequence text = "Saved Successfuly!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }, new Realm.Transaction.OnError() {

            @Override
            public void onError(Throwable error) {
                Context context = getActivity();
                CharSequence text = "Saving failed!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

        ContextWrapper cw = new ContextWrapper(getContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File myImageFile = new File(directory, "my_image.jpeg");
        if (myImageFile.delete()) Log.d("picassoImageTarget", " image on the disk deleted successfully!");

        Picasso.get().load(clickedImage.getImageUrl()).into(picassoImageTarget(getContext(), "imageDir", "my_image.jpeg"));

    }

    private Target picassoImageTarget(Context context, final String imageDir, final String imageName) {

        Log.d("picassoImageTarget", " picassoImageTarget");

        ContextWrapper cw = new ContextWrapper(context);
        final File directory = cw.getDir(imageDir, Context.MODE_PRIVATE); // path to /data/data/yourapp/app_imageDir
        return new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final File myImageFile = new File(directory, imageName); // Create image file
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(myImageFile);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.i("image", "image saved to >>>" + myImageFile.getAbsolutePath());

                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                if (placeHolderDrawable != null) {}
            }
        };
    }


}