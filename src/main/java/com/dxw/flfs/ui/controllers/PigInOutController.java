package com.dxw.flfs.ui.controllers;

import com.dxw.flfs.data.models.erp.Sty;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Created by zhang on 2016-07-02.
 */
public class PigInOutController {
    @FXML
    private TextField textFieldStyName;

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

    public int getNumber(){
        return Integer.parseInt(this.textFieldQuantity.getText());
    }

    public void setSty(Sty sty) {
        if(sty != null){
            this.textFieldStyName.setText(sty.getName());
        }
    }
}
