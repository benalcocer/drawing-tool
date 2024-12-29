package com.example.drawingstool.views;

import com.example.drawingstool.draw.SelectionRectangle;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * A canvas that can contain Nodes drawn.
 */
public class DrawingCanvas extends VBox {

    /**
     * The Drawing area of the canvas.
     */
    private final AnchorPane drawingArea = new AnchorPane();

    /**
     * The layer that contains the SelectionRectangle.
     */
    private final AnchorPane selectionLayer = new AnchorPane();

    /**
     * The Selection Rectangle that shows selected nodes.
     */
    private final SelectionRectangle selectionRectangle = new SelectionRectangle();

    /**
     * The clipping rectangle that clips nodes that travel outside the drawing canvas.
     */
    private final Rectangle clipRectangle = new Rectangle();

    public DrawingCanvas() {
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(drawingArea, selectionLayer);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(stackPane);
        scrollPane.setPrefViewportWidth(400);
        scrollPane.setPrefViewportHeight(400);

        drawingArea.setPrefSize(8000.0, 8000.0);
        drawingArea.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        clipRectangle.widthProperty().bind(this.widthProperty());
        clipRectangle.heightProperty().bind(this.heightProperty());
        setClip(clipRectangle);

        selectionLayer.getChildren().add(selectionRectangle.getRectangle());
        selectionRectangle.getRectangle().setVisible(false);
        selectionLayer.setMouseTransparent(true);

        getChildren().addAll(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
    }

    public final AnchorPane getDrawingArea() {
        return drawingArea;
    }

    public final SelectionRectangle getSelectionRectangle() {
        return selectionRectangle;
    }
}
