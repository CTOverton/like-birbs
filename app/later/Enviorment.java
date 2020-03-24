import java.util.ArrayList;
/*
     HOW RNG ROLLS WORK FOR ENVIORMENT - Jacob's RNG Magic 2.0
        Birb Range will be from 0 -> 65,536 (because 16 bit integer)
        Roll the possible range 0 - 99
            [0 -> 49] 50% - Standard range Where X is the ideal
                  (X - 5,000) < X < (X + 5000)
            [50 -> 79] 30% - Super Range Where X is ideal
                  (X - 8,000) < X < (X + 8000)
            [80 -> 95] 15% - Mega Range Where X is ideal
                  (X - 10,000) < X < (X + 10,000)
            [95 -> 99] 5% - Super Ultra Mega Giga Kayak Range Where X is ideal
                  (x - 15,000) < X < (X + 15,000)

      HOW RNG ROLLS WILL WORK FOR FOOD RELATED BIRB ON BIRB VIOLENCE
        Birb-AggressorStat - Birb-VictimState = X

        if X <= 0 Aggressor Fails
        if X > 0 Aggressor Rolls
        Roll the possible range 0 - 99
            [0 - 49] 50% - Standard
                if X > 10,000 Success for Aggressor
            [50 - 79] 30% - Super
                if X > 8,000 Success for aggressor
            [80 - 95] 15% - Mega
                if X > 5000 Success for aggressor
            [95 - 99] 5% - Super Ultra Mega Giga Kayak
                if X > 1000 Success for Aggressor
 */
public class Enviorment {
    // Birb Population
    private ArrayList<Birb> birbs;
    // Death and Birth log
    private BirbLog log;
    // Environment information
    private int predatorTypes;
    private int landType;
    private int vegFoodAmount;
    private int meatFoodAmount;
    private boolean hasWater;
    private int temperature;
    // Generations Passed Counter
    private int generationNum;
    // Random Event Type
    private int currentRandomEventType;
    private int randomEventDurationLeft;
    /*
     *  RANDOM EVENT TYPE KEYS
     *  0 - None
     *  1 - Bonus Food Veg
     *  2 - Bonus Food Meat
     *  3 - Increased Temperature
     *  4 - Decreased Temperature
     *  5 - Pack of Alien Preditors
     *  6 - Heavy Rains
     *  7 -
     */

    public Enviorment(ArrayList<Birb> initPopulation, int envType) {
        birbs = new ArrayList<>(initPopulation);
        log = new BirbLog();

        /*
         *   Enviorment type key :
         *         1. Standard
         *         2. Desert
         *         3. Snow
         *         4. Island
         */
        switch (envType) {
            case(1):
                predatorTypes    = 1;
                landType         = 1;
                vegFoodAmount    = 20;
                meatFoodAmount   = 20;
                hasWater         = false;
                temperature      = 20;
                generationNum    = 0;
                break;
            case(2):
                predatorTypes    = 2;
                landType         = 2;
                vegFoodAmount    = 5;
                meatFoodAmount   = 10;
                hasWater         = false;
                temperature      = 30;
                generationNum    = 0;
                break;
            case (3):
                predatorTypes    = 2;
                landType         = 3;
                vegFoodAmount    = 5;
                meatFoodAmount   = 20;
                hasWater         = false;
                temperature      = 5;
                generationNum    = 0;
            case (4):
                predatorTypes    = 3;
                landType         = 4;
                vegFoodAmount    = 30;
                meatFoodAmount   = 5;

        }
    }

    public void birbsEat() {
        // Vegetarian Birbs
        int fastestIndx = 0;
        int fastestSpeed = -1;
        int counter = 0;
        int tempVegAmount = this.vegFoodAmount;
        int tempMeatAmount = this.meatFoodAmount;
        boolean allEaten;
        do {
            allEaten = true;
            for (Birb birb : birbs) {
                if (birb.getSpeedDecimal() > fastestSpeed && !birb.isHasPlantFood() && !birb.isCarniverous()) {
                    allEaten = false;
                    fastestSpeed = birb.getSpeedDecimal();
                    fastestIndx = counter;
                }
                counter++;
            }

            if (!allEaten) {
                birbs.get(fastestIndx).setHasPlantFood(true);
                tempVegAmount--;
                fastestSpeed = -1;
            }
        } while (!allEaten && tempVegAmount != 0);

        // Carniverous Birbs
        do {
            allEaten = true;
            for (Birb birb : birbs) {
                if (birb.getSpeedDecimal() > fastestSpeed && !birb.isHasMeatFood() && birb.isCarniverous()) {
                    allEaten = false;
                    fastestSpeed = birb.getSpeedDecimal();
                    fastestIndx = counter;
                }
                counter++;
            }

            if (!allEaten) {
                birbs.get(fastestIndx).setHasMeatFood(true);
                tempMeatAmount--;
                fastestSpeed = -1;
            }
        } while (!allEaten && tempVegAmount != 0);

        // Cannibal Birbs
        // TODO: Work out RNG Rolls
    }

