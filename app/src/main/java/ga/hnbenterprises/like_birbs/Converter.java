package ga.hnbenterprises.like_birbs;

import androidx.room.TypeConverter;

public class Converter {
    @TypeConverter
    public static int[] fromString(String binString) {
        int[] binArry = new int[16];
        String[] tokens = binString.split("-");

        for (int i = 0; i < 16; i++) {
            binArry[i] = Integer.parseInt(tokens[i]);
        }

        return binArry;
    }

    @TypeConverter
    public static String fromArr(int binArry[]) {
        String binStr = "";
        int length = binArry.length;
        for (int i = 0; i < length; i++) {
            if (i != length - 1) {
                binStr = binStr + binArry[i] + "-";
            }
            else {
                binStr = binStr + binArry[i];
            }
        }

        return binStr;
    }
}