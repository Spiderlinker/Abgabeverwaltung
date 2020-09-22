package de.hsharz.abgabeverwaltung.ui.utils;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

public class LayoutUtils {

    private LayoutUtils() {
        // Utility Class
    }

    /* ##################
     * ##### Spacer #####
     * ################## */

    public static Region getHSpacer() {
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        return region;
    }

    public static Region getVSpacer() {
        Region region = new Region();
        VBox.setVgrow(region, Priority.ALWAYS);
        return region;
    }

    /* #########################
     * ##### GridPaneUtils #####
     * ######################### */

    public static void setColumnWidths(final GridPane root, final int... columnWidths) {
        for (int columnWidth : columnWidths) {
            ColumnConstraints c = new ColumnConstraints();
            c.setPercentWidth(columnWidth);
            root.getColumnConstraints().add(c);
        }
    }

    public static void setRowHeight(final GridPane root, final int... rowHeights) {
        for (int rowHeight : rowHeights) {
            RowConstraints r = new RowConstraints();
            r.setPercentHeight(rowHeight);
            root.getRowConstraints().add(r);
        }
    }
}
