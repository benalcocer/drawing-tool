package com.example.drawingstool.draw;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Shape that represents a rectangle.
 */
public class RectangleShape implements DrawObject {

    protected final Rectangle rectangle = new Rectangle();

    public RectangleShape() {
        rectangle.setStroke(Color.BLUE);
        rectangle.setFill(Color.TRANSPARENT);
    }

    public final Rectangle getRectangle() {
        return rectangle;
    }

    @Override
    public Node getDisplayNode() {
        return rectangle;
    }
}
