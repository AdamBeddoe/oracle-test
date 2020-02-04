import exceptions.InvalidCurrencyException;
import exceptions.NotEnoughChangeException;

import java.util.Map;

/**
 * API for the change tracker of the vending machine.
 * API caller is responsible for ensuring that the coins are valid.
 */
public interface VendingMachineChangeApi {

    /**
     * Setup the vending machine to an initial state.
     * @param initialFloat A map of coins to quantities, representing the amounts of different coins in the initial
     *                     float.
     * @throws InvalidCurrencyException If there exist any negative coin counts.
     */
    void setup(Map<Coin, Integer> initialFloat) throws InvalidCurrencyException;


    /**
     * Register a coin that has been deposited by the user, adding it to the float.
     * @param coin The coin that has been deposited.
     */
    void deposit(Coin coin);


    /**
     * Produces a collection of coins and removes them from the float, throwing an exception if not enough change is
     * available.
     * @param changeValue The value of the change to return.
     * @return A map of coins to quantities that totals the changeValue.
     * @throws NotEnoughChangeException If there is insufficient total float, or if the total cannot be produced with
     *                                  the current coin values.
     */
    Map<Coin, Integer> returnChange(Double changeValue) throws NotEnoughChangeException, InvalidCurrencyException;

}
