package com.dxw.flfs.ui.controllers;

import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;
import com.dxw.flfs.app.FlfsApp;
import com.dxw.flfs.data.HibernateService;
import com.dxw.flfs.data.dal.DefaultGenericRepository;
import com.dxw.flfs.data.dal.UnitOfWork;
import com.dxw.flfs.data.models.erp.FeedWarehouse;
import com.dxw.flfs.data.models.mes.Site;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Date;

/**
 * Created by zhang on 2016-05-26.
 */
public class StockManagementController {

    private HibernateService hibernateService;

    private UnitOfWork unitOfWork;

    @FXML
    TableView<FeedWarehouse> feedWarehouseTableView;


    @FXML
    public void initialize(){
        ServiceRegistry registry = ServiceRegistryImpl.getInstance();
        hibernateService = (HibernateService)registry.getService(Services.HIBERNATE_SERVICE);
        unitOfWork = new UnitOfWork(hibernateService.getSession());

        try{
            String siteCode = FlfsApp.getContext().getSiteCode();

            DefaultGenericRepository<Site> repository = unitOfWork.getSiteRepository();
            Site site = repository.findByNaturalId(siteCode);
            feedWarehouseTableView.getItems().addAll(site.getFeedWarehouses());

            Platform.runLater(() -> {
                feedWarehouseTableView.requestFocus();
                feedWarehouseTableView.getSelectionModel().select(0);
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

    public void onStockIn(){
        FeedWarehouse warehouse = feedWarehouseTableView.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/stockInOut.fxml"));
            Parent root = loader.load();

            StockInOutController controller = loader.getController();
            controller.setWarehouse(warehouse);

            Stage stage = new Stage();
            stage.setTitle("入库");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);
            stage.showAndWait();


            if( controller.isDialogResult()){

                float quantity = controller.getQuantity();

                Date now = new Date();
                warehouse.setModifyTime(now);
                warehouse.addQuantity(quantity);

                unitOfWork.begin();
                unitOfWork.getFeedWarehouseRepository().save(warehouse);
                unitOfWork.commit();

                refreshShedTable();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onStockOut(){
        FeedWarehouse warehouse = feedWarehouseTableView.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/stockInOut.fxml"));
            Parent root = loader.load();

            StockInOutController controller = loader.getController();
            controller.setWarehouse(warehouse);

            Stage stage = new Stage();
            stage.setTitle("出库");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);
            stage.showAndWait();


            if( controller.isDialogResult()){

                float quantity = controller.getQuantity();

                Date now = new Date();
                warehouse.setModifyTime(now);
                warehouse.subQuantity(quantity);

                unitOfWork.begin();
                unitOfWork.getFeedWarehouseRepository().save(warehouse);
                unitOfWork.commit();

                refreshShedTable();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onEditVendor(){
        /*Vendor vendor = styTableView.getSelectionModel().getSelectedItem();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/vendorDetail.fxml"));
            Parent root = loader.load();

            VendorDetailController controller = loader.getController();
            controller.setSty(vendor);
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
        }*/
    }
    public void onDeleteVendor(){
        /*Vendor vendor = styTableView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "确定要删除这个供应商？");
            alert.setHeaderText(null);
            alert.showAndWait().filter(response -> response == ButtonType.OK)
                    .ifPresent(response ->{
                        unitOfWork.begin();
                        unitOfWork.getVendorRepository().delete(vendor);
                        unitOfWork.commit();

                        styTableView.getItems().remove(vendor);
                    });*/
    }



    private void refreshShedTable() {
        feedWarehouseTableView.getColumns().get(0).setVisible(false);
        feedWarehouseTableView.getColumns().get(0).setVisible(true);
    }
}
