package expression.parser.expressions;

public interface Expression<T> {
    T evaluate(T x);
}
