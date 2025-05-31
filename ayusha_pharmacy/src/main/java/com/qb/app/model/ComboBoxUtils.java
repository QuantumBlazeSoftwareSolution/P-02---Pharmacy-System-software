/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qb.app.model;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.function.Function;
import javafx.application.Platform;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;

/**
 *
 * @author Vihanga
 */
public class ComboBoxUtils {

    /**
     * Generic method to load items into a ComboBox and configure display
     *
     * @param comboBox The ComboBox to populate
     * @param items List of items to populate the ComboBox with
     * @param displayProperty Function that extracts the display string from
     * each item
     * @param <T> The type of items in the ComboBox
     */
    public static <T> void configureComboBox(ComboBox<T> comboBox, List<T> items, Function<T, String> displayProperty) {
        comboBox.getItems().setAll(items);

        // Set cell factory to display the desired property
        comboBox.setCellFactory(listView -> new ListCell<T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : displayProperty.apply(item));
            }
        });

        // Set button cell to display the same way
        comboBox.setButtonCell(new ListCell<T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : displayProperty.apply(item));
            }
        });
    }

    public static <T> void loadComboBoxValues(ComboBox<T> comboBox,
            Class<T> entityClass,
            String sortProperty,
            Function<T, String> displayProperty) {
        try {
            JPATransaction.runInTransaction((em) -> {
                CriteriaBuilder cBuilder = em.getCriteriaBuilder();
                CriteriaQuery<T> cQuery = cBuilder.createQuery(entityClass);
                Root<T> root = cQuery.from(entityClass);
                cQuery.orderBy(cBuilder.asc(root.get(sortProperty)));
                List<T> items = em.createQuery(cQuery).getResultList();

                Platform.runLater(() -> {
                    ComboBoxUtils.configureComboBox(comboBox, items, displayProperty);
                });
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
