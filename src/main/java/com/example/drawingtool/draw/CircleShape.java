package com.example.drawingtool.draw;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Shape that represents a circle.
 */
public class CircleShape implements DrawObject {

    private final Circle circle = new Circle();

    public CircleShape() {
        circle.setStroke(Color.BLUE);
        circle.setFill(Color.TRANSPARENT);
    }

    public Circle getCircle() {
        return circle;
    }

    @Override
    public Node getDisplayNode() {
        return circle;
    }
}
