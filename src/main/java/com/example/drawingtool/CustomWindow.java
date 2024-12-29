package com.example.drawingtool;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CustomWindow {
    /**
     * The Stage of the CustomWindow.
     */
    private final Stage stage;

    /**
     * The VBox that serves as the root of the Scene.
     */
    private final VBox root = new VBox();

    /**
     * The Scene of the CustomWindow.
     */
    private final Scene scene = new Scene(root);

    public CustomWindow(Stage stage) {
        super();

        this.stage = stage;
        root.setMinSize(1280, 720);
        root.setFillWidth(true);
        stage.setScene(scene);
    }

    public final Stage getStage() {
        return stage;
    }

    public final Scene getScene() {
        return scene;
    }

    public final VBox getRoot() {
        return root;
    }

    public void show() {
        if (!stage.isShowing()) {
            stage.show();
        }
    }

    public void close() {
        if (stage.isShowing()) {
            stage.close();
        }
    }

    /**
     * Add listeners to the custom window.
     */
    public void addListeners() {}

    /**
     * Remove listeners from the custom window.
     */
    public void removeListeners() {}
}
