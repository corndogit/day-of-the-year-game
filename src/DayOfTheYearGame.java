import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * Days of the year game (2 players)
 * - Start on January 1st
 * - Player can choose to increment only the day or month per turn (from 1st-31st or January-December)
 * - Whichever player calls December 31st is the winner of the game
 */
public class DayOfTheYearGame {
    // Constants
    static final int DAY_MAX = 31;
    static final int MONTH_MAX = 12;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        // Create our LocalDate objects
        int currentYear = LocalDate.now().getYear();
        LocalDate gameDate = LocalDate.of(currentYear, 1, 1);
        LocalDate winDate = LocalDate.of(currentYear, MONTH_MAX, DAY_MAX); // equivalent to 1st Jan of <currentYear>
        LocalDate playerDate = null;  // this is the date created from the player's input after each turn

        // Run the game until gameDate is 2022-12-31 - players take turns altering the gameDate
        int playerTurn = 1;
        while (!gameDate.equals(winDate)) {
            System.out.printf("The current date is %d of %s %d%n",
                    gameDate.getDayOfMonth(), gameDate.getMonth(), gameDate.getYear());
            System.out.printf("It is Player %d's Turn!%n", playerTurn);
            System.out.println("Do you want to increase the day or month? (day or month)");
            String playerOneSelect = in.nextLine();

            switch (playerOneSelect.toLowerCase()) {
                case "day":
                    // check if max day of current month has been reached to prevent deadlock
                    if (gameDate.getDayOfMonth() >= gameDate.lengthOfMonth()) {
                        System.out.println("There are no more days which can be selected in this month!");
                        break;
                    }
                    System.out.printf("Please type a valid day number for %s%n", gameDate.getMonth());
                    while (playerDate == null) {
                        // Take the player's input day
                        int playerDay = 0;
                        try {
                            playerDay = Integer.parseInt(in.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid day - please enter an integer value.");
                        }
                        // check if player's day is greater than current (and that we didn't just catch an exception)
                        if ((playerDay <= gameDate.getDayOfMonth()) && (playerDay >= 0)) {
                            System.out.println("Invalid day - your day must be greater than the current one.");
                        } else {
                            try {
                                playerDate = gameDate.withDayOfMonth(playerDay);
                                gameDate = playerDate;
                            } catch (DateTimeException e) {
                                System.out.println("Invalid day - outside the range of days in this month.");
                            }
                        }
                    }
                    // change between player 1 and 2 and reset playerDate if no one won
                    if (!gameDate.equals(winDate)) {
                        playerTurn = (playerTurn == 1) ? 2 : 1;
                        playerDate = null;
                    }
                    break;

                case "month":
                    // check we haven't reached the maximum month value to prevent deadlock
                    if (gameDate.getMonthValue() == MONTH_MAX) {
                        System.out.println("Cannot choose any more months!");
                        break;
                    }
                    System.out.printf("Please type a valid month number between %d and 12%n", gameDate.getMonthValue());
                    while (playerDate == null) {
                        // Take the player's input month
                        int playerMonth = 0;
                        try {
                            playerMonth = Integer.parseInt(in.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid month - please enter an integer value.");
                        }
                        if (playerMonth <= gameDate.getMonthValue()) {
                            System.out.println("Invalid month - your month must be greater than the current one.");
                        } else {
                            try {
                                // check if the new month has enough days
                                int lengthOfNewMonth = LocalDate.of(currentYear, playerMonth, 1).lengthOfMonth();
                                if (lengthOfNewMonth >= gameDate.getDayOfMonth()) {
                                    playerDate = gameDate.withMonth(playerMonth);
                                    gameDate = playerDate;
                                } else {
                                    System.out.println("Invalid month - new month has less days than current.");
                                }
                            } catch (DateTimeException e) {
                                System.out.println("Invalid month - outside the range of possible months.");
                            }
                        }
                    }
                    if (!gameDate.equals(winDate)) {
                        playerTurn = (playerTurn == 1) ? 2 : 1;  // change between player 1 and 2 if no one won
                        playerDate = null;
                    }
                    break;

                case "exit":
                    // todo remove this before submission
                    System.out.println("Exiting...");
                    System.exit(0);

                default:
                    System.out.println("Input invalid, please try again!");
            }
        }
        System.out.printf("The current date is %d of %s %d%n",
                gameDate.getDayOfMonth(), gameDate.getMonth(), gameDate.getYear());
        System.out.printf("Player %d is the winner of the game!%n", playerTurn);
    }
}
