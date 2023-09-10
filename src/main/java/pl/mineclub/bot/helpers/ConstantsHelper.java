package pl.mineclub.bot.helpers;

import java.awt.*;

public class ConstantsHelper {

    public static Color color = new Color(56, 132, 255);
    public static String imgUrl = "https://cdn.discordapp.com/attachments/932208812054695968/1031847201359528037/311146119_410753797741288_978757450474059140_n.jpg";

    public static double procentsOne(int i, int i2){

        return (double) i /( i + i2) * 100;

    }

    public static double procentsTwo(int i, int i2){

        return (double) i2 /( i + i2) * 100;

    }
}
