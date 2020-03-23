import java.util.ArrayList;

public class BirbLog {
    private ArrayList<String> deaths;
    private ArrayList<String> births;

    // Death Codes
    private static final int HUNGER_DEATH      = 0;
    private static final int EATEN_DEATH       = 1;
    private static final int CANNIBAL_DEATH    = 2;
    private static final int HYPOTHERMIA_DEATH = 3;
    private static final int HEATSTROKE_DEATH  = 4;
    private static final int DROWNING_DEATH    = 5;

    public BirbLog() {
        deaths = new ArrayList<>();
        births = new ArrayList<>();
    }

    public ArrayList<String> getDeaths() {
        return deaths;
    }

    public ArrayList<String> getBirths() {
        return births;
    }

    public void addDeath(String name, int deathCode, int aliveTime) {
        String logMessage;
        String deathMessage;

        switch (deathCode) {
            case (HUNGER_DEATH):
                deathMessage = " Has Died of Hunger ";
                break;
            case (EATEN_DEATH):
                deathMessage = " Has Been Eaten by a Predator ";
                break;
            case (CANNIBAL_DEATH):
                deathMessage = " Has Been Eaten by a Cannibal Birb ";
                break;
            case (HYPOTHERMIA_DEATH):
                deathMessage = " Has Died of the Cold ";
                break;
            case (HEATSTROKE_DEATH):
                deathMessage = " Has Died of Heatstroke ";
                break;
            case (DROWNING_DEATH):
                deathMessage = " Has Drown ";
                break;
            default:
                deathMessage = "AHHHHHHHHH";
                break;
        }

        logMessage = "Birb " + name + ", "  + deathMessage + "after " + aliveTime + " Generations.";
        deaths.add(logMessage);
    }

    public void addBirth(String parent1Name, String parent2Name, String newbornName) {
        String logMessage;

        logMessage = "Birb " + parent1Name + " and " + parent2Name + " had Child Birb " + newbornName + ".";
        births.add(logMessage);
    }

    public void clearLogs() {
        deaths = new ArrayList<>();
        births = new ArrayList<>();
    }

}
