package com.dxw.flfs.ui.controllers;

import com.dxw.flfs.data.models.erp.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Created by zhang on 2016-05-26.
 */
public class UserDetailController {

    @FXML
    private TextField textFieldName;

    @FXML
    private TextField textFieldCode;

    @FXML
    private TextField textFieldPassword;

    @FXML
    private Button buttonCancel;

    @FXML
    private Button buttonOk;

    @FXML
    public void initialize(){
        Platform.runLater(() -> {
            textFieldName.requestFocus();
        });
    }
    private boolean dialogResult;
    private User user;

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

    public String getTextFieldName() {
        return textFieldName.getText();
    }

    public String getTextFieldCode() {
        return textFieldCode.getText();
    }

    public String getTextFieldPassword() {

        return textFieldPassword.getText();
    }

    public boolean isDialogResult() {
        return dialogResult;
    }

    public void setUser(User shed) {
        this.user = shed;

        if(shed != null){
            this.textFieldPassword.setText(shed.getPassword());
            this.textFieldCode.setText(shed.getCode());
            this.textFieldName.setText(shed.getName());
        }

    }
}
