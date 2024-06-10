package com.example.calculatorapp;

public class CalculatorModel {
    private StringBuilder currentNumber = new StringBuilder();
    private StringBuilder currentExpression = new StringBuilder();
    private String operator = "";
    private double previousValue = 0;
    private boolean clearNext = false;
    private String errorMessage = "";

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

    public void setOperator(String operator) {
        try {
            if (!this.operator.isEmpty()) {
                calculate(); // Perform the calculation if there's a pending operator
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

    private String formatNumber(double number) {
        if (number == (long) number) {
            return String.format("%d", (long) number);
        } else {
            return String.format("%s", number);
        }
    }

    public void calculate() {
        try {
            if (operator.isEmpty()) return; // No pending operation

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

            previousValue = result; // Update previousValue with the result for the next operation
            currentNumber.setLength(0);
            currentNumber.append(formatNumber(result));
            currentExpression.append(" = ").append(formatNumber(result));
            operator = "";
            clearNext = true;
        } catch (NumberFormatException e) {
            setError("Ошибка: неверный формат числа");
        }
    }

    public void clear() {
        currentNumber.setLength(0);
        currentNumber.append("0");
        currentExpression.setLength(0);
        operator = "";
        previousValue = 0;
        errorMessage = "";
    }

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

    public String getCurrentValue() {
        if (!errorMessage.isEmpty()) {
            return errorMessage;
        }
        return formatNumber(Double.parseDouble(currentNumber.toString()));
    }

    public String getCurrentExpression() {
        return currentExpression.toString();
    }

    public void setError(String message) {
        errorMessage = message;
        currentNumber.setLength(0);
        currentNumber.append("Ошибка");
        currentExpression.setLength(0);
        currentExpression.append("Ошибка");
    }
}
