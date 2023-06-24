import javax.xml.transform.Result;
import java.sql.*;

public class Player {
    String url = "jdbc:postgresql://localhost:5432/game?user=postgres&password=12345";
    String firstName;
    String lastName;
    Double fieldGoalPercentage;
    Double threePointPercentage;
    Double freeThrowPercentage;
    Double rebounds;
    Double assists;
    Double blocks;
    Double steals;


    public Player(String firstName, String lastName, Double FGP, Double TPP, Double FTP, Double rebounds, Double assists, Double steals, Double blocks) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fieldGoalPercentage = FGP;
        this.freeThrowPercentage = FTP;
        this.threePointPercentage = TPP;
        this.rebounds = rebounds;
        this.assists = assists;
        this.blocks = blocks;
        this.steals = steals;
    }

    // 2 query

    public void viewPlayerStats() {
        try(Connection connection = DriverManager.getConnection(url)) {
            String sql = "SELECT * from players WHERE first_name = ? AND last_name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, this.firstName.toLowerCase());
            statement.setString(2, this.lastName.toLowerCase());

            ResultSet results = statement.executeQuery();
            if (results.next()) {
                System.out.println(this.firstName + " " + this.lastName);
                System.out.println("Field Goal Percentage: "  + results.getDouble("fgp"));
                System.out.println("Free Throw Percentage: "  + results.getDouble("ftp"));
                System.out.println("Three Point Percentage: "  + results.getDouble("tpp"));
                System.out.println("Blocks Average Per Game: "  + results.getDouble("blocks"));
                System.out.println("Steals Average Per Game: "  + results.getDouble("steals"));
                System.out.println("Rebounds Average Per Game: "  + results.getDouble("rb"));
                System.out.println("Assists Average Per Game: "  + results.getDouble("assists"));

            }

        } catch (SQLException error) {
            System.out.println(error.getMessage());
        }
    }
}
