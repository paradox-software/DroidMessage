package net.thenightwolf.dm.desktop.controller.components.message;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import net.thenightwolf.dm.common.model.comparator.TemporalSmsComparator;
import net.thenightwolf.dm.common.model.message.ConversationMessageBundle;
import net.thenightwolf.dm.common.model.message.Message;
import net.thenightwolf.dm.desktop.controller.components.SmsBlob;
import net.thenightwolf.dm.desktop.data.LocalMessageBundle;

public class MessageGrid extends GridPane {

    private Color senderColor = Color.DARKBLUE;
    private Color receiverColor = Color.MINTCREAM;

    private String currentNumber = "9189231326";

    private int index = 0;

    private Image contactImage;


    public MessageGrid(){
        setHgap(25);
        setVgap(15);
        setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));
        setPadding(new Insets(10, 10, 10, 10));
    }

    public void load(LocalMessageBundle thread){
        if(thread.getContactImage() != null)
            contactImage = thread.getContactImage();
        thread.getBundle().messages.sort(new TemporalSmsComparator(true));
        thread.getBundle().messages.forEach(this::addSms);
        autosize();
        layout();
    }

    public void addSms(Message message){
        if(message.getNumber().equals(currentNumber)){
            SmsBlob blob = new SmsBlob(message.getContent(), message.getSentDate().toGMTString());
            setHgrow(blob, Priority.ALWAYS);
            setVgrow(blob, Priority.ALWAYS);
            setColumnSpan(blob, 2);
            add(blob, 1, index);
        } else {
            SmsBlob blob = new SmsBlob(message.getContent(), message.getSentDate().toGMTString(), contactImage);
            setHgrow(blob, Priority.ALWAYS);
            setVgrow(blob, Priority.ALWAYS);
            setColumnSpan(blob, 2);
            add(blob, 0, index);
        }
        index++;
    }

    public void reset(){
        getChildren().clear();
    }
}
