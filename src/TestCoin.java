import exceptions.InvalidCurrencyException;

import java.util.Objects;

/**
 * Test coin class allowing for creating coins of arbitrary positive value.
 */
public class TestCoin implements Coin {

    private Double value;

    TestCoin(Double value) throws InvalidCurrencyException {
        if (value > 0) {
            this.value = value;
        }
        else {
            throw new InvalidCurrencyException();
        }
    }

    @Override
    public double getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestCoin testCoin = (TestCoin) o;
        return Objects.equals(value, testCoin.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
