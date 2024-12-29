package com.example.drawingstool;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApplication extends Application {
    private static DrawingWindow drawingWindow;

    public void initialize() {
        Thread.setDefaultUncaughtExceptionHandler(MainApplication::handleUncaughtException);
        launch();
    }

    private static void handleUncaughtException(Thread thread, Throwable throwable) {
        System.out.println("Uncaught exception in " + thread.getName() + " " + throwable.getMessage());
        throwable.printStackTrace();
    }

    @Override
    public void start(Stage stage) {
        drawingWindow = new DrawingWindow(stage);
        drawingWindow.addListeners();
        drawingWindow.show();
    }

    @Override
    public void stop(){
        drawingWindow.close();
        drawingWindow.removeListeners();
    }
}
