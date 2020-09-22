package de.hsharz.abgabeverwaltung.ui.utils;

import java.io.File;
import java.util.function.Consumer;

import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;

public class UiUtils {

    private UiUtils() {
        // Utility Class
    }

    public static void addFilesDropFeature(final Pane pane, final Consumer<File> droppedFileConsumer) {
        pane.setOnDragOver(event -> {
            if (event.getGestureSource() != pane && event.getDragboard().hasFiles()) {
                /* allow for both copying and moving, whatever user chooses */
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        pane.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                db.getFiles().stream().filter(File::isFile).forEach(droppedFileConsumer::accept);
                success = true;
            }
            /* let the source know whether the string was successfully
             * transferred and used */
            event.setDropCompleted(success);

            event.consume();
        });
    }

}
