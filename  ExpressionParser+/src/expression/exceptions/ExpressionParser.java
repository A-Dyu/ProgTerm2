package expression.exceptions;

import java.util.Map;

public class ExpressionParser extends BaseParser implements Parser {
    private String lastOperator;
    private boolean isEnded;
    private static final int TOP_LEVEL = 2;
    private static final int primeLevel = 0;
    private static final Map<String, Integer> priorities = Map.of(
            "+", 2,
            "-", 2,
            "*", 1,
            "/", 1,
            ")", TOP_LEVEL + 1
    );
    private static final Map<Character, String> firstCharToOperator = Map.of(
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
        if (level == primeLevel) {
            CommonExpression primeExpression = getPrimeExpression();
            skipWhitespaces();
            if (!testOperator()) {
                throw error("Expected operator");
            }
            return primeExpression;
        }
        CommonExpression expression = parseLevel(level - 1);
        while (lastOperator != null && priorities.get(lastOperator) == level) {
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
        }
        if (!firstCharToOperator.containsKey(ch)) {
            return false;
        }
        getOperator();
        skipWhitespaces();
        return true;
    }

    private void getOperator() {
        String operator = firstCharToOperator.get(ch);
        expect(operator);
        lastOperator = operator;
    }

    private CommonExpression makeExpression(String operator, CommonExpression a, CommonExpression b) {
        if (operator.equals("+")) {
            return new CheckedAdd(a, b);
        }
        if (operator.equals("-")) {
            return new CheckedSubtract(a, b);
        }
        if (operator.equals("*")) {
            return new CheckedMultiply(a, b);
        }
        if (operator.equals("/")) {
            return new CheckedDivide(a, b);
        }
        throw error("Unsupported operator: " + operator);
    }

    private void skipWhitespaces() {
        while (Character.isWhitespace(ch)) {
            nextChar();
        }
    }
}
