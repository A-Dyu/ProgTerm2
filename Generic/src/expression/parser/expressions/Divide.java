package expression.parser.expressions;

import expression.parser.operator.*;

public class Divide<T extends Number> extends AbstractBinaryOperator<T> {
    public Divide(CommonExpression<T> a, CommonExpression<T> b, final Operator<T> operator) {
        super(a, b, operator);
    }

    @Override
    protected String getOperator() {
        return " / ";
    }

    @Override
    protected int getPriority() {
        return 2;
    }

    @Override
    protected T operate(T a, T b) {
        return operator.divide(a, b);
    }

    @Override
    protected boolean isOrdered() {
        return true;
    }

}
