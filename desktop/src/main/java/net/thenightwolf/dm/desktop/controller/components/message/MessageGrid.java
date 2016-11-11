package net.thenightwolf.dm.desktop.controller.components.message;

import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import net.thenightwolf.dm.common.model.comparator.TemporalSmsComparator;
import net.thenightwolf.dm.common.model.comparator.TemporalThreadComparator;
import net.thenightwolf.dm.common.model.message.ConvoThread;
import net.thenightwolf.dm.common.model.message.Sms;
import net.thenightwolf.dm.common.model.message.ThreadBundle;
import net.thenightwolf.dm.desktop.controller.components.SmsBlob;

import static java.awt.Color.blue;

public class MessageGrid extends GridPane {

    private Color senderColor = Color.DARKBLUE;
    private Color receiverColor = Color.MINTCREAM;

    private String currentNumber = "9189231326";

    private int index = 0;



    public MessageGrid(){
        setHgap(25);
        setVgap(15);
        setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));
        setPadding(new Insets(10, 10, 10, 10));
    }

    public void load(ThreadBundle thread){
        thread.messages.sort(new TemporalSmsComparator(true));
        thread.messages.forEach(this::addSms);
        autosize();
        layout();
    }

    public void addSms(Sms sms){
        if(sms.getNumber().equals(currentNumber)){
            SmsBlob blob = new SmsBlob(sms.getMessage(), sms.getSentDate().toGMTString());
            setHgrow(blob, Priority.ALWAYS);
            setVgrow(blob, Priority.ALWAYS);
            setColumnSpan(blob, 2);
            add(blob, 1, index);
        } else {
            SmsBlob blob = new SmsBlob(sms.getMessage(), sms.getSentDate().toGMTString(), new Image("/view/icons/account_black_48.png"));
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
