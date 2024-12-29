package com.example.drawingstool.views;

import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class DrawingView extends VBox {
    /**
     * The top drawing buttons toolbar.
     */
    private final DrawingToolBar drawingToolBar = new DrawingToolBar();

    /**
     * The Drawing Canvas.
     */
    private final DrawingCanvas drawingCanvas = new DrawingCanvas();

    public DrawingView() {
        setFillWidth(true);
        setMinSize(1, 1);
        getChildren().addAll(
            drawingToolBar,
            drawingCanvas
        );
        VBox.setVgrow(drawingCanvas, Priority.ALWAYS);
    }

    public DrawingToolBar getDrawingToolBar() {
        return drawingToolBar;
    }

    public DrawingCanvas getDrawingCanvas() {
        return drawingCanvas;
    }
}
