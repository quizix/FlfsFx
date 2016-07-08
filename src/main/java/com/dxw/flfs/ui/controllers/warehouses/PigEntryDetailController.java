package com.dxw.flfs.ui.controllers.warehouses;

import com.dxw.flfs.data.dal.UnitOfWork;
import com.dxw.flfs.data.models.erp.Pig;
import com.dxw.flfs.data.models.erp.Sty;
import com.dxw.flfs.data.models.erp.Vendor;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Collection;

/**
 * Created by zhang on 2016-05-26.
 */
public class PigEntryDetailController {

    @FXML
    private TextField textFieldCode;

    @FXML
    private TextField textFieldPurchaseCode;

    @FXML
    private TextField textFieldNumber;

    @FXML
    private ChoiceBox<Pig> choiceBoxPig;

    @FXML
    private ChoiceBox<Vendor> choiceBoxVendor;



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

    public Pig getPig() {
        return this.choiceBoxPig.getValue();
    }

    public Vendor getVendor() {
        return this.choiceBoxVendor.getValue();
    }

    public void setUnitOfWork(UnitOfWork unitOfWork){
        if( unitOfWork != null){

            Collection<Vendor> vendors = unitOfWork.getVendorRepository().findAll();
            this.choiceBoxVendor.setItems(FXCollections.observableArrayList(vendors) );

            Collection<Pig> pigs = unitOfWork.getPigRepository().findAll();
            this.choiceBoxPig.setItems(FXCollections.observableArrayList(pigs) );

            choiceBoxPig.getSelectionModel().select(0);
            choiceBoxVendor.getSelectionModel().select(0);

        }
    }
}
