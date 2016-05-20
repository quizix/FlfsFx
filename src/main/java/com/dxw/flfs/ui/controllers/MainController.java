package com.dxw.flfs.ui.controllers;

import com.dxw.common.ms.Notification;
import com.dxw.common.ms.NotificationManager;
import com.dxw.common.ms.NotificationTags;
import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

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

    }
}
