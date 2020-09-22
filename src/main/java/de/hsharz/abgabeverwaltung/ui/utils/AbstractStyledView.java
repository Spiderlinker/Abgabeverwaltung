package de.hsharz.abgabeverwaltung.ui.utils;

import javafx.scene.layout.Pane;

public abstract class AbstractStyledView<P extends Pane> extends AbstractView<P> {

    public AbstractStyledView(final P root) {
        super(root);
        this.loadStylesheet();
    }

    private void loadStylesheet() {
        this.root.getStylesheets().add(this.getClass().getResource(this.getStylesheet()).toExternalForm());
    }

    protected abstract String getStylesheet();

}
