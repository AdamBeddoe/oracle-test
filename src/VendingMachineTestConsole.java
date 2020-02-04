import exceptions.InvalidCurrencyException;
import exceptions.NotEnoughChangeException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A simple console application for using the vending machine change API implementation.
 */
public class VendingMachineTestConsole {

    public static void main(String[] args) {
        VendingMachineChangeApi vendingMachine = new VendingMachineChangeController();

        Scanner in = new Scanner(System.in);

        boolean running = true;
        while (running) {
            System.out.println("Command line change API running:");
            System.out.println("Enter 'quit' or 'q' to exit.");
            System.out.println("Enter 'setup' to setup the vending machine.");
            System.out.println("Enter 'deposit' to deposit a coin.");
            System.out.println("Enter 'change' to request change of a certain amount.");
            String s = in.nextLine().toLowerCase();

            switch (s) {
                case "q":
                    running = false;
                    break;
                case "quit":
                    running = false;
                    break;
                case "setup":
                    setup(vendingMachine);
                    break;
                case "deposit":
                    deposit(vendingMachine);
                    break;
                case "change":
                    change(vendingMachine);
                    break;
                default:
                    System.out.println("Invalid command.");
                    break;
            }
        }
    }

    private static void setup(VendingMachineChangeApi vendingMachine) {
        List<Double> values = inputDoubles();

        Map<Double, Long> counts = values.stream()
                .collect(Collectors.groupingBy(x -> x, Collectors.counting()));

        Map<Coin, Integer> coins = new HashMap<>();
        counts.forEach((k, v) -> {
            try {
                coins.put(new TestCoin(k), v.intValue());
            } catch (InvalidCurrencyException e) {
                System.out.println("Invalid coin value!");
            }
        });

        try {
            vendingMachine.setup(coins);
            if (coins.keySet().size() > 0) System.out.println("Setup with float:");
            coins.forEach((k, v) -> System.out.println(k.getValue() + " x" + v));
        } catch (InvalidCurrencyException e) {
            System.out.println("Setup failed, invalid input, please try again.");
        }
    }

    private static void deposit(VendingMachineChangeApi vendingMachine) {
        Double value = inputDouble();

        try {
            vendingMachine.deposit(new TestCoin(value));
            System.out.println("Successfully deposited coin!");
        } catch (InvalidCurrencyException e) {
            System.out.println("Invalid coin value!");
        }
    }

    private static void change(VendingMachineChangeApi vendingMachine) {
        Double value = inputDouble();

        try {
            Map<Coin, Integer> coins = vendingMachine.returnChange(value);
            if (coins.keySet().size() > 0) System.out.println("Change returned:");
            coins.forEach((k, v) -> System.out.println(k.getValue() + " x" + v));

        } catch (InvalidCurrencyException e) {
            System.out.println("Invalid coin value!");
        } catch (NotEnoughChangeException e) {
            System.out.println("Cannot return change for this value!");
        }
    }

    private static List<Double> inputDoubles() {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter values separated by spaces.");

        List<String> valueStrings = Arrays.stream(in.nextLine().split(" "))
                .collect(Collectors.toList());

        valueStrings.removeAll(Collections.singleton(""));

        return valueStrings.stream()
                .map(Double::parseDouble)
                .collect(Collectors.toList());
    }

    private static Double inputDouble() {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the value.");

        List<String> valueStrings = Arrays.stream(in.nextLine().split(" "))
                .collect(Collectors.toList());

        valueStrings.removeAll(Collections.singleton(""));

        Optional<String> value = valueStrings.stream().findFirst();

        if (value.isPresent()) {
            return Double.parseDouble(value.get());
        }
        else {
            System.out.println("Invalid input value.");
            return 0.0;
        }
    }

}
