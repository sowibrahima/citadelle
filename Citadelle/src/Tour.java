import carte.Construction;
import carte.Joueur;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Tour {

    private Scanner scanner;
    private String input;
    private Power power;



    public Tour(){
        scanner = new Scanner(System.in);
    }

    //Jouer le tour du joueur
    public List<Joueur> playTour(Joueur joueur, List<Joueur> joueurs){
        if(joueur.isStolen()){
            System.out.println("Le voleur vous a volé vos pièces");
            int pieces = joueur.getCoins();
            joueur.removeCoins(pieces);
            for (int i=0; i<joueurs.size(); i++){
                if(joueurs.get(i).getCharacter().getPosition()==2)
                    joueurs.get(i).addCoins(pieces);
            }
            joueur.setStolen(false);
        }

        displayGameData(joueur, joueurs);
        displayGiftChoice();
        input = scanner.nextLine();
        while (!Utils.isNumeric(input) || !input.equals("1") && !input.equals("2")){
            System.out.println("\nVeuillez choisir 1 ou 2 : ");
            input = scanner.nextLine();
        }

        int choice = Integer.parseInt(input);
        if(choice == 1){
            System.out.println("\nVous avez pris 2 pièces");
            joueur.addCoins(2);
        } else if(choice == 2) {
            pickCards(2, 1, joueur);
        } else {
            System.out.println("\nChoix invalide");
        }


        displayPowerChoice();
        input = scanner.nextLine();
        while (!input.toUpperCase().equals("O") && !input.toUpperCase().equals("N")) {
            System.out.println("\nVeuillez choisir entre O/o ou N/n ");
            input = scanner.nextLine();
        }

        if(input.toUpperCase().equals("O")){
            System.out.println("\nVous avez appliqué votre pouvoir");
            joueurs = applyPower(joueur, joueurs);
        } else if(input.toUpperCase().equals("N")){
            System.out.println("\nVous n'avez pas appliqué votre pouvoir");
        }

        displayConstruction(joueur);
        input = scanner.nextLine();
        if(input.equals("-1")){
            System.out.println("\nPas de construction. FIN DU TOUR\n");
        } else {
            int nbConstruits = 0;
            int nbMaxCons  = joueur.getCharacter().getPosition() == 8 ? 3 : 2;
            while (Utils.isNumeric(input) && !input.equals("-1") && nbConstruits<nbMaxCons) {

                if(Integer.parseInt(input) < joueur.getConstructions().size()){
                    if(joueur.getConstructions().get(Integer.parseInt(input)).isBuilt()){
                        System.out.println("\nDéja construit\n");
                    } else if(joueur.getConstructions().get(Integer.parseInt(input)).getPrice() <= joueur.getCoins()){
                        joueur.getConstructions().get(Integer.parseInt(input)).changeBuilding();
                        joueur.removeCoins(joueur.getConstructions().get(Integer.parseInt(input)).getPrice());
                        System.out.println(joueur.getConstructions().get(Integer.parseInt(input)).getName()+" construit");
                        nbConstruits++;
                    } else {
                        System.out.println("Il vous manque : "+(joueur.getConstructions().get(Integer.parseInt(input)).getPrice()-joueur.getCoins())+" pièces");
                        System.out.println("\nChoix construction : ");
                    }
                } else
                    System.out.println("\nChoix invalde\n");

                System.out.println("Veuillez choisir un batiment à construire ou -1 pour terminer");
                input = scanner.nextLine();
            }
            if(nbConstruits == nbMaxCons)
                System.out.println("Vous avez déjà construit "+nbConstruits+" biens");

            System.out.println("\nFin construction. FIN DU TOUR\n");
        }
        return joueurs;
    }

    //Choisir des cartes au hasard
    private void pickCards(int numberOfCards, int numberToKeep, Joueur joueur){
        final int[] randomInts = new Random().ints(0, Utils.availableCardsOnDeck().size()).distinct().limit(numberOfCards).toArray();

        System.out.println("\nCartes choisies : \n");
        List<Construction> choosenCards = new ArrayList<>();
        for(int i=0; i<randomInts.length; i++){
            choosenCards.add(Utils.availableCardsOnDeck().get(randomInts[i]));
            System.out.println(i+" - "+((Construction)Utils.availableCardsOnDeck().get(randomInts[i])).getName());
        }

        System.out.println("\nChoisir "+numberToKeep+" carte(s) à garder : \n");
        for(int i=0; i<numberToKeep; i++){
            input = scanner.nextLine();
            while (!Utils.isNumeric(input) || Integer.parseInt(input)<0 || Integer.parseInt(input) > numberToKeep) {
                System.out.println("\nVeuillez choisir une carte valide : ");
                input = scanner.nextLine();
            }
            int choice = Integer.parseInt(input);
            keepCard(choosenCards.get(choice), choice, joueur);
            System.out.println("\n"+choosenCards.get(choice).getName()+" choisie\n");
        }
    }

    //Enregistrer la carte à garder sur la liste des bien du joueur
    private void keepCard(Construction construction, int choice, Joueur joueur){
        System.out.println("\nVous avez gardé la carte : " + construction.getName());
        construction.changeOwnership();
        joueur.addConstruction(construction);
        construction.changeOwnership();
        ((Construction)Game.gameData.getBiens().get(choice)).removeOccur(1);
    }

    //Afficher les informations de la partie
    private void displayGameData(Joueur joueur, List<Joueur> joueurs){
        String leftAlignFormat = "| %-6s | %-14s | %-8s | %-25s |%n";

        System.out.format("+--------+----------------+----------+---------------------------+%n");
        System.out.format("| Joueur |   Personnage   |  Pièces  |           Biens           |%n");
        System.out.format("+--------+----------------+----------+---------------------------+%n");
        for(int i=0; i<joueurs.size(); i++){
            System.out.format(leftAlignFormat, i+"", joueurs.get(i).getCharacter().getName(), joueurs.get(i).getCoins()+"", "-");
            for(int j=0; j<joueurs.get(i).getConstructions().size(); j++){
                if(joueurs.get(i).getConstructions().get(j).isBuilt())
                    System.out.format(leftAlignFormat, "-", "-","-",joueurs.get(i).getConstructions().get(j).getName()+'('+joueurs.get(i).getConstructions().get(j).getPrice()+" pièces)");
            }
        }
        System.out.format("+--------+----------------+----------+---------------------------+%n");



        System.out.println("Tour joueur : "+joueur.getCharacter().getName());
    }

    //Afficher choix de pièces ou cartes
    private void displayGiftChoice(){
        System.out.println("\n1 : 2 pièces\n2 : 2 cartes\nChoix : ");
    }

    //Afficher choix de pouvoir
    private void displayPowerChoice(){
        System.out.println("\nAppliquer pouvoir O/N\n\n");
    }

    //Afficher les biens construits d'un joueur
    private void displayConstruction(Joueur joueur){
        System.out.println("\nConstrure : ");
        System.out.println("-1 - Fin construction");
        for (int i=0; i<joueur.getConstructions().size(); i++){
            Construction currentConstruction = joueur.getConstructions().get(i);
            if(!currentConstruction.isBuilt())
                System.out.println(i+" - "+currentConstruction.getName()+" - "+currentConstruction.getPrice()+" pièces");
        }
    }

    //Appliquer le pouvoir d'un joueur
    private List<Joueur> applyPower(Joueur joueur, List<Joueur> joueurs){
        power = new Power(joueurs);
        switch (joueur.getCharacter().getPosition()-1){
            case 0:
                joueurs = power.assassin();
                break;
            case 1:
                joueurs = power.voleur();
                break;
            case 2:
                joueurs = power.magicien(joueur);
                break;
            case 3:
                power.roi(joueur);
                break;
            case 4:
                power.eveque(joueur);
                break;
            case 5:
                power.marchand(joueur);
                break;
            case 6:
                power.architecte(joueur);
                break;
            case 7:
                power.condotiere(joueur);
                break;
        }
        return joueurs;
    }



}
