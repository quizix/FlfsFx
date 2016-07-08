package com.dxw.flfs.ui.controllers.warehouses;

import com.dxw.flfs.data.dal.UnitOfWork;
import com.dxw.flfs.data.models.erp.Sty;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Created by zhang on 2016-05-26.
 */
public class PigDeliveryDetailController {

    @FXML
    private TextField textFieldCode;

    @FXML
    private TextField textFieldPurchaseCode;

    @FXML
    private TextField textFieldNumber;



    @FXML
    private Button buttonCancel;

    @FXML
    private Button buttonOk;

    @FXML
    public void initialize(){
        Platform.runLater(() -> textFieldCode.requestFocus());
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

    public String getCode() {
        return textFieldCode.getText();
    }

    public String getPurchaseCode() {
        return textFieldPurchaseCode.getText();
  }

    public int getNumber(){
        return Integer.parseInt( this.textFieldNumber.getText());
    }

    public boolean isDialogResult() {
        return dialogResult;
    }



    private Sty sty;
    public void setSty(Sty sty) {
        this.sty = sty;
    }

}
