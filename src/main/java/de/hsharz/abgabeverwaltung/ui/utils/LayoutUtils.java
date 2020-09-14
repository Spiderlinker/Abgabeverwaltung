package de.hsharz.abgabeverwaltung.ui.utils;

import javafx.scene.layout.*;

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

    public static void setColumnWidths(GridPane root, int... columnWidths) {
        for (int columnWidth : columnWidths) {
            ColumnConstraints c = new ColumnConstraints();
            c.setPercentWidth(columnWidth);
            root.getColumnConstraints().add(c);
        }
    }

    public static void setRowHeight(GridPane root, int... rowHeights) {
        for (int rowHeight : rowHeights) {
            RowConstraints r = new RowConstraints();
            r.setPercentHeight(rowHeight);
            root.getRowConstraints().add(r);
        }
    }
}
