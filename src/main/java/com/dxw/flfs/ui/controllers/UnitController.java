package com.dxw.flfs.ui.controllers;

import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;
import com.dxw.flfs.data.HibernateService;
import com.dxw.flfs.data.dal.DefaultGenericRepository;
import com.dxw.flfs.data.dal.UnitOfWork;
import com.dxw.flfs.data.models.erp.Unit;
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
public class UnitController {

    private HibernateService hibernateService;

    private UnitOfWork unitOfWork;

    @FXML
    TableView<Unit> unitTableView;

    @FXML
    public void initialize(){
        ServiceRegistry registry = ServiceRegistryImpl.getInstance();
        hibernateService = (HibernateService)registry.getService(Services.HIBERNATE_SERVICE);
        unitOfWork = new UnitOfWork(hibernateService.getSession());

        try{
            DefaultGenericRepository<Unit> unitRepository = unitOfWork.getUnitRepository();
            Collection<Unit> units = unitRepository.findAll();
            unitTableView.getItems().addAll(units);

            Platform.runLater(() -> {
                unitTableView.requestFocus();
                unitTableView.getSelectionModel().select(0);
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

    public void onAddUnit(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/unitDetail.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("添加单位");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);
            stage.showAndWait();

            UnitDetailController controller = loader.getController();
            if( controller.isDialogResult()){
                String name = controller.getTextFieldName();
                String symbol = controller.getTextFieldSymbol();

                Unit unit = new Unit();
                Date now = new Date();
                unit.setModifyTime(now);
                unit.setCreateTime(now);
                unit.setName(name);
                unit.setSymbol(symbol);

                unitOfWork.begin();
                unitOfWork.getUnitRepository().save(unit);
                unitOfWork.commit();

                unitTableView.getItems().add(unit);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onEditUnit(){
        Unit unit = unitTableView.getSelectionModel().getSelectedItem();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/unitDetail.fxml"));
            Parent root = loader.load();

            UnitDetailController controller = loader.getController();
            controller.setUnit(unit);
            Stage stage = new Stage();
            stage.setTitle("修改单位");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);
            stage.showAndWait();

            if( controller.isDialogResult()){
                String name = controller.getTextFieldName();
                String symbol = controller.getTextFieldSymbol();

                Date now = new Date();
                unit.setModifyTime(now);

                unit.setSymbol(symbol);
                unit.setName(name);
                unitOfWork.begin();
                unitOfWork.getUnitRepository().save(unit);
                unitOfWork.commit();

                refreshShedTable();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void onDeleteUnit(){
        Unit unit = unitTableView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "确定要删除这个单位？");
            alert.setHeaderText(null);
            alert.showAndWait().filter(response -> response == ButtonType.OK)
                    .ifPresent(response ->{
                        unitOfWork.begin();
                        unitOfWork.getUnitRepository().delete(unit);
                        unitOfWork.commit();

                        unitTableView.getItems().remove(unit);
                    });
    }



    private void refreshShedTable() {
        unitTableView.getColumns().get(0).setVisible(false);
        unitTableView.getColumns().get(0).setVisible(true);
    }
}
