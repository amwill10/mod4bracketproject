import java.sql.*;

import java.util.*;

public class Main {
    public static Scanner scan = new Scanner(System.in);
    public static String url = "jdbc:postgresql://localhost:5432/game?user=postgres&password=12345";

    public static void main(String[] args) {
        String userResponse;
        boolean playAgain = true;
        Team userPickedTeam;
        Bracket bracket = createTeams();

        System.out.println("Let's play some bball.");
        System.out.println("When prompted pick which team you think is going to win from each match. ");
        System.out.println("Today's matchups are: ");
        System.out.println("");

        while (playAgain) {
            bracket.resetBracket();
            bracket.printOriginalBracket();


            System.out.println("=======================");
            System.out.println("The first match is: " + bracket.bracketPositions.get(1).name + " VS. " + bracket.bracketPositions.get(2).name);
            Team firstRoundWinner = firstRoundSemiFinals(scan, bracket);


            System.out.println("=======================");
            System.out.println("The second match is: " + bracket.bracketPositions.get(3).name + " VS. " + bracket.bracketPositions.get(4).name);
            Team secondRoundWinner = secondRoundSemiFinals(scan, bracket);

            bracket.storeThirdAndFourth();
            bracket.printSemiFinalResults(firstRoundWinner, secondRoundWinner);

            bracket.setUpFinals();

            System.out.println("=======================");

            finalsStuff(scan, bracket);

            System.out.println("=================");
            bracket.storeFirstAndSecond();
            bracket.recordResultsInDatabase();
            playAgain = userPlayAgain(scan);


        }


    }

// 1 query
    public static Bracket createTeams() {
        ArrayList<Team> teams = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url)) {

            String sql = "SELECT * FROM teams";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                teams.add(new Team(results.getInt("id"), results.getString("name").toUpperCase()));
            }

            for (int i = 0; i < teams.size(); i++) {
                teams.get(i).addPlayers();
            }

        } catch (SQLException error) {
            System.out.println(error.getMessage());
        }


        return new Bracket(teams);

    }



    // VALIDATION
    public static String mainUserValidation(Scanner scan) {
        System.out.println("Would you like to [1] view a team's stats OR [2] choose who you think will win?");
        String userResponse = scan.nextLine();
        while (!userResponse.equalsIgnoreCase("1") && !userResponse.equalsIgnoreCase(("2"))) {
            System.out.println("WRONG RESPONSE BRO");
            System.out.println("Would you like to [1] view a team's stats OR [2] choose who you think will win?");
            userResponse = scan.nextLine();
        }

        return userResponse;
    }

    public static Team viewTeamInfoValidationFirst(Scanner scan, Bracket bracket) {
        System.out.println("Which team would you like to view information on? [1] " + bracket.bracketPositions.get(1).name + " OR " + "[2] " + bracket.bracketPositions.get(2).name);
        String userResponse = scan.nextLine();
        while (!userResponse.equalsIgnoreCase("1") && !userResponse.equalsIgnoreCase(("2"))) {
            System.out.println("WRONG RESPONSE BRO");
            System.out.println("Which team would you like to view information on? [1] " + bracket.bracketPositions.get(1).name + " OR " + "[2] " + bracket.bracketPositions.get(2).name);
            userResponse = scan.nextLine();
        }


        if (userResponse.equalsIgnoreCase("1")) {
            return bracket.bracketPositions.get(1);
        } else {
            return bracket.bracketPositions.get(2);
        }

    }

    public static Team viewTeamInfoValidationSecond(Scanner scan, Bracket bracket) {
        System.out.println("Which team would you like to view information on? [1] " + bracket.bracketPositions.get(3).name + " OR " + "[2] " + bracket.bracketPositions.get(4).name);
        String userResponse = scan.nextLine();
        while (!userResponse.equalsIgnoreCase("1") && !userResponse.equalsIgnoreCase(("2"))) {
            System.out.println("WRONG RESPONSE BRO");
            System.out.println("Which team would you like to view information on? [1] " + bracket.bracketPositions.get(3).name + " OR " + "[2] " + bracket.bracketPositions.get(4).name);
            userResponse = scan.nextLine();
        }


        if (userResponse.equalsIgnoreCase("1")) {
            return bracket.bracketPositions.get(3);
        } else {
            return bracket.bracketPositions.get(4);
        }

    }


    public static Team viewTeamInfoValidationFinals(Scanner scan, Bracket bracket) {
        System.out.println("Which team would you like to view information on? [1] " + bracket.finalsBracketPositions.get(1).name + " OR " + "[2] " + bracket.finalsBracketPositions.get(2).name);
        String userResponse = scan.nextLine();
        while (!userResponse.equalsIgnoreCase("1") && !userResponse.equalsIgnoreCase(("2"))) {
            System.out.println("WRONG RESPONSE BRO");
            System.out.println("Which team would you like to view information on? [1] " + bracket.finalsBracketPositions.get(1).name + " OR " + "[2] " + bracket.finalsBracketPositions.get(2).name);
            userResponse = scan.nextLine();
        }


        if (userResponse.equalsIgnoreCase("1")) {
            return bracket.finalsBracketPositions.get(1);
        } else {
            return bracket.finalsBracketPositions.get(2);
        }

    }

    public static String viewTeamStatsOrPlayerStatsValidation(Scanner scan, Team team) {
        System.out.println(team.name);
        System.out.println("The players on this team are: ");
        team.listPlayers();
        System.out.println("===============");
        System.out.println("Would you like to [1] view a specific player's stats OR [2] view the amount of times this team has won OR [3] view the amount of times this team has lost?");
        String userResponse = scan.nextLine();
        while (!userResponse.equalsIgnoreCase("1") && !userResponse.equalsIgnoreCase("2") && !userResponse.equalsIgnoreCase("3")) {
            System.out.println("WRONG RESPONSE BRO");
            System.out.println("Would you like to [1] view a specific player's stats OR [2] view the amount of times this team has won OR [3] view the amount of times this team has lost?");
            userResponse = scan.nextLine();
        }

        return userResponse;


    }

    public static String viewPlayersStatsYesOrNoValidation(Scanner Scan) {
        System.out.println("Would you like to view this player's stats? [1] Yes OR [2] No");
        String userResponse = scan.nextLine();
        while (!userResponse.equalsIgnoreCase("1") && !userResponse.equalsIgnoreCase("2")) {
            System.out.println("WRONG RESPONSE BRO");
            System.out.println("Would you like to view this player's stats? [1] Yes OR [2] No");
            userResponse = scan.nextLine();
        }
        return userResponse;
    }

    public static void viewSpecificPlayerStats(Scanner scan, Team team) {
        String userResponse;
        for (int i = 0; i < team.playersOnTeam.size(); i++) {
            System.out.println(team.playersOnTeam.get(i).firstName.toUpperCase() + " " + team.playersOnTeam.get(i).lastName.toUpperCase());
            userResponse = viewPlayersStatsYesOrNoValidation(scan);
            if (userResponse.equalsIgnoreCase("1")) {
                team.playersOnTeam.get(i).viewPlayerStats();
            }
        }
    }


    // USER PICK TEAMS VALIDATION

    public static Team userPickTeamRound1(Scanner scan, Bracket bracket) {
        System.out.println("Who do you think will win the second battle? [1] " + bracket.bracketPositions.get(1).name + " OR [2] " + bracket.bracketPositions.get(2).name);
        String userResponse = scan.nextLine();
        while (!userResponse.equalsIgnoreCase("1") && !userResponse.equalsIgnoreCase(("2"))) {
            System.out.println("WRONG RESPONSE BRO");
            System.out.println("Who do you think will win the second battle? [1] " + bracket.bracketPositions.get(1).name + " OR [2] " + bracket.bracketPositions.get(2).name);
            userResponse = scan.nextLine();
        }

        if (userResponse.equalsIgnoreCase("1")) {
            return bracket.bracketPositions.get(1);
        } else {
            return bracket.bracketPositions.get(2);
        }
    }

    public static Team userPickTeamRound2(Scanner scan, Bracket bracket) {
        System.out.println("Who do you think will win the second battle? [1] " + bracket.bracketPositions.get(3).name + " OR [2] " + bracket.bracketPositions.get(4).name);
        String userResponse = scan.nextLine();
        while (!userResponse.equalsIgnoreCase("1") && !userResponse.equalsIgnoreCase(("2"))) {
            System.out.println("WRONG RESPONSE BRO");
            System.out.println("Who do you think will win the second battle? [1] " + bracket.bracketPositions.get(3).name + " OR [2] " + bracket.bracketPositions.get(4).name);
            userResponse = scan.nextLine();
        }

        if (userResponse.equalsIgnoreCase("1")) {
            return bracket.bracketPositions.get(3);
        } else {
            return bracket.bracketPositions.get(4);
        }
    }

    public static Team userPickTeamFinals(Scanner scan, Bracket bracket) {
        System.out.println("Who do you think will win the finals? [1] " + bracket.finalsBracketPositions.get(1).name + " OR [2] " + bracket.finalsBracketPositions.get(2).name);
        String userResponse = scan.nextLine();
        while (!userResponse.equalsIgnoreCase("1") && !userResponse.equalsIgnoreCase("2")) {
            System.out.println("WRONG RESPONSE BRO");
            System.out.println("Who do you think will win the finals? [1] " + bracket.finalsBracketPositions.get(1).name + " OR [2] " + bracket.finalsBracketPositions.get(2).name);
            userResponse = scan.nextLine();
        }

        if (userResponse.equalsIgnoreCase("1")) {
            return bracket.finalsBracketPositions.get(1);
        } else {
            return bracket.finalsBracketPositions.get(2);
        }
    }

    // FIRST ROUND SEMI FINALS

    public static Team firstRoundSemiFinals(Scanner scan, Bracket bracket) {
        boolean didChooseTeam = false;
        Team userPickedTeam;
        Team viewPickedTeam;
        String userResponse;
        String userResponse2;
        while (!didChooseTeam) {
            userResponse = mainUserValidation(scan);
            if (userResponse.equalsIgnoreCase("1")) {
                viewPickedTeam = viewTeamInfoValidationFirst(scan, bracket);
                userResponse2 = viewTeamStatsOrPlayerStatsValidation(scan, viewPickedTeam);
                if (userResponse2.equalsIgnoreCase("1")) {
                    viewSpecificPlayerStats(scan, viewPickedTeam);
                } else if (userResponse2.equalsIgnoreCase("2")) {
                    viewPickedTeam.checkTeamWins();
                } else {
                    viewPickedTeam.checkTeamLosses();
                }

            } else {
                didChooseTeam = true;
            }
        }

        userPickedTeam = userPickTeamRound1(scan, bracket);
        Team firstRoundWinner = bracket.firstRoundOfSemiFinals();

        if (userPickedTeam == firstRoundWinner) {
            System.out.println("You did it!");
        } else {
            System.out.println("You suck.");
        }

        return firstRoundWinner;

    }


    public static Team secondRoundSemiFinals(Scanner scan, Bracket bracket) {
        boolean didChooseTeam = false;
        Team userPickedTeam;
        Team viewPickedTeam;
        String userResponse;
        String userResponse2;
        while (!didChooseTeam) {
            userResponse = mainUserValidation(scan);
            if (userResponse.equalsIgnoreCase("1")) {
                viewPickedTeam = viewTeamInfoValidationSecond(scan, bracket);
                userResponse2 = viewTeamStatsOrPlayerStatsValidation(scan, viewPickedTeam);
                if (userResponse2.equalsIgnoreCase("1")) {
                    viewSpecificPlayerStats(scan, viewPickedTeam);
                } else if (userResponse2.equalsIgnoreCase("2")) {
                    viewPickedTeam.checkTeamWins();
                } else {
                    viewPickedTeam.checkTeamLosses();
                }

            } else {
                didChooseTeam = true;
            }
        }

        userPickedTeam = userPickTeamRound2(scan, bracket);
        Team secondRoundWinner = bracket.secondRoundOfSemiFinals();

        if (userPickedTeam == secondRoundWinner) {
            System.out.println("You did it!");
        } else {
            System.out.println("You suck.");
        }

        return secondRoundWinner;

    }

    public static void finalsStuff(Scanner scan, Bracket bracket) {
        boolean didChooseTeam = false;
        Team userPickedTeam;
        Team viewPickedTeam;
        String userResponse;
        String userResponse2;
        while (!didChooseTeam) {
            userResponse = mainUserValidation(scan);
            if (userResponse.equalsIgnoreCase("1")) {
                viewPickedTeam = viewTeamInfoValidationFinals(scan, bracket);
                userResponse2 = viewTeamStatsOrPlayerStatsValidation(scan, viewPickedTeam);
                if (userResponse2.equalsIgnoreCase("1")) {
                    viewSpecificPlayerStats(scan, viewPickedTeam);
                } else if (userResponse2.equalsIgnoreCase("2")) {
                    viewPickedTeam.checkTeamWins();
                } else {
                    viewPickedTeam.checkTeamLosses();
                }

            } else {
                didChooseTeam = true;
            }
        }

        userPickedTeam = userPickTeamFinals(scan, bracket);

        Team finalsWinner = bracket.finals();

        if (userPickedTeam == finalsWinner) {
            System.out.println("You did it!");
        } else {
            System.out.println("You suck.");
        }


    }




    // USER PLAY AGAIN STUFF
    public static boolean userPlayAgain(Scanner scan) {
        System.out.println("Do you want to play again? [Y] or [N] > ");
        String userResponse = scan.nextLine();
        while (!userResponse.equalsIgnoreCase("Y") && !userResponse.equalsIgnoreCase("N")) {
            System.out.println("WRONG RESPONSE BRO");
            System.out.println("Do you want to play again? [Y] or [N] > ");
            userResponse = scan.nextLine();
        }

        if (userResponse.equalsIgnoreCase("Y")) {
            return true;
        } else {
            return false;
        }
    }


}