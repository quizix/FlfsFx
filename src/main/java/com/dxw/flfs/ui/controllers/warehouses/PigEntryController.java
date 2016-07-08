package com.dxw.flfs.ui.controllers.warehouses;

import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;
import com.dxw.flfs.data.HibernateService;
import com.dxw.flfs.data.dal.DefaultGenericRepository;
import com.dxw.flfs.data.dal.UnitOfWork;
import com.dxw.flfs.data.models.mes.PigEntry;
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
 * Created by zhang on 2016-07-07.
 */
public class PigEntryController {

    private HibernateService hibernateService;

    private UnitOfWork unitOfWork;

    @FXML
    TableView<PigEntry> pigEntryTableView;

    @FXML
    public void initialize(){
        ServiceRegistry registry = ServiceRegistryImpl.getInstance();
        hibernateService = (HibernateService)registry.getService(Services.HIBERNATE_SERVICE);
        unitOfWork = new UnitOfWork(hibernateService.getSession());

        try{
            DefaultGenericRepository<PigEntry> pigEntryRepository = unitOfWork.getPigEntryRepository();
            Collection<PigEntry> pigEntries = pigEntryRepository.findAll();
            pigEntryTableView.getItems().addAll(pigEntries);

            Platform.runLater(() -> {
                pigEntryTableView.requestFocus();
                pigEntryTableView.getSelectionModel().select(0);
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

    public void onAddPigEntry(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/warehouses/pigEntryDetail.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("新增小猪入栏");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);
            stage.showAndWait();

            PigEntryDetailController controller = loader.getController();
            if( controller.isDialogResult()){

                String code = controller.getCode();

                PigEntry pigEntry = new PigEntry();
                Date now = new Date();
                pigEntry.setModifyTime(now);
                pigEntry.setCreateTime(now);


                unitOfWork.begin();
                unitOfWork.getPigEntryRepository().save(pigEntry);
                unitOfWork.commit();

                pigEntryTableView.getItems().add(pigEntry);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
