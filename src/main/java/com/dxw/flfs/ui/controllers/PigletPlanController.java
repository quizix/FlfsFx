package com.dxw.flfs.ui.controllers;

import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;
import com.dxw.flfs.data.HibernateService;
import com.dxw.flfs.data.dal.DefaultGenericRepository;
import com.dxw.flfs.data.dal.UnitOfWork;
import com.dxw.flfs.data.models.PigletPlan;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;

/**
 * Created by zhang on 2016-05-27.
 */
public class PigletPlanController {

    private HibernateService hibernateService;
    private UnitOfWork unitOfWork;

    @FXML
    private TableView<PigletPlan> tableViewPlan;

    @FXML
    public void initialize(){
        ServiceRegistry registry = ServiceRegistryImpl.getInstance();
        hibernateService = (HibernateService)registry.getService(Services.HIBERNATE_SERVICE);
        unitOfWork = new UnitOfWork(hibernateService.getSession());

        try{
            DefaultGenericRepository<PigletPlan> shedRepository = unitOfWork.getPigletPlanRepository();
            Collection<PigletPlan> plans = shedRepository.findAll();
            tableViewPlan.getItems().addAll(plans);

            Platform.runLater(() -> {
                tableViewPlan.requestFocus();
                tableViewPlan.getSelectionModel().select(0);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        tableViewPlan.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {

                }
        );
    }

    @FXML
    public void onAdd(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader()
                    .getResource("ui/dialogs/pigletPlanDetail.fxml"));
            Parent root = loader.load();

            PigletPlanDetailController controller = loader.getController();
            controller.setPigletPlan(null);


            Stage stage = new Stage();
            stage.setTitle("添加小猪入栏计划");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);
            controller.setStage(stage);

            stage.showAndWait();

            if( controller.isDialogResult()){

                PigletPlan plan = new PigletPlan();

                Date now = new Date();
                plan.setModifyTime(now);
                plan.setCreateTime(now);
                plan.setCount( controller.getCount());
                plan.setDate(controller.getDate());

                unitOfWork.begin();
                unitOfWork.getPigletPlanRepository().save(plan);
                unitOfWork.commit();

                tableViewPlan.getItems().add(plan);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onEdit(){
        PigletPlan plan = tableViewPlan.getSelectionModel().getSelectedItem();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader()
                    .getResource("ui/dialogs/pigletPlanDetail.fxml"));
            Parent root = loader.load();

            PigletPlanDetailController controller = loader.getController();
            controller.setPigletPlan(null);
            controller.setPigletPlan(plan);
            Stage stage = new Stage();
            stage.setTitle("修改小猪入栏计划");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);
            controller.setStage(stage);

            stage.showAndWait();

            if( controller.isDialogResult()){
                plan.setModifyTime(new Date());

                plan.setCount( controller.getCount());
                plan.setDate( controller.getDate());

                unitOfWork.begin();
                unitOfWork.getPigletPlanRepository().save(plan);
                unitOfWork.commit();

                refreshTable();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshTable() {
        tableViewPlan.getColumns().get(0).setVisible(false);
        tableViewPlan.getColumns().get(0).setVisible(true);
    }

    @FXML
    public void onFinish(){

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
}
