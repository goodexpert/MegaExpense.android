package com.example.apps.mega.network;

import com.example.apps.mega.model.HistoricalResponse;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WebService {
    @GET("/historical")
    Observable<Response<HistoricalResponse>> getHistoricalList(
            @Query("access_key") String accessKey,
            @Query("source") String source,
            @Query("currencies") String currencies,
            @Query("date") String date,
            @Query("format") Integer format);
}
