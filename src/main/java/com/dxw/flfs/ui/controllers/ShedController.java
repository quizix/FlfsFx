package com.dxw.flfs.ui.controllers;

import com.dxw.flfs.data.models.Shed;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Created by zhang on 2016-05-26.
 */
public class ShedController {

    @FXML
    private TextField textFieldName;

    @FXML
    private TextField textFieldCode;

    @FXML
    private TextField textFieldAddress;

    @FXML
    private Button buttonCancel;

    @FXML
    private Button buttonOk;

    private boolean dialogResult;
    private Shed shed;

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

    public String getTextFieldAddress() {

        return textFieldAddress.getText();
    }

    public boolean isDialogResult() {
        return dialogResult;
    }

    public void setShed(Shed shed) {
        this.shed = shed;

        if(shed != null){
            this.textFieldAddress.setText(shed.getAddress());
            this.textFieldCode.setText(shed.getCode());
            this.textFieldName.setText(shed.getName());

        }
    }
}
