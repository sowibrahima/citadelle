package carte;


import java.util.ArrayList;
import java.util.List;

public class Humain extends Joueur{

    public Humain(Character character, List<Construction> constructions, int coins) {
        super(character, true, new ArrayList<>(), coins);
    }
}
