import exceptions.InvalidCurrencyException;
import exceptions.NotEnoughChangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class VendingMachineChangeControllerTest {

    private VendingMachineChangeController vendingMachine;

    static private Coin pound;
    static private Coin fifty;
    static private Coin ten;

    static {
        try {
            pound = new TestCoin(1.0);
            fifty = new TestCoin(0.5);
            ten = new TestCoin(0.1);
        } catch (InvalidCurrencyException e) {
            // Do nothing because I promise its right
        }
    }


    @BeforeEach
    void setUp() {
        this.vendingMachine = new VendingMachineChangeController();
    }

    @Test
    void setup_throws_exception_for_negative_values() {
        Map<Coin, Integer> invalidInitialFloat = new HashMap<>();
        invalidInitialFloat.put(pound, 2);
        invalidInitialFloat.put(fifty, 1);
        invalidInitialFloat.put(ten, -1);

        assertThrows(InvalidCurrencyException.class, () -> vendingMachine.setup(invalidInitialFloat));
    }

    @Test
    void setup_for_valid_doesnt_throw_exception() throws InvalidCurrencyException {
        Map<Coin, Integer> invalidInitialFloat = new HashMap<>();
        invalidInitialFloat.put(pound, 2);
        invalidInitialFloat.put(fifty, 1);
        invalidInitialFloat.put(ten, 1);

        vendingMachine.setup(invalidInitialFloat);
    }

    @Test
    void deposit() throws InvalidCurrencyException, NotEnoughChangeException {
        Map<Coin, Integer> initialFloat = new HashMap<>();
        initialFloat.put(pound, 2);
        initialFloat.put(ten, 1);

        vendingMachine.setup(initialFloat);

        Map<Coin, Integer> correctChange = new HashMap<>();
        correctChange.put(fifty, 1);

        vendingMachine.deposit(fifty);

        assertEquals(vendingMachine.returnChange(0.5), correctChange);
    }

    @Test
    void returnChange_correct_valid_values() throws NotEnoughChangeException, InvalidCurrencyException {
        Map<Coin, Integer> initialFloat = new HashMap<>();
        initialFloat.put(pound, 2);
        initialFloat.put(fifty, 1);
        initialFloat.put(ten, 1);

        vendingMachine.setup(initialFloat);

        Map<Coin, Integer> correctChange = new HashMap<>();
        correctChange.put(fifty, 1);

        assertEquals(vendingMachine.returnChange(0.5), correctChange);

        correctChange = new HashMap<>();
        correctChange.put(pound, 1);
        correctChange.put(ten, 1);

        assertEquals(vendingMachine.returnChange(1.1), correctChange);

        correctChange = new HashMap<>();
        correctChange.put(pound, 1);
        correctChange.put(ten, 1);

        assertThrows(NotEnoughChangeException.class, () -> vendingMachine.returnChange(1.1));
    }

    @Test
    void returnChange_total_too_small() throws InvalidCurrencyException {
        Map<Coin, Integer> initialFloat = new HashMap<>();
        initialFloat.put(pound, 2);
        initialFloat.put(fifty, 1);
        initialFloat.put(ten, 1);

        vendingMachine.setup(initialFloat);

        assertThrows(NotEnoughChangeException.class, () -> vendingMachine.returnChange(12.1));
    }

    @Test
    void returnChange_coins_impossible() throws InvalidCurrencyException {
        Map<Coin, Integer> initialFloat = new HashMap<>();
        initialFloat.put(pound, 2);
        initialFloat.put(fifty, 1);
        initialFloat.put(ten, 1);

        vendingMachine.setup(initialFloat);

        assertThrows(NotEnoughChangeException.class, () -> vendingMachine.returnChange(0.3));
    }

    @Test
    void returnChange_negative_total() throws InvalidCurrencyException {
        Map<Coin, Integer> initialFloat = new HashMap<>();
        initialFloat.put(pound, 2);
        initialFloat.put(fifty, 1);
        initialFloat.put(ten, 1);

        vendingMachine.setup(initialFloat);

        assertThrows(InvalidCurrencyException.class, () -> vendingMachine.returnChange(-1.0));
    }
}