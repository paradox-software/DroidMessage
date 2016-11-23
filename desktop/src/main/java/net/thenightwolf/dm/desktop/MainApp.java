package net.thenightwolf.dm.desktop;

import com.jfoenix.controls.JFXDecorator;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.thenightwolf.dm.desktop.controller.MainController;
import net.thenightwolf.dm.desktop.startup.LogHeaderCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainApp extends Application {

    public static final String VERSION = "0.0.0-ALPHA";
    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);

    @FXMLViewFlowContext
    private ViewFlowContext context;

    public static void main(String... args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        /*
        ConversationMessageBundle bundle = new ConversationMessageBundle();
        bundle.messages = new ArrayList<>();

        String myNumber = "9189231326";
        String otherNumber ="6669996666";

        for(int i=0; i<15; i++){
            Message sms = new Message(50, "Hello " + i, i % 2 == 0 ? myNumber : otherNumber, new Date());
            bundle.messages.add(sms);
        }

        MessageGrid grid = new MessageGrid();
        grid.load(bundle);

        Scene scene = new Scene(grid, 350, 400);

        stage.setScene(scene);
        stage.setTitle("SMS Blob");
        stage.show();
        */


        new LogHeaderCommand().execute();

        Flow flow = new Flow(MainController.class);
        DefaultFlowContainer container = new DefaultFlowContainer();
        context = new ViewFlowContext();
        context.register("Stage", stage);
        flow.createHandler(context).start(container);

        JFXDecorator decorator = new JFXDecorator(stage, container.getView(), false, true, true);
        decorator.setCustomMaximize(true);
        Scene scene = new Scene(decorator, 1000, 800);
        scene.getStylesheets().add(MainApp.class.getResource("/view/css/jfoenix-fonts.css").toExternalForm());
        scene.getStylesheets().add(MainApp.class.getResource("/view/css/jfoenix-design.css").toExternalForm());
        scene.getStylesheets().add(MainApp.class.getResource("/view/css/jfoenix-md.css").toExternalForm());
        scene.getStylesheets().add(MainApp.class.getResource("/view/css/theme/twilight.css").toExternalForm());

        stage.setMinWidth(1000);
        stage.setMinWidth(800);
        stage.setScene(scene);
        stage.setOnCloseRequest((event -> System.exit(0)));
        stage.show();
    }
}
