package com.dxw.flfs.ui.controllers;

import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;
import com.dxw.flfs.data.HibernateService;
import com.dxw.flfs.data.dal.DefaultGenericRepository;
import com.dxw.flfs.data.dal.UnitOfWork;
import com.dxw.flfs.data.models.erp.Pig;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;

/**
 * Created by zhang on 2016-05-26.
 */
public class PigController {

    private HibernateService hibernateService;

    private UnitOfWork unitOfWork;

    @FXML
    TableView<Pig> pigTableView;

    @FXML
    public void initialize(){
        ServiceRegistry registry = ServiceRegistryImpl.getInstance();
        hibernateService = (HibernateService)registry.getService(Services.HIBERNATE_SERVICE);
        unitOfWork = new UnitOfWork(hibernateService.getSession());

        try{
            DefaultGenericRepository<Pig> pigRepository = unitOfWork.getPigRepository();
            Collection<Pig> pigs = pigRepository.findAll();
            pigTableView.getItems().addAll(pigs);

            Platform.runLater(() -> {
                pigTableView.requestFocus();
                pigTableView.getSelectionModel().select(0);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dispose() {
        if( unitOfWork != null){
            try {
                unitOfWork.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onAddPig(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/pigDetail.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("添加猪品种");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);
            stage.showAndWait();

            PigDetailController controller = loader.getController();
            if( controller.isDialogResult()){
                String name = controller.getTextFieldName();
                String code = controller.getTextFieldCode();

                Pig pig = new Pig();
                Date now = new Date();
                pig.setModifyTime(now);
                pig.setCreateTime(now);
                pig.setCode(code);
                pig.setName(name);

                unitOfWork.begin();
                unitOfWork.getPigRepository().save(pig);
                unitOfWork.commit();

                pigTableView.getItems().add(pig);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onEditPig(){
        Pig pig = pigTableView.getSelectionModel().getSelectedItem();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/pigDetail.fxml"));
            Parent root = loader.load();

            PigDetailController controller = loader.getController();
            controller.setUser(pig);
            Stage stage = new Stage();
            stage.setTitle("修改猪种");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);
            stage.showAndWait();

            if( controller.isDialogResult()){
                String name = controller.getTextFieldName();
                String code = controller.getTextFieldCode();

                Date now = new Date();
                pig.setModifyTime(now);

                pig.setCode(code);
                pig.setName(name);
                unitOfWork.begin();
                unitOfWork.getPigRepository().save(pig);
                unitOfWork.commit();

                refreshShedTable();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void onDeletePig(){
        //删除用户
        Pig pig = pigTableView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "确定要删除这个猪种？");
            alert.setHeaderText(null);
            alert.showAndWait().filter(response -> response == ButtonType.OK)
                    .ifPresent(response ->{
                        unitOfWork.begin();
                        unitOfWork.getPigRepository().delete(pig);
                        unitOfWork.commit();

                        pigTableView.getItems().remove(pig);
                    });
    }



    private void refreshShedTable() {
        pigTableView.getColumns().get(0).setVisible(false);
        pigTableView.getColumns().get(0).setVisible(true);
    }
}
