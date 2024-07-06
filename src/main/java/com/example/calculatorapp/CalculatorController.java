package com.example.calculatorapp;

/**
 * Контроллер калькулятора, отвечающий за взаимодействие между представлением (View) и моделью (Model).
 * Этот класс обрабатывает ввод пользователя и обновляет интерфейс в соответствии с результатами вычислений.
 */
public class CalculatorController {
    private CalculatorModel model; // Модель калькулятора
    private CalculatorView view; // Представление калькулятора

    /**
     * Конструктор инициализирует контроллер с указанным представлением.
     * @param view представление калькулятора
     */
    public CalculatorController(CalculatorView view) {
        this.view = view;
        this.model = new CalculatorModel();
    }

    /**
     * Обрабатывает ввод пользователя и выполняет соответствующие действия.
     * @param input ввод пользователя
     */
    public void processInput(String input) {
        try {
            switch (input) {
                case "+": // Установка оператора сложения
                case "-": // Установка оператора вычитания
                case "*": // Установка оператора умножения
                case "/": // Установка оператора деления
                case "%": // Установка оператора остатка от деления
                case "1/x": // Установка оператора обратного значения
                case "x^2": // Установка оператора возведения в квадрат
                case "√x": // Установка оператора квадратного корня
                    model.setOperator(input);
                    break;
                case "=": // Выполнение вычисления
                    model.calculate();
                    break;
                case "C": // Очистка всех значений
                    model.clear();
                    break;
                case "CE": // Очистка текущего значения
                    model.clearEntry();
                    break;
                case "⌫": // Удаление последнего символа
                    model.backspace();
                    break;
                case "+/-": // Переключение знака текущего числа
                    model.toggleSign();
                    break;
                default: // Добавление цифры или десятичной точки
                    model.appendNumber(input);
            }
            // Обновление отображаемого значения и текущего выражения
            view.updateDisplay(model.getCurrentValue());
            view.updateCurrentOperationDisplay(model.getCurrentExpression());
        } catch (Exception e) {
            // Обработка неизвестных ошибок
            model.setError("Ошибка: неизвестная ошибка");
            view.updateDisplay(model.getCurrentValue());
            view.updateCurrentOperationDisplay(model.getCurrentExpression());
        }
    }
}
