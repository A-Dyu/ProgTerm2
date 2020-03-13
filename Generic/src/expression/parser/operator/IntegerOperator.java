package expression.parser.operator;

import expression.parser.exceptions.*;

public class IntegerOperator implements Operator<Integer> {
    private final boolean isChecked;

    public IntegerOperator(boolean isChecked) {
        this.isChecked = isChecked;
    }

    @Override
    public Integer add(Integer a, Integer b) {
        if (isChecked && (b > 0 && Integer.MAX_VALUE - b < a || b < 0 && Integer.MIN_VALUE - b > a)) {
            throw new AddOverflowException(a, b);
        }
        return a + b;
    }

    @Override
    public Integer subtract(Integer a, Integer b) {
        if (isChecked && (b > 0 && Integer.MIN_VALUE + b > a || b < 0 && Integer.MAX_VALUE + b < a)) {
            throw new SubtractOverflowException(a, b);
        }
        return a - b;
    }

    @Override
    public Integer multiply(Integer a, Integer b) {
        if (isChecked && a != 0 && b != 0 && ((a * b) / a != b || (a * b) / b != a)) {
            throw new MultiplyOverflowException(a, b);
        }
        return a * b;
    }

    @Override
    public Integer divide(Integer a, Integer b) {
        if (b == 0) {
            throw new DivideByZeroException(a);
        }
        if (isChecked && a == Integer.MIN_VALUE && b == -1) {
            throw new DivideOverflowException(a, b);
        }
        return a / b;
    }

    @Override
    public Integer min(Integer a, Integer b) {
        return Integer.min(a, b);
    }

    @Override
    public Integer max(Integer a, Integer b) {
        return Integer.max(a, b);
    }

    @Override
    public Integer negate(Integer x) {
        if (isChecked && x == Integer.MIN_VALUE) {
            throw new NegateOverflowException(x);
        }
        return -x;
    }

    @Override
    public Integer count(Integer x) {
        return Integer.bitCount(x);
    }

    @Override
    public Integer parse(String value) {
        return Integer.parseInt(value);
    }

    @Override
    public Integer parse(int value) {
        return value;
    }
}
