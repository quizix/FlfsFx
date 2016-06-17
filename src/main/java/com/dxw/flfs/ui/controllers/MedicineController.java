package com.dxw.flfs.ui.controllers;

import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;
import com.dxw.flfs.data.HibernateService;
import com.dxw.flfs.data.dal.DefaultGenericRepository;
import com.dxw.flfs.data.dal.UnitOfWork;
import com.dxw.flfs.data.models.erp.Medicine;
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
public class MedicineController {

    private HibernateService hibernateService;

    private UnitOfWork unitOfWork;

    @FXML
    TableView<Medicine> medicineTableView;

    @FXML
    public void initialize(){
        ServiceRegistry registry = ServiceRegistryImpl.getInstance();
        hibernateService = (HibernateService)registry.getService(Services.HIBERNATE_SERVICE);
        unitOfWork = new UnitOfWork(hibernateService.getSession());

        try{
            DefaultGenericRepository<Medicine> medicineRepository = unitOfWork.getMedicineRepository();
            Collection<Medicine> medicines = medicineRepository.findAll();
            medicineTableView.getItems().addAll(medicines);

            Platform.runLater(() -> {
                medicineTableView.requestFocus();
                medicineTableView.getSelectionModel().select(0);
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

    public void onAddMedicine(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/medicineDetail.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("添加药品");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);
            stage.showAndWait();

            MedicineDetailController controller = loader.getController();
            if( controller.isDialogResult()){
                String name = controller.getTextFieldName();
                String code = controller.getTextFieldCode();

                Medicine medicine = new Medicine();
                Date now = new Date();
                medicine.setModifyTime(now);
                medicine.setCreateTime(now);
                medicine.setCode(code);
                medicine.setName(name);

                unitOfWork.begin();
                unitOfWork.getMedicineRepository().save(medicine);
                unitOfWork.commit();

                medicineTableView.getItems().add(medicine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onEditMedicine(){
        Medicine medicine = medicineTableView.getSelectionModel().getSelectedItem();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/medicineDetail.fxml"));
            Parent root = loader.load();

            MedicineDetailController controller = loader.getController();
            controller.setMedicine(medicine);
            Stage stage = new Stage();
            stage.setTitle("修改药品");
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
                medicine.setModifyTime(now);

                medicine.setCode(code);
                medicine.setName(name);
                unitOfWork.begin();
                unitOfWork.getMedicineRepository().save(medicine);
                unitOfWork.commit();

                refreshShedTable();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void onDeleteMedicine(){
        //删除用户
        Medicine medicine = medicineTableView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "确定要删除这个药品？");
            alert.setHeaderText(null);
            alert.showAndWait().filter(response -> response == ButtonType.OK)
                    .ifPresent(response ->{
                        unitOfWork.begin();
                        unitOfWork.getMedicineRepository().delete(medicine);
                        unitOfWork.commit();

                        medicineTableView.getItems().remove(medicine);
                    });
    }



    private void refreshShedTable() {
        medicineTableView.getColumns().get(0).setVisible(false);
        medicineTableView.getColumns().get(0).setVisible(true);
    }
}
