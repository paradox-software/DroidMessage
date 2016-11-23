/*
 * Copyright (c) 2016 Jordan Knott
 * License file can be found in the root directory (LICENSE.txt)
 *
 * For the project DroidMessage, which can be found at: www.github.com/paradox-software/droidmessage
 *
 */

package net.thenightwolf.dm.desktop.controller;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXRippler;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.container.AnimatedFlowContainer;
import io.datafx.controller.flow.container.ContainerAnimations;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import net.thenightwolf.dm.desktop.controller.inner.MessengerOverviewController;

import javax.annotation.PostConstruct;

@FXMLController(value = "/view/Main.fxml", title = "DroidMessage")
public class MainController {

    @FXMLViewFlowContext
    private ViewFlowContext context;

    @FXML private StackPane root;

    @FXML private StackPane titleBurgerContainer;
    @FXML private JFXHamburger titleBurger;

    @FXML private StackPane optionsBurger;
    @FXML private JFXRippler optionsRippler;

    @FXML private JFXDrawer drawer;
    @FXML private JFXPopup toolbarPopup;
    @FXML private Label exit;

    @PostConstruct
    public void init() throws FlowException {
        buildTitleBurger();

        buildOptionsBurger();

        context = new ViewFlowContext();

        buildInnerFlow();

        buildSideMenuFlow();
    }

    private void buildTitleBurger(){
        drawer.setOnDrawerOpening((event -> {
            titleBurger.getAnimation().setRate(1);
            titleBurger.getAnimation().play();
        }));
        drawer.setOnDrawerClosing((event -> {
            titleBurger.getAnimation().setRate(-1);
            titleBurger.getAnimation().play();
        }));
        titleBurgerContainer.setOnMouseClicked((event -> {
            if(drawer.isHidden() || drawer.isHidding()) drawer.open();
            else drawer.close();
        }));
    }

    private void buildOptionsBurger(){
        toolbarPopup.setPopupContainer(root);
        toolbarPopup.setSource(optionsRippler);
        root.getChildren().remove(toolbarPopup);

        optionsBurger.setOnMouseClicked((event -> {
            toolbarPopup.show(JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT, -12, 15);
        }));

        exit.setOnMouseClicked((event) -> {
            Platform.exit();
        });
    }

    private void buildInnerFlow() throws FlowException {
        Flow innerFlow = new Flow(net.thenightwolf.dm.desktop.controller.inner.ConnectionOverviewController.class)
                .withLink(net.thenightwolf.dm.desktop.controller.inner.ConnectionOverviewController.class, "messenger", MessengerOverviewController.class);
        FlowHandler flowHandler = innerFlow.createHandler(context);
        context.register("ContentFlowHandler", flowHandler);
        context.register("ContentFlow", innerFlow);
        drawer.setContent(flowHandler.start(new AnimatedFlowContainer(Duration.millis(320), ContainerAnimations.SWIPE_LEFT)));
        context.register("ContentPane", drawer.getContent().get(0));
    }

    private void buildSideMenuFlow() throws FlowException {
        Flow sideMenuFlow = new Flow(SideMenuController.class);
        FlowHandler sideMenuFlowHandler = sideMenuFlow.createHandler(context);
        drawer.setSidePane(sideMenuFlowHandler.start(new AnimatedFlowContainer(Duration.millis(320), ContainerAnimations.SWIPE_LEFT)));
    }
}
