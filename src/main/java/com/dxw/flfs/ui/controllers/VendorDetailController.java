package com.dxw.flfs.ui.controllers;

import com.dxw.flfs.data.models.erp.Vendor;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Created by zhang on 2016-05-26.
 */
public class VendorDetailController {

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
    private Vendor vendor;

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

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;

        if(vendor != null){
            this.textFieldCode.setText(vendor.getCode());
            this.textFieldName.setText(vendor.getName());
        }
    }
}
