package net.thenightwolf.dm.desktop.controller.components.icon;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class SearchIcon extends ImageView {

    public SearchIcon(int size){
        super(new Image("/view/icons/magnify.png"));
        super.setFitHeight(size);
        super.setFitWidth(size);
    }
}
