package com.dxw.flfs.app;/**
 * Created by zhang on 2016-05-19.
 */

import com.dxw.common.services.ServiceException;
import com.dxw.common.services.ServiceRegistryImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class FlfsApp extends Application {

    private static ApplicationContext context = new ApplicationContext();

    public static ApplicationContext getContext() {
        return context;
    }

    private AppInitiator initiator;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {

        try {
            initiator = new AppInitiator(ServiceRegistryImpl.getInstance());
            initiator.initServices();
            loadMain(stage);
        } catch (ServiceException e) {
            e.printStackTrace();

        }

    }

    /*private void loadSvg(Stage stage) throws IOException {
        URL location = getClass().getClassLoader().getResource("ui/svg.fxml");

        Pane root = FXMLLoader.load(location);

        Scene scene = new Scene(root, 800,600);
        stage.setTitle("svg");
        stage.setScene(scene);
        stage.show();
    }*/
    private void loadMain(Stage primaryStage) throws IOException {
        URL location = getClass().getClassLoader().getResource("ui/main.fxml");
        FXMLLoader loader = new FXMLLoader(location);

        Pane root = loader.load();

        Scene scene = new Scene(root);
        primaryStage.setTitle("发酵式液态料饲喂系统——[稻香湾科技]");
        primaryStage.getIcons().add(new Image("images/piggy-bank-icon.png"));
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);

        primaryStage.setOnCloseRequest(we -> dispose());
        primaryStage.show();


    }

    private void dispose() {
        if(initiator!=null)
            initiator.dispose();
    }

    private void loadReminder(Stage primaryStage) throws IOException {
        URL location = getClass().getClassLoader().getResource("ui/reminder.fxml");
        FXMLLoader loader = new FXMLLoader(location);

        Pane root = loader.load();

        /*ReminderController controller = loader.getController();
        controller.addReminder( new Reminder("", "12121212"));
        controller.removeAll();*/

        Scene scene = new Scene(root);
        primaryStage.setTitle("发酵式液态料饲喂系统——稻香湾科技");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);

        primaryStage.show();
    }
}
