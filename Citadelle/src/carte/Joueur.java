package carte;


import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Condition;

public class Joueur {

    private Character character;
    private boolean isHuman, isAssassinated, isStolen, isKing;
    private List<Construction> constructions;
    private int coins;


    //Constructeur
    public Joueur(Character character, boolean isHuman, List<Construction> constructions, int coins){
        this.character = character;
        this.isHuman = isHuman;
        this.constructions = constructions;
        this.coins = coins;
        this.isAssassinated = false;
        this.isStolen = false;
        this.isKing = false;
    }


    //Obtenir le personnage du joueur
    public Character getCharacter() {
        return this.character;
    }

    //Modifier le personnage du joueur
    public void setCharacter(Character character){
        this.character = character;
    }

    //Déterminer si le joueur est humain ou pas
    public boolean isHuman(){
        return this.isHuman;
    }

    //Déterminer si le joueur est assasiné dans la partie en cours
    public boolean isAssassinated(){
        return this.isAssassinated;
    }

    //Définir l'assassinement du joueur
    public void setAssassinated(boolean assassinated){
        this.isAssassinated = assassinated;
    }

    //Déterminer si le joueur est volé
    public boolean isStolen(){
        return this.isStolen;
    }

    //Définir le vol du joueur
    public void setStolen(boolean stolen){
        this.isStolen = stolen;
    }

    //Déterminer si le joueur possède la carte couronne
    public boolean isKing(){
        return this.isKing;
    }

    //Définir la couronne du joueur
    public void setKing(boolean king){
        this.isKing = king;
    }

    //Définir si le joueur est humain ou pas
    public void changeHumanity(){
        this.isHuman = !this.isHuman;
    }

    //Liste des biens du joueur
    public List<Construction> getConstructions(){
        return constructions;
    }

    //Définir la liste des biens
    public void setConstructions(List<Construction> constructions){
        this.constructions = constructions;
    }

    //Ajouter un bien sur la liste des biens du joueur
    public void addConstruction(Construction construction){
        this.constructions.add(construction);
    }

    //Supprimer un bien sur la liste des biens du joueur
    public void removeConstruction(Construction construction){
        constructions.removeIf(constRemove -> constRemove.getName().equals(construction.getName()) && constRemove.getDescription().equals(construction.getDescription()));
    }

    //Ajoiter des pièces sur le compte du joueur
    public void addCoins(int coins){
        this.coins += coins;
    }

    //Obtenir le montant de pièces du joueur
    public int getCoins(){
        return this.coins;
    }

    //Enlever des pièces au joueur
    public void removeCoins(int coins){
        this.coins = this.coins>=coins?(this.coins-coins):0;
    }

    @Override
    public String toString(){
        return this.character.getName();
    }
}
