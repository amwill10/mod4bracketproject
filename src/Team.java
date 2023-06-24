import java.sql.*;
import java.util.*;
public class Team {

    public String url = "jdbc:postgresql://localhost:5432/game?user=postgres&password=12345";

    int id;
    String name;
    Double fieldGoalPercentage;
    Double threePointPercentage;
    Double freeThrowPercentage;
    Double blocks;
    Double steals;
    Double assists;
    Double rebounds;


    ArrayList<Player> playersOnTeam = new ArrayList<>();

    Double totalPointsDuringMatch;

    Double defensivePoints;
    boolean didWin;

    public Team(int id, String teamName) {
        this.id = id;
        this.name = teamName;
        this.totalPointsDuringMatch = 0.0;
        this.didWin = false;
    }

    // join query
    public void addPlayers() {
        try (Connection connection = DriverManager.getConnection(url)) {
            String sql = "SELECT * FROM teams INNER JOIN players ON teams.player1_id = players.id WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, this.name.toLowerCase());
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                this.playersOnTeam.add(new Player(results.getString("first_name"), results.getString("last_name"), results.getDouble("fgp"), results.getDouble("ftp"), results.getDouble("tpp"), results.getDouble("rb"), results.getDouble("assists"), results.getDouble("blocks"), results.getDouble("steals")));
            }

            String sql2 = "SELECT * FROM teams INNER JOIN players ON teams.player2_id = players.id WHERE name = ?";
            PreparedStatement statement2 = connection.prepareStatement(sql2);
            statement2.setString(1, this.name.toLowerCase());
            ResultSet results2 = statement2.executeQuery();
            if (results2.next()) {
                this.playersOnTeam.add(new Player(results2.getString("first_name"), results2.getString("last_name"), results2.getDouble("fgp"), results2.getDouble("ftp"), results2.getDouble("tpp"), results2.getDouble("rb"), results2.getDouble("assists"), results2.getDouble("blocks"), results2.getDouble("steals")));
            }

            String sql3 = "SELECT * FROM teams INNER JOIN players ON teams.player3_id = players.id WHERE name = ?";
            PreparedStatement statement3 = connection.prepareStatement(sql3);
            statement3.setString(1, this.name.toLowerCase());
            ResultSet results3 = statement3.executeQuery();
            if (results3.next()) {
                this.playersOnTeam.add(new Player(results3.getString("first_name"), results3.getString("last_name"), results3.getDouble("fgp"), results3.getDouble("ftp"), results3.getDouble("tpp"), results3.getDouble("rb"), results3.getDouble("assists"), results3.getDouble("blocks"), results3.getDouble("steals")));
            }


            String sql4 = "SELECT * FROM teams INNER JOIN players ON teams.player4_id = players.id WHERE name = ?";
            PreparedStatement statement4 = connection.prepareStatement(sql4);
            statement4.setString(1, this.name.toLowerCase());
            ResultSet results4 = statement4.executeQuery();
            if (results4.next()) {

                this.playersOnTeam.add(new Player(results4.getString("first_name"), results4.getString("last_name"), results4.getDouble("fgp"), results4.getDouble("ftp"), results4.getDouble("tpp"), results4.getDouble("rb"), results4.getDouble("assists"), results4.getDouble("blocks"), results4.getDouble("steals")));
            }

            String sql5 = "SELECT * FROM teams INNER JOIN players ON teams.player5_id = players.id WHERE name = ?";
            PreparedStatement statement5 = connection.prepareStatement(sql5);
            statement5.setString(1, this.name.toLowerCase());
            ResultSet results5 = statement5.executeQuery();
            if (results5.next()) {
                this.playersOnTeam.add(new Player(results5.getString("first_name"), results5.getString("last_name"), results5.getDouble("fgp"), results5.getDouble("ftp"), results5.getDouble("tpp"), results5.getDouble("rb"), results5.getDouble("assists"), results5.getDouble("blocks"), results5.getDouble("steals")));
            }


        } catch (SQLException error) {
            System.out.println(error.getMessage());
        }

