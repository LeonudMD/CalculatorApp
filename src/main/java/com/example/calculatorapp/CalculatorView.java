package com.example.calculatorapp;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;

import java.util.HashMap;
import java.util.Map;

public class CalculatorView {
    private CalculatorController controller;
    private Label display;
    private Label currentOperationDisplay;
    private BorderPane borderPane;
    private Map<String, Button> buttonMap;

    public CalculatorView() {
        this.controller = new CalculatorController(this);
        this.buttonMap = new HashMap<>();
    }

    public Parent createContent() {
        borderPane = new BorderPane();
        borderPane.getStyleClass().add("root");

        currentOperationDisplay = new Label("");
        currentOperationDisplay.getStyleClass().add("current-operation-label");
        currentOperationDisplay.setMaxWidth(Double.MAX_VALUE);
        currentOperationDisplay.setAlignment(Pos.CENTER_RIGHT);

        display = new Label("0");
        display.getStyleClass().add("label");
        display.setMaxWidth(Double.MAX_VALUE);
        display.setAlignment(Pos.CENTER_RIGHT);

        VBox vbox = new VBox(5, currentOperationDisplay, display);
        vbox.setPadding(new Insets(10));
        vbox.setMaxWidth(Double.MAX_VALUE);

        HBox topContainer = new HBox(2, vbox);
        topContainer.setPadding(new Insets(3));
        topContainer.setAlignment(Pos.CENTER_RIGHT);

        borderPane.setTop(topContainer);
        borderPane.setCenter(createButtonGrid());

        String css = getClass().getResource("/com/example/calculatorapp/style.css").toExternalForm();
        borderPane.getStylesheets().add(css);

        return borderPane;
    }

    private GridPane createButtonGrid() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);

        String[][] buttons = {
                {"%", "CE", "C", "⌫"},
                {"1/x", "x^2", "√x", "/"},
                {"7", "8", "9", "*"},
                {"4", "5", "6", "-"},
                {"1", "2", "3", "+"},
                {"+/-", "0", ".", "="}
        };

        for (int row = 0; row < buttons.length; row++) {
            for (int col = 0; col < buttons[row].length; col++) {
                String text = buttons[row][col];
                Button button = new Button(text);
                button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                button.setPrefSize(80, 80);
                if (text.matches("[0-9]")) {
                    button.getStyleClass().add("number-button");
                } else if (text.equals("=")) {
                    button.getStyleClass().add("equals-button");
                } else {
                    button.getStyleClass().add("button");
                }
                button.setOnAction(e -> controller.processInput(text));
                buttonMap.put(text, button);
                GridPane.setFillWidth(button, true);
                GridPane.setFillHeight(button, true);
                gridPane.add(button, col, row);
            }
        }

        for (int col = 0; col < buttons[0].length; col++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setHgrow(Priority.ALWAYS);
            gridPane.getColumnConstraints().add(cc);
        }
        for (int row = 0; row < buttons.length; row++) {
            RowConstraints rc = new RowConstraints();
            rc.setVgrow(Priority.ALWAYS);
            gridPane.getRowConstraints().add(rc);
        }

        return gridPane;
    }

    public void updateDisplay(String text) {
        display.setText(text);
    }

    public void updateCurrentOperationDisplay(String text) {
        currentOperationDisplay.setText(text);
    }

    public void handleKeyPress(String key) {
        String buttonText = mapKeyToButtonText(key);
        if (buttonText != null) {
            Button button = buttonMap.get(buttonText);
            if (button != null) {
                simulateButtonPress(button);
                controller.processInput(buttonText);
            }
        }
    }

    private String mapKeyToButtonText(String key) {
        return switch (key) {
            case "DIGIT0", "NUMPAD0" -> "0";
            case "DIGIT1", "NUMPAD1" -> "1";
            case "DIGIT2", "NUMPAD2" -> "2";
            case "DIGIT3", "NUMPAD3" -> "3";
            case "DIGIT4", "NUMPAD4" -> "4";
            case "DIGIT5", "NUMPAD5" -> "5";
            case "DIGIT6", "NUMPAD6" -> "6";
            case "DIGIT7", "NUMPAD7" -> "7";
            case "DIGIT8", "NUMPAD8" -> "8";
            case "DIGIT9", "NUMPAD9" -> "9";
            case "DECIMAL" -> ".";
            case "ENTER" -> "=";
            case "PLUS" -> "+";
            case "MINUS" -> "-";
            case "MULTIPLY" -> "*";
            case "DIVIDE" -> "/";
            case "BACK_SPACE" -> "⌫";
            case "DELETE" -> "C";
            case "ESCAPE" -> "CE";
            case "P" -> "%";
            case "R" -> "√x";
            case "X" -> "x^2";
            case "I" -> "1/x";
            case "S" -> "+/-";
            default -> null;
        };
    }

    private void simulateButtonPress(Button button) {
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.YELLOW);
        button.setEffect(shadow);
        new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
            button.setEffect(null);
        }).start();
    }

    public void setTheme(String theme) {
        borderPane.getStylesheets().clear();
        String css = getClass().getResource("/com/example/calculatorapp/" + theme).toExternalForm();
        borderPane.getStylesheets().add(css);
    }

    public Map<String, Button> getButtonMap() {
        return buttonMap;
    }

    public BorderPane getBorderPane() {
        return borderPane;
    }
}

