package com.dxw.flfs.ui.controllers;

import com.dxw.common.ms.Notification;
import com.dxw.common.ms.NotificationManager;
import com.dxw.common.ms.NotificationTags;
import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhang on 2016-05-19.
 */
public class MainController {

    public void onClickTest(){
       // PlcDelegate delegate = PlcDelegateFactory.getPlcDelegate();
       // delegate.getModel().setSystemStatus((short)2);
        ServiceRegistry registry = ServiceRegistryImpl.getInstance();

        NotificationManager notificationManager = (NotificationManager) registry.getService(Services.NOTIFICATION_MANAGER);
        Notification n = new Notification();
        n.setContent("系统提示：请输入明天的入栏计划");
        n.setWhen( System.currentTimeMillis());
        if(notificationManager!=null)
            notificationManager.notify(NotificationTags.Remind, n);
    }

    public void onClickStart(){

    }

    public void onClickStop(){

    }

    public void onClickClean(){

    }

    public void onClickExit(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "确定要退出系统吗？");
        alert.setHeaderText(null);
        alert.showAndWait().filter(response -> response == ButtonType.OK)
                .ifPresent(response ->{
                    System.exit(0);
                });

    }

    public void onClickShedManagement(){

    }

    public void onClickBatchManagement(){

    }

    public void onClickPigPlan(){

    }

    public void onClickAbout(){
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("ui/aboutDialog.fxml"));
            Stage stage = new Stage();
            stage.setTitle("关于发酵式液态料饲喂系统");
            stage.setScene(new Scene(root, 450, 320));
            stage.initModality(Modality.APPLICATION_MODAL);
            //stage.initStyle(StageStyle.UTILITY);
            stage.setResizable(false);
            stage.initOwner(null);
            stage.show();

            //hide this current window (if this is whant you want
            //((Node)(event.getSource())).getScene().getWindow().hide();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void popupErrorMsg() {
        final Stage myDialog = new Stage();
        myDialog.initModality(Modality.APPLICATION_MODAL);
        Button okButton = new Button("Ok");
        okButton.setOnAction(arg0 -> myDialog.close());
        Date todayDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Scene myDialogScene = new Scene(VBoxBuilder.create()
                .children(new Text("Please Enter Validate Date \n \t "+ dateFormat.format(todayDate)), okButton)
                .spacing(30)
                .alignment(Pos.CENTER)
                .padding(new Insets(10))
                .build());

        myDialog.setScene(myDialogScene);
        myDialog.show();
    }
}
