package com.example.calculatorapp;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

/**
 * Главный класс приложения Калькулятор, наследующий Application.
 * Этот класс отвечает за создание и настройку главного окна приложения.
 */
public class Main extends Application {

    // Переменные для отслеживания перемещения и изменения размера окна
    private double xOffset = 0;
    private double yOffset = 0;
    private double startX = 0;
    private double startY = 0;
    private double startWidth = 0;
    private double startHeight = 0;

    /**
     * Метод start, вызываемый при запуске приложения.
     * @param primaryStage главный Stage(сцена / окно) приложения
     */
    @Override
    public void start(Stage primaryStage) {
        // Убираем стандартную панель заголовка окна
        primaryStage.initStyle(StageStyle.UNDECORATED);

        // Создаем представление калькулятора
        CalculatorView calculatorView = new CalculatorView();
        BorderPane root = new BorderPane();
        // Устанавливаем пользовательскую панель заголовка
        root.setTop(createCustomTitleBar(primaryStage, calculatorView));
        // Устанавливаем содержимое калькулятора в центр
        root.setCenter(calculatorView.createContent());

        // Создаем сцену с указанными размерами
        Scene scene = new Scene(root, 400, 600);
        // Добавляем обработчик нажатий клавиш
        scene.setOnKeyPressed(e -> calculatorView.handleKeyPress(e.getCode().toString()));

        // Устанавливаем заголовок окна
        primaryStage.setTitle("Калькулятор");
        // Устанавливаем иконку окна
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/calculatorapp/ico_1.png"))));
        // Устанавливаем сцену
        primaryStage.setScene(scene);
        // Устанавливаем минимальные размеры окна
        primaryStage.setMinWidth(300);
        primaryStage.setMinHeight(485);

        // Добавляем слушатели изменения размеров окна
        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> adjustGridSize(calculatorView));
        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> adjustGridSize(calculatorView));

        // Включаем возможность изменения размеров окна
        enableWindowResizing(primaryStage, root);

        // Отображаем окно
        primaryStage.show();
    }

    /**
     * Метод для корректировки размеров кнопок калькулятора при изменении размеров окна.
     * @param calculatorView представление калькулятора
     */
    private void adjustGridSize(CalculatorView calculatorView) {
        for (Button button : calculatorView.getButtonMap().values()) {
            button.setPrefWidth(calculatorView.getBorderPane().getWidth() / 4 - 20);
            button.setPrefHeight(calculatorView.getBorderPane().getHeight() / 6 - 20);
        }
    }

    /**
     * Метод для создания пользовательской панели заголовка окна.
     * @param stage основной Stage приложения
     * @param calculatorView представление калькулятора
     * @return пользовательская панель заголовка
     */
    private HBox createCustomTitleBar(Stage stage, CalculatorView calculatorView) {
        HBox titleBar = new HBox();
        titleBar.setStyle("-fx-background-color: #202124; -fx-padding: 5;");
        titleBar.setPadding(new Insets(5));
        titleBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        titleBar.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        // Добавление иконки приложения
        ImageView iconView = new ImageView(new Image(getClass().getResourceAsStream("/com/example/calculatorapp/ico_1.png")));
        iconView.setFitWidth(24); // Установите ширину иконки
        iconView.setFitHeight(24); // Установите высоту иконки
        Label iconLabel = new Label();
        iconLabel.setGraphic(iconView);
        iconLabel.setPadding(new Insets(0, 10, 0, 0));

        // Создание контекстного меню для выбора темы
        ContextMenu themeMenu = new ContextMenu();
        MenuItem darkTheme = new MenuItem("Тёмная");
        MenuItem lightTheme = new MenuItem("Светлая");
        MenuItem gamerTheme = new MenuItem("Геймерская");

        darkTheme.setOnAction(e -> calculatorView.setTheme("dark.css"));
        lightTheme.setOnAction(e -> calculatorView.setTheme("light.css"));
        gamerTheme.setOnAction(e -> calculatorView.setTheme("gamer.css"));

        themeMenu.getItems().addAll(darkTheme, lightTheme, gamerTheme);

        // Добавление события для показа контекстного меню при нажатии на иконку
        iconLabel.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                themeMenu.show(iconLabel, event.getScreenX(), event.getScreenY());
            }
        });

        // Добавление заголовка окна
        Label title = new Label("Калькулятор");
        title.setStyle("-fx-text-fill: white;");

        // Создание разделителя для выравнивания кнопок справа
        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Создание кнопок управления окном (свернуть, развернуть, закрыть)
        Button minimizeButton = new Button("_");
        minimizeButton.setStyle("-fx-background-color: #555555; -fx-text-fill: white;");
        minimizeButton.setOnAction(event -> stage.setIconified(true));

        Button maximizeButton = new Button("□");
        maximizeButton.setStyle("-fx-background-color: #555555; -fx-text-fill: white;");
        maximizeButton.setOnAction(event -> stage.setMaximized(!stage.isMaximized()));

        Button closeButton = new Button("X");
        closeButton.setStyle("-fx-background-color: #ff5555; -fx-text-fill: white;");
        closeButton.setOnAction(event -> stage.close());

        // Добавление кнопок в панель заголовка
        HBox buttons = new HBox(5, minimizeButton, maximizeButton, closeButton);

        // Добавление всех элементов в панель заголовка
        titleBar.getChildren().addAll(iconLabel, title, spacer, buttons);
        return titleBar;
    }

    /**
     * Метод для включения возможности изменения размеров окна.
     * @param stage основной Stage приложения
     * @param root корневой элемент BorderPane
     */
    private void enableWindowResizing(Stage stage, BorderPane root) {
        final int RESIZE_MARGIN = 10;

        root.setOnMousePressed(event -> {
            startX = event.getScreenX();
            startY = event.getScreenY();
            startWidth = stage.getWidth();
            startHeight = stage.getHeight();
        });

        root.setOnMouseDragged(event -> {
            if (root.getCursor() == javafx.scene.Cursor.E_RESIZE) {
                double newWidth = startWidth + (event.getScreenX() - startX);
                if (newWidth >= stage.getMinWidth()) {
                    stage.setWidth(newWidth);
                }
            } else if (root.getCursor() == javafx.scene.Cursor.S_RESIZE) {
                double newHeight = startHeight + (event.getScreenY() - startY);
                if (newHeight >= stage.getMinHeight()) {
                    stage.setHeight(newHeight);
                }
            }
        });

        root.setOnMouseMoved(event -> {
            if (event.getX() > stage.getWidth() - RESIZE_MARGIN) {
                root.setCursor(javafx.scene.Cursor.E_RESIZE);
            } else if (event.getY() > stage.getHeight() - RESIZE_MARGIN) {
                root.setCursor(javafx.scene.Cursor.S_RESIZE);
            } else {
                root.setCursor(javafx.scene.Cursor.DEFAULT);
            }
        });

        root.setOnMouseReleased(event -> {
            root.setCursor(javafx.scene.Cursor.DEFAULT);
        });
    }

    /**
     * Главный метод запуска приложения.
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        launch(args);
    }
}
