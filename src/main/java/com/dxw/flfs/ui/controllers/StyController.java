package com.dxw.flfs.ui.controllers;

import com.dxw.flfs.data.models.Sty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Created by zhang on 2016-05-26.
 */
public class StyController {

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

    public String getCode() {
        return textFieldCode.getText();
    }

    public int getNo() {

        return Integer.parseInt(textFieldNo.getText());
    }

    public boolean isDialogResult() {
        return dialogResult;
    }

    public void setSty(Sty sty) {
        this.sty = sty;

        if(sty != null){
            this.textFieldNo.setText(Integer.toString(sty.getNo()));
            this.textFieldCode.setText(sty.getCode());
            this.textFieldName.setText(sty.getName());

        }
    }
}