    public void strongBirbsTakeFood() {
        int succRange = 0;
        int birbStrengthDiff = 0;
        for (Birb aggressor : birbs) {
            for (Birb victim : birbs) {
                if (aggressor != victim) {
                    if ((victim.isHasPlantFood() && !aggressor.isCarniverous() && !aggressor.isHasPlantFood()) ||
                        (victim.isHasMeatFood() && aggressor.isCarniverous() && !aggressor.isHasMeatFood())) {
                        succRange = (int) (Math.random() * 100);
                        birbStrengthDiff = aggressor.getStrengthDecimal() - victim.getStrengthDecimal();
                        if (!(birbStrengthDiff < 1000)) {
                            if (succRange <= 49 && birbStrengthDiff >= 10_000) {
                                if (aggressor.isCarniverous()) {
                                    victim.setHasMeatFood(false);
                                    aggressor.setHasMeatFood(true);
                                }
                                else {
                                    victim.setHasPlantFood(false);
                                    aggressor.setHasPlantFood(true);
                                }
                                break;
                            }
                            else if (50 <= succRange && succRange <= 79 && birbStrengthDiff >= 8_000) {
                                if (aggressor.isCarniverous()) {
                                    victim.setHasMeatFood(false);
                                    aggressor.setHasMeatFood(true);
                                }
                                else {
                                    victim.setHasPlantFood(false);
                                    aggressor.setHasPlantFood(true);
                                }
                                break;
                            }
                            else if (80 <= succRange && succRange <= 94 && birbStrengthDiff >= 5_000) {
                                if (aggressor.isCarniverous()) {
                                    victim.setHasMeatFood(false);
                                    aggressor.setHasMeatFood(true);
                                }
                                else {
                                    victim.setHasPlantFood(false);
                                    aggressor.setHasPlantFood(true);
                                }
                                break;
                            }
                            else { // Super Mega Ultra Giga Kayak
                                if (aggressor.isCarniverous()) {
                                    victim.setHasMeatFood(false);
                                    aggressor.setHasMeatFood(true);
                                }
                                else {
                                    victim.setHasPlantFood(false);
                                    aggressor.setHasPlantFood(true);
                                }
                                break;
                            }

                        }
                    }
                }
            }
        }
    }

    public void birbsStarve() {
        for (Birb birb : birbs) {
            if (!birb.isHasMeatFood() && !birb.isHasPlantFood()) {
                log.addDeath(birb.getName(), 0, birb.getGenerationsAlive());
                birbs.remove(birb);
            }
        }
    }
    /*
     * Predator Detection range is based on enviorment
     *      Meadow: GREEN (ie. 00000 11111 00000) or 992
     *      Snow:  WHITE (ie. 11111 11111 11111) or 32767
     *      Island: BLUE (ie. 00000 00000 11111) or 31
     *      Desert: YELLOW (ie. 11111 11111 00000) or 32736
     *
     *      Nocturnal: BLACK (ie. 00000 00000 00000) or 0
     */
    public void predatorsEat() {
        // TODO:
        for (Birb birb : birbs) {
            int succRange = (int) (Math.random() * 100);
            int idealColor;
            int[] predatorDetection = new int[4];
            int predatorSpeed;

            switch (predatorTypes) {
                case (1):
                    predatorSpeed = 10000;
                    break;
                case (2):
                    predatorSpeed = 20000;
                    break;
                case (3):
                    predatorSpeed = 30000;
                    break;
                default:
                    // error
                    predatorSpeed = 0;
                    break;
            }

            switch (landType) {
                case (1):
                    idealColor = 992;
                    predatorDetection = {50, 100, 200, 500};
                    break;
                case (2):
                    idealColor = 32736;
                    predatorDetection = {500, 1000, 2000, 5000};
                    break;
                case (3):
                    idealColor = 32767;
                    predatorDetection = {500, 1000, 2000, 5000};
                    break;
                case (4):
                    idealColor = 31;
                    predatorDetection = {25, 50, 100, 200};
                    break;
                default:
                    // error
                    idealColor = 0;
                    predatorDetection = {0, 0, 0, 0};
                    break;
            }

            if (birb.isNocturnal()) {
                idealColor = 0;
                predatorDetection = {25, 50, 100, 200}
            }
        }
    }

    public void birbsTemperature() {
        // TODO: Work out RNG Rolls
    }

    public void birbsDrown() {
        // TODO: Work out RNG Rolls
    }

    public boolean enoughToReproduce() {
        return (birbs.size() > 1);
    }

    public void birbsReproduce() {
        int initBirbSize = birbs.size();
        for (int i = 0; i < initBirbSize - 1; i++) {
            birbs.add(new Birb(birbs.get(i), birbs.get(i+1)));
        }
    }

    public boolean allDead() {
        return (birbs.size() == 0);
    }

    public void birbsShuffle() {
        Birb tempBirb;
        int swapIdx1;
        int swapIdx2;

        for (int i = 0; i < birbs.size(); i++) {

            do {
                swapIdx1 = (int) (Math.random() * birbs.size());
                swapIdx2 = (int) (Math.random() * birbs.size());
            } while (swapIdx1 == swapIdx2);

            tempBirb = birbs.get(swapIdx1);
            birbs.set(swapIdx1, birbs.get(swapIdx2));
            birbs.set(swapIdx2, tempBirb);
        }
    }

    public void increaseAliveTime() {
        for (Birb birb : birbs) {
            birb.incrementGenerationAlive();
        }
    }

    public void /*Might need to be not void */ displayStats() {

    }

    public void setRandomEvent(int type, int duration) {
        this.currentRandomEventType = type;
        this.randomEventDurationLeft = duration;
    }

    public void decrementRandomEventDuration() {
        if (0 == --(this.randomEventDurationLeft) {
            this.currentRandomEventType = 0;
        }
    }

    public BirbLog getLogs() {
        return log;
    }

    public void clearLogs() {
        log.clearLogs();
    }


}
