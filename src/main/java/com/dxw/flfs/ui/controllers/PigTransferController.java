package com.dxw.flfs.ui.controllers;

import com.dxw.flfs.data.models.erp.Sty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Collection;

/**
 * Created by zhang on 2016-07-02.
 */
public class PigTransferController {
    @FXML
    private ChoiceBox<Sty> choiceBoxFrom;

    @FXML
    private ChoiceBox<Sty> choiceBoxTo;

    @FXML
    private TextField textFieldQuantity;

    @FXML
    private Button buttonCancel;

    @FXML
    private Button buttonOk;

    @FXML
    public void initialize(){

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

    public Sty getFromSty(){
        return this.choiceBoxFrom.getValue();
    }

    public Sty getToSty(){
        return  this.choiceBoxTo.getValue();
    }

    public void setSty(Collection<Sty> sties) {
        if(sties != null){
            choiceBoxFrom.setItems( FXCollections.observableArrayList(sties));
            choiceBoxTo.setItems( FXCollections.observableArrayList(sties));

            choiceBoxFrom.getSelectionModel().select(0);
            choiceBoxTo.getSelectionModel().select(0);
        }
    }
}
