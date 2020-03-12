package expression.parser.expressions;

import java.util.Objects;

import expression.parser.operator.*;

public abstract class AbstractBinaryOperator<T> implements CommonExpression<T> {
    private CommonExpression<T> a;
    private CommonExpression<T> b;
    protected final Operator<T> operator;

    public AbstractBinaryOperator(CommonExpression<T> a, CommonExpression<T> b, final Operator<T> operator) {
        this.a = a;
        this.b = b;
        this.operator = operator;
    }


    protected abstract String getOperator();
    protected abstract int getPriority();
    protected abstract T operate(T a, T b);
    protected abstract boolean isOrdered();

    @Override
    public T evaluate(T x) {
        return operate(a.evaluate(x), b.evaluate(x));
    }

    @Override
    public T evaluate(T x, T y, T z) { return operate(a.evaluate(x, y, z), b.evaluate(x, y, z));}

    @Override
    public String toString() {
        return "(" + a + getOperator()+ b + ')';
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        AbstractBinaryOperator abstractBinaryOperator = (AbstractBinaryOperator) object;
        return this.a.equals(abstractBinaryOperator.a) && b.equals(abstractBinaryOperator.b);
    }

    @Override
    public int hashCode() {
        return 42424241 * a.hashCode() + 31 * 31 * (Objects.hash(getOperator())) + 31 * b.hashCode();
    }
}
