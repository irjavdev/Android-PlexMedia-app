package com.vacuum.app.cinema.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vacuum.app.cinema.Activities.WatchActivity;
import com.vacuum.app.cinema.Adapter.CreditsAdapter;
import com.vacuum.app.cinema.Adapter.CrewAdapter;
import com.vacuum.app.cinema.Adapter.ImagesAdapter;
import com.vacuum.app.cinema.Adapter.TrailerAdapter;
import com.vacuum.app.cinema.Model.Backdrop;
import com.vacuum.app.cinema.Model.Cast;
import com.vacuum.app.cinema.Model.Credits;
import com.vacuum.app.cinema.Model.Crew;
import com.vacuum.app.cinema.Model.Genre;
import com.vacuum.app.cinema.Model.Images_tmdb;
import com.vacuum.app.cinema.Model.Movie;
import com.vacuum.app.cinema.Model.MovieDetails;
import com.vacuum.app.cinema.Model.Poster;
import com.vacuum.app.cinema.Model.Trailer;
import com.vacuum.app.cinema.Model.TrailerResponse;
import com.vacuum.app.cinema.R;
import com.vacuum.app.cinema.Utility.ApiClient;
import com.vacuum.app.cinema.Utility.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Home on 3/3/2018.
 */

public class DetailsMovie_Fragment extends Fragment implements View.OnClickListener{

    private static boolean LIKE = true;
    public static final String TAG_DetailsMovie_Fragment = "TAG_DetailsMovie_Fragment";

