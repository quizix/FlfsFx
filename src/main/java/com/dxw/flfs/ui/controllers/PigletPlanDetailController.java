package com.dxw.flfs.ui.controllers;

import com.dxw.common.utils.TimeUtil;
import com.dxw.flfs.data.models.mes.PigletPlan;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Date;

/**
 * Created by zhang on 2016-05-27.
 */
public class PigletPlanDetailController {
    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField textFieldCount;



    private boolean dialogResult;

    @FXML
    public void onOk() {
        this.dialogResult = true;
        close();
    }

    @FXML
    public void onCancel() {
        this.dialogResult = false;
        close();
    }

    @FXML
    public void initialize(){
        Platform.runLater(() -> textFieldCount.requestFocus());
    }

    private PigletPlan plan;
    private Stage stage;

    public void setPigletPlan(PigletPlan plan) {
        this.plan = plan;

        if (this.plan != null) {
            this.datePicker.setValue(TimeUtil.toLocalDate(plan.getDate()));
            this.textFieldCount.setText(Integer.toString(plan.getCount()));
        } else {
            this.datePicker.setValue(LocalDate.now());
            this.textFieldCount.setText("100");
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public boolean isDialogResult() {
        return dialogResult;
    }

    private void close() {
        if (stage != null) {
            stage.close();
        }
    }

    public Date getDate(){
        return TimeUtil.toDate( this.datePicker.getValue());
    }

    public int getCount(){
        return Integer.parseInt( this.textFieldCount.getText());
    }
}
