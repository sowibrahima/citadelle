package carte;


import java.util.ArrayList;
import java.util.List;

public class Machine extends Joueur{

    int intelligence;

    public Machine(Character character, List<Construction> constructions, int coins, int intelligence) {
        super(character, false, constructions, coins);
        this.intelligence = intelligence;
    }
}
