package com.dxw.flfs.ui.controllers;

import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;
import com.dxw.flfs.app.FlfsApp;
import com.dxw.flfs.data.HibernateService;
import com.dxw.flfs.data.dal.DefaultGenericRepository;
import com.dxw.flfs.data.dal.UnitOfWork;
import com.dxw.flfs.data.models.erp.Sty;
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
 * 栏位管理：
 * 猪的入栏，出栏，移栏，死猪处理
 * Created by zhang on 2016-05-26.
 */
public class StyManagementController {

    private HibernateService hibernateService;

    private UnitOfWork unitOfWork;

    @FXML
    TableView<Sty> styTableView;


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

    public void onPigIn(){
       Sty sty = styTableView.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/pigInOut.fxml"));
            Parent root = loader.load();

            PigInOutController controller = loader.getController();
            controller.setSty(sty);

            Stage stage = new Stage();
            stage.setTitle("入栏");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);
            stage.showAndWait();

            if( controller.isDialogResult()){
                int quantity = controller.getNumber();

                Date now = new Date();
                sty.setModifyTime(now);
                sty.addCurrent(quantity);

                unitOfWork.begin();
                unitOfWork.getStyRepository().save(sty);
                unitOfWork.commit();

                refreshShedTable();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onPigOut(){
        Sty sty = styTableView.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/pigInOut.fxml"));
            Parent root = loader.load();

            PigInOutController controller = loader.getController();
            controller.setSty(sty);

            Stage stage = new Stage();
            stage.setTitle("出栏");
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

                refreshShedTable();

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

                refreshShedTable();

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
            controller.setSty(site.getSties());

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

                refreshShedTable();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    private void refreshShedTable() {
        styTableView.getColumns().get(0).setVisible(false);
        styTableView.getColumns().get(0).setVisible(true);
    }
}
