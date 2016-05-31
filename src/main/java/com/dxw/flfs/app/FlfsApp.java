package com.dxw.flfs.app;/**
 * Created by zhang on 2016-05-19.
 */

import com.dxw.common.services.ServiceException;
import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;
import com.dxw.flfs.data.HibernateService;
import com.dxw.flfs.data.dal.UnitOfWork;
import com.dxw.flfs.data.models.Site;
import com.dxw.flfs.ui.controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import java.util.Properties;

public class FlfsApp extends Application {

    private static ApplicationContext context = new ApplicationContext();

    public static ApplicationContext getContext() {
        return context;
    }

    private AppInitiator initiator;

    private boolean enableSplash = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        try {
            initiator = new AppInitiator(ServiceRegistryImpl.getInstance());
            initiator.initServices();
            initContext();

        } catch (ServiceException e) {
            e.printStackTrace();
        }

        loadLoginSplash(stage);

    }

    private void loadLoginSplash(Stage primaryStage) {
        if (enableSplash) {

            URL location = getClass().getClassLoader().getResource("ui/dialogs/login.fxml");
            FXMLLoader loader = new FXMLLoader(location);

            Pane root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            LoginController controller = loader.getController();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("发酵式液态料饲喂系统——登录");
            stage.getIcons().add(new Image("images/piggy-bank-icon.png"));
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.sizeToScene();
            stage.centerOnScreen();
            stage.setOnCloseRequest(we -> dispose());

            controller.setStage(stage);

            stage.showAndWait();
            Optional<ButtonType> result = controller.getDialogResult();

            if (result.isPresent()) {
                ButtonType value = result.get();
                if (value == ButtonType.OK) {
                    try {
                        loadMain(primaryStage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.exit(0);
                }
            }
        } else {
            try {
                loadMain(primaryStage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initContext() {

        Properties prop = new Properties();
        InputStream in = this.getClass().getResourceAsStream("/flfs.config");
        try {
            prop.load(in);
            String siteCode = prop.getProperty("siteCode");
            context.setSiteCode(siteCode);

            ServiceRegistry registry = ServiceRegistryImpl.getInstance();
            HibernateService hibernateService = (HibernateService) registry.getService(Services.HIBERNATE_SERVICE);
            UnitOfWork unitOfWork = new UnitOfWork(hibernateService.getSession());

            Site site = unitOfWork.getSiteConfigRepository().findByNaturalId(siteCode);

            if (site == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "无法获取站点数据！");
                alert.setHeaderText(null);
                alert.show();
                System.exit(0);
            }

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "无法获取站点代码！");
            alert.setHeaderText(null);
            alert.show();
            System.exit(0);
        }
    }

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
        if (initiator != null)
            initiator.dispose();
    }

}
