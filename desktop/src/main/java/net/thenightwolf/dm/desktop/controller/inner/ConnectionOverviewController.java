package net.thenightwolf.dm.desktop.controller.inner;

import com.jfoenix.controls.JFXButton;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import io.datafx.controller.flow.context.ActionHandler;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.FlowActionHandler;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import net.thenightwolf.dm.common.model.message.ServerBundle;
import net.thenightwolf.dm.desktop.controller.components.dialog.DirectConnectDialog;
import net.thenightwolf.dm.desktop.controller.components.dialog.ErrorDialog;
import net.thenightwolf.dm.desktop.controller.subscribers.connection.ConnectionSubscriber;
import net.thenightwolf.dm.desktop.controller.subscribers.connection.ConnectionInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.observables.JavaFxObservable;

import javax.annotation.PostConstruct;

@FXMLController(value = "/view/ConnectionOverview.fxml", title = "DroidMessage")
public class ConnectionOverviewController implements ConnectionInterface {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionOverviewController.class);

    @FXMLViewFlowContext private ViewFlowContext context;

    @FXML private StackPane root;

    @FXML private ImageView titleImage;

    @ActionTrigger("directConnect")
    @FXML private JFXButton directConnect;

    @ActionHandler
    private FlowActionHandler actionHandler;

    private DirectConnectDialog directDialog;

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

        ConnectionSubscriber subscriber = new ConnectionSubscriber();
        subscriber.subscribe(this, fieldSource, buttonSource);

        directDialog.getIpField().setText("192.168.1.106");
        directDialog.getPasswordField().setText("nightwolf");

        directDialog.show();
    }

    @Override
    public void showSpinner() {
        directDialog.showSpinner();
    }

    @Override
    public String getIpAddress() {
        return directDialog.getIPAddress();
    }

    @Override
    public String getPassword() {
        return directDialog.getPassword();
    }

    @Override
    public void showErrorDialog(String title, String message) {
        ErrorDialog errorDialog = new ErrorDialog(root, title, message);
        directDialog.close();
        errorDialog.show();
    }

    @Override
    public void connectionComplete(ServerBundle bundle) {
        directDialog.hideSpinner();
        directDialog.showCompleteLabel();
        context.register("ServerBundle", bundle);
        try {
            actionHandler.navigate(MessengerOverviewController.class);
        } catch (Exception e){
            logger.error("Exception while navigating to MessengerOverviewController", e);
        }
    }
}
