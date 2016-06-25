package com.dxw.flfs.ui.controllers;

import com.dxw.flfs.data.models.erp.Device;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Created by zhang on 2016-05-26.
 */
public class DeviceDetailController {

    @FXML
    private TextField textFieldName;

    @FXML
    private TextField textFieldCode;

    @FXML
    private Button buttonCancel;

    @FXML
    private Button buttonOk;

    @FXML
    public void initialize(){
        Platform.runLater(() -> textFieldName.requestFocus());
    }

    private boolean dialogResult;
    private Device sty;

    public void onOk(){
        dialogResult = true;
        close();
    }

    public void onCancel(){
        dialogResult = false;
        close();
    }

    private void close(){
        Stage stage = (Stage) buttonCancel.getScene().getWindow();
        stage.close();
    }

    public String getName() {
        return textFieldName.getText();
    }

    public String getCode() {
        return textFieldCode.getText();
    }

    public boolean isDialogResult() {
        return dialogResult;
    }

    public void setDevice(Device device) {
        this.sty = device;

        if(device != null){
            this.textFieldCode.setText(device.getCode());
            this.textFieldName.setText(device.getName());

        }
    }
}
