/*
 * Copyright (c) 2016 Jordan Knott
 * License file can be found in the root directory (LICENSE.txt)
 *
 * For the project DroidMessage, which can be found at: www.github.com/paradox-software/droidmessage
 *
 */

package net.thenightwolf.dm.desktop.controller.components;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import net.thenightwolf.dm.desktop.controller.components.message.SmsFlow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static javafx.scene.input.KeyCode.O;


public class SmsBlob extends StackPane {
    //private static final String fxmlResource = "/view/sms_blob.fxml";
    private static final Logger logger = LoggerFactory.getLogger(net.thenightwolf.dm.desktop.controller.components.card.ContactCard.class);

    private ImageView contactImage = new ImageView();
    private SmsFlow messageFlow = new SmsFlow();
    private Label messageDate = new Label();

    private HBox messageContainer = new HBox();

    public SmsBlob(String message, String date){
        setMessage(message, true);
        setMessageDate(date);

        setAlignment(Pos.CENTER_RIGHT);
        messageContainer.setAlignment(Pos.CENTER_RIGHT);

        messageContainer.getChildren().add(messageFlow);
        setupLayout();
        setOwnerStyle();
    }

    public SmsBlob(String message, String date, Image image){
        setMessage(message, false);
        setMessageDate(date);
        setContactImage(image);

        setAlignment(Pos.CENTER_LEFT);
        messageContainer.setAlignment(Pos.CENTER_LEFT);

        setupContactImage();
        messageContainer.getChildren().addAll(contactImage, messageFlow);
        messageFlow.setTextAlignment(TextAlignment.LEFT);
        setupLayout();
        setReceivedStyle();
    }

    private void setupContactImage(){
        contactImage.setFitHeight(32);
        contactImage.setFitWidth(32);
    }

    private void setupLayout(){
        messageFlow.setPadding(new Insets(15, 15, 15, 15));
        getChildren().add(messageContainer);
    }

    public void setContactImage(Image image){
        contactImage.setImage(image);
    }

    public void setMessageDate(String date){
        messageDate.setText(date);
    }

    public void setMessage(String message, boolean owner){
        Text text = new Text(message.trim());
        if(owner)
            text.getStyleClass().add("text-primary-color");
        else
            text.getStyleClass().add("text-color");
        messageFlow.getChildren().add(text);
    }

    private void setOwnerStyle(){
        messageFlow.getStyleClass().add("owner-message-color");
        messageFlow.getStyleClass().add("message-blob");
    }

    private void setReceivedStyle(){
        messageFlow.getStyleClass().add("primary-color");
        messageFlow.getStyleClass().add("message-blob");
    }

}
