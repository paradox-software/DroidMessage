package net.thenightwolf.dm.desktop.data;

import javafx.scene.image.Image;
import net.thenightwolf.dm.common.model.message.ConversationMessageBundle;

public class LocalMessageBundle {
    private ConversationMessageBundle bundle;
    private Image contactImage;

    public LocalMessageBundle(ConversationMessageBundle bundle, Image contactImage) {
        this.bundle = bundle;
        this.contactImage = contactImage;
    }

    public ConversationMessageBundle getBundle() {
        return bundle;
    }

    public Image getContactImage() {
        return contactImage;
    }
}
