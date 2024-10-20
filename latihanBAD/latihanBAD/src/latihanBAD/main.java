package latihanBAD;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class main extends Application implements EventHandler<ActionEvent> {
    private BorderPane bp;
    private GridPane gp;
    private TextField tf;
    private String text = "";
    private double number = 0;
    private double result = 0;
    private String operator = "";

    @Override
    public void start(Stage stage) throws Exception {
        initialization();
        setComponent();

        stage.setTitle("Calculator");
        Scene scene = new Scene(bp, 350, 400);
        stage.setScene(scene);
        stage.show();
    }

    public void initialization() {
        bp = new BorderPane();
        gp = new GridPane();
        tf = new TextField();

        String[] text = {"<-", "C", "√", "1/x",
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+"};

        int x = 0, y = 0;

        for (String buttonText : text) {
            Button buttons = new Button(buttonText);
            buttons.setPrefSize(90, 70);
            buttons.setStyle("-fx-font-size:20;");
            buttons.setOnAction(this);

            if (x == 4) {
                x = 0;
                y++;
            }

            gp.add(buttons, x, y);
            x++;
        }
    }

    public void setComponent() {
        tf.setPrefHeight(70);
        tf.setAlignment(Pos.CENTER_RIGHT);
        tf.setStyle("-fx-font-size:20;");
        tf.setEditable(false);
        bp.setTop(tf);
        bp.setCenter(gp);

        bp.setAlignment(gp, Pos.CENTER);
    }

    private double evalExpression(String expression) {
		String[] parts = expression.split(" ");
		  if (parts.length % 2 == 0) {
		        throw new IllegalArgumentException("Harus ada angka setelah operator");
		    }

		    double result = Double.parseDouble(parts[0]);

		    for (int i = 1; i < parts.length - 1; i += 2) {
		        String operator = parts[i];
		        double operand = Double.parseDouble(parts[i + 1]);
		        switch (operator) {
	            case "+":
	                result += operand;
	                break;
	            case "-":
	                result -= operand;
	                break;
	            case "*":
	                result *= operand;
	                break;
	            case "/":
	                if (operand == 0) {
	                    throw new ArithmeticException("Cannot divide by zero");
	                }
	                result /= operand;
	                break;
	            default:
	                throw new IllegalArgumentException("Invalid operator: " + operator);
	        }
	    }
	    return result;
	}

    @Override
    public void handle(ActionEvent e) {
        Button btn = (Button) e.getSource();
        switch (btn.getText()) {
            case "<-":
                if (tf.getText().length() == 1) {
                    tf.setText("0");
                } else {
                    text = tf.getText().substring(0, tf.getText().length() - 1);
                    tf.setText(text);
                }
                break;

            case "C":
                text = "";
                tf.clear();
                result = 0;
                operator = "";
                break;

            case "√":
                number = Math.sqrt(Double.parseDouble(tf.getText()));
                text = String.valueOf(number);
                tf.setText(text);
                break;

            case "1/x":
                number = 1 / Double.parseDouble(tf.getText());
                text = String.valueOf(number);
                tf.setText(text);
                break;

            case "/":
            case "*":
            case "-":
            case "+":
               
                if (!operator.isEmpty()) {
                    try {
                        result = evalExpression(tf.getText());
                        tf.setText(String.valueOf(result));
                    } catch (Exception ex) {
                        tf.setText("Error");
                    }
                }

                operator = btn.getText();
                text = tf.getText() + " " + operator + " ";
                tf.setText(text);
                break;

            case ".":
                if (text.startsWith(".")) {
                    return;
                } else {
                    text = tf.getText() + ".";
                    tf.setText(text);
                }
                break;

            case "=":
                try {
                    
                    String expression = tf.getText();
                    result = evalExpression(expression);
                    tf.setText(String.valueOf(result));
                    
                    operator = "";
                } catch (Exception ex) {
                    tf.setText("Error");
                }
                break;

            default:
                if (text.equals("0")) {
                    text = btn.getText();
                    tf.setText(text);
                } else {
                    text += btn.getText();
                    tf.setText(text);
                }
                break;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
