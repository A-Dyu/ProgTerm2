package expression.parser.expressions;

public interface TripleExpression<T> {
    T evaluate(T x, T y, T z);
}