package de.hsharz.abgabeverwaltung.ui.utils;

import javafx.scene.layout.Pane;

public abstract class AbstractStyledView<P extends Pane> extends AbstractView<P> {

    public AbstractStyledView(P root) {
        super(root);
        loadStylesheet();
    }

    private void loadStylesheet() {
        root.getStylesheets().add(getClass().getResource(getStylesheet()).toExternalForm());
    }

    protected abstract String getStylesheet();

}
