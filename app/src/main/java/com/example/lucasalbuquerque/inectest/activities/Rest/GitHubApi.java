package com.example.lucasalbuquerque.inectest.activities.Rest;

import com.example.lucasalbuquerque.inectest.activities.Models.Repository;
import com.example.lucasalbuquerque.inectest.activities.Models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Url;

/**
 * Created by Lucas Albuquerque on 26/12/2017.
 */

public interface GitHubApi {
    @Headers({
            "Accept: application/json"
    })
    @GET
    Call<User> getUser(@Url String url);

    @Headers({
            "Accept: application/json"
    })
    @GET
    Call<List<Repository>> getRepository(@Url String url);


}

