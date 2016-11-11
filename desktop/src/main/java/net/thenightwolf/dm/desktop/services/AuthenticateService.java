package net.thenightwolf.dm.desktop.services;

import retrofit2.Call;
import retrofit2.http.*;
import rx.Observable;

public interface AuthenticateService {

    @FormUrlEncoded
    @POST("/login")
    Call<String> authenticate(@Field("password") String password);

}
