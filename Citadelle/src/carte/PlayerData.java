package carte;

public class PlayerData {
    private int nbCons, coins, consTotalPrice;

    //Constructeur données d'un joueur
    public PlayerData(int nbCons, int coins, int consTotalPrice){
        this.nbCons = nbCons;
        this.coins = coins;
        this.consTotalPrice = consTotalPrice;
    }

    //Nombre de construction d'un joueur
    public int getNbCons(){
        return nbCons;
    }

    //Nombre de pièces d'un joueur
    public int coins(){
        return coins;
    }

    //Forture d'un joueur
    public int getWealth(){
        return consTotalPrice+coins;
    }
}
