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

/**
 * Класс представления калькулятора (View), отвечающий за графический интерфейс приложения.
 * Этот класс создает все элементы интерфейса и управляет их размещением и стилем.
 */
public class CalculatorView {
    private CalculatorController controller; // Контроллер для обработки логики калькулятора
    private Label display; // Основное поле отображения текущего значения
    private Label currentOperationDisplay; // Поле отображения текущей операции
    private BorderPane borderPane; // Основная панель компоновки
    private Map<String, Button> buttonMap; // Карта для хранения кнопок по их меткам

    /**
     * Конструктор инициализирует контроллер и карту кнопок.
     */
    public CalculatorView() {
        this.controller = new CalculatorController(this);
        this.buttonMap = new HashMap<>();
    }

    /**
     * Создает и возвращает содержимое интерфейса калькулятора.
     * @return основная панель с содержимым калькулятора
     */
    public Parent createContent() {
        borderPane = new BorderPane();
        borderPane.getStyleClass().add("root");

        // Создание метки для отображения текущей операции
        currentOperationDisplay = new Label("");
        currentOperationDisplay.getStyleClass().add("current-operation-label");
        currentOperationDisplay.setMaxWidth(Double.MAX_VALUE);
        currentOperationDisplay.setAlignment(Pos.CENTER_RIGHT);

        // Создание метки для отображения текущего значения
        display = new Label("0");
        display.getStyleClass().add("label");
        display.setMaxWidth(Double.MAX_VALUE);
        display.setAlignment(Pos.CENTER_RIGHT);

        // Объединение меток в вертикальный бокс
        VBox vbox = new VBox(5, currentOperationDisplay, display);
        vbox.setPadding(new Insets(10));
        vbox.setMaxWidth(Double.MAX_VALUE);

        // Создание верхнего контейнера и добавление в него бокса с метками
        HBox topContainer = new HBox(2, vbox);
        topContainer.setPadding(new Insets(3));
        topContainer.setAlignment(Pos.CENTER_RIGHT);

        // Установка верхнего контейнера и сетки кнопок в основную панель
        borderPane.setTop(topContainer);
        borderPane.setCenter(createButtonGrid());

        // Загрузка и установка CSS стилей
        String css = getClass().getResource("/com/example/calculatorapp/style.css").toExternalForm();
        borderPane.getStylesheets().add(css);

        return borderPane;
    }

    /**
     * Создает сетку кнопок калькулятора.
     * @return сетка с кнопками калькулятора
     */
    private GridPane createButtonGrid() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);

        // Определение кнопок и их размещение в сетке
        String[][] buttons = {
                {"%", "CE", "C", "⌫"},
                {"1/x", "x^2", "√x", "/"},
                {"7", "8", "9", "*"},
                {"4", "5", "6", "-"},
                {"1", "2", "3", "+"},
                {"+/-", "0", ".", "="}
        };

        // Создание и настройка кнопок, добавление их в сетку
        for (int row = 0; row < buttons.length; row++) {
            for (int col = 0; col < buttons[row].length; col++) {
                String text = buttons[row][col];
                Button button = new Button(text);
                button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                button.setPrefSize(80, 80);
                // Применение различных стилей к кнопкам в зависимости от их типа
                if (text.matches("[0-9]")) {
                    button.getStyleClass().add("number-button");
                } else if (text.equals("=")) {
                    button.getStyleClass().add("equals-button");
                } else {
                    button.getStyleClass().add("button");
                }
                // Добавление обработчика нажатия кнопки
                button.setOnAction(e -> controller.processInput(text));
                buttonMap.put(text, button);
                GridPane.setFillWidth(button, true);
                GridPane.setFillHeight(button, true);
                gridPane.add(button, col, row);
            }
        }

        // Настройка колонок и строк для растягивания
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

    /**
     * Обновляет текст основного поля отображения.
     * @param text новый текст для отображения
     */
    public void updateDisplay(String text) {
        display.setText(text);
    }

    /**
     * Обновляет текст поля отображения текущей операции.
     * @param text новый текст для отображения
     */
    public void updateCurrentOperationDisplay(String text) {
        currentOperationDisplay.setText(text);
    }

    /**
     * Обрабатывает нажатия клавиш и симулирует нажатия кнопок калькулятора.
     * @param key нажатая клавиша
     */
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

    /**
     * Преобразует нажатие клавиши в соответствующий текст кнопки калькулятора.
     * @param key нажатая клавиша
     * @return текст кнопки или null, если клавиша не соответствует ни одной кнопке
     */
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

    /**
     * Симулирует нажатие кнопки, применяя визуальный эффект.
     * @param button кнопка для симуляции нажатия
     */
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

    /**
     * Устанавливает тему оформления для калькулятора.
     * @param theme имя файла CSS с новой темой
     */
    public void setTheme(String theme) {
        borderPane.getStylesheets().clear();
        String css = getClass().getResource("/com/example/calculatorapp/" + theme).toExternalForm();
        borderPane.getStylesheets().add(css);
    }

    /**
     * Возвращает карту кнопок.
     * @return карта кнопок
     */
    public Map<String, Button> getButtonMap() {
        return buttonMap;
    }

    /**
     * Возвращает основную панель компоновки.
     * @return основная панель компоновки
     */
    public BorderPane getBorderPane() {
        return borderPane;
    }
}
