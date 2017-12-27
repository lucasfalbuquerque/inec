package com.example.lucasalbuquerque.inectest.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.lucasalbuquerque.inectest.R;
import com.example.lucasalbuquerque.inectest.activities.Adapters.RepositoryAdapter;
import com.example.lucasalbuquerque.inectest.activities.Models.Repository;
import com.example.lucasalbuquerque.inectest.activities.Rest.GitHubApi;
import com.example.lucasalbuquerque.inectest.activities.Util.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RepositoryActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private RepositoryAdapter mAdapter;
    private List<Repository> mRepositorys;
    private String user_at;
    private SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository);
        initialize();
        openRepository();
    }

    private void initialize() {
        Intent mIntent = getIntent();
        user_at = mIntent.getStringExtra("login");

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_layour_recycler);

        pDialog = new SweetAlertDialog(RepositoryActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void setupRecycler() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new RepositoryAdapter(mRepositorys);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void openRepository() {
        if (!Util.isOnline(getApplicationContext())) {
            new SweetAlertDialog(RepositoryActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(getString(R.string.sem_conexao))
                    .setContentText(getString(R.string.verificar_conexao))
                    .show();
        } else {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(getString(R.string.BASE_URL))
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            GitHubApi gitHubApi = retrofit.create(GitHubApi.class);

            Call<List<Repository>> call = gitHubApi.getRepository(getString(R.string.BASE_URL) + user_at + "/repos");
            call.enqueue(new Callback<List<Repository>>() {
                @Override
                public void onResponse(Call<List<Repository>> call, Response<List<Repository>> response) {
                    pDialog.dismissWithAnimation();
                    if (response.isSuccessful()) {
                        mRepositorys = response.body();
                        if (mRepositorys.isEmpty()) {
                            new SweetAlertDialog(RepositoryActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText(getString(R.string.repositorio_vazio))
                                    .setConfirmText("OK!")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            onBackPressed();
                                        }
                                    })
                                    .show();
                        } else {
                            setupRecycler();
                        }
                    } else {
                        System.out.println(response.errorBody());
                        Toast.makeText(getApplicationContext(), getString(R.string.repositorio_vazio), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Repository>> call, Throwable t) {
                    t.printStackTrace();
                    pDialog.dismissWithAnimation();
                    new SweetAlertDialog(RepositoryActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(getString(R.string.falhou))
                            .show();
                }
            });
        }
    }

}
