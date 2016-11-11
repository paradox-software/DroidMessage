package net.thenightwolf.dm.desktop.controller;

import com.jfoenix.controls.JFXButton;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import io.datafx.controller.flow.context.ActionHandler;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.FlowActionHandler;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import net.thenightwolf.dm.common.model.Manifest;
import net.thenightwolf.dm.common.model.comparator.LexicalContactComparator;
import net.thenightwolf.dm.common.model.comparator.TemporalThreadComparator;
import net.thenightwolf.dm.common.model.message.ServerBundle;
import net.thenightwolf.dm.desktop.controller.components.dialog.DirectConnectDialog;
import net.thenightwolf.dm.desktop.controller.components.dialog.ErrorDialog;
import net.thenightwolf.dm.desktop.services.AuthenticateService;
import net.thenightwolf.dm.desktop.services.ManifestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.observables.JavaFxObservable;
import rx.schedulers.JavaFxScheduler;
import rx.schedulers.Schedulers;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@FXMLController(value = "/view/ConnectionOverview.fxml", title = "DroidMessage")
public class ConnectionOverviewController {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionOverviewController.class);

    @FXMLViewFlowContext private ViewFlowContext context;

    @FXML private StackPane root;

    @FXML private ImageView titleImage;

    @ActionTrigger("directConnect")
    @FXML private JFXButton directConnect;

    @ActionHandler
    protected FlowActionHandler actionHandler;

    private DirectConnectDialog directDialog;
    private ErrorDialog errorDialog;
    private String token;

    @PostConstruct
    public void init(){
        titleImage.setImage(new Image("/view/logo/droidmessage_logo.png"));
    }


    @ActionMethod("directConnect")
    public void directConnect(){
        directDialog = new DirectConnectDialog(root);

        Observable<KeyEvent> fieldSource = JavaFxObservable.fromNodeEvents(directDialog.getPasswordField(), KeyEvent.KEY_RELEASED)
                .filter(e -> e.getCode() == KeyCode.ENTER);

        Observable<ActionEvent> buttonSource = JavaFxObservable.fromActionEvents(directDialog.getConnectButton());

        Observable.merge(fieldSource, buttonSource)
                .observeOn(Schedulers.io())
                .map(s -> {
                    directDialog.getConnectionSpinner().setOpacity(1);
                    return s;
                })
                .filter(s -> directDialog.getIPAddress() != null && !directDialog.getIPAddress().trim().equals(""))
                .filter(s -> directDialog.getPassword() != null && !directDialog.getPassword().trim().equals(""))
                .map(s -> directDialog.getIPAddress())
                .map(url -> {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://" + url + ":5001")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    logger.info("Getting token");
                    AuthenticateService authService = retrofit.create(AuthenticateService.class);
                    Call<String> tokenCall = authService.authenticate(directDialog.getPassword());
                    String token = null;
                    try {
                        token = tokenCall.execute().body();
                    } catch (IOException ignored) {}
                    return new ServerBundle(url, token, null);

                })
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
                    } catch (IOException ignored) {}
                    return target;
                })
                .map(serverBundle -> {
                    logger.info("Sorting");
                    Collections.sort(serverBundle.manifest.contacts, new LexicalContactComparator());
                    Collections.sort(serverBundle.manifest.threads, new TemporalThreadComparator());
                    return serverBundle;
                })
                .map(serverBundle -> {
                    directDialog.getConnectionSpinner().setOpacity(0);
                    directDialog.getConnectionAchievedLabel().setOpacity(1);
                    return serverBundle;
                })
                .delay(1, TimeUnit.SECONDS)
                .observeOn(JavaFxScheduler.getInstance())
                .subscribe(serverBundle -> {
                    logger.info("Registering bundle and navigating");
                    context.register("ServerBundle", serverBundle);
                    try {
                        actionHandler.navigate(MessengerOverviewController.class);
                    } catch (VetoException | FlowException ignored) {}
                }, throwable -> {
                    errorDialog = new ErrorDialog(root, "Error", "There was a problem connecting to the server. Please check your connection and try again");
                    directDialog.close();
                    errorDialog.show();
                });

        // DEBUG //
        directDialog.getIpField().setText("192.168.1.106");
        directDialog.getPasswordField().setText("nightwolf");

        directDialog.show();
    }

}
