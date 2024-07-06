package com.example.calculatorapp;

/**
 * Модель калькулятора, отвечающая за выполнение всех вычислений и управление состоянием калькулятора.
 */
public class CalculatorModel {
    private StringBuilder currentNumber = new StringBuilder(); // Текущее число, введенное пользователем
    private StringBuilder currentExpression = new StringBuilder(); // Текущее выражение, введенное пользователем
    private String operator = ""; // Текущий оператор
    private double previousValue = 0; // Предыдущее значение
    private boolean clearNext = false; // Флаг, указывающий на необходимость очистки следующего числа
    private String errorMessage = ""; // Сообщение об ошибке

    /**
     * Добавляет цифру или десятичную точку к текущему числу.
     * @param number цифра или десятичная точка для добавления
     */
    public void appendNumber(String number) {
        if (clearNext) {
            currentNumber.setLength(0);
            clearNext = false;
        }
        if (number.equals(".") && currentNumber.indexOf(".") != -1) {
            return;
        }
        if (currentNumber.length() == 1 && currentNumber.charAt(0) == '0' && !number.equals(".")) {
            currentNumber.setLength(0);
        }
        currentNumber.append(number);
        currentExpression.append(number);
    }

    /**
     * Устанавливает оператор для следующего вычисления.
     * @param operator оператор для установки
     */
    public void setOperator(String operator) {
        try {
            if (!this.operator.isEmpty()) {
                calculate(); // Выполняет вычисление, если есть ожидающий оператор
            } else if (currentNumber.length() > 0) {
                previousValue = Double.parseDouble(currentNumber.toString());
            }
            this.operator = operator;
            currentExpression.append(" ").append(operator).append(" ");
            clearNext = true;
        } catch (NumberFormatException e) {
            setError("Ошибка: неверный формат числа");
        }
    }

    /**
     * Форматирует число в строку, убирая лишние десятичные нули.
     * @param number число для форматирования
     * @return отформатированное число в виде строки
     */
    private String formatNumber(double number) {
        if (number == (long) number) {
            return String.format("%d", (long) number);
        } else {
            return String.format("%s", number);
        }
    }

    /**
     * Выполняет вычисление на основе текущего оператора и числа.
     * Ваня взял для диаграммы деятельности
     */
    public void calculate() {
        try {
            if (operator.isEmpty()) return; // Если нет ожидающей операции, выйти

            double currentValue = Double.parseDouble(currentNumber.toString());
            double result = previousValue;

            switch (operator) {
                case "+":
                    result = previousValue + currentValue;
                    break;
                case "-":
                    result = previousValue - currentValue;
                    break;
                case "*":
                    result = previousValue * currentValue;
                    break;
                case "/":
                    if (currentValue != 0) {
                        result = previousValue / currentValue;
                    } else {
                        setError("Ошибка: деление на ноль");
                        return;
                    }
                    break;
                case "%":
                    result = previousValue % currentValue;
                    break;
                case "1/x":
                    if (currentValue != 0) {
                        result = 1 / currentValue;
                    } else {
                        setError("Ошибка: деление на ноль");
                        return;
                    }
                    break;
                case "x^2":
                    result = currentValue * currentValue;
                    break;
                case "√x":
                    if (currentValue >= 0) {
                        result = Math.sqrt(currentValue);
                    } else {
                        setError("Ошибка: отрицательное число");
                        return;
                    }
                    break;
            }

            previousValue = result; // Обновляем previousValue результатом для следующей операции
            currentNumber.setLength(0);
            currentNumber.append(formatNumber(result));
            currentExpression.append(" = ").append(formatNumber(result));
            operator = "";
            clearNext = true;
        } catch (NumberFormatException e) {
            setError("Ошибка: неверный формат числа");
        }
    }

    /**
     * Очищает все текущие значения и выражения.
     */
    public void clear() {
        currentNumber.setLength(0);
        currentNumber.append("0");
        currentExpression.setLength(0);
        operator = "";
        previousValue = 0;
        errorMessage = "";
    }

    /**
     * Очищает текущее число.
     */
    public void clearEntry() {
        currentNumber.setLength(0);
        currentNumber.append("0");
        if (currentExpression.length() > 0) {
            int lastIndex = currentExpression.lastIndexOf(" ");
            if (lastIndex != -1) {
                currentExpression.setLength(lastIndex + 1);
            } else {
                currentExpression.setLength(0);
            }
        }
    }

    /**
     * Удаляет последний введенный символ.
     */
    public void backspace() {
        if (currentNumber.length() > 0) {
            currentNumber.deleteCharAt(currentNumber.length() - 1);
        }
        if (currentNumber.length() == 0) {
            currentNumber.append("0");
        }
        if (currentExpression.length() > 0) {
            currentExpression.deleteCharAt(currentExpression.length() - 1);
        }
    }

    /**
     * Переключает знак текущего числа.
     */
    public void toggleSign() {
        if (currentNumber.length() > 0 && !currentNumber.toString().equals("0")) {
            if (currentNumber.charAt(0) == '-') {
                currentNumber.deleteCharAt(0);
            } else {
                currentNumber.insert(0, "-");
            }
        }
        if (currentExpression.length() > 0) {
            if (currentExpression.charAt(currentExpression.length() - 1) == '-') {
                currentExpression.deleteCharAt(currentExpression.length() - 1);
            } else {
                currentExpression.append("-");
            }
        }
    }

    /**
     * Возвращает текущее значение в виде строки.
     * @return текущее значение
     */
    public String getCurrentValue() {
        if (!errorMessage.isEmpty()) {
            return errorMessage;
        }
        return formatNumber(Double.parseDouble(currentNumber.toString()));
    }

    /**
     * Возвращает текущее выражение в виде строки.
     * @return текущее выражение
     */
    public String getCurrentExpression() {
        return currentExpression.toString();
    }

    /**
     * Устанавливает сообщение об ошибке и очищает текущие значения.
     * @param message сообщение об ошибке
     */
    public void setError(String message) {
        errorMessage = message;
        currentNumber.setLength(0);
        currentNumber.append("Ошибка");
        currentExpression.setLength(0);
        currentExpression.append("Ошибка");
    }
}
