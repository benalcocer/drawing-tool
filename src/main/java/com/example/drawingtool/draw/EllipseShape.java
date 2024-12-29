package com.example.drawingtool.draw;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

/**
 * Shape that represents an ellipse.
 */
public class EllipseShape implements DrawObject {

    private final Ellipse ellipse = new Ellipse();

    public EllipseShape() {
        ellipse.setStroke(Color.BLUE);
        ellipse.setFill(Color.TRANSPARENT);
    }

    public Ellipse getEllipse() {
        return ellipse;
    }

    @Override
    public Node getDisplayNode() {
        return ellipse;
    }
}
