package expression.parser.expressions;

import expression.parser.operator.*;

public class Negate<T> extends AbstractUnaryOperator<T> {
    public Negate(CommonExpression<T> expression, Operator<T> operator) {
        super(expression, operator);
    }

    @Override
    protected T operate(T x) {
        return operator.negate(x);
    }

    @Override
    protected String getOperator() {
        return "-";
    }
}
