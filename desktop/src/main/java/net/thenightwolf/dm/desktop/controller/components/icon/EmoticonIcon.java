package net.thenightwolf.dm.desktop.controller.components.icon;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class EmoticonIcon extends ImageView {

    public EmoticonIcon(int size){
        super(new Image("/view/icons/emoticon.png"));
        super.setFitHeight(size);
        super.setFitWidth(size);
    }
}
