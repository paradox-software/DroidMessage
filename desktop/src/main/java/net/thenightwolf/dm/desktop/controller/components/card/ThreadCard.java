/*
 * Copyright (c) 2016 Jordan Knott
 * License file can be found in the root directory (LICENSE.txt)
 *
 * For the project DroidMessage, which can be found at: www.github.com/paradox-software/droidmessage
 *
 */

package net.thenightwolf.dm.desktop.controller.components.card;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import net.thenightwolf.dm.common.model.message.ConvoThread;
import net.thenightwolf.dm.common.model.message.Sms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ThreadCard extends AnchorPane {

    private static final String fxmlResource = "/view/thread_card.fxml";
    private static final Logger logger = LoggerFactory.getLogger(ThreadCard.class);
    private static SimpleDateFormat dateForm = new SimpleDateFormat("dd/MM HH:mm");

    @FXML
    private ImageView contactImage;

    @FXML
    private Label contactName;

    @FXML
    private Label threadSnippet;

    @FXML
    private Label threadDate;

    private String number;
    private ConvoThread convoThread;


    public ThreadCard(ConvoThread thread, String name) {
        load();
        this.convoThread = thread;
        DefaultContactImage image = new DefaultContactImage();
        setContactImage(image.getImage(name.substring(0, 1)));
        setContactName(name);
        setNumber(thread.address);

        if (thread.body != null)
            setThreadSnippet(thread.body);
        else
            setThreadSnippet("Null");

        setThreadDate(dateForm.format(new Date(thread.date)));
    }

    public ThreadCard(Sms sms, String name) {
        load();
        setContactImage(new Image("/view/icons/account_black_48.png"));
        setContactName(name);
        setNumber(sms.getNumber());

        if (sms.getMessage() != null)
            setThreadSnippet(sms.getMessage());
        else
            setThreadSnippet("Null");

        setThreadDate(dateForm.format(sms.getSentDate()));
    }

    public ThreadCard() {
        load();
    }

    private void load() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlResource));
        loader.setRoot(this);
        loader.setController(this);
        autosize();
        try {
            loader.load();
        } catch (Exception e) {
            logger.error("ContactCard FXML loader caused an exception", e);
        }
    }

    public void setContactImage(Image image) {
        contactImage.setImage(image);
    }

    public void setContactName(String name) {
        contactName.setText(name);
    }

    public void setThreadSnippet(String snippet) {
        threadSnippet.setText(snippet);
    }

    public void setThreadDate(String snippet) {
        threadDate.setText(snippet);
    }

    public void setThreadDate(Date date) {
        setThreadDate(dateForm.format(date));
    }

    public StringProperty getContactNameProperty() {
        return contactName.textProperty();
    }

    public StringProperty getThreadSnippetProperty() {
        return threadSnippet.textProperty();
    }

    public StringProperty getThreadDateProperty() {
        return threadDate.textProperty();
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


    public ConvoThread getConvoThread() {
        return convoThread;
    }
}
