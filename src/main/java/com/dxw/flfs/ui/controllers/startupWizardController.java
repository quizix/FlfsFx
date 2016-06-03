package com.dxw.flfs.ui.controllers;

import com.dxw.flfs.data.dal.UnitOfWork;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Created by zhang on 2016-05-27.
 */
public class StartupWizardController {
    private boolean dialogResult = false;

    private int currentPage = 0;
    private int pages = 0;
    private UnitOfWork unitOfWork;

    /*
        注意：
        根据http://stackoverflow.com/questions/12543487/javafx-nested-controllers-fxml-include
        the names of my controller variables should be <fx:id>Controller.
     */
    @FXML
    StartupWizardPage2Controller startupWizardPage2Controller;

    @FXML
    StartupWizardPage1Controller startupWizardPage1Controller;

    @FXML
    StackPane stackPane;

    @FXML
    Button buttonNext;

    @FXML
    Button buttonPrev;

    @FXML
    Button buttonOk;

    @FXML
    public void initialize() {
        pages = stackPane.getChildren().size();
    }

    @FXML
    public void onPrev() {
        if (currentPage > 0) {
            currentPage--;

            enableButtons(currentPage);
            setTitle(currentPage);

            for (int i = 0; i < stackPane.getChildren().size(); i++) {
                Node node = stackPane.getChildren().get(i);
                if (i == currentPage)
                    node.setVisible(true);
                else
                    node.setVisible(false);
            }
        }
    }



    @FXML
    public void onNext() {
        if (currentPage < stackPane.getChildren().size() - 1) {
            currentPage++;

            enableButtons(currentPage);
            setTitle(currentPage);
            for (int i = 0; i < stackPane.getChildren().size(); i++) {
                Node node = stackPane.getChildren().get(i);
                if (i == currentPage)
                    node.setVisible(true);
                else
                    node.setVisible(false);
            }
        }
    }

    private void enableButtons(int currentPage) {
        buttonPrev.setDisable(currentPage==0?true:false);
        buttonNext.setDisable(currentPage==pages-1?true:false);
        buttonOk.setDisable(currentPage==pages-1?false:true);
    }

    @FXML
    public void onOk() {
        dialogResult = true;
        this.close();
    }

    @FXML
    void onCancel() {
        dialogResult = false;
        this.close();
    }

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void close() {
        if (stage != null)
            stage.close();

        this.dispose();
    }

    private void dispose() {
        /*if(unitOfWork !=null){
            try {
                unitOfWork.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/

    }

    public boolean isDialogResult() {
        return dialogResult;
    }

    public void setUnitOfWork(UnitOfWork unitOfWork) {
        if (this.unitOfWork != null)
            return;

        this.unitOfWork = unitOfWork;

        this.startupWizardPage1Controller.setUnitOfWork(unitOfWork);
        this.startupWizardPage2Controller.setUnitOfWork(unitOfWork);
    }

    public void setTitle(int currentPage) {
        if( currentPage == 0)
            this.stage.setTitle("配置向导-第一步：设置栏位关联");
        else if( currentPage == 1)
            this.stage.setTitle("配置向导-第一步：设置小猪入栏计划");
    }
}
