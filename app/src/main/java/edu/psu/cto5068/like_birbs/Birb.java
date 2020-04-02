package edu.psu.cto5068.like_birbs;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName="birbs")
public class Birb {
    // DNA Arrays

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "strBin")
    private int[] strength;

    @ColumnInfo(name = "speedBin")
    private int[] speed;

    @ColumnInfo(name = "feathersBin")
    private int[] feathers;

    @ColumnInfo(name = "colBin")
    private int[] color;

    @ColumnInfo(name = "swimBin")
    private int[] swimming;

    // Decimal representation of Binary Array
    @ColumnInfo(name = "strDec")
    private int strengthDecimal;

    @ColumnInfo(name = "speedDec")
    private int speedDecimal;

    @ColumnInfo(name = "feathersDec")
    private int feathersDecimal;

    @ColumnInfo(name = "colDec")
    private int colorDecimal;

    @ColumnInfo(name = "swimDec")
    private int swimmingDecimal;



    // Traits
    @ColumnInfo(name = "nocturnal")
    private boolean nocturnal;
    @ColumnInfo(name = "carniverous")
    private boolean carniverous;
    @ColumnInfo(name = "cannibalistic")
    private boolean cannibalistic;

    // Food Status
    @ColumnInfo(name = "meatFood")
    private boolean hasMeatFood;
    @ColumnInfo(name = "plantFood")
    private boolean hasPlantFood;

    // Birb name
    @ColumnInfo(name = "name")
    private String name;

    // Generation alive count
    @ColumnInfo(name = "gens")
    private int generationsAlive;

    public Birb() {
        // I don't think we will use this
    }

    // Customize Birb Constructor (only use on game begin)
    public Birb(int id, int strength, int speed, int feathers, int color, int swimming, boolean[] traits, String name) {
        this.id = id;
        this.strengthDecimal = strength;
        this.speedDecimal = speed;
        this.feathersDecimal = feathers;
        this.colorDecimal = color;
        this.swimmingDecimal = swimming;

        this.generationsAlive = 0;
        this.name = name;

        // traits[0] = nocturnal, traits[1] = carnivorous
        if (traits[0]) {
            nocturnal = true;
            carniverous = false;
            cannibalistic = false;
        }
        else if (traits[1]) {
            nocturnal = false;
            carniverous = true;
            cannibalistic = false;
        }
        else {
            nocturnal = false;
            carniverous = false;
            cannibalistic = false;
        }

        // Decimal to Binary conversion for DNA
        String tempBinString = Integer.toBinaryString(strength);
        int tempBinStringLength = tempBinString.length();
        this.strength = new int[16];
        for (int i = 0; i < 16; i++) {
            if (i > tempBinStringLength) {
                this.strength[i] = 0;
            }
            else {
                this.strength[i] = tempBinString.charAt(tempBinStringLength - 1 - i);
            }
        }

        tempBinString = Integer.toBinaryString(speed);
        tempBinStringLength = tempBinString.length();
        this.speed = new int[16];
        for (int i = 0; i < 16; i++) {
            if (i > tempBinStringLength) {
                this.speed[i] = 0;
            }
            else {
                this.speed[i] = tempBinString.charAt(tempBinStringLength - 1 - i);
            }
        }

        tempBinString = Integer.toBinaryString(feathers);
        tempBinStringLength = tempBinString.length();
        this.feathers = new int[16];
        for (int i = 0; i < 16; i++) {
            if (i > tempBinStringLength) {
                this.feathers[i] = 0;
            }
            else {
                this.feathers[i] = tempBinString.charAt(tempBinStringLength - 1 - i);
            }
        }

        tempBinString = Integer.toBinaryString(color);
        tempBinStringLength = tempBinString.length();
        this.color = new int[15];
        for (int i = 0; i < 15; i++) {
            if (i > tempBinStringLength) {
                this.color[i] = 0;
            }
            else {
                this.color[i] = tempBinString.charAt(tempBinStringLength - 1 - i);
            }
        }

        tempBinString = Integer.toBinaryString(swimming);
        tempBinStringLength = tempBinString.length();
        this.swimming = new int[16];
        for (int i = 0; i < 16; i++) {
            if (i > tempBinStringLength) {
                this.swimming[i] = 0;
            }
            else {
                this.swimming[i] = tempBinString.charAt(tempBinStringLength - 1 - i);
            }
        }

    }
    // Reproduction Constructor (used on each generation)
    public Birb(Birb parent1, Birb parent2) {
        // Pre-Mutation
        this.strength    = new int[16];
        this.speed       = new int[16];
        this.feathers    = new int[16];
        this.color       = new int[15];
        this.swimming    = new int[16];

        int strengthSplit    = (int) (Math.random() * 16);
        int speedSplit       = (int) (Math.random() * 16);
        int feathersSplit    = (int) (Math.random() * 16);
        int colorSplit       = (int) (Math.random() * 15);
        int swimmingSplit    = (int) (Math.random() * 16);

        BirbNameGenerator nameMaker = new BirbNameGenerator();
        name = nameMaker.getRandomName();

        for (int i = 0; i < 16; i++) {
            // Strength DNA Cross
            if (i <= strengthSplit) {
                this.strength[i] = parent1.getStrength()[i];
            } else {
                this.strength[i] = parent2.getStrength()[i];
            }
            this.strengthDecimal += this.strength[i] * Math.pow(2, (15 - i));

            // Speed DNA Cross
            if (i <= speedSplit) {
                this.speed[i] = parent1.getSpeed()[i];
            } else {
                this.speed[i] = parent2.getSpeed()[i];
            }
            this.speedDecimal += this.speed[i] * Math.pow(2, (15 - i));

            // Feather DNA Cross
            if (i <= feathersSplit) {
                this.feathers[i] = parent1.getFeathers()[i];
            } else {
                this.feathers[i] = parent2.getFeathers()[i];
            }
            this.feathersDecimal += this.feathers[i] * Math.pow(2, (15 - i));

            // Color DNA Cross
            if (i < 15) {
                if (i <= colorSplit) {
                    this.color[i] = parent1.getColor()[i];
                } else {
                    this.color[i] = parent2.getColor()[i];
                }
                this.colorDecimal += this.color[i] * Math.pow(2, (14 - i));
            }

            // Swimming DNA Cross
            if (i <= swimmingSplit) {
                this.swimming[i] = parent1.getSwimming()[i];
            } else {
                this.swimming[i] = parent2.getSwimming()[i];
            }
            this.swimmingDecimal += this.swimming[i] * Math.pow(2, (15 - i));
        }

        // Trait DNA - Both parents must carry trait for child to posses
        this.nocturnal = parent1.isNocturnal() && parent2.isNocturnal();
        this.carniverous = parent1.isCarniverous() && parent2.isNocturnal();
        this.cannibalistic = parent1.isCannibalistic() && parent2.isCannibalistic();

        /*
         *    MUTATIONS Explanation -
         *          A Roll occurs for each strand of DNA, each Strand has a 10% Chance of Mutation
         *          Traits Mutation Rates are as follows
         *              Nocturnal : 2%
         *              Carnivorous : 1%
         *              Cannibalistic : 1% IF Already Carnivorous
         *                  - If Carnivorous is rolled successfully on birth, Cannibalism will be rolled too
         */

        // Using 160 so it can be used both as the index for mutation AND as a 10% Roll
        int strengthRoll = (int) (Math.random() * 160);
        int speedRoll    = (int) (Math.random() * 160);
        int colorRoll    = (int) (Math.random() * 150);
        int featherRoll  = (int) (Math.random() * 160);
        int swimmingRoll = (int) (Math.random() * 160);

        if (strengthRoll < 16) {
            if (this.strength[strengthRoll] == 0) {
                this.strength[strengthRoll]  = 1;
            }
            else {
                this.strength[strengthRoll] = 0;
            }
            strengthDecimal = 0;
            for (int i = 0; i < 16; i++) {
                strengthDecimal += this.strength[i] * Math.pow(2, (15 - i));
            }
        }

        if (speedRoll < 16) {
            if (this.speed[speedRoll] == 0) {
                this.speed[speedRoll]  = 1;
            }
            else {
                this.speed[speedRoll] = 0;
            }
            speedDecimal = 0;
            for (int i = 0; i < 16; i++) {
                speedDecimal += this.speed[i] * Math.pow(2, (15 - i));
            }
        }

        if (colorRoll < 15) {
            if (this.color[colorRoll] == 0) {
                this.color[colorRoll]  = 1;
            }
            else {
                this.color[colorRoll] = 0;
            }
            colorDecimal = 0;
            for (int i = 0; i < 15; i++) {
                colorDecimal += this.color[i] * Math.pow(2, (14 - i));
            }
        }

        if (featherRoll < 16) {
            if (this.feathers[featherRoll] == 0) {
                this.feathers[featherRoll]  = 1;
            }
            else {
                this.feathers[featherRoll] = 0;
            }
            feathersDecimal = 0;
            for (int i = 0; i < 16; i++) {
                feathersDecimal += this.feathers[i] * Math.pow(2, (15 - i));
            }
        }

        if (swimmingRoll < 16) {
            if (this.swimming[swimmingRoll] == 0) {
                this.swimming[swimmingRoll]  = 1;
            }
            else {
                this.swimming[swimmingRoll] = 0;
            }
            swimmingDecimal = 0;
            for (int i = 0; i < 16; i++) {
                swimmingDecimal += this.swimming[i] * Math.pow(2, (15 - i));
            }
        }

        int nocturnalRoll    = (int) (Math.random() * 50);
        int carniverousRoll  = (int) (Math.random() * 100);
        int cannibalRoll     = (int) (Math.random() * 100);

        if (nocturnalRoll == 0) {
            this.nocturnal = !(this.nocturnal);
        }
        if (carniverousRoll == 0) {
            this.carniverous = !(this.carniverous);
        }
        if (cannibalRoll == 0 && this.carniverous) {
            this.cannibalistic = !(this.cannibalistic);
        }



    }

    public void incrementGenerationAlive() {
        generationsAlive++;
    }
    public int getGenerationsAlive() {
        return generationsAlive;
    }

    // Food Setters / Getters
    public boolean isHasPlantFood() {
        return hasPlantFood;
    }

    public boolean isHasMeatFood() {
        return hasMeatFood;
    }

    public void setHasMeatFood(boolean hasMeatFood) {
        this.hasMeatFood = hasMeatFood;
    }
    public void setHasPlantFood(boolean hasPlantFood) {
        this.hasPlantFood = hasPlantFood;
    }

    // Getters for DNA used in parents for reproduction
    public int[] getStrength() {
        return strength;
    }

    public int[] getSpeed() {
        return speed;
    }

    public int[] getFeathers() {
        return feathers;
    }

    public int[] getColor() {
        return color;
    }

    public int[] getSwimming() {
        return swimming;
    }

    // Getters for Traits for easy calculations / parent reproduction
    public boolean isNocturnal() {
        return nocturnal;
    }

    public boolean isCarniverous() {
        return carniverous;
    }

    public boolean isCannibalistic() {
        return cannibalistic;
    }

    // Getters to easily run calculations from Decimal
    public int getStrengthDecimal() {
        return strengthDecimal;
    }

    public int getSpeedDecimal() {
        return speedDecimal;
    }

    public int getFeathersDecimal() {
        return feathersDecimal;
    }

    public int getColorDecimal() {
        return colorDecimal;
    }

    public int getSwimmingDecimal() {
        return swimmingDecimal;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setStrength(int[] str) {
        strength = str;
    }
    public void setSpeed(int[] spd) {
        speed = spd;
    }

    public void setColor(int[] col) {
        color = col;
    }

    public void setFeathers(int[] feath) {
        feathers = feath;
    }

    public void setSwimming(int[] swm) {
        swimming = swm;
    }

    public void setStrengthDecimal(int strDec) {
        strengthDecimal = strDec;
    }

    public void setSpeedDecimal(int spdDec) {
        speedDecimal = spdDec;
    }

    public void setFeathersDecimal(int feathDec) {
        feathersDecimal = feathDec;
    }

    public void setColorDecimal(int colDec) {
        colorDecimal = colDec;
    }

    public void setSwimmingDecimal(int swmDec) {
        swimmingDecimal = swmDec;
    }

    public void setNocturnal(boolean noc) {
        nocturnal = noc;
    }

    public void setCarniverous(boolean carn) {
        carniverous = carn;
    }

    public void setCannibalistic(boolean cann) {
        cannibalistic = cann;
    }

    public void setName(String nm) {
        name = nm;
    }

    public void setGenerationsAlive(int gens) {
        generationsAlive = gens;
    }
}
