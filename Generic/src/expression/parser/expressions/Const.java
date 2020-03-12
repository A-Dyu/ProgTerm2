package expression.parser.expressions;

import java.util.Objects;

public class Const<T> implements CommonExpression<T> {
    private T val;

    public Const(T val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return val.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Const<?> aConst = (Const<?>) o;
        return Objects.equals(val, aConst.val);
    }

    @Override
    public int hashCode() {
        return Objects.hash(val);
    }

    @Override
    public T evaluate(T x) {
        return val;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return val;
    }
}
