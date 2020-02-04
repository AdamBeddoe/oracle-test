/**
 * Interface for a coin of any currency, defined by its value.
 * Implementer must check that coins values are positive.
 */
public interface Coin {

    /**
     * Get the value of this type of coin.
     * @return The value of the coin.
     */
    double getValue();

}
