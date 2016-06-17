package com.dxw.flfs.ui.controllers;

import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;
import com.dxw.flfs.data.HibernateService;
import com.dxw.flfs.data.dal.DefaultGenericRepository;
import com.dxw.flfs.data.dal.UnitOfWork;
import com.dxw.flfs.data.models.erp.Feed;
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
public class FeedController {

    private HibernateService hibernateService;

    private UnitOfWork unitOfWork;

    @FXML
    TableView<Feed> feedTableView;

    @FXML
    public void initialize(){
        ServiceRegistry registry = ServiceRegistryImpl.getInstance();
        hibernateService = (HibernateService)registry.getService(Services.HIBERNATE_SERVICE);
        unitOfWork = new UnitOfWork(hibernateService.getSession());

        try{
            DefaultGenericRepository<Feed> feedRepository = unitOfWork.getFeedRepository();
            Collection<Feed> feeds = feedRepository.findAll();
            feedTableView.getItems().addAll(feeds);

            Platform.runLater(() -> {
                feedTableView.requestFocus();
                feedTableView.getSelectionModel().select(0);
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

    public void onAddFeed(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/feedDetail.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("添加饲料");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);
            stage.showAndWait();

            FeedDetailController controller = loader.getController();
            if( controller.isDialogResult()){
                String name = controller.getTextFieldName();
                String code = controller.getTextFieldCode();

                Feed feed = new Feed();
                Date now = new Date();
                feed.setModifyTime(now);
                feed.setCreateTime(now);
                feed.setCode(code);
                feed.setName(name);

                unitOfWork.begin();
                unitOfWork.getFeedRepository().save(feed);
                unitOfWork.commit();

                feedTableView.getItems().add(feed);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onEditFeed(){
        Feed feed = feedTableView.getSelectionModel().getSelectedItem();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/feedDetail.fxml"));
            Parent root = loader.load();

            FeedDetailController controller = loader.getController();
            controller.setFeed(feed);
            Stage stage = new Stage();
            stage.setTitle("修改饲料");
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
                feed.setModifyTime(now);

                feed.setCode(code);
                feed.setName(name);
                unitOfWork.begin();
                unitOfWork.getFeedRepository().save(feed);
                unitOfWork.commit();

                refreshShedTable();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void onDeleteFeed(){
        //删除用户
        Feed feed = feedTableView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "确定要删除这个饲料？");
            alert.setHeaderText(null);
            alert.showAndWait().filter(response -> response == ButtonType.OK)
                    .ifPresent(response ->{
                        unitOfWork.begin();
                        unitOfWork.getFeedRepository().delete(feed);
                        unitOfWork.commit();

                        feedTableView.getItems().remove(feed);
                    });
    }



    private void refreshShedTable() {
        feedTableView.getColumns().get(0).setVisible(false);
        feedTableView.getColumns().get(0).setVisible(true);
    }
}
