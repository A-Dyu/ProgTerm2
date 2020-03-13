package expression.parser.operator;

import expression.parser.exceptions.DivideByZeroException;

import java.math.BigInteger;

public class BigIntegerOperator implements Operator<BigInteger> {
    @Override
    public BigInteger add(BigInteger a, BigInteger b) {
        return a.add(b);
    }

    @Override
    public BigInteger subtract(BigInteger a, BigInteger b) {
        return a.subtract(b);
    }

    @Override
    public BigInteger multiply(BigInteger a, BigInteger b) {
        return a.multiply(b);
    }

    @Override
    public BigInteger divide(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.valueOf(0))) {
            throw new DivideByZeroException(a);
        }
        return a.divide(b);
    }

    @Override
    public BigInteger min(BigInteger a, BigInteger b) {
        return a.min(b);
    }

    @Override
    public BigInteger max(BigInteger a, BigInteger b) {
        return a.max(b);
    }

    @Override
    public BigInteger negate(BigInteger x) {
        return x.negate();
    }

    @Override
    public BigInteger count(BigInteger x) {
        return BigInteger.valueOf(x.bitCount());
    }

    @Override
    public BigInteger parse(String value) {
        return new BigInteger(value);
    }

    @Override
    public BigInteger parse(int value) {
        return BigInteger.valueOf(value);
    }
}
