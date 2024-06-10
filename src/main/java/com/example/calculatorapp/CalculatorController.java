package com.example.calculatorapp;

public class CalculatorController {
    private CalculatorModel model;
    private CalculatorView view;

    public CalculatorController(CalculatorView view) {
        this.view = view;
        this.model = new CalculatorModel();
    }

    public void processInput(String input) {
        try {
            switch (input) {
                case "+":
                case "-":
                case "*":
                case "/":
                case "%":
                case "1/x":
                case "x^2":
                case "√x":
                    model.setOperator(input);
                    break;
                case "=":
                    model.calculate();
                    break;
                case "C":
                    model.clear();
                    break;
                case "CE":
                    model.clearEntry();
                    break;
                case "⌫":
                    model.backspace();
                    break;
                case "+/-":
                    model.toggleSign();
                    break;
                default:
                    model.appendNumber(input);
            }
            view.updateDisplay(model.getCurrentValue());
            view.updateCurrentOperationDisplay(model.getCurrentExpression());
        } catch (Exception e) {
            model.setError("Ошибка: неизвестная ошибка");
            view.updateDisplay(model.getCurrentValue());
            view.updateCurrentOperationDisplay(model.getCurrentExpression());
        }
    }
}

