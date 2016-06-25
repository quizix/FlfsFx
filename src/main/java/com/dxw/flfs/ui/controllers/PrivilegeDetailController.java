package com.dxw.flfs.ui.controllers;

import com.dxw.flfs.data.models.erp.Sty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Created by zhang on 2016-05-26.
 */
public class PrivilegeDetailController {

    @FXML
    private TextField textFieldName;

    @FXML
    private Button buttonCancel;

    @FXML
    private Button buttonOk;

    private boolean dialogResult;
    private Sty sty;

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

    public boolean isDialogResult() {
        return dialogResult;
    }

    public void setSty(Sty sty) {
        this.sty = sty;

        if(sty != null){
            this.textFieldName.setText(sty.getName());
        }
    }
}
