package net.thenightwolf.dm.desktop.controller.components.icon;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class InsertIcon extends ImageView {

    public InsertIcon(int size){
        super(new Image("/view/icons/ic_insert_photo_black_36dp.png"));
        super.setFitHeight(size);
        super.setFitWidth(size);
    }
}
