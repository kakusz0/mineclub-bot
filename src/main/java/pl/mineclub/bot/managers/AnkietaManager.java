package pl.mineclub.bot.managers;

import pl.mineclub.bot.objects.Ankieta;

import java.util.HashMap;
import java.util.Map;

public class AnkietaManager {
    public Map<Ankieta, Ankieta> ankiets;


    public AnkietaManager(){
        ankiets = new HashMap<>();
    }

    public Ankieta getAnkieta(Ankieta ankieta) {
        return this.ankiets.get(ankieta);
    }

}
