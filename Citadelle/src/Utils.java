import carte.Construction;

import java.util.ArrayList;
import java.util.List;


public class Utils {

    //Déterminer si une chaîne de caractère est un chiffre
    public static boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }

    //Liste des cartes libres disponibles dans la partie
    public static List<Construction> availableCardsOnDeck(){
        List<Construction> available = new ArrayList<>();

        for (int i=0; i<Game.gameData.getBiens().size(); i++){
            Construction consToAdd = (Construction)Game.gameData.getBiens().get(i);
            if(consToAdd.getNumberOccur()>0)
                available.add(consToAdd);
        }
        return available;
    }
}
