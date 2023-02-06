# csm41-coursework1
A simple CLI game written in Java as part of my first CSCM41 coursework at Swansea University. I received a mark of 95% for this coursework, and would've received
full marks if not for one small bug where one string would sometimes print twice if an invalid option was selected. This bug has sinced been rectified.

## Rules  
- Players take turns increasing either the **day** or the **month** of the year, starting from a given date (default is 1st January)
- Players can only increase the day or month by entering its number, and the option to increase past the maximum day or month will be not allowed. (e.g. 30th February or a month value greater than December)
Leap years are accounted for however, so the 29th February is allowed if it's currently a leap year!
- The winner of the game is the player who increases the date to the 31st December.

## Setup
- Clone the repository and build the DayOfTheYear.java source file by typing `javac DayOfTheYearGame.java`
- Run the program by typing `java DayOfTheYearGame <day> <month>` (Note: day and month are optional arguments and the program will default to 1st January if omitted)
