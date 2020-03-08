package expression.expressions;

import expression.parser.operator.*;

public class Subtract<T extends Number> extends AbstractBinaryOperator<T> {
    public Subtract(CommonExpression<T> a, CommonExpression<T> b, Operator<T> operator) {
        super(a, b, operator);
    }

    @Override
    protected String getOperator() {
        return " - ";
    }

    @Override
    protected boolean isOrdered() {
        return true;
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    protected T operate(T a, T b) {
        return operator.subtract(a, b);
    }
}
