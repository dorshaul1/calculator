package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView result;
    private String expression = "";
    private boolean isEqualClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        result = findViewById(R.id.textViewResult);
    }

    public void handleClickOnNumber(View view) {
        Button button = (Button) view;
        String number = button.getText().toString();

        if (isEqualClicked) {
            expression = "";
            isEqualClicked = false;
        }

        expression += number;
        result.setText(expression);
    }

    public void handleClickOnOperator(View view) {
        Button button = (Button) view;
        String operator = button.getText().toString();

        if (!expression.isEmpty() && "+-×÷%".contains(expression.substring(expression.length() - 1))) {
            return;
        }

        expression += " " + operator + " ";
        result.setText(expression);
    }


    public void handleClickOnEqual(View view) {
        try {
            double calculationResult = evaluateExpression(expression);
            result.setText(formatResult(calculationResult));
            expression = formatResult(calculationResult);
            isEqualClicked = true;
        } catch (Exception e) {
            result.setText("Error");
            expression = "";
        }
    }


    public void handleClickOnClear(View view) {
        expression = "";
        result.setText("");
    }

    private double evaluateExpression(String expr) {
        String[] tokens = expr.split(" ");
        double result = 0.0;
        double current = Double.parseDouble(tokens[0]);
        String currentOperator = "+";

        for (int i = 1; i < tokens.length; i++) {
            String token = tokens[i];
            if ("×÷%".contains(token)) {
                double nextNumber = Double.parseDouble(tokens[++i]);
                current = applyOperator(current, nextNumber, token);
            } else if ("+-".contains(token)) {
                result = applyOperator(result, current, currentOperator);
                currentOperator = token;
                current = Double.parseDouble(tokens[++i]);
            }
        }

        result = applyOperator(result, current, currentOperator);
        return result;
    }


    private double applyOperator(double num1, double num2, String operator) {
        switch (operator) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "×":
                return num1 * num2;
            case "÷":
                if (num2 != 0) {
                    return num1 / num2;
                } else {
                    throw new ArithmeticException("Cannot divide by zero");
                }
            case "%":
                if (num2 != 0) {
                    return num1 % num2;
                } else {
                    throw new ArithmeticException("Cannot perform modulus with zero");
                }
            default:
                return num1;
        }
    }


    private String formatResult(double value) {
        if (value == (int) value) {
            return String.valueOf((int) value);
        } else {
            return String.valueOf(value);
        }
    }
}