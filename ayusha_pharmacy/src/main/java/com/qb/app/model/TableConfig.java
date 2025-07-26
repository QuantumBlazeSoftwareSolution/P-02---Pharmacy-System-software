package com.qb.app.model;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;

/**
 * Utility class for reusable TableView column configurations.
 */
public class TableConfig {

    /**
     * Sets a cell factory that formats double values to 2 decimal places
     * on any TableColumn, regardless of the model class.
     *
     * @param column The TableColumn to configure
     * @param <T>    The row model type (can be any object)
     */
    public static <T> void formatDecimalColumn(TableColumn<T, Double> column) {
        column.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", value));
                }
            }
        });
    }
}
