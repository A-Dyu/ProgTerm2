package expression.exceptions;

import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        ExpressionParser expressionParser = new ExpressionParser();
        TripleExpression expression;
        while (true) {
            try {
                expression = expressionParser.parse(scanner.nextLine());
                break;
            } catch (ParserException e) {
                System.out.println("Invalid expression!");
                System.out.println(e.getMessage());
                System.out.println("Please, try again");
            }
        }
        for (int x = 0; x <= 10; x++) {
            System.out.print(x + "    ");
            try {
                System.out.println(expression.evaluate(x, 0, 0));
            } catch (ExpressionException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
