package com.dxw.flfs.ui;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.util.Callback;

/**
 * Created by zhang on 2016-05-29.
 */
public class CheckBoxTableCellFactory<S, T> implements Callback<TableColumn<S, T>, TableCell<S, T>> {
    public TableCell<S, T> call(TableColumn<S, T> param) {
        return new CheckBoxTableCell<S,T>();
    }
}
