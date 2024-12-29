package com.example.drawingstool.views;

import com.example.drawingstool.draw.DrawObjectType;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.scene.control.Button;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;

public class DrawingToolBar extends ToolBar {
    /**
     * The ToggleGroup containing the drawing buttons.
     */
    private final ToggleGroup toggleGroup = new ToggleGroup();

    /**
     * The Rectangle draw button.
     */
    private final ToggleButton rectangle = new ToggleButton("Rectangle");

    /**
     * The Circle draw button.
     */
    private final ToggleButton circle = new ToggleButton("Circle");

    /**
     * The Ellipse draw button.
     */
    private final ToggleButton ellipse = new ToggleButton("Ellipse");

    /**
     * The delete button.
     */
    private final Button deleteButton = new Button("Delete");

    /**
     * Object property containing the current selected DrawObjectType.
     */
    private final ReadOnlyObjectWrapper<DrawObjectType> selectedDrawObjectType = new ReadOnlyObjectWrapper<>(DrawObjectType.NONE);

    /**
     * ChangeListener that listens to changes of the selected ToggleButton.
     */
    private final ChangeListener<Toggle> selectedToggleCL = (observable, oldValue, newValue) -> {
        if (newValue == rectangle) {
            selectedDrawObjectType.setValue(DrawObjectType.RECTANGLE);
        } else if (newValue == circle) {
            selectedDrawObjectType.setValue(DrawObjectType.CIRCLE);
        } else if (newValue == ellipse) {
            selectedDrawObjectType.setValue(DrawObjectType.ELLIPSE);
        } else {
            selectedDrawObjectType.setValue(DrawObjectType.NONE);
        }
    };

    public DrawingToolBar() {
        rectangle.setToggleGroup(toggleGroup);
        circle.setToggleGroup(toggleGroup);
        ellipse.setToggleGroup(toggleGroup);
        toggleGroup.selectedToggleProperty().addListener(new WeakChangeListener<>(selectedToggleCL));

        this.getItems().addAll(
            rectangle,
            circle,
            ellipse,
            deleteButton
        );
    }

    public ToggleGroup getToggleGroup() {
        return toggleGroup;
    }

    public final Button getDeleteButton() {
        return deleteButton;
    }

    /**
     * Get the selectedDrawObjectType read-only property.
     */
    public ReadOnlyObjectProperty<DrawObjectType> selectedDrawObjectTypeProperty() {
        return selectedDrawObjectType.getReadOnlyProperty();
    }

    /**
     * Get the selected DrawObjectType value.
     */
    public final DrawObjectType getSelectedDrawObjectType() {
        return selectedDrawObjectType.getValue();
    }

    /**
     * Clear any selected ToggleButtons.
     */
    public void clearSelection() {
        getToggleGroup().selectToggle(null);
    }
}
