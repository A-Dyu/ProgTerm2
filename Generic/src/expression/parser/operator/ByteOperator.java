package expression.parser.operator;

import expression.parser.exceptions.DivideByZeroException;

public class ByteOperator implements Operator<Byte> {
    @Override
    public Byte add(Byte a, Byte b) {
        return (byte)(a + b);
    }

    @Override
    public Byte subtract(Byte a, Byte b) {
        return (byte)(a - b);
    }

    @Override
    public Byte multiply(Byte a, Byte b) {
        return (byte)(a * b);
    }

    @Override
    public Byte divide(Byte a, Byte b) {
        if (b.equals((byte)0)) {
            throw new DivideByZeroException(a);
        }
        return (byte)(a / b);
    }

    @Override
    public Byte min(Byte a, Byte b) {
        return a < b ? a : b;
    }

    @Override
    public Byte max(Byte a, Byte b) {
        return a > b ? a : b;
    }

    @Override
    public Byte negate(Byte x) {
        return (byte)-x;
    }

    @Override
    public Byte count(Byte x) {
        return x == -1 ? 8 : (byte)(Integer.bitCount(x) % 8);
    }

    @Override
    public Byte parse(String value) {
        return Byte.parseByte(value);
    }

    @Override
    public Byte parse(int value) {
        return (byte)value;
    }
}
