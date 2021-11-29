package FitBot;
import net.dv8tion.jda.api.*;


public class DatabaseUtility {
    public static Database x = new Database("jdbc:postgresql://127.0.0.1:5432/dbot_dbs", "postgres", "fuck");
    
    public static boolean startCycle(String endDate) {
        String query = String.format("insert into fitness_db.workout cycle values ('today', '%s')", endDate);
        return x.sql_update(query);
    }

    public static void setUserAimForCurrentCycle(String aim, String userID) {
        if (!checkUserExists(userID)) addUser(userID);
        //x.sql_getString(query, column)
    }

    public static void setUserReflectionForCurrentCycle(String reflection, String userID) {

    }

    public static boolean checkUserExists(String userID) {
        return true;
    }

    public static void addUser(String userID) {

    }

    public static boolean currentlyInCycle() {
        return false;
    }

    public static void addWorkout(String userID, String evidenceDescription) {
        if (!checkUserExists(userID)) addUser(userID);
    }
}
