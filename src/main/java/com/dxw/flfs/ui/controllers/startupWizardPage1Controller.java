package com.dxw.flfs.ui.controllers;

import com.dxw.flfs.app.FlfsApp;
import com.dxw.flfs.data.dal.DefaultGenericRepository;
import com.dxw.flfs.data.dal.UnitOfWork;
import com.dxw.flfs.data.models.Site;
import com.dxw.flfs.data.models.Sty;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

/**
 * Created by zhang on 2016-05-28.
 */
public class StartupWizardPage1Controller {

    private UnitOfWork unitOfWork;

    @FXML
    private TableView<Sty> tableView;

    @FXML
    public void initialize(){
    }

    public void setUnitOfWork(UnitOfWork unitOfWork){
        if( this.unitOfWork != null)
            return;
        this.unitOfWork = unitOfWork;

        String siteCode = FlfsApp.getContext().getSiteCode();

        try{
            DefaultGenericRepository<Site> repository = unitOfWork.getSiteConfigRepository();
            Site site = repository.findByNaturalId(siteCode);
            Collection<Sty> sties = site.getSties();

            tableView.getItems().addAll(sties);
            tableView.getSelectionModel().setSelectionMode(
                    SelectionMode.MULTIPLE
            );

            Platform.runLater(() -> {
                tableView.requestFocus();
                tableView.getSelectionModel().select(0);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onAssociate(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/wizard/chooseSties.fxml"));
            Parent root = loader.load();

            ChooseStiesController controller = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("选择栏位");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);

            controller.setUnitOfWork(this.unitOfWork);
            controller.setStage(stage);

            stage.showAndWait();

            if( controller.isDialogResult()){
                Set<Sty> selectedSties = controller.getSelectedSties();

                String siteCode = FlfsApp.getContext().getSiteCode();
                unitOfWork.begin();
                DefaultGenericRepository<Site> repository = unitOfWork.getSiteConfigRepository();
                Site site = repository.findByNaturalId(siteCode);
                Collection<Sty> sties = site.getSties();
                sties.addAll( selectedSties);
                repository.save(site);

                unitOfWork.commit();

                tableView.getItems().addAll(selectedSties);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onUnassociate(){
        ObservableList<Sty> selectedItems = tableView.getSelectionModel().getSelectedItems();

        if( selectedItems == null)
            return;

        String siteCode = FlfsApp.getContext().getSiteCode();
        try {
            unitOfWork.begin();

            DefaultGenericRepository<Site> repository = unitOfWork.getSiteConfigRepository();
            Site site = repository.findByNaturalId(siteCode);
            site.removeSties(selectedItems);
            unitOfWork.commit();

            tableView.getItems().removeAll(selectedItems);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

    }
}
