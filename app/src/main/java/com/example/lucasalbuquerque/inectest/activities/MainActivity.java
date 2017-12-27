package com.example.lucasalbuquerque.inectest.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lucasalbuquerque.inectest.R;
import com.example.lucasalbuquerque.inectest.activities.Models.User;
import com.example.lucasalbuquerque.inectest.activities.Rest.GitHubApi;
import com.example.lucasalbuquerque.inectest.activities.Util.Util;
import com.example.lucasalbuquerque.inectest.databinding.ActivityMainBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private LinearLayout linear_content;
    private TextView tv_sem_resultado, repos_url;
    private Button btn_search;
    private EditText et_search;
    private ActivityMainBinding binding;
    private String user_at;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initialize();
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_search.getText().toString().trim().isEmpty()) {
                    et_search.setError(getString(R.string.campo_obrigatorio));
                } else if (Util.isOnline(getApplicationContext())) {
                    SweetAlertDialog pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("Loading");
                    pDialog.setCancelable(false);
                    pDialog.show();
                    searchUser(et_search.getText().toString(), pDialog);
                } else {
                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(getString(R.string.sem_conexao))
                            .setContentText(getString(R.string.verificar_conexao))
                            .show();
                }

            }
        });

        repos_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, RepositoryActivity.class);
                i.putExtra("login", user_at); //Optional parameters
                startActivity(i);
            }
        });
    }

    public void initialize() {

        tv_sem_resultado = (TextView) findViewById(R.id.tv_sem_resultado);
        repos_url = (TextView) findViewById(R.id.repos_url);
        tv_sem_resultado.setVisibility(View.VISIBLE);
        et_search = (EditText) findViewById(R.id.et_search);
        btn_search = (Button) findViewById(R.id.btn_search);
        linear_content = (LinearLayout) findViewById(R.id.linear_content);
    }

    public void searchUser(String user, final SweetAlertDialog pDialog) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.BASE_URL))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        GitHubApi gitHubApi = retrofit.create(GitHubApi.class);

        Call<User> call = gitHubApi.getUser(getString(R.string.BASE_URL) + user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                pDialog.dismissWithAnimation();
                if (response.isSuccessful()) {
                    User changesList = response.body();
                    changesList.setCreated_at(changesList.getCreated_at().replace("T", " - ").replace("Z", ""));
                    changesList.setUpdated_at(changesList.getUpdated_at().replace("T", " - ").replace("Z", ""));
                    linear_content.setVisibility(View.VISIBLE);
                    tv_sem_resultado.setVisibility(View.GONE);
                    binding.setUser(changesList);
                    user_at = changesList.getLogin();
                    new Util.DownloadImageTask((ImageView) findViewById(R.id.avatar))
                            .execute(changesList.getAvatar_url());
                } else {
                    linear_content.setVisibility(View.GONE);
                    tv_sem_resultado.setVisibility(View.VISIBLE);
                    System.out.println(response.errorBody());
                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(getString(R.string.usuario_nao_encontrado))
                            .show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
                linear_content.setVisibility(View.GONE);
                tv_sem_resultado.setVisibility(View.VISIBLE);
                pDialog.dismissWithAnimation();
                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(getString(R.string.falhou))
                        .show();
            }
        });
    }
}
