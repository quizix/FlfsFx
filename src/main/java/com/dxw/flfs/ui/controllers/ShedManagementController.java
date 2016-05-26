package com.dxw.flfs.ui.controllers;

import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;
import com.dxw.flfs.data.HibernateService;
import com.dxw.flfs.data.dal.DefaultGenericRepository;
import com.dxw.flfs.data.dal.UnitOfWork;
import com.dxw.flfs.data.models.Shed;
import com.dxw.flfs.data.models.Sty;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import java.util.Collection;
import java.util.Set;

/**
 * Created by zhang on 2016-05-26.
 */
public class ShedManagementController {

    private HibernateService hibernateService;

    private UnitOfWork unitOfWork;

    @FXML
    TableView<Shed> shedTableView;

    @FXML
    TableView<Sty> styTableView;

    @FXML
    public void initialize(){
        ServiceRegistry registry = ServiceRegistryImpl.getInstance();
        hibernateService = (HibernateService)registry.getService(Services.HIBERNATE_SERVICE);
        unitOfWork = new UnitOfWork(hibernateService.getSession());

        try{
            DefaultGenericRepository<Shed> shedRepository = unitOfWork.getShedRepository();
            Collection<Shed> sheds = shedRepository.findAll();
            shedTableView.getItems().addAll(sheds);
        } catch (Exception e) {
            e.printStackTrace();
        }

        shedTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    Set<Sty> sties = newValue.getSties();
                    styTableView.getItems().clear();
                    styTableView.getItems().addAll(sties);
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
}
