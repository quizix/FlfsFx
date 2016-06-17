package com.dxw.flfs.ui.controllers;

import com.dxw.flfs.data.models.erp.Medicine;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Created by zhang on 2016-05-26.
 */
public class MedicineDetailController {

    @FXML
    private TextField textFieldName;

    @FXML
    private TextField textFieldCode;

    @FXML
    private Button buttonCancel;

    @FXML
    private Button buttonOk;

    private boolean dialogResult;
    private Medicine medicine;

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

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;

        if(medicine != null){
            this.textFieldCode.setText(medicine.getCode());
            this.textFieldName.setText(medicine.getName());
        }
    }
}
