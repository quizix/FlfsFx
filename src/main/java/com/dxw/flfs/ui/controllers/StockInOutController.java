package com.dxw.flfs.ui.controllers;

import com.dxw.flfs.data.models.erp.FeedWarehouse;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Created by zhang on 2016-07-02.
 */
public class StockInOutController {
    @FXML
    private TextField textFieldWarehouseName;

    @FXML
    private TextField textFieldQuantity;

    @FXML
    private Button buttonCancel;

    @FXML
    private Button buttonOk;

    @FXML
    public void initialize(){
        Platform.runLater(() -> textFieldQuantity.requestFocus());
    }

    private boolean dialogResult;

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

    public boolean isDialogResult() {
        return dialogResult;
    }

    public float getQuantity(){
        return Float.parseFloat(this.textFieldQuantity.getText());
    }

    public void setWarehouse(FeedWarehouse warehouse) {
        if(warehouse != null){
            this.textFieldWarehouseName.setText(warehouse.getName());
        }
    }
}
