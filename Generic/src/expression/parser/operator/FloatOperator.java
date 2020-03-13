package expression.parser.operator;

public class FloatOperator implements Operator<Float> {
    @Override
    public Float add(Float a, Float b) {
        return a + b;
    }

    @Override
    public Float subtract(Float a, Float b) {
        return a - b;
    }

    @Override
    public Float multiply(Float a, Float b) {
        return a * b;
    }

    @Override
    public Float divide(Float a, Float b) {
        return a / b;
    }

    @Override
    public Float min(Float a, Float b) {
        return Float.min(a, b);
    }

    @Override
    public Float max(Float a, Float b) {
        return Float.max(a, b);
    }

    @Override
    public Float negate(Float x) {
        return -x;
    }

    @Override
    public Float count(Float x) {
        return (float) Integer.bitCount(Float.floatToIntBits(x));
    }

    @Override
    public Float parse(String value) {
        return Float.parseFloat(value);
    }

    @Override
    public Float parse(int value) {
        return (float) value;
    }
}
