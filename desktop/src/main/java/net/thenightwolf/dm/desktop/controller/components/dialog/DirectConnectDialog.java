package net.thenightwolf.dm.desktop.controller.components.dialog;

import com.jfoenix.controls.*;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class DirectConnectDialog extends JFXDialog {

    private JFXSpinner connectionSpinner;
    private Label connectionAchievedLabel;

    private JFXButton connectButton;
    private JFXButton closeButton;

    private JFXTextField ipField;
    private JFXPasswordField passwordField;

    public DirectConnectDialog(StackPane root){
        super.setDialogContainer(root);

        GridPane dialogContent = new GridPane();
        dialogContent.setPadding(new Insets(25, 25, 25, 25));
        dialogContent.setAlignment(Pos.CENTER);

        Label ipAddressLabel = new Label("IP Address");
        ipField = new JFXTextField();

        Label passwordLabel = new Label("Password");
        passwordField = new JFXPasswordField();

        connectButton = new JFXButton("Accept");

        closeButton = new JFXButton("Cancel");
        closeButton.setOnAction(e -> this.close());

        ipField.setText("192.168.1.106");
        passwordField.setText("nightwolf");

        connectionSpinner = new JFXSpinner();
        connectionSpinner.setOpacity(0);

        connectionAchievedLabel = new Label("Connected!");
        connectionAchievedLabel.setOpacity(0);

        dialogContent.add(ipAddressLabel, 0, 0);
        dialogContent.add(ipField, 1, 0);

        dialogContent.add(passwordLabel, 0, 1);
        dialogContent.add(passwordField, 1, 1);

        dialogContent.add(connectButton, 0, 2);
        dialogContent.add(closeButton, 1, 2);

        dialogContent.add(connectionSpinner, 0, 3);
        GridPane.setColumnSpan(connectionSpinner, 2);
        GridPane.setHalignment(connectionSpinner, HPos.CENTER);

        dialogContent.add(connectionAchievedLabel, 0, 3);
        GridPane.setColumnSpan(connectionAchievedLabel, 2);
        GridPane.setHalignment(connectionAchievedLabel, HPos.CENTER);

        super.setContent(dialogContent);
        super.setOverlayClose(false);
    }

    public JFXSpinner getConnectionSpinner() {
        return connectionSpinner;
    }

    public Label getConnectionAchievedLabel() {
        return connectionAchievedLabel;
    }

    public JFXButton getConnectButton() {
        return connectButton;
    }

    public JFXButton getCloseButton() {
        return closeButton;
    }

    public String getIPAddress(){
        return ipField.getText().trim();
    }

    public String getPassword(){
        return passwordField.getText().trim();
    }

    public JFXTextField getIpField() {
        return ipField;
    }

    public JFXPasswordField getPasswordField() {
        return passwordField;
    }
}
