import carte.Construction;
import carte.Joueur;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Power {

    private List<Joueur> joueurs;
    private Scanner scanner;

    //Constructeur avec la liste des joueurs
    public Power(List<Joueur> joueurs) {
        this.joueurs = joueurs;
        scanner = new Scanner(System.in);
    }

    /*
        Méthodes pour appliquer le pouvoir de chaque personnage
    */


    public List<Joueur> assassin() {
        System.out.println("\nVeuillez choisir le joueur à assassiner : ");
        Joueur victim = victim(1, "none");
        for (int i = 0; i < this.joueurs.size(); i++) {
            if (this.joueurs.get(i).getCharacter().equals(victim.getCharacter()))
                this.joueurs.get(i).setAssassinated(true);
        }

        return this.joueurs;
    }

    public List<Joueur> voleur() {
        System.out.println("\nVeuillez choisir le joueur à voler : ");
        Joueur victim = victim(2, "none");
        System.out.println("Victim " + victim.getCharacter().getName());

        for (int i = 0; i < this.joueurs.size(); i++) {
            if (this.joueurs.get(i).getCharacter().equals(victim.getCharacter()))
                this.joueurs.get(i).setStolen(true);
        }

        return this.joueurs;
    }

    public List<Joueur> magicien(Joueur magician) {
        System.out.println("\nVeuillez choisir :\n1 pour echanger tes cartes contre un autre joueur");
        System.out.println("2 pour echanger certaines cartes dans la pioche\nChoix : ");
        String input = scanner.nextLine();
        while (!Utils.isNumeric(input) || !input.equals("1") && !input.equals("2")) {
            System.out.println("\nVeuillez choisir 1 ou 2 : ");
            input = scanner.nextLine();
        }

        switch (input) {
            case "1":
                System.out.println("\nVeuillez choisir le joueur contre qui echanger vos cartes ");
                Joueur victim = victim(0, "magicien");
                    for (int i = 0; i < this.joueurs.size(); i++) {
                        if (this.joueurs.get(i).getCharacter().equals(victim.getCharacter())) {
                            List<Construction> magicianConstructions = magician.getConstructions();
                            magician.setConstructions(victim.getConstructions());
                            this.joueurs.get(i).setConstructions(magicianConstructions);
                        }
                    }
                return this.joueurs;
            case "2":
                List<Construction> consToRemove = new ArrayList<>();
                for (int i = 0; i < magician.getConstructions().size(); i++) {
                    System.out.print("\n" + i + " - " + magician.getConstructions().get(i).getName());
                }
                System.out.println("\nVeuillez choisir les cartes à échanger et terminer par -1\n");
                String card = scanner.nextLine();
                while (!card.equals("-1")) {
                    if (Utils.isNumeric(card) && Integer.parseInt(card) > 0 && Integer.parseInt(card) < magician.getConstructions().size()) {
                        Construction cardToAdd = magician.getConstructions().get(Integer.parseInt(card));
                        if (consToRemove.contains(cardToAdd))
                            System.out.println("Carte déja choisie");
                        else
                            consToRemove.add(cardToAdd);
                    } else
                        System.out.println("Choix invalide");
                    card = scanner.nextLine();
                }

                final int[] randomInts = new Random().ints(0, Utils.availableCardsOnDeck().size()).distinct().limit(consToRemove.size()).toArray();
                for (int i = 0; i < consToRemove.size(); i++) {
                    Construction newConsToAdd = Utils.availableCardsOnDeck().get(randomInts[i]);
                    for (int j = 0; j < Game.gameData.getBiens().size(); j++) {
                        Construction constToUpdate = (Construction) Game.gameData.getBiens().get(j);
                        if (constToUpdate.getName().equals(consToRemove.get(i).getName()))
                            ((Construction) Game.gameData.getBiens().get(j)).addOccur(1);

                        if (constToUpdate.getName().equals(newConsToAdd.getName()))
                            ((Construction) Game.gameData.getBiens().get(j)).removeOccur(1);
                    }
                    magician.addConstruction(newConsToAdd);
                }
                break;
            default:
                System.out.println("\nChoix invalide");
                break;
        }
        return this.joueurs;
    }

    public void roi(Joueur roi) {
        roi.setKing(true);
        roi.addCoins(bonusCoins(roi));
    }

    public void eveque(Joueur eveque) {
        eveque.addCoins(bonusCoins(eveque));
    }

    public void marchand(Joueur marchand) {
        marchand.addCoins(bonusCoins(marchand) + 1);
    }

    public void architecte(Joueur architecte) {
        final int[] randomInts = new Random().ints(0, Utils.availableCardsOnDeck().size()).distinct().limit(2).toArray();
        for (int i = 0; i < 2; i++) {
            Construction newConsToAdd = Utils.availableCardsOnDeck().get(randomInts[i]);
            for (int j = 0; j < Game.gameData.getBiens().size(); j++) {
                Construction constToUpdate = (Construction) Game.gameData.getBiens().get(j);

                if (constToUpdate.getName().equals(newConsToAdd.getName()))
                    ((Construction) Game.gameData.getBiens().get(j)).removeOccur(1);
            }
            architecte.addConstruction(newConsToAdd);
        }
    }

    public void condotiere(Joueur condotiere) {
        condotiere.addCoins(bonusCoins(condotiere));
        detruireQuartiers(condotiere);
    }

    //Nombre de biens construits par un joueur
    private int nbConstruction(Joueur joueur) {
        int nbCons = 0;
        for (Construction construction : joueur.getConstructions()) {
            if (construction.isBuilt())
                nbCons++;
        }
        return nbCons;
    }


    //Afficher les personnages des joueurs
    private void afficherJoueurs() {
        for (int i = 0; i < joueurs.size(); i++) {
            System.out.println(i + " - " + joueurs.get(i).getCharacter().getName());
        }
    }

    //Afficher les bien construits par un joueur
    private void afficherQuartiersJoueur(Joueur joueur) {
        for (int i = 0; i < joueur.getConstructions().size(); i++) {
            if (joueur.getConstructions().get(i).isBuilt())
                System.out.println(i + " - " + joueur.getConstructions().get(i) + ", valeur : " + joueur.getConstructions().get(i).getPrice());
        }
    }

    //Méthode pour détruite des quartiers
    private void detruireQuartiers(Joueur condotiere) {
        afficherJoueurs();
        System.out.println("Veuillez choisir un joueur pour afficher ses quartiers et terminer par -1");
        String input = scanner.nextLine();
        while (!input.equals("-1")) {
            if (Utils.isNumeric(input) && Integer.parseInt(input) < this.joueurs.size() && Integer.parseInt(input) >= 0) {
                if (nbConstruction(joueurs.get(Integer.parseInt(input))) >= 8)
                    System.out.println("Ce joueur a déja son cité complet");
                else {
                    int joueurPos = Integer.parseInt(input);
                    while (!input.equals("-1")) {
                        afficherQuartiersJoueur(joueurs.get(joueurPos));
                        System.out.println("Veuilez choisir le batiment à détruire : ");
                        input = scanner.nextLine();
                        if (!input.equals("-1")) {
                            if (joueurs.get(joueurPos).getConstructions().get(Integer.parseInt(input)).getPrice() - 1 <= condotiere.getCoins()) {
                                joueurs.get(joueurPos).getConstructions().get(Integer.parseInt(input)).changeBuilding();
                                condotiere.removeCoins(joueurs.get(joueurPos).getConstructions().get(Integer.parseInt(input)).getPrice() - 1);
                            } else
                                System.out.println("Vous n'avez pas assez de pièces");
                        }
                    }
                }
            } else
                System.out.println("Choix invalide");

            System.out.println("Veuillez choisir un joueur ou -1 pour quitter");
            input = scanner.nextLine();
        }
        System.out.println("Fin destruction");
    }

    //Donner une pièce au joueur pour chaque quartier rapportant correspondant à son personnage
    private int bonusCoins(Joueur character) {
        int bonusCoins = 0;
        for (int i = 0; i < character.getConstructions().size(); i++) {
            if (character.getConstructions().get(i).getCharacter() == character.getCharacter().getPosition() && character.getConstructions().get(i).isBuilt())
                bonusCoins += 1;
        }
        return bonusCoins;
    }

    //Joueur victime sur qui on applique un pouvoir
    private Joueur victim(int minChoice, String playerToExclude) {
        for (int i = minChoice; i < Game.gameData.getJoueurs().size(); i++) {
            Joueur joueur = (Joueur) Game.gameData.getJoueurs().get(i);
            if (!joueur.getCharacter().getName().toUpperCase().equals(playerToExclude.toUpperCase()))
                System.out.println(i + " - " + joueur.getCharacter().getName());
        }
        System.out.println("\nChoix : ");
        String input = scanner.nextLine();
        while (!Utils.isNumeric(input) || Integer.parseInt(input) < minChoice || Integer.parseInt(input) >= Game.gameData.getJoueurs().size()) {
            System.out.println("\nVeuillez entrer un choix valide : ");
            input = scanner.nextLine();
        }

        return (Joueur) Game.gameData.getJoueurs().get(Integer.parseInt(input));
    }
}
