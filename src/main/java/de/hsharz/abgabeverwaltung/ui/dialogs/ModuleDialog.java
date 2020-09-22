package de.hsharz.abgabeverwaltung.ui.dialogs;

import java.util.Comparator;
import java.util.Objects;

import com.jfoenix.controls.JFXDialog;

import de.hsharz.abgabeverwaltung.model.Module;
import de.hsharz.abgabeverwaltung.model.ModuleDatabase;
import javafx.scene.layout.StackPane;

public class ModuleDialog extends AbstractDialog {

    private ModuleDialogView moduleDialogView;
    private Module           module;

    public ModuleDialog(final StackPane parent, final Module module) {
        super(parent, JFXDialog.DialogTransition.TOP);
        this.module = Objects.requireNonNull(module);

        this.createWidgets();
        this.setupInteractions();

        this.setOverlayClose(false);
    }

    private void createWidgets() {
        this.moduleDialogView = new ModuleDialogView(this.module);
        this.setContent(this.moduleDialogView.getPane());
    }

    private void setupInteractions() {
        this.moduleDialogView.btnDelete.setOnAction(e -> {
            ModuleDatabase.getInstance().removeModule(this.module);
            this.close();
        });
        this.moduleDialogView.btnSave.setOnAction(e -> {
            this.close();
            ModuleDatabase.getInstance().getModules().sort(Comparator.comparing(Module::getName));
        });

        this.visibleProperty().addListener((observable, oldValue, newValue) -> {
            this.moduleDialogView.fldModuleName.requestFocus();
            this.moduleDialogView.fldModuleName.selectAll();
        });
    }

}
