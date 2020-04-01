package edu.psu.cto5068.like_birbs;

public class Converter {
    @TypeConverter
    public static int[] fromString(String binString) {
        int[16] binArry = new int[16];
        String[] tokens = binString.split("-");

        for (int i = 0; i < 16; i++) {
            binArry[i] = Integer.parseInt(tokens[i]);
        }

        return binArry;
    }

    @TypeConverter
    public static String fromArr(int binArry[]) {
        String binStr = "";
        for (int i = 0; i < 16; i++) {
            if (i != 15) {
                binStr = binStr + binArry[i] + "-";
            }
            else {
                binStr = binStr + binArry[i]
            }
        }

        return binStr;
    }
}