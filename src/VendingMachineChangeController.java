import exceptions.InvalidCurrencyException;
import exceptions.NotEnoughChangeException;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of the vending machine change tracker API.
 */
public class VendingMachineChangeController implements VendingMachineChangeApi {

    private Map<Coin, Integer> currentFloat;

    /**
     * Construct a new vending machine with empty float.
     */
    VendingMachineChangeController() {
        this.currentFloat = new HashMap<>();
    }

    /**
     * Setup the vending machine with a given float.
     * @param initialFloat A map of coins to quantities, representing the amounts of different coins in the initial
     *                     float.
     * @throws InvalidCurrencyException If a negative number of coins is passed.
     */
    @Override
    public void setup(Map<Coin, Integer> initialFloat) throws InvalidCurrencyException {
        for (Integer v : initialFloat.values()) {
            if (v < 0) throw new InvalidCurrencyException();
        }

        this.currentFloat = initialFloat;
    }

    /**
     * Deposit a coin into the vending machine.
     * @param coin The coin that has been deposited.
     */
    @Override
    public void deposit(Coin coin) {
        if (currentFloat.containsKey(coin)) {
            Integer numberOfCoins = currentFloat.get(coin);
            this.currentFloat.put(coin, numberOfCoins + 1);
        } else {
            this.currentFloat.put(coin, 1);
        }
    }

    /**
     * Return the coins to return to the user to make the total value of change.
     * @param changeValue The value of the change to return.
     * @return A map of coin types to number of coins.
     * @throws NotEnoughChangeException If it is not possible to construct the total with current float.
     * @throws InvalidCurrencyException If the total is negative.
     */
    @Override
    public Map<Coin, Integer> returnChange(Double changeValue) throws NotEnoughChangeException, InvalidCurrencyException {
        if (changeValue <= 0) throw new InvalidCurrencyException();

        List<Coin> coinsBySize = currentFloat.keySet().stream()
                .sorted(Comparator.comparingDouble(Coin::getValue).reversed())
                .collect(Collectors.toList());

        Map<Coin, Integer> updatedFloat = new HashMap<>(currentFloat);
        Map<Coin, Integer> returnedChange = new HashMap<>();
        Double amountRemaining = changeValue;


        for (Coin coin : coinsBySize) {
            // Taking the floor (casting to int) ensures that we do not go over the amount.
            int numberOfCoin = Math.min((int)(amountRemaining / coin.getValue()), updatedFloat.get(coin));

            if (numberOfCoin != 0) returnedChange.put(coin, numberOfCoin);
            amountRemaining -= numberOfCoin * coin.getValue();
            updatedFloat.put(coin, updatedFloat.get(coin) - numberOfCoin);
        }

        // This doesn't compare equality with zero to deal with arithmetic errors in double precision numbers
        if (amountRemaining < 0.000001) {
            this.currentFloat = updatedFloat;
            return returnedChange;
        } else {
            throw new NotEnoughChangeException();
        }
    }
}
