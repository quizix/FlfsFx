package com.dxw.flfs.ui.controllers.wizards;

import com.dxw.flfs.app.FlfsApp;
import com.dxw.flfs.data.dal.DefaultGenericRepository;
import com.dxw.flfs.data.dal.UnitOfWork;
import com.dxw.flfs.data.models.erp.Shed;
import com.dxw.flfs.data.models.erp.FeedWarehouse;
import com.dxw.flfs.data.models.mes.Site;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by zhang on 2016-05-29.
 */
public class ChooseFeedWarehousesController {

    private Stage stage;
    private UnitOfWork unitOfWork;

    @FXML
    private TableView<FeedWarehouse> tableViewFeedWarehouse;

    @FXML
    private TableView<Shed> tableViewShed;

    private Set<FeedWarehouse> feedWarehouses;

    public Set<FeedWarehouse> getSelectedFeedWarehouses(){
        return feedWarehouses;
    }

    @FXML
    public void initialize(){
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    private boolean dialogResult;
    public void onOk(){
        dialogResult = true;
        feedWarehouses = new HashSet<>();
        ObservableList<FeedWarehouse> selectedItems =
                tableViewFeedWarehouse.getItems();

        feedWarehouses.addAll(selectedItems.stream().filter(warehouse -> warehouse.getChecked()).collect(Collectors.toList()));

        this.close();
    }

    public void onCancel(){
        dialogResult = false;
        this.close();
    }

    private void close(){
        if( stage != null)
            stage.close();

    }

    public boolean isDialogResult() {
        return dialogResult;
    }

    public void setUnitOfWork(UnitOfWork unitOfWork){
        if(this.unitOfWork != null)
            return;

        this.unitOfWork = unitOfWork;

        try{
            unitOfWork.begin();
            DefaultGenericRepository<Shed> shedRepository = unitOfWork.getShedRepository();
            Collection<Shed> sheds = shedRepository.findAll();
            tableViewShed.getItems().addAll(sheds);
            unitOfWork.commit();

            Platform.runLater(() -> {
                tableViewShed.requestFocus();
                tableViewShed.getSelectionModel().select(0);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        tableViewShed.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    tableViewFeedWarehouse.getItems().clear();
                    if(newValue != null){
                        unitOfWork.begin();
                        Set<FeedWarehouse> sties = newValue.getFeedWarehouses();
                        DefaultGenericRepository<Site> siteRepository = unitOfWork.getSiteRepository();
                        Site site = siteRepository.findByNaturalId(FlfsApp.getContext().getSiteCode());
                        unitOfWork.commit();

                        if( sties!=null)
                            tableViewFeedWarehouse.getItems().addAll(
                                    sties.stream()
                                            .filter(warehouse-> !site.getFeedWarehouses().contains(warehouse))
                                            .map(warehouse->{warehouse.setChecked(false); return warehouse;})
                                            .collect(Collectors.toSet())

                            );
                    }
                }
        );
    }
}
