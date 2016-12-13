package net.thenightwolf.dm.desktop.controller.subscribers.connection;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import net.thenightwolf.dm.common.model.Manifest;
import net.thenightwolf.dm.common.model.comparator.LexicalContactComparator;
import net.thenightwolf.dm.common.model.comparator.TemporalThreadComparator;
import net.thenightwolf.dm.common.model.message.ServerBundle;
import net.thenightwolf.dm.desktop.services.AuthenticateService;
import net.thenightwolf.dm.desktop.services.ManifestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.schedulers.JavaFxScheduler;
import rx.schedulers.Schedulers;

import java.io.IOException;
import java.util.Collections;

public class ConnectionSubscriber {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionSubscriber.class);

    public void subscribe(ConnectionInterface connector, Observable<KeyEvent> fieldSource, Observable<ActionEvent> buttonSource){
        Observable.merge(fieldSource, buttonSource)
                .observeOn(Schedulers.io())
                .map(s -> {
                    connector.showSpinner();
                    return s;
                })
                .filter(s -> !connector.getIpAddress().equals(""))
                .filter(s -> !connector.getPassword().equals(""))
                .map(s -> connector.getIpAddress())
                .map(url -> {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://" + url + ":5001")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    logger.info("Getting token");
                    AuthenticateService authService = retrofit.create(AuthenticateService.class);
                    Call<String> tokenCall = authService.authenticate(connector.getPassword());
                    String token = "failed";
                    try {
                        Response<String> response = tokenCall.execute();
                        logger.info("Authentication return code: {}", response.code());
                        if(response.isSuccessful()){
                            token = response.body();
                        } else {
                            if(response.code() == 403) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        connector.showErrorDialog("Error", "Invalid password! Please try again.");
                                    }
                                });
                            }
                        }
                    } catch (Exception e) { e.printStackTrace(); }
                    return new ServerBundle(url, token, null);
                })
                .filter(target -> !target.token.equals("failed"))
                .map(target -> {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://" + target.url + ":5001")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    logger.info("Getting manifest");
                    ManifestService manifestService = retrofit.create(ManifestService.class);
                    Call<Manifest> manifestCall = manifestService.getManifest(target.token);

                    Manifest manifest = null;
                    try {
                        target.manifest = manifestCall.execute().body();
                    } catch (IOException e) { e.printStackTrace(); }
                    return target;
                })
                .map(serverBundle -> {
                    Collections.sort(serverBundle.manifest.contacts, new LexicalContactComparator());
                    Collections.sort(serverBundle.manifest.threads, new TemporalThreadComparator());
                    return serverBundle;
                })
                .observeOn(JavaFxScheduler.getInstance())
                .subscribe(connector::connectionComplete, throwable -> {
                    connector.showErrorDialog("Error", "There was a problem while connecting. Please try again.");
                });
    }

}
