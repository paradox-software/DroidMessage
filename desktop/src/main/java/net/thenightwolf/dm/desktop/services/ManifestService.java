package net.thenightwolf.dm.desktop.services;

import javafx.collections.ObservableList;
import net.thenightwolf.dm.common.model.Manifest;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

public interface ManifestService {

    @POST("/manifest")
    Call<Manifest> getManifest(@Header("Authorization") String token);
}
