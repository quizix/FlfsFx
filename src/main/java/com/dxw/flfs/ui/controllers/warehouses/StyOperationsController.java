package com.dxw.flfs.ui.controllers.warehouses;

import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;
import com.dxw.flfs.app.FlfsApp;
import com.dxw.flfs.data.HibernateService;
import com.dxw.flfs.data.dal.DefaultGenericRepository;
import com.dxw.flfs.data.dal.UnitOfWork;
import com.dxw.flfs.data.models.erp.Sty;
import com.dxw.flfs.data.models.mes.PigDelivery;
import com.dxw.flfs.data.models.mes.PigEntry;
import com.dxw.flfs.data.models.mes.Site;
import com.dxw.flfs.ui.controllers.PigInOutController;
import com.dxw.flfs.ui.controllers.PigTransferController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Date;
import java.util.Set;

/**
 * 栏位管理：
 * 猪的入栏，出栏，移栏，死猪处理
 * Created by zhang on 2016-05-26.
 */
public class StyOperationsController {

    private HibernateService hibernateService;

    private UnitOfWork unitOfWork;

    @FXML
    TableView<Sty> styTableView;

    @FXML
    TableView<PigEntry> pigEntryTableView;

    @FXML
    TableView<PigDelivery> pigDeliveryTableView;

    @FXML
    TabPane tabPane;

    @FXML
    public void initialize(){
        ServiceRegistry registry = ServiceRegistryImpl.getInstance();
        hibernateService = (HibernateService)registry.getService(Services.HIBERNATE_SERVICE);
        unitOfWork = new UnitOfWork(hibernateService.getSession());

        try{
            String siteCode = FlfsApp.getContext().getSiteCode();

            DefaultGenericRepository<Site> repository = unitOfWork.getSiteRepository();
            Site site = repository.findByNaturalId(siteCode);
            styTableView.getItems().addAll(site.getSties());

            Platform.runLater(() -> {
                styTableView.requestFocus();
                styTableView.getSelectionModel().select(0);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        styTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    switch (tabPane.getSelectionModel().getSelectedIndex()){
                        case 0:
                            pigEntryTableView.getItems().clear();
                            if(newValue != null){
                                Set<PigEntry> pigEntries = newValue.getPigEntries();
                                if( pigEntries!=null)
                                    pigEntryTableView.getItems().addAll(pigEntries);
                            }
                            break;
                        case 1:
                            pigDeliveryTableView.getItems().clear();
                            if(newValue != null){
                                Set<PigDelivery> pigDeliveries = newValue.getPigDeliveries();
                                if( pigDeliveries!=null)
                                    pigDeliveryTableView.getItems().addAll(pigDeliveries);
                            }
                            break;
                    }

                }
        );
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

    public void onAddPigEntry(){
       Sty sty = styTableView.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/warehouses/pigEntryDetail.fxml"));
            Parent root = loader.load();

            PigEntryDetailController controller = loader.getController();
            controller.setSty(sty);
            controller.setUnitOfWork(unitOfWork);

            Stage stage = new Stage();
            stage.setTitle("新增入栏表");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);
            stage.showAndWait();

            if( controller.isDialogResult()){

                Date now = new Date();
                sty.setModifyTime(now);

                PigEntry pigEntry = new PigEntry();
                pigEntry.setCreateTime(now);
                pigEntry.setModifyTime(now);
                pigEntry.setCode(controller.getCode());
                pigEntry.setPurchaseCode(controller.getPurchaseCode());
                pigEntry.setPig(controller.getPig());
                pigEntry.setNumber(controller.getNumber());
                pigEntry.setVendor(controller.getVendor());

                sty.addPigEntry(pigEntry);

                try {
                    unitOfWork.begin();
                    unitOfWork.getStyRepository().save(sty);
                    unitOfWork.commit();
                    refreshStyTable();
                }
                catch(Exception ex){
                    unitOfWork.rollback();
                }

                pigEntryTableView.getItems().add(pigEntry);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onAddPigDelivery(){
        Sty sty = styTableView.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/warehouses/pigDeliveryDetail.fxml"));
            Parent root = loader.load();

            PigDeliveryDetailController controller = loader.getController();
            controller.setSty(sty);

            Stage stage = new Stage();
            stage.setTitle("新增出栏表");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);
            stage.showAndWait();

            if( controller.isDialogResult()){

                Date now = new Date();
                sty.setModifyTime(now);

                PigDelivery pigDelivery = new PigDelivery();
                pigDelivery.setCreateTime(now);
                pigDelivery.setModifyTime(now);
                pigDelivery.setCode(controller.getCode());
                pigDelivery.setNumber(controller.getNumber());
                sty.addPigDelivery(pigDelivery);

                try {
                    unitOfWork.begin();
                    unitOfWork.getStyRepository().save(sty);
                    unitOfWork.commit();
                    refreshStyTable();
                }
                catch(Exception ex){
                    unitOfWork.rollback();
                }

                pigDeliveryTableView.getItems().add(pigDelivery);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void onPigDead(){
        Sty sty = styTableView.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/pigInOut.fxml"));
            Parent root = loader.load();

            PigInOutController controller = loader.getController();
            controller.setSty(sty);

            Stage stage = new Stage();
            stage.setTitle("死猪处理");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);
            stage.showAndWait();

            if( controller.isDialogResult()){
                int number = controller.getNumber();

                Date now = new Date();
                sty.setModifyTime(now);
                sty.subCurrent(number);

                unitOfWork.begin();
                unitOfWork.getStyRepository().save(sty);
                unitOfWork.commit();

                refreshStyTable();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void onPigTransfer(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/pigTransfer.fxml"));
            Parent root = loader.load();

            String siteCode = FlfsApp.getContext().getSiteCode();
            DefaultGenericRepository<Site> repository = unitOfWork.getSiteRepository();
            Site site = repository.findByNaturalId(siteCode);

            PigTransferController controller = loader.getController();
            controller.setSties(site.getSties());

            Stage stage = new Stage();
            stage.setTitle("移栏");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);
            stage.showAndWait();

            if( controller.isDialogResult()){
                int number = controller.getNumber();
                Sty from = controller.getFromSty();
                Sty to = controller.getToSty();
                Date now = new Date();
                from.subCurrent(number);
                from.setModifyTime(now);
                to.addCurrent(number);
                to.setModifyTime(now);

                unitOfWork.begin();
                unitOfWork.getStyRepository().save(from);
                unitOfWork.getStyRepository().save(to);
                unitOfWork.commit();

                refreshStyTable();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshStyTable() {
        styTableView.getColumns().get(0).setVisible(false);
        styTableView.getColumns().get(0).setVisible(true);
    }


}
