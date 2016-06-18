package com.dxw.flfs.ui.controllers;

import com.dxw.flfs.data.models.erp.Unit;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Created by zhang on 2016-05-26.
 */
public class UnitDetailController {

    @FXML
    private TextField textFieldName;

    @FXML
    private TextField textFieldSymbol;

    @FXML
    private Button buttonCancel;

    @FXML
    private Button buttonOk;

    private boolean dialogResult;
    private Unit unit;

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

    public String getTextFieldSymbol() {
        return textFieldSymbol.getText();
    }

    public boolean isDialogResult() {
        return dialogResult;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;

        if(unit != null){
            this.textFieldSymbol.setText(unit.getSymbol());
            this.textFieldName.setText(unit.getName());
        }
    }
}
