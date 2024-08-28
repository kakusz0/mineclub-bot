package pl.mineclub.bot.helpers;

import java.awt.*;

public class ConstantsHelper {

    public static Color color = new Color(56, 132, 255);
    //public static String imgUrl = "https://cdn.discordapp.com/attachments/932208812054695968/1031847201359528037/311146119_410753797741288_978757450474059140_n.jpg";

    public static String imgUrl = "https://cdn.discordapp.com/attachments/932208812054695968/1278386101194592308/Logo_-_Bez_ta.jpeg?ex=66d09d55&is=66cf4bd5&hm=23fb44ce1204f1310dc4099ac4ac0e960524de8f59c843b72fa5b9d4d47d116d&";


    public static double procentsOne(int i, int i2){

        return (double) i /( i + i2) * 100;

    }

    public static double procentsTwo(int i, int i2){

        return (double) i2 /( i + i2) * 100;

    }
}
