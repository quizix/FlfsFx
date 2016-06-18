package com.dxw.flfs.ui.controllers;

import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;
import com.dxw.flfs.data.HibernateService;
import com.dxw.flfs.data.dal.DefaultGenericRepository;
import com.dxw.flfs.data.dal.UnitOfWork;
import com.dxw.flfs.data.models.erp.Vendor;
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
public class VendorController {

    private HibernateService hibernateService;

    private UnitOfWork unitOfWork;

    @FXML
    TableView<Vendor> vendorTableView;

    @FXML
    public void initialize(){
        ServiceRegistry registry = ServiceRegistryImpl.getInstance();
        hibernateService = (HibernateService)registry.getService(Services.HIBERNATE_SERVICE);
        unitOfWork = new UnitOfWork(hibernateService.getSession());

        try{
            DefaultGenericRepository<Vendor> vendorRepository = unitOfWork.getVendorRepository();
            Collection<Vendor> vendors = vendorRepository.findAll();
            vendorTableView.getItems().addAll(vendors);

            Platform.runLater(() -> {
                vendorTableView.requestFocus();
                vendorTableView.getSelectionModel().select(0);
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

    public void onAddVendor(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/vendorDetail.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("添加供应商");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);
            stage.showAndWait();

            VendorDetailController controller = loader.getController();
            if( controller.isDialogResult()){
                String name = controller.getTextFieldName();
                String code = controller.getTextFieldCode();

                Vendor vendor = new Vendor();
                Date now = new Date();
                vendor.setModifyTime(now);
                vendor.setCreateTime(now);
                vendor.setCode(code);
                vendor.setName(name);

                unitOfWork.begin();
                unitOfWork.getVendorRepository().save(vendor);
                unitOfWork.commit();

                vendorTableView.getItems().add(vendor);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onEditVendor(){
        Vendor vendor = vendorTableView.getSelectionModel().getSelectedItem();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/vendorDetail.fxml"));
            Parent root = loader.load();

            VendorDetailController controller = loader.getController();
            controller.setVendor(vendor);
            Stage stage = new Stage();
            stage.setTitle("修改供应商");
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
                vendor.setModifyTime(now);

                vendor.setCode(code);
                vendor.setName(name);
                unitOfWork.begin();
                unitOfWork.getVendorRepository().save(vendor);
                unitOfWork.commit();

                refreshShedTable();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void onDeleteVendor(){
        Vendor vendor = vendorTableView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "确定要删除这个供应商？");
            alert.setHeaderText(null);
            alert.showAndWait().filter(response -> response == ButtonType.OK)
                    .ifPresent(response ->{
                        unitOfWork.begin();
                        unitOfWork.getVendorRepository().delete(vendor);
                        unitOfWork.commit();

                        vendorTableView.getItems().remove(vendor);
                    });
    }



    private void refreshShedTable() {
        vendorTableView.getColumns().get(0).setVisible(false);
        vendorTableView.getColumns().get(0).setVisible(true);
    }
}
