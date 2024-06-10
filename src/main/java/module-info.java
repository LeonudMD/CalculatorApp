module com.example.calculatorapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.calculatorapp to javafx.fxml;
    exports com.example.calculatorapp;
}