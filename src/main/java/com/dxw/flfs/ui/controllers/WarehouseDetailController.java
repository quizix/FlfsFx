package com.dxw.flfs.ui.controllers;

import com.dxw.flfs.data.models.mes.Warehouse;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Created by zhang on 2016-05-26.
 */
public class WarehouseDetailController {

    @FXML
    private TextField textFieldName;

    @FXML
    private TextField textFieldCode;

    @FXML
    private TextField textFieldNo;

    @FXML
    private Button buttonCancel;

    @FXML
    private Button buttonOk;

    private boolean dialogResult;
    private Warehouse sty;

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

    public int getNo() {

        return Integer.parseInt(textFieldNo.getText());
    }

    public boolean isDialogResult() {
        return dialogResult;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.sty = warehouse;

        if(warehouse != null){
            this.textFieldNo.setText(Integer.toString(warehouse.getNo()));
            this.textFieldCode.setText(warehouse.getCode());
            this.textFieldName.setText(warehouse.getName());

        }
    }
}
