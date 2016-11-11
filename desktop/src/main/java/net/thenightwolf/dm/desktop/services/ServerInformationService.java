package net.thenightwolf.dm.desktop.services;

import net.thenightwolf.dm.common.model.ServerInformation;
import retrofit2.http.GET;
import rx.Observable;

public interface ServerInformationService {

    @GET("/")
    Observable<ServerInformation> getServerInformation();
}
