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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;

/**
 * Created by zhang on 2016-05-28.
 */
public class StartupWizardPage2Controller {
    @FXML
    private TableView<PigletPlan> tableView;

    private UnitOfWork unitOfWork;

    @FXML
    public void initialize() {
        ServiceRegistry registry = ServiceRegistryImpl.getInstance();
        HibernateService hibernateService = (HibernateService) registry.getService(Services.HIBERNATE_SERVICE);
        unitOfWork = new UnitOfWork(hibernateService.getSession());
        try {
            DefaultGenericRepository<PigletPlan> repository = unitOfWork.getPigletPlanRepository();
            Collection<PigletPlan> plans = repository.findAll();
            tableView.getItems().addAll(plans);

            Platform.runLater(() -> {
                tableView.requestFocus();
                tableView.getSelectionModel().select(0);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onAdd() {
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

            if (controller.isDialogResult()) {

                PigletPlan plan = new PigletPlan();

                Date now = new Date();
                plan.setModifyTime(now);
                plan.setCreateTime(now);
                plan.setCount(controller.getCount());
                plan.setDate(controller.getDate());

                unitOfWork.begin();
                unitOfWork.getPigletPlanRepository().save(plan);
                unitOfWork.commit();

                tableView.getItems().add(plan);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onEdit() {
        PigletPlan plan = tableView.getSelectionModel().getSelectedItem();
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

            if (controller.isDialogResult()) {
                plan.setModifyTime(new Date());
                plan.setCount(controller.getCount());
                plan.setDate(controller.getDate());

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
        tableView.getColumns().get(0).setVisible(false);
        tableView.getColumns().get(0).setVisible(true);
    }

    @FXML
    public void onDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "确定要删除这个入栏计划？");
        alert.setHeaderText(null);
        alert.showAndWait().filter(response -> response == ButtonType.OK)
                .ifPresent(response -> {
                    PigletPlan plan = tableView.getSelectionModel().getSelectedItem();

                    try {
                        unitOfWork.begin();
                        unitOfWork.getPigletPlanRepository().delete(plan);
                        unitOfWork.commit();

                        tableView.getItems().remove(plan);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });


    }

    public void setUnitOfWork(UnitOfWork unitOfWork) {
        if (this.unitOfWork == null)
            return;

        this.unitOfWork = unitOfWork;
    }
}
