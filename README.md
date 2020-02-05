# Oracle Test

## Compiling and Running

Project requires maven and Java8.

To compile, run the following in the main project directory:

> mvn compile

To run unit tests:

> mvn test

To run the interactive console:

> mvn package
> java -jar target/OracleTest-1.0.jar

## API Design

The API is specified by the VendingMachineChangeAPI interface, and provides the following endpoints:

### Setup

`void setup(Map<Coin, Integer> initialFloat) throws InvalidCurrencyException`

Setup the vending machine with an initial float of value `initialFloat`, provided as a map of coin type to counts of each type 
in the initial float.
Throws an `InvalidCurrencyException` if any value is negative.

### Desposit

`void deposit(Coin coin);`

Deposit a coin into the vending machine; adding the coin to the float.


#### Design Notes:
- Assuming that checking of the coins being valid (not zero or negative) is done elsewhere by the vending machine, this may or
may not be worth checking here too depending on the reliability of the API user


### ReturnChange

`Map<Coin, Integer> returnChange(Double changeValue) throws NotEnoughChangeException, InvalidCurrencyException;`

Calculate the change to return to the user that sums to the `changeValue`, returns a map of coin type to counts of coin type.
Throws a `NotEnoughChangeException` if the total cannot be made with the current coins, or an `InvalidCurrencyException` if the
total is not positive.

#### Design Notes:
- If performance was an issue, throwing an exception might be inappropriate for the case of not enough change, since it may be
a common operation and throwing exceptions is much slower than returning a value. However, I thought it was a clearer way of 
indicating the error.


## General Design Notes
- Could have used `Double`s for coins but think that the `Coin` interface makes it clearer 
- Could have also used something like an `Enum` for coins, but this restricts to one set of coins, but would help ensure 
correctness. Current implementation allows for VendingMachines outside the UK market in the future easily.
- Could have used something like a list of coins rather than a Map. I think that a Map probably works best for what the hardware
would have: storing some number of coins of each type, but it might be harder to interface with a lower level language as it is
a higher-level datatype.
- Used `InvalidCurrencyException`s for negative and zero numbers. I think this is nicer as it makes it clear why it is not 
allowed, but perhaps one of the native java exceptions would have been better.
- Decided not to provide methods for bulk actions such as accepting a list of coins deposited, as I thought it was unlikely that 
the vending machine would need this and it would add to the code base that requires managing. However, it would not be an easy 
addition in future if the requirement arose.
- Unsure exactly what was meant by an interactive test harness, so built the simple console application that can be seen when
running the jar, along with unit tests as I was developing
- Tried to keep the API limited to tracking change as was specified, rather than, for example, making sure the user had deposited enough coins to accept the item after change was requested
