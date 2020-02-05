package expression.exceptions;

import java.util.Map;

public class ExpressionParser extends BaseParser implements Parser {
    private String lastOperator;
    private boolean isEnded;
    private static final int TOP_LEVEL = 2;
    private static final int PRIME_LEVEL = 0;
    private static final Map<String, Integer> PRIORITIES = Map.of(
            "+", 2,
            "-", 2,
            "*", 1,
            "/", 1,
            ")", TOP_LEVEL + 1
    );
    private static final Map<Character, String> FIRST_CHAR_TO_OPERATOR = Map.of(
            '+', "+",
            '-', "-",
            '*', "*",
            '/', "/",
            ')', ")"
    );

    @Override
    public TripleExpression parse(String expression) {
        setSource(new StringSource(expression));
        nextChar();
        skipWhitespaces();
        isEnded = false;
        final TripleExpression tripleExpression = parseLevel(TOP_LEVEL);
        if (!isEnded) {
            throw error("Unexpected close bracket");
        }
        return tripleExpression;
    }

    private CommonExpression parseLevel(int level) {
        if (level == PRIME_LEVEL) {
            CommonExpression primeExpression = getPrimeExpression();
            skipWhitespaces();
            if (!testOperator()) {
                throw error("Expected operator");
            }
            return primeExpression;
        } else {
            CommonExpression expression = parseLevel(level - 1);
            while (lastOperator != null && PRIORITIES.get(lastOperator) == level) {
                expression = makeExpression(lastOperator, expression, parseLevel(level - 1));
            }
            if (level == TOP_LEVEL) {
                if (lastOperator == null || !lastOperator.equals(")")) {
                    throw error("Expected close bracket");
                }
                lastOperator = null;
            }
            return expression;
        }
    }

    private CommonExpression getPrimeExpression() {
        if (test('(')) {
            return parseLevel(TOP_LEVEL);
        } else if (test('-')) {
            skipWhitespaces();
            if (between('0', '9')) {
                return getConstExpression(true);
            } else {
                return new CheckedNegate(getPrimeExpression());
            }
        } else if (testOperator()) {
            throw error("Unexpected operator");
        } else if (between('0', '9')) {
            return getConstExpression(false);
        } else {
            return getVariableExpression();
        }
    }


    private CommonExpression getVariableExpression() {
        StringBuilder stringBuilder = new StringBuilder();
        while (between('x', 'z') || between('0', '9')) {
            stringBuilder.append(ch);
            nextChar();
        }
        if (stringBuilder.length() == 0) {
            throw error("Unsupported variable " + ch);
        }
        return new Variable(stringBuilder.toString());
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
            throw error("Constant overflow");
        }
    }

    private boolean testOperator() {
        if (ch == '\0') {
            if (isEnded) {
                throw error("Expected close bracket");
            }
            isEnded = true;
            lastOperator = ")";
            return true;
        } else if (!FIRST_CHAR_TO_OPERATOR.containsKey(ch)) {
            return false;
        } else {
            getOperator();
            skipWhitespaces();
            return true;
        }
    }

    private void getOperator() {
        String operator = FIRST_CHAR_TO_OPERATOR.get(ch);
        expect(operator);
        lastOperator = operator;
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
