package expression.parser;

import expression.parser.expressions.*;
import expression.parser.operator.*;

import java.util.List;
import java.util.Map;

public class ExpressionParser<T> extends BaseParser {
    private String lastOperator;
    private static final int TOP_LEVEL = 3;
    private static final int PRIME_LEVEL = 0;
    private final Operator<T> modeOperator;
    private static final Map<String, Integer> PRIORITIES = Map.of(
            "min", 3,
            "max", 3,
            "+", 2,
            "-", 2,
            "*", 1,
            "/", 1
    );
    private static final List<String> UNARY_OPERATORS = List.of(
            "count"
    );

    public ExpressionParser(Operator<T> modeOperator) {
        this.modeOperator = modeOperator;
    }

    public CommonExpression<T> parse(String expression) {
        setSource(new StringSource(expression));
        nextChar();
        lastOperator = null;
        final CommonExpression<T> commonExpression = parseLevel(TOP_LEVEL);
        if (ch != '\0') {
            throw error("Unexpected close bracket");
        }
        return commonExpression;
    }

    private CommonExpression<T> parseLevel(int level) {
        if (level == PRIME_LEVEL) {
            return getPrimeExpression();
        } else {
            CommonExpression<T> expression = parseLevel(level - 1);
            skipWhitespaces();
            while (lastOperator != null || ch != '\0' && ch != ')') {
                if (lastOperator == null) {
                    getOperator();
                }
                if (PRIORITIES.get(lastOperator) == level) {
                    String op = lastOperator;
                    lastOperator = null;
                    expression = makeExpression(op, expression, parseLevel(level - 1));
                    skipWhitespaces();
                } else {
                    break;
                }
            }
            return expression;
        }
    }

    private void getOperator() {
        StringBuilder stringBuilder = new StringBuilder();
        while (ch != '\0' && !PRIORITIES.containsKey(stringBuilder.toString()) && stringBuilder.length() <= 3) {
            stringBuilder.append(ch);
            nextChar();
        }
        if (!PRIORITIES.containsKey(stringBuilder.toString())) {
            throw error("Expected operator");
        } else {
            lastOperator = stringBuilder.toString();
        }
    }

    private CommonExpression<T> getPrimeExpression() {
        skipWhitespaces();
        if (ch == '(') {
            nextChar();
            CommonExpression<T> expression = parseLevel(TOP_LEVEL);
            expect(')');
            return expression;
        } else if (ch == '-') {
            nextChar();
            if (between('0', '9')) {
                return getConstExpression(true);
            } else {
                return new Negate<>(getPrimeExpression(), modeOperator);
            }
        } else if (between('0', '9')) {
            return getConstExpression(false);
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            while (between('a', 'z') || between('0', '9')) {
                stringBuilder.append(ch);
                nextChar();
            }
            if (UNARY_OPERATORS.contains(stringBuilder.toString())) {
                return makeExpression(stringBuilder.toString(), getPrimeExpression());
            } else if (checkVariable(stringBuilder.toString())) {
                return new Variable<>(stringBuilder.toString(), modeOperator);
            } else {
                throw error("Invalid variable");
            }
        }
    }

    private boolean checkVariable(String var) {
        return var.length() == 1 && var.charAt(0) >= 'x' && var.charAt(0) <= 'z';
    }

    private CommonExpression<T> getConstExpression(boolean isNegative) {
        StringBuilder stringBuilder = new StringBuilder(isNegative ? "-" : "");
        while (between('0', '9')) {
            stringBuilder.append(ch);
            nextChar();
        }
        try {
            return new Const<>(modeOperator.parse(stringBuilder.toString()));
        } catch (NumberFormatException e) {
            throw error("Invalid const expression");
        }
    }

    private CommonExpression<T> makeExpression(String operator, CommonExpression<T> a, CommonExpression<T> b) {
        switch (operator) {
            case "+":
                return new Add<>(a, b, modeOperator);
            case "-":
                return new Subtract<>(a, b, modeOperator);
            case "*":
                return new Multiply<>(a, b, modeOperator);
            case "/":
                return new Divide<>(a, b, modeOperator);
            case "min":
                return new Min<>(a, b, modeOperator);
            case "max":
                return new Max<>(a, b, modeOperator);
            default:
                throw error("Unsupported operator: " + operator);
        }
    }

    private CommonExpression<T> makeExpression(String operator, CommonExpression<T> x) {
        switch (operator) {
            case "count":
                return new Count<>(x, modeOperator);
            default:
                throw error("Unsupported operator: " + operator);
        }
    }

    private void skipWhitespaces() {
        while (Character.isWhitespace(ch)) {
            nextChar();
        }
    }
}
