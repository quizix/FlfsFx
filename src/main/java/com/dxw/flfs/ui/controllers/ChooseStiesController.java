package com.dxw.flfs.ui.controllers;

import com.dxw.flfs.app.FlfsApp;
import com.dxw.flfs.data.dal.DefaultGenericRepository;
import com.dxw.flfs.data.dal.UnitOfWork;
import com.dxw.flfs.data.models.Shed;
import com.dxw.flfs.data.models.Site;
import com.dxw.flfs.data.models.Sty;
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
public class ChooseStiesController {

    private Stage stage;
    private UnitOfWork unitOfWork;

    @FXML
    private TableView<Sty> tableViewSty;

    @FXML
    private TableView<Shed> tableViewShed;

    private Set<Sty> sties;

    public Set<Sty> getSelectedSties(){
        return sties;
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
        sties = new HashSet<>();
        ObservableList<Sty> selectedItems =
                tableViewSty.getItems();

        sties.addAll(selectedItems.stream().filter(sty -> sty.getChecked()).collect(Collectors.toList()));

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
                    tableViewSty.getItems().clear();
                    if(newValue != null){

                        unitOfWork.begin();
                        Set<Sty> sties = newValue.getSties();
                        DefaultGenericRepository<Site> siteRepository = unitOfWork.getSiteConfigRepository();
                        Site site = siteRepository.findByNaturalId(FlfsApp.getContext().getSiteCode());
                        unitOfWork.commit();
                        //sties.removeAll(site.getSties());


                        if( sties!=null)
                            tableViewSty.getItems().addAll(
                                    sties.stream()
                                            .filter(sty-> !site.getSties().contains(sty))
                                            .collect(Collectors.toList())
                            );
                    }
                }
        );
    }
}
