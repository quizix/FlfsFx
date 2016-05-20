package com.dxw.flfs.ui.controllers;

import com.dxw.flfs.communication.PlcDelegate;
import com.dxw.flfs.communication.PlcDelegateFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Created by zhang on 2016-05-19.
 */
public class MainController {

    public void onClickTest(){
        PlcDelegate delegate = PlcDelegateFactory.getPlcDelegate();
        delegate.getModel().setSystemStatus((short)2);
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
