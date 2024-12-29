package com.example.drawingtool;

import com.example.drawingtool.draw.CircleShape;
import com.example.drawingtool.draw.DrawObject;
import com.example.drawingtool.draw.DrawObjectType;
import com.example.drawingtool.draw.EllipseShape;
import com.example.drawingtool.draw.RectangleShape;
import com.example.drawingtool.views.DrawingView;
import java.util.ArrayList;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class DrawingWindow extends CustomWindow {
    /**
     * The DrawingView.
     */
    private final DrawingView drawingView;

    /**
     * An object property that tracks the currently selected object.
     */
    private final SimpleObjectProperty<DrawObject> selectedObject = new SimpleObjectProperty<>(null);

    /**
     * An object property that tracks the currently drawing object.
     */
    private final SimpleObjectProperty<DrawObject> currentDraw = new SimpleObjectProperty<>(null);

    /**
     * An object property that tracks the start point of a draw.
     */
    private final SimpleObjectProperty<Point2D> drawStart = new SimpleObjectProperty<>(null);

    /**
     * A list of routines to run to clean up listeners.
     */
    private final ArrayList<Runnable> cleanUpRoutines = new ArrayList<>();

    public DrawingWindow(Stage stage) {
        super(stage);
        getStage().setTitle("Drawing Tool");
        drawingView = new DrawingView();
        getRoot().getChildren().addAll(
            drawingView
        );
        VBox.setVgrow(drawingView, Priority.ALWAYS);
    }

    @Override
    public void addListeners() {
        drawingView.getDrawingToolBar().getDeleteButton().disableProperty().bind(selectedObject.isNull());
        drawingView.getDrawingToolBar().getDeleteButton().setOnAction(event -> {
            deleteObject(selectedObject.getValue());
        });

        EventHandler<KeyEvent> keyPressedEvent = event -> {
            if (event.getCode() == KeyCode.DELETE) {
                deleteObject(selectedObject.getValue());
            }
        };
        getStage().addEventHandler(KeyEvent.KEY_PRESSED, keyPressedEvent);
        cleanUpRoutines.add(() -> getStage().removeEventHandler(KeyEvent.KEY_PRESSED, keyPressedEvent));

        drawingView.getDrawingCanvas().getDrawingArea().setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                selectObject(null);
                DrawObject drawObject = null;
                switch (drawingView.getDrawingToolBar().getSelectedDrawObjectType()) {
                    case RECTANGLE -> drawObject = new RectangleShape();
                    case CIRCLE -> drawObject = new CircleShape();
                    case ELLIPSE -> drawObject = new EllipseShape();
                }
                if (drawObject != null) {
                    drawObject.getDisplayNode().setLayoutX(Math.floor(event.getX()));
                    drawObject.getDisplayNode().setLayoutY(Math.floor(event.getY()));
                    currentDraw.setValue(drawObject);
                    drawStart.setValue(new Point2D(event.getX(), event.getY()));
                    drawingView.getDrawingCanvas()
                        .getDrawingArea()
                        .getChildren()
                        .add(drawObject.getDisplayNode());
                    drawingView.getDrawingToolBar().setDisable(true);
                }
            }
        });
        drawingView.getDrawingCanvas().getDrawingArea().setOnMouseDragged(event -> {
            if (event.getButton() != MouseButton.PRIMARY || currentDraw.getValue() == null) {
                return;
            }
            Point2D dragPoint = new Point2D(event.getX(), event.getY());
            if (currentDraw.getValue() instanceof RectangleShape) {
                double newX = Math.min(drawStart.getValue().getX(), dragPoint.getX());
                double newWidth = Math.abs(drawStart.getValue().getX() - dragPoint.getX());
                double newY = Math.min(drawStart.getValue().getY(), dragPoint.getY());
                double newHeight = Math.abs(drawStart.getValue().getY() - dragPoint.getY());

                Rectangle rectangle = ((RectangleShape) currentDraw.getValue()).getRectangle();
                rectangle.setLayoutX(newX);
                rectangle.setWidth(Math.max(1.0, newWidth));
                rectangle.setLayoutY(newY);
                rectangle.setHeight(Math.max(1.0, newHeight));
            } else if (currentDraw.getValue() instanceof CircleShape) {
                Circle circle = ((CircleShape) currentDraw.getValue()).getCircle();
                double newRadius = Math.max(1.0, drawStart.getValue().distance(dragPoint));
                circle.setRadius(newRadius);
            } else if (currentDraw.getValue() instanceof EllipseShape) {
                double newWidth = Math.abs(drawStart.getValue().getX() - dragPoint.getX());
                double newHeight = Math.abs(drawStart.getValue().getY() - dragPoint.getY());

                Ellipse ellipse = ((EllipseShape) currentDraw.getValue()).getEllipse();
                ellipse.setRadiusX(Math.max(1.0, newWidth));
                ellipse.setRadiusY(Math.max(1.0, newHeight));
            }
        });
        ChangeListener<Boolean> focusCL = (observable, oldValue, newValue) -> {
            if (!newValue) {
                placeObject();
            }
        };
        drawingView.getDrawingCanvas().getDrawingArea().focusedProperty().addListener(focusCL);
        getStage().focusedProperty().addListener(focusCL);
        cleanUpRoutines.add(() -> {
            drawingView.getDrawingCanvas().getDrawingArea().focusedProperty().removeListener(focusCL);
            getStage().focusedProperty().removeListener(focusCL);
        });
        drawingView.getDrawingCanvas().getDrawingArea().setOnMouseReleased(event -> {
            placeObject();
        });
        drawingView.getDrawingCanvas().getDrawingArea().setOnMouseEntered(event -> {
            if (drawingView.getDrawingToolBar().getSelectedDrawObjectType() != DrawObjectType.NONE) {
                getScene().setCursor(Cursor.CROSSHAIR);
            }
        });
        drawingView.getDrawingCanvas().getDrawingArea().setOnMouseExited(event -> {
            if (drawingView.getDrawingToolBar().getSelectedDrawObjectType() != DrawObjectType.NONE) {
                getScene().setCursor(Cursor.DEFAULT);
            }
        });

        ChangeListener<DrawObjectType> selectedDrawObjectTypeCL = (observable, oldValue, newValue) -> {
            if (newValue == DrawObjectType.NONE) {
                getScene().setCursor(Cursor.DEFAULT);
            } else {
                selectObject(null);
            }
        };
        drawingView.getDrawingToolBar().selectedDrawObjectTypeProperty().addListener(selectedDrawObjectTypeCL);
        cleanUpRoutines.add(() -> drawingView.getDrawingToolBar().selectedDrawObjectTypeProperty().removeListener(selectedDrawObjectTypeCL));
    }

    /**
     * Place the drawn object.
     */
    private void placeObject() {
        if (currentDraw.getValue() == null) {
            return;
        }
        if (currentDraw.getValue() instanceof RectangleShape) {
            Rectangle rectangle = ((RectangleShape) currentDraw.getValue()).getRectangle();
            rectangle.setStroke(Color.BLACK);
            rectangle.setFill(Color.WHITE);
        } else if (currentDraw.getValue() instanceof CircleShape) {
            Circle circle = ((CircleShape) currentDraw.getValue()).getCircle();
            circle.setStroke(Color.BLACK);
            circle.setFill(Color.WHITE);
        } else if (currentDraw.getValue() instanceof EllipseShape) {
            Ellipse ellipse = ((EllipseShape) currentDraw.getValue()).getEllipse();
            ellipse.setStroke(Color.BLACK);
            ellipse.setFill(Color.WHITE);
        }
        addDrawObjectListeners(currentDraw.getValue());
        currentDraw.setValue(null);
        drawStart.setValue(null);
        drawingView.getDrawingToolBar().clearSelection();
        drawingView.getDrawingToolBar().setDisable(false);
    }

    /**
     * Add listeners for a DrawObject.
     */
    private void addDrawObjectListeners(DrawObject drawObject) {
        SimpleDoubleProperty prevX = new SimpleDoubleProperty(0.0);
        SimpleDoubleProperty prevY = new SimpleDoubleProperty(0.0);
        drawObject.getDisplayNode().setOnMousePressed(event -> {
            if (event.getButton() != MouseButton.PRIMARY) {
                return;
            }
            prevX.setValue(event.getScreenX());
            prevY.setValue(event.getScreenY());
            if (drawingView.getDrawingToolBar().getSelectedDrawObjectType() == DrawObjectType.NONE) {
                selectObject(drawObject);
                event.consume();
            }
        });
        drawObject.getDisplayNode().setOnMouseDragged(event -> {
            if (event.getButton() == MouseButton.PRIMARY && drawingView.getDrawingToolBar().getSelectedDrawObjectType() == DrawObjectType.NONE) {
                double xDistance = event.getScreenX() - prevX.doubleValue();
                double yDistance =  event.getScreenY() - prevY.doubleValue();
                drawObject.getDisplayNode().setLayoutX(drawObject.getDisplayNode().getLayoutX() + xDistance);
                drawObject.getDisplayNode().setLayoutY(drawObject.getDisplayNode().getLayoutY() + yDistance);
                prevX.setValue(event.getScreenX());
                prevY.setValue(event.getScreenY());
                selectObject(drawObject);
            }
        });
    }

    @Override
    public void removeListeners() {
        drawingView.getDrawingToolBar().getDeleteButton().disableProperty().unbind();
        drawingView.getDrawingToolBar().getDeleteButton().setOnAction(null);

        drawingView.getDrawingCanvas().getDrawingArea().setOnMousePressed(null);
        drawingView.getDrawingCanvas().getDrawingArea().setOnMouseDragged(null);
        drawingView.getDrawingCanvas().getDrawingArea().setOnMouseReleased(null);

        drawingView.getDrawingCanvas().getDrawingArea().setOnMouseEntered(null);
        drawingView.getDrawingCanvas().getDrawingArea().setOnMouseExited(null);

        for (Node displayNode : drawingView.getDrawingCanvas().getDrawingArea().getChildren()) {
            displayNode.setOnMousePressed(null);
            displayNode.setOnMouseDragged(null);
        }

        for (Runnable routine : cleanUpRoutines) {
            try {
                routine.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        cleanUpRoutines.clear();
    }

    /**
     * Delete a draw object from the canvas.
     */
    private void deleteObject(DrawObject drawObject) {
        if (drawObject != null) {
            drawingView.getDrawingCanvas().getDrawingArea().getChildren().remove(drawObject.getDisplayNode());
            drawObject.getDisplayNode().setOnMousePressed(null);
            drawObject.getDisplayNode().setOnMouseDragged(null);
            if (drawObject == selectedObject.getValue()) {
                selectObject(null);
            }
        }
    }

    /**
     * Select an object on the canvas.
     */
    private void selectObject(DrawObject drawObject) {
        selectedObject.setValue(drawObject);
        if (drawObject != null) {
            drawingView.getDrawingCanvas().getSelectionRectangle().setPosition(drawObject.getDisplayNode());
            drawingView.getDrawingCanvas().getSelectionRectangle().getRectangle().setVisible(true);
        } else {
            drawingView.getDrawingCanvas().getSelectionRectangle().getRectangle().setVisible(false);
        }
    }
}
