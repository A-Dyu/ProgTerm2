package expression.exceptions;

import java.util.List;
import java.util.Map;

public class ExpressionParser extends BaseParser implements Parser {
    private String lastOperator;
    private static final int TOP_LEVEL = 2;
    private static final int PRIME_LEVEL = -1;
    private static final Map<String, Integer> PRIORITIES = Map.of(
            "+", 2,
            "-", 2,
            "*", 1,
            "/", 1,
            "**", 0,
            "//", 0
    );
    private static final List<String> UNARY_OPERATORS = List.of(
            "log2",
            "pow2"
    );

    @Override
    public TripleExpression parse(String expression) {
        setSource(new StringSource(expression));
        nextChar();
        lastOperator = null;
        final TripleExpression tripleExpression = parseLevel(TOP_LEVEL);
        if (ch != '\0') {
            throw error("Unexpected close bracket");
        }
        return tripleExpression;
    }

    private CommonExpression parseLevel(int level) {
        if (level == PRIME_LEVEL) {
            return getPrimeExpression();
        } else {
            CommonExpression expression = parseLevel(level - 1);
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
        char first = ch;
        nextChar();
        if (PRIORITIES.containsKey(Character.toString(first) + ch)) {
            lastOperator = Character.toString(first) + ch;
            nextChar();
        } else if (PRIORITIES.containsKey(Character.toString(first))) {
            lastOperator = Character.toString(first);
        } else {
            throw error("Expected operator");
        }
    }

    private CommonExpression getPrimeExpression() {
        skipWhitespaces();
        if (ch == '(') {
            nextChar();
            CommonExpression expression = parseLevel(TOP_LEVEL);
            expect(')');
            return expression;
        } else if (ch == '-') {
            nextChar();
            if (between('0', '9')) {
                return getConstExpression(true);
            } else {
                return new CheckedNegate(getPrimeExpression());
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
            } else {
                if (checkVariable(stringBuilder.toString())) {
                    return new Variable(stringBuilder.toString());
                } else {
                    throw error("Invalid variable");
                }
            }
        }
    }

    private boolean checkVariable(String var) {
        return var.length() == 1 && var.charAt(0) >= 'x' && var.charAt(0) <= 'z';
    }

    private CommonExpression getConstExpression(boolean isNegative) {
        StringBuilder stringBuilder = new StringBuilder(isNegative ? "-" : "");
        while (between('0', '9')) {
            stringBuilder.append(ch);
            nextChar();
        }
        try {
            return new Const(Integer.parseInt(stringBuilder.toString()));
        } catch (NumberFormatException e) {
            throw error("Invalid const expression");
        }
    }

    private CommonExpression makeExpression(String operator, CommonExpression a, CommonExpression b) {
        switch (operator) {
            case "+":
                return new CheckedAdd(a, b);
            case "-":
                return new CheckedSubtract(a, b);
            case "*":
                return new CheckedMultiply(a, b);
            case "/":
                return new CheckedDivide(a, b);
            case "**":
                return new CheckedPow(a, b);
            case "//":
                return new CheckedLog(a, b);
            default:
                throw error("Unsupported operator: " + operator);
        }
    }

    private CommonExpression makeExpression(String operator, CommonExpression x) {
        switch (operator) {
            case "log2":
                return new CheckedLog2(x);
            case "pow2":
                return new CheckedPow2(x);
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
