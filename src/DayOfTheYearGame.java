import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Scanner;

public class DayOfTheYearGame {
    // Constants
    static final int DECIMAL_BASE = 10;
    static final int DAY_MAX = 31;
    static final int MONTH_MAX = 12;
    static final int CURRENT_YEAR = LocalDate.now().getYear();  // year is required to create a valid LocalDate
    static final String ERROR_MESSAGE = "Input invalid, please try again!";
    static final String[] DAY_SUFFIXES = {"st", "nd", "rd"};
    /**
     * Formats the output string to fit the marking criteria using a provided LocalDate object
     * @param gameDate A LocalDate object
     * @param withOrdinal Boolean which, if true, formats the date with the ordinal (e.g. 1st, 2nd, 3rd, 4th)
     * @return Formatted output string
     */
    private static String currentDateFormatter(LocalDate gameDate, boolean withOrdinal) {
        // See if we need to format with the ordinal date (was the code run with args?)
        int day = gameDate.getDayOfMonth();
        String suffix = "";
        if (withOrdinal) {
            int index = day % DECIMAL_BASE - 1; // 0 = "st", 1 = "nd", 2 = "rd"
            if (index < DAY_SUFFIXES.length && index >= 0) {
                suffix = DAY_SUFFIXES[index];
            } else {
                suffix = "th";
            }
        }

        // Capitalize the month (e.g. JANUARY -> January)
        String month = gameDate.getMonth().toString().toLowerCase();
        month = month.substring(0, 1).toUpperCase() + month.substring(1).toLowerCase();
        return String.format("The current date is: %d%s of %s", day, suffix, month);
    }
    /**
     * Method for parsing arguments which may be used to change the game's starting date. Return the default value
     * of [CURRENT_YEAR]-1-1 if a valid date cannot be created
     * @param argsToParse the arguments provided when the file is run
     * @return LocalDate object of the date to start the game from
     */
    private static LocalDate getStartDateFromArgs(String[] argsToParse) {
        // check if program was run without arguments (use default date of Jan 1st CURRENT_YEAR)
        if (argsToParse.length == 0) {
            return LocalDate.of(CURRENT_YEAR, 1, 1);
        }
        // check if exactly 2 args (a day and month) were provided
        if (argsToParse.length != 2) {
            throw new IllegalArgumentException("Invalid number of arguments were specified");
        }
        try {
            // try and convert the String arguments to integers
            int day = Integer.parseInt(argsToParse[0]);
            int month = Integer.parseInt(argsToParse[1]);
            return LocalDate.of(CURRENT_YEAR, month, day);

        } catch (NumberFormatException | DateTimeException e) {
            // Catch exceptions thrown if arguments cannot be parsed to int, or if date is invalid.
            throw new IllegalArgumentException("Arguments could not be parsed to a date");
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        boolean hasArgs = (args.length != 0);

        // Create our LocalDate objects
        LocalDate gameDate = getStartDateFromArgs(args);
        LocalDate winDate = LocalDate.of(CURRENT_YEAR, MONTH_MAX, DAY_MAX); // equivalent to 31st Dec of <currentYear>
        LocalDate playerDate = null;  // this is the date created from the player's input after each turn

        // Run the game until gameDate is 2022-12-31 - players take turns altering the gameDate
        int playerTurn = 1;
        System.out.println(currentDateFormatter(gameDate, hasArgs));
        System.out.printf("It is Player %d's Turn!%n", playerTurn);
        System.out.print("Do you want to increase the day or month? (day or month): ");

        while (!gameDate.equals(winDate)) {
            // take the player's input
            String playerOneSelect = in.nextLine();
            switch (playerOneSelect.toLowerCase()) {
                case "day":
                    // check if max day of current month has been reached to prevent deadlock
                    if (gameDate.getDayOfMonth() >= gameDate.lengthOfMonth()) {
                        System.out.println(ERROR_MESSAGE);
                        break;
                    }
                    System.out.print("Which day do you want to pick: ");
                    while (playerDate == null) {
                        // Take the player's input day
                        int playerDay = 0;
                        try {
                            playerDay = Integer.parseInt(in.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println(ERROR_MESSAGE);
                        }
                        // check if player's day is greater than current (and that we didn't just catch an exception)
                        if ((playerDay <= gameDate.getDayOfMonth()) && (playerDay >= 0)) {
                            System.out.println(ERROR_MESSAGE);
                        } else {
                            try {
                                // set current date to input date and print the result
                                playerDate = gameDate.withDayOfMonth(playerDay);
                                gameDate = playerDate;
                                System.out.println(currentDateFormatter(gameDate, hasArgs));
                            } catch (DateTimeException e) {
                                System.out.println(ERROR_MESSAGE);
                            }
                        }
                    }
                    // change between player 1 and 2, reset playerDate and print next player's turn if no one won
                    if (!gameDate.equals(winDate)) {
                        playerTurn = (playerTurn == 1) ? 2 : 1;
                        playerDate = null;
                        System.out.printf("It is Player %d's Turn!%n", playerTurn);
                        System.out.print("Do you want to increase the day or month? (day or month): ");
                    }
                    break;

                case "month":
                    // check we haven't reached the maximum month value to prevent deadlock
                    if (gameDate.getMonthValue() == MONTH_MAX) {
                        System.out.println(ERROR_MESSAGE);
                        break;
                    }
                    System.out.print("Which month do you want to pick: ");
                    while (playerDate == null) {
                        // Take the player's input month
                        int playerMonth;
                        try {
                            playerMonth = Integer.parseInt(in.nextLine());

                            // check if the input month is larger than the current
                            if ((playerMonth <= gameDate.getMonthValue()) && (playerMonth > 0)) {
                                System.out.println(ERROR_MESSAGE);
                            } else {
                                try {
                                    // check if the new month has enough valid days
                                    int lengthOfNewMonth = LocalDate.of(CURRENT_YEAR, playerMonth, 1).lengthOfMonth();
                                    if (lengthOfNewMonth >= gameDate.getDayOfMonth()) {
                                        playerDate = gameDate.withMonth(playerMonth);
                                        gameDate = playerDate;
                                        System.out.println(currentDateFormatter(gameDate, hasArgs));
                                    } else {
                                        // ask the user the input another month number
                                        System.out.println(ERROR_MESSAGE);
                                    }
                                } catch (DateTimeException e) {
                                    // catch the exception thrown if an invalid date is generated
                                    System.out.println(ERROR_MESSAGE);
                                }
                            }
                        } catch (NumberFormatException e) {
                            // catch the exception thrown if a non-integer value is entered
                            System.out.println(ERROR_MESSAGE);
                        }
                    }
                    // change between player 1 and 2's turn if no one won
                    if (!gameDate.equals(winDate)) {
                        playerTurn = (playerTurn == 1) ? 2 : 1;
                        playerDate = null;
                        System.out.printf("It is Player %d's Turn!%n", playerTurn);
                        System.out.print("Do you want to increase the day or month? (day or month): ");
                    }
                    break;

                default:
                    System.out.println(ERROR_MESSAGE);
            }
        }
        // Game has reached a win state
        System.out.printf("Player %d is the winner of the game!%n", playerTurn);
    }
}