        Double FGP = 0.0;
        Double TPP = 0.0;
        Double FTP = 0.0;
        Double block = 0.0;
        Double steal = 0.0;
        Double assist = 0.0;
        Double rebound = 0.0;

        for (int i = 0; i < this.playersOnTeam.size(); i++) {
            FGP += this.playersOnTeam.get(i).fieldGoalPercentage;
            TPP += this.playersOnTeam.get(i).threePointPercentage;
            FTP += this.playersOnTeam.get(i).freeThrowPercentage;
            block += this.playersOnTeam.get(i).blocks;
            steal += this.playersOnTeam.get(i).steals;
            assist += this.playersOnTeam.get(i).assists;
            rebound += this.playersOnTeam.get(i).rebounds;
        }

        this.fieldGoalPercentage = FGP / 5;
        this.threePointPercentage = TPP / 5;
        this.freeThrowPercentage = FTP / 5;
        this.blocks = block / 5;
        this.steals = steal / 5;
        this.assists = assist / 5;
        this.rebounds = rebound / 5;
    }

    public void teamScore(Double otherTeamDefensiveScore) {
        for (int iTPP = 0; iTPP < 4; iTPP++){
            if (randomInt() <= (this.threePointPercentage)) {
                this.totalPointsDuringMatch += 3;
            }
        }

        for (int iFGP = 0; iFGP < 4; iFGP++) {
            if (randomInt() <= (this.fieldGoalPercentage)) {
                this.totalPointsDuringMatch += 2;
            }
        }

        for (int iFTP = 0; iFTP < 4; iFTP++) {
            if (randomInt() <= (this.freeThrowPercentage)) {
                this.totalPointsDuringMatch += 1;
            }
        }

        for (int iAssists = 0; iAssists < (this.assists / (randomInt() / 25)); iAssists++) {
            this.totalPointsDuringMatch += 2;
        }

        for (int iRebounds = 0; iRebounds < (this.rebounds / (randomInt() / 25)); iRebounds++) {
            this.totalPointsDuringMatch += 2;
        }

        this.totalPointsDuringMatch -= otherTeamDefensiveScore;
        if (this.totalPointsDuringMatch < 0) {
            this.totalPointsDuringMatch = 0.0;
        }
    }

    public void defensivePointsScore() {
        this.defensivePoints = 0.0;
        for (int iBlocks = 0; iBlocks < 4; iBlocks++) {
            if ((randomInt() / 5) < this.blocks) {
                this.defensivePoints += 2;
            }
        }

        for (int iSteals = 0; iSteals < 4; iSteals++) {
            if ((randomInt() / 5) < this.steals) {
                this.defensivePoints += 3;
            }
        }


    }

    public int randomInt() {
        int result = (int)(Math.random() * (100 - 1 + 1)) + 1;
        return result;
    }

    public void resetTeamScore() {

        this.totalPointsDuringMatch = 0.0;
        this.defensivePoints = 0.0;
    }

    public void listPlayers(){
        for (int i = 0; i < this.playersOnTeam.size(); i++) {
            System.out.println(this.playersOnTeam.get(i).firstName.toUpperCase() + " " + this.playersOnTeam.get(i).lastName.toUpperCase());
        }
    }

    // query 4
    public void checkTeamWins() {
        try(Connection connection = DriverManager.getConnection(url)) {
            String sql = "SELECT COUNT(*) FROM results WHERE first = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, this.id);

            ResultSet results = statement.executeQuery();
            if (results.next()) {
                System.out.println(this.name + " has won " + results.getInt("count") + " times.");
            }

        } catch (SQLException error) {
            System.out.println(error.getMessage());
        }

    }

    // query 3
    public void checkTeamLosses() {
        try(Connection connection = DriverManager.getConnection(url)) {
            String sql = "SELECT COUNT(*) FROM results WHERE first != ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, this.id);

            ResultSet results = statement.executeQuery();
            if (results.next()) {
                System.out.println(this.name + " has lost " + results.getInt("count") + " times.");
            }

        } catch (SQLException error) {
            System.out.println(error.getMessage());
        }

    }

}


