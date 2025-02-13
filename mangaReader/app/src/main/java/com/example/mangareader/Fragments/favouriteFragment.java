package com.example.mangareader.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.mangareader.ConnectionManager;
import com.example.mangareader.Models.comic;
import com.example.mangareader.R;
import com.example.mangareader.Adapters.RecyclerViewAdapter;
import com.example.mangareader.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.mangareader.Constants.main_path;
import static com.example.mangareader.Constants.url_get_favourite_manga;

public class favouriteFragment extends Fragment {


    String static_url = main_path;
    ImageView conan_image;
    TextView conan_text;
    Context thisContext;
    List<comic> lstComic;
    RecyclerView myrecyclerview;
    RecyclerViewAdapter myAdapter;
    SessionManager sessionManager;
    Toolbar fav_Toolbar;
    Context contextThemeWrapper;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        thisContext = container.getContext();
        sessionManager = new SessionManager(getContext());
        if (sessionManager.loadNightModeState()==true){
            contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.darktheme);
//            container.setTheme(R.style.darktheme);
        }
        else{
            contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.AppTheme);
//            setTheme(R.style.AppTheme);
        }

        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

        View view = localInflater.inflate(R.layout.fragment_favourite, container, false);

        myrecyclerview = view.findViewById(R.id.recyclerId);
        conan_image = view.findViewById(R.id.conan_image);
        conan_text = view.findViewById(R.id.conan_text);

        if (!sessionManager.isLoggin()){

            myrecyclerview.setVisibility(View.GONE);
            conan_image.setVisibility(View.VISIBLE);
            conan_text.setVisibility(View.VISIBLE);

            conan_text.setText("You need to Login to save mangas to favourite.\n No Favourites is added");

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("OOPS!!!")
                    .setMessage("You need to Login to Put your mangas in to favourite and to see them")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                            write code if you want to do anything after clicking OK button
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

//        View view = inflater.inflate(R.layout.fragment_favourite, container, false);

//        fav_Toolbar = view.findViewById(R.id.fav_Toolbar);
//        fav_Toolbar.setTitle("Favourites");


        HashMap<String, String> user = sessionManager.getUserDetails();
        String email = user.get(sessionManager.EMAIL);

        lstComic = new ArrayList<>();
//        lstComic.add(new comic("One Piece", "Category Comic", "Description Comic", R.drawable.one_piece));
//        lstComic.add(new comic("One Piece", "Category Comic", "Description Comic", R.drawable.one_piece));
//        lstComic.add(new comic("One Piece", "Category Comic", "Description Comic", R.drawable.one_piece));
//        lstComic.add(new comic("One Piece", "Category Comic", "Description Comic", R.drawable.one_piece));
//        lstComic.add(new comic("One Piece", "Category Comic", "Description Comic", R.drawable.one_piece));
//        lstComic.add(new comic("One Piece", "Category Comic", "Description Comic", R.drawable.one_piece));
//        lstComic.add(new comic("One Piece", "Category Comic", "Description Comic", R.drawable.one_piece));
//        lstComic.add(new comic("One Piece", "Category Comic", "Description Comic", R.drawable.one_piece));
//        lstComic.add(new comic("One Piece", "Category Comic", "Description Comic", R.drawable.one_piece));
//        lstComic.add(new comic("One Piece", "Category Comic", "Description Comic", R.drawable.one_piece));
//        lstComic.add(new comic("One Piece", "Category Comic", "Description Comic", R.drawable.one_piece));
//        lstComic.add(new comic("One Piece", "Category Comic", "Description Comic", R.drawable.one_piece));

        showFavouriteManga(email);

        myAdapter = new RecyclerViewAdapter(getActivity(), lstComic);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);
//        LinearLayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);
        myrecyclerview.setLayoutManager(manager);
        myrecyclerview.setAdapter(myAdapter);


        return view;
    }

    private void showFavouriteManga(String email){

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        JSONObject jsonObject = new JSONObject();

        try{
            jsonObject.put("email_id", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ConnectionManager.sendData(jsonObject.toString(), requestQueue, url_get_favourite_manga, new ConnectionManager.VolleyCallback() {
            @Override
            public void onSuccessResponse(String response) {
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    String success = jsonObject1.getString("success");
                    JSONArray jsonArray = jsonObject1.getJSONArray("favourites");

                    if (success.equals("true")){

//                        Don't put toast at run time because if user moves to fast then the app will crash

//                        Toast.makeText(getContext(),"Favourites Mangas", Toast.LENGTH_SHORT).show();

                        if (jsonArray.length() == 0){
                            conan_image.setVisibility(View.VISIBLE);
                            conan_text.setVisibility(View.VISIBLE);
                            myrecyclerview.setVisibility(View.GONE);

                            conan_text.setText("No manga is added to favourites!!!");
                        }
                        else {

                            conan_text.setVisibility(View.GONE);
                            conan_image.setVisibility(View.GONE);
                            myrecyclerview.setVisibility(View.VISIBLE);
                            
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                                String title = jsonObject2.getString("title");
                                String cover_photo = jsonObject2.getString("cover_picture");
                                String description = jsonObject2.getString("description");
                                String manga_id = jsonObject2.getString("manga_id");
                                String favourite = "TRUE";

                                String thumbnail = static_url + cover_photo;

                                lstComic.add(new comic(title, "Fun", description, thumbnail, manga_id, favourite));

                            }

//                        This is to set the adapter such that it notifies every time the data is changed. If not written then the data would not show.
                            myAdapter.notifyDataSetChanged();

                        }

                    }

                } catch (JSONException e) {

//                    Maybe by adding below line i.e e.printStackTrace(); will cause application to crash if this get executed.
//                    e.printStackTrace();
                    System.out.println(e.toString());
                    Toast.makeText(getContext(), "Mangas not Found : " + e, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

//                Toast.makeText(getContext(), "Manga Loading Error : "+error, Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(getContext())
                        .setTitle("Server error!")
                        .setMessage("Some issues with server has occurred, Please try again later.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).show();

            }
        });


    }

}
