package expression.parser.operator;

public interface Operator<T> {
    T add(T a, T b);
    T subtract(T a, T b);
    T multiply(T a, T b);
    T divide(T a, T b);
    T min(T a, T b);
    T max(T a, T b);
    T negate(T x);
    T count(T x);
    T parse(String value);
    T parse(int value);
}
