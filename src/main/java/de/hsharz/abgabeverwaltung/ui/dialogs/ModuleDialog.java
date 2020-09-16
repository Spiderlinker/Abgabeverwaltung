package de.hsharz.abgabeverwaltung.ui.dialogs;

import com.jfoenix.controls.JFXDialog;
import de.hsharz.abgabeverwaltung.model.Module;
import de.hsharz.abgabeverwaltung.model.ModuleDatabase;
import javafx.scene.layout.StackPane;

import java.util.Comparator;
import java.util.Objects;

public class ModuleDialog extends AbstractDialog {

    private ModuleDialogView moduleDialogView;
    private Module module;

    public ModuleDialog(StackPane parent, Module module) {
        super(parent, JFXDialog.DialogTransition.TOP);
        this.module = Objects.requireNonNull(module);

        createWidgets();
        setupInteractions();

        setOverlayClose(false);
    }

    private void createWidgets() {
        moduleDialogView = new ModuleDialogView(module);
        setContent(moduleDialogView.getPane());
    }

    private void setupInteractions() {
        moduleDialogView.btnDelete.setOnAction(e -> {
            ModuleDatabase.getInstance().removeModule(module);
            close();
        });
        moduleDialogView.btnSave.setOnAction(e -> {
            close();
            ModuleDatabase.getInstance().getModules().sort(Comparator.comparing(Module::getName));
        });

        visibleProperty().addListener((observable, oldValue, newValue) -> {
            moduleDialogView.fldModuleName.requestFocus();
            moduleDialogView.fldModuleName.selectAll();
        });
    }

}
