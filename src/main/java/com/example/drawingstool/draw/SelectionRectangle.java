package com.example.drawingstool.draw;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.paint.Color;

/**
 * Rectangle shape that displays current selection.
 */
public class SelectionRectangle extends RectangleShape {

    public SelectionRectangle() {
        super();
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.BLUE);
    }

    /**
     * Set the position of the SelectionRectangle to the given Node.
     */
    public void setPosition(Node node) {
        Bounds bounds = node.getBoundsInParent();
        rectangle.setLayoutX(bounds.getMinX());
        rectangle.setLayoutY(bounds.getMinY());
        rectangle.setWidth(bounds.getMaxX() - bounds.getMinX());
        rectangle.setHeight(bounds.getMaxY() - bounds.getMinY());
    }
}
