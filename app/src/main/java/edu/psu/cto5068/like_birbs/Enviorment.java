package edu.psu.cto5068.like_birbs;

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
     *  5 - Pack of Alien Predators - increase detection, increase speed of predators
     *  6 - Heavy Rains - swimming rng increased difficulty in places w/ water, add swimming to non water envs
     *  7 - Radioactive Fallout - increase mutation rate (will need to change birb object)
     *  8 - Pandemic - "Birb Flu" Random birbs die
     *  9 - Birby Boomers - 2x reproduction
     *  10 - Crashed Ship - Can spawn ...
     *              - Bonus Food (of 1 type)
     *              - Alien Predators
     *              - Birby Boomers
     *              - Pandemic
     *              - Fallout
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
                hasWater         = true;
                temperature      = 25;
                generationNum    = 0;

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

        for (Birb birb: birbs) {
            if (birb.isCannibalistic() && !birb.isHasMeatFood()){
                int victimIndx;
                boolean done = false;
                do {
                    victimIndx = (int) (Math.random() * birbs.size());
                    if (birb != birbs.get(victimIndx)) {
                        done = true;
                        int differentSpeed = birb.getSpeedDecimal() - birbs.get(victimIndx).getSpeedDecimal();
                        if (differentSpeed > 0) {
                            int differentStrength = birb.getStrengthDecimal() - birbs.get(victimIndx).getStrengthDecimal();
                            if (differentStrength > 0) {
                                birb.setHasMeatFood(true);
                                log.addDeath(birbs.get(victimIndx).getName(), BirbLog.CANNIBAL_DEATH, birbs.get(victimIndx).getGenerationsAlive());
                                birbs.remove(birbs.get(victimIndx));
                            }
                        }
                    }
                }while (!done);
            }
        }
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
                log.addDeath(birb.getName(), BirbLog.HUNGER_DEATH, birb.getGenerationsAlive());
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
            int[] predatorDetection;
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
                    predatorDetection = new int[]{50, 100, 200, 500};
                    break;
                case (2):
                    idealColor = 32736;
                    predatorDetection = new int[]{500, 1000, 2000, 5000};
                    break;
                case (3):
                    idealColor = 32767;
                    predatorDetection = new int[]{500, 1000, 2000, 5000};
                    break;
                case (4):
                    idealColor = 31;
                    predatorDetection = new int[]{25, 50, 100, 200};
                    break;
                default:
                    // error
                    idealColor = 0;
                    predatorDetection = new int[]{0, 0, 0, 0};
                    break;
            }

            if (birb.isNocturnal()) {
                idealColor = 0;
                predatorDetection = new int[]{25, 50, 100, 200};
            }

            boolean detected = true;
            if (succRange < 50) {
                if (idealColor - predatorDetection[0] < birb.getColorDecimal() &&
                    birb.getColorDecimal() < idealColor + predatorDetection[0]) {
                    detected = false;
                }
            }
            else if (50 < succRange && succRange < 79) {
                if (idealColor - predatorDetection[1] < birb.getColorDecimal() &&
                    birb.getColorDecimal() < idealColor + predatorDetection[1]) {
                    detected = false;
                }
            }
            else if (80 < succRange && succRange < 95) {
                if (idealColor - predatorDetection[2] < birb.getColorDecimal() &&
                        birb.getColorDecimal() < idealColor + predatorDetection[2]) {
                    detected = false;
                }
            }
            else {
                if (idealColor - predatorDetection[3] < birb.getColorDecimal() &&
                        birb.getColorDecimal() < idealColor + predatorDetection[3]) {
                    detected = false;
                }
            }

            succRange = (int) (Math.random() * 100);
            if (detected) {
                if (succRange < 50) {
                    if (!(predatorSpeed - 5000 < birb.getSpeedDecimal() &&
                        birb.getSpeedDecimal() < predatorSpeed + 5000)) {
                        log.addDeath(birb.getName(), BirbLog.EATEN_DEATH, birb.getGenerationsAlive());
                        birbs.remove(birb);
                    }
                }
                else if (succRange < 79) {
                    if (!(predatorSpeed - 8000 < birb.getSpeedDecimal() &&
                            birb.getSpeedDecimal() < predatorSpeed + 8000)) {
                        log.addDeath(birb.getName(), BirbLog.EATEN_DEATH, birb.getGenerationsAlive());
                        birbs.remove(birb);
                    }
                }
                else if (succRange < 95) {
                    if (!(predatorSpeed - 10000 < birb.getSpeedDecimal() &&
                            birb.getSpeedDecimal() < predatorSpeed + 10000)) {
                        log.addDeath(birb.getName(), BirbLog.EATEN_DEATH, birb.getGenerationsAlive());
                        birbs.remove(birb);
                    }
                }
                else {
                    if (!(predatorSpeed - 15000 < birb.getSpeedDecimal() &&
                            birb.getSpeedDecimal() < predatorSpeed + 15000)) {
                        log.addDeath(birb.getName(), BirbLog.EATEN_DEATH, birb.getGenerationsAlive());
                        birbs.remove(birb);
                    }
                }
            }
        }
    }

    public void birbsTemperature() {
        // target feathers number is 65536 - [temperature * 1,000]
        int targetDecFeathers = 65_536 - temperature * 1000;
        // Survival range
        int succRange;
        for (Birb birb: birbs) {
            succRange = (int) (Math.random() * 100);

            if (succRange < 50) {
                if (!(targetDecFeathers - 5000 < birb.getFeathersDecimal() &&
                        birb.getFeathersDecimal() < targetDecFeathers + 5000)) {
                    if (birb.getFeathersDecimal() > targetDecFeathers + 5000) {
                        log.addDeath(birb.getName(), BirbLog.HEATSTROKE_DEATH, birb.getGenerationsAlive());
                    } else {
                        log.addDeath(birb.getName(), BirbLog.HYPOTHERMIA_DEATH, birb.getGenerationsAlive());
                    }
                    birbs.remove(birb);
                }
            }
            if (succRange < 79) {
                if (!(targetDecFeathers - 8000 < birb.getFeathersDecimal() &&
                        birb.getFeathersDecimal() < targetDecFeathers + 8000)) {
                    if (birb.getFeathersDecimal() > targetDecFeathers + 8000) {
                        log.addDeath(birb.getName(), BirbLog.HEATSTROKE_DEATH, birb.getGenerationsAlive());
                    } else {
                        log.addDeath(birb.getName(), BirbLog.HYPOTHERMIA_DEATH, birb.getGenerationsAlive());
                    }
                    birbs.remove(birb);
                }
            }
            if (succRange < 95) {
                if (!(targetDecFeathers - 10000 < birb.getFeathersDecimal() &&
                        birb.getFeathersDecimal() < targetDecFeathers + 10000)) {
                    if (birb.getFeathersDecimal() > targetDecFeathers + 10000) {
                        log.addDeath(birb.getName(), BirbLog.HEATSTROKE_DEATH, birb.getGenerationsAlive());
                    } else {
                        log.addDeath(birb.getName(), BirbLog.HYPOTHERMIA_DEATH, birb.getGenerationsAlive());
                    }
                    birbs.remove(birb);
                }
            }
            else {
                if (!(targetDecFeathers - 15000 < birb.getFeathersDecimal() &&
                        birb.getFeathersDecimal() < targetDecFeathers + 15000)) {
                    if (birb.getFeathersDecimal() > targetDecFeathers + 15000) {
                        log.addDeath(birb.getName(), BirbLog.HEATSTROKE_DEATH, birb.getGenerationsAlive());
                    }
                    else {
                        log.addDeath(birb.getName(), BirbLog.HYPOTHERMIA_DEATH, birb.getGenerationsAlive());
                    }
                    birbs.remove(birb);
                }
            }
        }
    }

    public void birbsDrown() {
        // How drowning will work is if the place has water... RNG rolls between 0 - 60000
        // and using the range key at top of this class using the birbs swimming ability birbs die

        if (hasWater) {
            int succRange = (int) (Math.random() * 100);
            int waterLevels = (int) (Math.random() * 60000);

            for (Birb birb : birbs) {
                if (succRange < 50) {
                    if (!(waterLevels - 5000 < birb.getSwimmingDecimal() &&
                            birb.getSwimmingDecimal() < waterLevels + 5000)) {
                        log.addDeath(birb.getName(), BirbLog.DROWNING_DEATH, birb.getGenerationsAlive());
                        birbs.remove(birb);
                    }
                }
                else if (succRange < 79) {
                    if (!(waterLevels - 8000 < birb.getSwimmingDecimal() &&
                            birb.getSwimmingDecimal() < waterLevels + 8000)) {
                        log.addDeath(birb.getName(), BirbLog.DROWNING_DEATH, birb.getGenerationsAlive());
                        birbs.remove(birb);
                    }
                }
                else if (succRange < 95) {
                    if (!(waterLevels - 10000 < birb.getSwimmingDecimal() &&
                            birb.getSwimmingDecimal() < waterLevels + 10000)) {
                        log.addDeath(birb.getName(), BirbLog.DROWNING_DEATH, birb.getGenerationsAlive());
                        birbs.remove(birb);
                    }
                }
                else {
                    if (!(waterLevels - 15000 < birb.getSwimmingDecimal() &&
                            birb.getSwimmingDecimal() < waterLevels + 15000)) {
                        log.addDeath(birb.getName(), BirbLog.DROWNING_DEATH, birb.getGenerationsAlive());
                        birbs.remove(birb);
                    }
                }
            }
        }
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
        if (0 == --(this.randomEventDurationLeft)) {
            this.currentRandomEventType = 0;
        }
    }

    public BirbLog getLogs() {
        return log;
    }

    public void clearLogs() {
        log.clearLogs();
    }

    public int randomEvent() {
        if (currentRandomEventType == 0) {
            int newRandom = (int) (Math.random() * 4);
            if (newRandom == 0) {
                newRandom = (int) (Math.random() * 11);
                return newRandom; // tell driver to set new random event
            }
            else {
                return 0; // tell driver to do nothing, no new event
            }
        }
        else if (currentRandomEventType == 8) { //  Birb Flu
            int decemator; // 1/10 chance of dying of birb flu
            for (Birb birb: birbs) {
                decemator = (int) (Math.random() * 10);
                if (decemator == 0) {
                    log.addDeath(birb.getName(), BirbLog.PANDEMIC_DEATH, birb.getGenerationsAlive());
                    birbs.remove(birb);
                }
            }
            return -1; // tell driver to decrease random event timer
        }
        else if (currentRandomEventType == 9) { // birby boomers
            this.birbsReproduce();
            return -1;
        }

        return -1;
    }
}
