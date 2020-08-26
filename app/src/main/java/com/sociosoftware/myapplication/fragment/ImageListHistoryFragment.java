package com.sociosoftware.myapplication.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.Volley;
import com.sociosoftware.myapplication.R;
import com.sociosoftware.myapplication.adapter.ImageListHistoryRecyclerViewAdapter;
import com.sociosoftware.myapplication.adapter.RecyclerViewAdapter;
import com.sociosoftware.myapplication.model.ImageModel;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ImageListHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageListHistoryFragment extends Fragment {

    Realm realm;

    private RecyclerView recyclerView;
    private ImageListHistoryRecyclerViewAdapter imageListHistoryRecyclerViewAdapter;
    private ArrayList<ImageModel> imageList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ImageListHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ImageHistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ImageListHistoryFragment newInstance(String param1, String param2) {
        ImageListHistoryFragment fragment = new ImageListHistoryFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_image_list_history, container, false);

        recyclerView = view.findViewById(R.id.image_list_history_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        imageList = new ArrayList<>();

        RealmQuery<ImageModel> imageModelRealmQuery = realm.where(ImageModel.class);
        RealmResults<ImageModel> imageModelRealmResults = imageModelRealmQuery.findAll();

        for(int i = 0; i < imageModelRealmResults.size(); i++){
            String imageUrl = imageModelRealmResults.get(i).getImageUrl();
            String date = imageModelRealmResults.get(i).getCurrentDate();
            String time = imageModelRealmResults.get(i).getCurrentTime();

            imageList.add(new ImageModel(imageUrl, date, time));
        }

        imageListHistoryRecyclerViewAdapter = new ImageListHistoryRecyclerViewAdapter(getContext(), imageList);
        recyclerView.setAdapter(imageListHistoryRecyclerViewAdapter);

        // Inflate the layout for this fragment
        return view;
    }
}