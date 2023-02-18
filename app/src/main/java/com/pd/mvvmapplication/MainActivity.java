package com.pd.mvvmapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.pd.mvvmapplication.Adapter.ActorAdapter;
import com.pd.mvvmapplication.Model.Actor;
import com.pd.mvvmapplication.Network.Api;
import com.pd.mvvmapplication.Repository.ActorRepository;
import com.pd.mvvmapplication.ViewModel.ActorViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ActorViewModel actorViewModel;
    private static final String URL_DATA = "http://codingwithjks.tech/";
    private RecyclerView recyclerView;
    private List<Actor> actorList;
    private ActorRepository actorRepository;
    private ActorAdapter actorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        actorRepository = new ActorRepository(getApplication());

        actorList = new ArrayList<>();
        actorAdapter = new ActorAdapter(this, actorList);
        actorViewModel = new ViewModelProvider(this).get(ActorViewModel.class);
        
        actorViewModel.getAllActor().observe(this, new Observer<List<Actor>>() {
            @Override
            public void onChanged(List<Actor> actorList) {
                actorAdapter.getAllActors(actorList);
                recyclerView.setAdapter(actorAdapter);
                Log.d("main", "onChanged: "+actorList);
            }
        });

        networkRequest();
    }

    private void networkRequest() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_DATA)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<List<Actor>> call = api.getAllActors();
        call.enqueue(new Callback<List<Actor>>() {
            @Override
            public void onResponse(Call<List<Actor>> call, Response<List<Actor>> response) {
                if (response.isSuccessful()){
                    actorRepository.insert(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Actor>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something went Wrong...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}