    TextView title,year,rating,overview,runtime,voteCount,genre1,genre2,genre3,id_number;
    ImageView cover;
    Context mContext;
    RecyclerView recyclerView_trailers,recyclerView_actors,recyclerView_crew,recyclerView_images;
    RelativeLayout layout_genre1,layout_genre2,layout_genre3;
    LinearLayout trailer_layout,actors_layout,crew_layout,image_layout;
    RatingBar ratingBar;
    ImageView like,watch;
    int x;
    Movie movie;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.details_movie_fragment_layout, container, false);

        mContext = this.getActivity();

        title = view.findViewById(R.id.title);
        year = view.findViewById(R.id.year);
        rating = view.findViewById(R.id.rating);
        overview = view.findViewById(R.id.overview);
        runtime = view.findViewById(R.id.runtime);
        cover = view.findViewById(R.id.cover);
        ratingBar = view.findViewById(R.id.ratingBar);
        voteCount = view.findViewById(R.id.voteCount);
        genre1 = view.findViewById(R.id.genre1);
        genre2 = view.findViewById(R.id.genre2);
        genre3 = view.findViewById(R.id.genre3);
        id_number = view.findViewById(R.id.id_number);
        layout_genre1 = view.findViewById(R.id.layout_genre1);
        layout_genre2 = view.findViewById(R.id.layout_genre2);
        layout_genre3 = view.findViewById(R.id.layout_genre3);
        like = view.findViewById(R.id.like);
        watch = view.findViewById(R.id.watch);
        trailer_layout = view.findViewById(R.id.trailer_layout);
        actors_layout = view.findViewById(R.id.actors_layout);
        image_layout = view.findViewById(R.id.image_layout);

        recyclerView_trailers = view.findViewById(R.id.recyclerView_trailers);
        recyclerView_actors = view.findViewById(R.id.recyclerView_actors);
        recyclerView_images = view.findViewById(R.id.recyclerView_images);
        recyclerView_crew = view.findViewById(R.id.recyclerView_crew);
        crew_layout = view.findViewById(R.id.crew_layout);
        like.setOnClickListener(this);
        watch.setOnClickListener(this);



        movie = (Movie)getArguments().getSerializable("movie");



        try{
            title.setText(movie.getOriginalTitle());
            year.setText(movie.getReleaseDate().substring(0, 4));
            rating.setText(movie.getVoteAverage().toString());
            overview.setText(movie.getOverview());
            ratingBar.setRating(movie.getVoteAverage().floatValue()/2);
            voteCount.setText(movie.getVoteCount().toString());
            x= movie.getId();
            id_number.setText("id:" + String.valueOf(x));

            Glide.with(this)
                    .load("http://image.tmdb.org/t/p/w500"+movie.getBackdropPath().toString())
                    .into(cover);
        }catch (Exception e){
            Log.i("TAG :",e.toString());
        }



        retrofit();
        return view;
    }

    private void retrofit() {
        String API_KEY = getResources().getString(R.string.TMBDB_API_KEY);
        ApiInterface apiService =
                ApiClient.getClient(mContext).create(ApiInterface.class);

        Call<MovieDetails> call_details = apiService.getMovieDetails(x,API_KEY);
        call_details.enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                MovieDetails m = response.body();


                    try{
                        int t = m.getRuntime();
                        int hours = t / 60; //since both are ints, you get an int
                        int minutes = t % 60;
                        System.out.printf("%d:%02d", hours, minutes);
                        runtime.setText(String.valueOf(hours)+"h "+String.valueOf(minutes)+"min");
                        setgenre(m.getGenres(),m.getGenres().size());
                    }catch (Exception e){
                        Log.i("TAG :",e.toString());
                    }




                //============================++++++++++++++++++++++++++



            }

            @Override
            public void onFailure(Call<MovieDetails> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
            }
        });
        //setRecyclerView_images
        //==================================================================
        Call<Images_tmdb> call_RecyclerView_images = apiService.getImages("movie",x,API_KEY);
        call_RecyclerView_images.enqueue(new Callback<Images_tmdb>() {
            @Override
            public void onResponse(Call<Images_tmdb> call, Response<Images_tmdb> response) {
                //List<Poster> posters;
                List<Backdrop> posters;
                posters = response.body().getBackdrops();
                image_layout.setVisibility(View.VISIBLE);
                recyclerView_images.setLayoutManager(new LinearLayoutManager(mContext,
                        LinearLayoutManager.HORIZONTAL, false));
                recyclerView_images.setAdapter(new ImagesAdapter(posters, mContext));

            }

            @Override
            public void onFailure(Call<Images_tmdb> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
            }
        });
        //setRecyclerView_trailers
        //==================================================================
        Call<TrailerResponse> call_RecyclerView_trailers = apiService.gettrailers("movie",x,API_KEY);
        call_RecyclerView_trailers.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                List<Trailer> trailers = null;
                try {
                    trailers = response.body().getResults();
                    if (response.body().getResults() != null)
                    {
                        trailer_layout.setVisibility(View.VISIBLE);
                        recyclerView_trailers.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                        recyclerView_trailers.setAdapter(new TrailerAdapter(trailers, mContext));


                    }
                }catch (Exception e ){
                    Log.e("Exception::",e.toString());
                }
            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
            }
        });

        //setRecyclerView_actors
        //==================================================================
        Call<Credits> call_recyclerView_actors = apiService.getMovieCredits(x,API_KEY);
        call_recyclerView_actors.enqueue(new Callback<Credits>() {
            @Override
            public void onResponse(Call<Credits> call, Response<Credits> response) {
                List<Cast> casts = null;
                try {
                    casts = response.body().getCast();
                    if (casts != null)
                    {
                        actors_layout.setVisibility(View.VISIBLE);
                        recyclerView_actors.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                        recyclerView_actors.setAdapter(new CreditsAdapter(casts, mContext));

                    }
                }catch (Exception e ){
                    Log.e("Exception::>>",e.toString());
                }
            }

            @Override
            public void onFailure(Call<Credits> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
            }
        });


        //setRecyclerView_actors
        //==================================================================
        Call<Credits> call_recyclerView_crew = apiService.getMovieCredits(x,API_KEY);
        call_recyclerView_crew.enqueue(new Callback<Credits>() {
            @Override
            public void onResponse(Call<Credits> call, Response<Credits> response) {
                List<Crew> crews = null;
                try {
                    crews = response.body().getCrew();
                    if (response.body().getCrew() != null)
                    {
                        crew_layout.setVisibility(View.VISIBLE);
                        recyclerView_crew.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                        recyclerView_crew.setAdapter(new CrewAdapter(crews, mContext));

                    }
                }catch (Exception e ){
                    Log.e("Exception::",e.toString());
                }
            }

            @Override
            public void onFailure(Call<Credits> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
            }
        });

    }

    public void setgenre(List<Genre> names, int size){

        //Log.e("size",String.valueOf(size));

        if (size==1){
            genre1.setText(String.valueOf(names.get(0).getName()));
            layout_genre1.setVisibility(View.VISIBLE);

        }else if(size ==2){
            genre1.setText(String.valueOf(names.get(0).getName()));
            layout_genre1.setVisibility(View.VISIBLE);
            genre2.setText(String.valueOf(names.get(1).getName()));
            layout_genre2.setVisibility(View.VISIBLE);

        }else if(size >= 3) {
            genre1.setText(String.valueOf(names.get(0).getName()));
            layout_genre1.setVisibility(View.VISIBLE);
            genre2.setText(String.valueOf(names.get(1).getName()));
            layout_genre2.setVisibility(View.VISIBLE);
            genre3.setText(String.valueOf(names.get(2).getName()));
            layout_genre3.setVisibility(View.VISIBLE);
        }


    }





    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.like:
                if (LIKE){
                    like.setImageResource(R.drawable.if_heart_119_111093);
                    LIKE = false;
                }else {
                    like.setImageResource(R.drawable.if_heart_1814104);
                    LIKE = true;
                }
                    break;
            case  R.id.watch:
                watchActivity();
                break;
        }
    }



    private void watchActivity() {
        String url = "";
        String openload_thumbnail_url = "";
        String title = "";
        String intArray[] = {url,title,openload_thumbnail_url};
        Intent inent = new Intent(mContext, WatchActivity.class);
        inent.putExtra("url", intArray);
        mContext.startActivity(inent);
    }
}
