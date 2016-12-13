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
import net.thenightwolf.dm.common.model.message.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContactCard extends AnchorPane {

    private static final String fxmlResource = "/view/contact_card.fxml";
    private static final Logger logger = LoggerFactory.getLogger(ContactCard.class);

    @FXML
    private ImageView contactImage;

    @FXML
    private Label contactName;

    @FXML
    private Label contactNumber;

    public Contact getContact() {
        return contact;
    }

    private Contact contact;

    public ContactCard() {
        load();
    }

    public ContactCard(Contact contact){
        load();
        this.contact = contact;
        setContactName(contact.name);
        setContactNumber(contact.cleanNumber);

        DefaultContactImage image = new DefaultContactImage();
        setContactImage(image.getImage(contact.name.substring(0, 1)));
    }

    private void load(){
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

    public void setContactImage(Image image){
        contactImage.setImage(image);
    }

    public void setContactName(String name){
        contactName.setText(name);
    }

    public void setContactNumber(String number){
        contactNumber.setText(number);
    }

    public StringProperty getContactNameProperty(){
        return contactName.textProperty();
    }

    public StringProperty getContactNumberProperty(){
        return contactNumber.textProperty();
    }

}
