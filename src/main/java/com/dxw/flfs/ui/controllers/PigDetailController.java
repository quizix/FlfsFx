package com.dxw.flfs.ui.controllers;

import com.dxw.flfs.data.models.erp.Pig;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Created by zhang on 2016-05-26.
 */
public class PigDetailController {

    @FXML
    private TextField textFieldName;

    @FXML
    private TextField textFieldCode;

    @FXML
    private Button buttonCancel;

    @FXML
    private Button buttonOk;

    private boolean dialogResult;
    private Pig pig;

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

    public boolean isDialogResult() {
        return dialogResult;
    }

    public void setUser(Pig pig) {
        this.pig = pig;

        if(pig != null){
            this.textFieldCode.setText(pig.getCode());
            this.textFieldName.setText(pig.getName());
        }
    }
}
