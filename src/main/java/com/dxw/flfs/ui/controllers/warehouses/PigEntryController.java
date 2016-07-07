package com.dxw.flfs.ui.controllers.warehouses;

import com.dxw.flfs.data.HibernateService;
import com.dxw.flfs.data.dal.UnitOfWork;
import com.dxw.flfs.data.models.mes.PigEntry;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

/**
 * Created by zhang on 2016-07-07.
 */
public class PigEntryController {

    private HibernateService hibernateService;

    private UnitOfWork unitOfWork;

    @FXML
    TableView<PigEntry> feedTableView;
    public void onAddPigEntry(){

    }
}
