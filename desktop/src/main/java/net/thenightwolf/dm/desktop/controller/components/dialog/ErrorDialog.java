package net.thenightwolf.dm.desktop.controller.components.dialog;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class ErrorDialog extends JFXDialog {


    public ErrorDialog(StackPane root, String title, String message){
        super();
        super.setDialogContainer(root);

        VBox errorContent = new VBox();
        errorContent.setPadding(new Insets(25, 25, 25 ,25));
        errorContent.setAlignment(Pos.CENTER);
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold");
        Label messageLabel = new Label(message);
        messageLabel.setPrefWidth(250);
        messageLabel.setWrapText(true);

        JFXButton closeButton = new JFXButton("Close");
        closeButton.setOnAction((event -> this.close()));

        titleLabel.setAlignment(Pos.CENTER);

        messageLabel.setAlignment(Pos.CENTER);
        messageLabel.setTextAlignment(TextAlignment.CENTER);

        closeButton.setAlignment(Pos.CENTER);
        errorContent.getChildren().addAll(titleLabel, messageLabel, closeButton);

        super.setContent(errorContent);
    }
}
