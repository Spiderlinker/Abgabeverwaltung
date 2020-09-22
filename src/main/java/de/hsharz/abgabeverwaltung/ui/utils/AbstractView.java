package de.hsharz.abgabeverwaltung.ui.utils;

import javafx.scene.layout.Pane;

public abstract class AbstractView<P extends Pane> {

    protected P root;

    public AbstractView(final P root) {
        this.root = root;
    }

    protected void initializeView() {
        this.createWidgets();
        this.setupInteractions();
        this.addWidgets();
    }

    protected void createWidgets() {
        // Implement your stuff here
    }

    protected void setupInteractions() {
        // Implement your stuff here
    }

    protected void addWidgets() {
        // Implement your stuff here
    }

    public P getPane() {
        return this.root;
    }

}
