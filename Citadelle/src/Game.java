import carte.Character;
import carte.Construction;
import carte.Joueur;
import carte.PlayerData;

import java.util.*;

public class Game {

    static GameData gameData;
    private int nbJoueurs;
    private List<Joueur> joueurs;
    private List<Integer> characteresChoisis;
    private ArrayList<PlayerData> playerDatas;
    private Scanner scanner;
    private boolean gameOver;
    private int nbTours;

    public Game(int nbJoueurs){
        this.gameOver = false;
        this.nbTours = 0;
        this.nbJoueurs = nbJoueurs;
        this.joueurs = new ArrayList<>();
        this.playerDatas = new ArrayList<>();
        this.characteresChoisis = new ArrayList<>();
        scanner = new Scanner(System.in);
    }


    //Instantier le jeu
    private void instantiateGame(){
        System.out.println("Veuillez choisir 1 pour un humain et 2 pour un robot\n");
        for(int i=1; i<= nbJoueurs; i++){
            System.out.println("Joueur "+i+" : ");
            String input = scanner.nextLine();
            while (!Utils.isNumeric(input) || !input.equals("1") && !input.equals("2")){
                System.out.println("\nVeuillez entrer un choix valide : ");
                input = scanner.nextLine();
            }

            this.joueurs.add(new Joueur(new Character("none","none",-1), false, new ArrayList<Construction>(), 2));
            if(input.equals("1"))
                this.joueurs.get(i-1).changeHumanity();
        }
        choixCarte();
    }

    //Jouer un tour tant que le jeu n'est pas terminé
    private void tour(){
        while (!gameOver){
            int[] characterPresent = new int[gameData.getJoueurs().size()];
            for(int i=0; i<characterPresent.length; i++)
                characterPresent[i] = -1;

            for(int i=0; i<this.joueurs.size(); i++){
                switch (this.joueurs.get(i).getCharacter().getPosition()){
                    case 1:
                        characterPresent[0] = i;
                        break;
                    case 2:
                        characterPresent[1] = i;
                        break;
                    case 3:
                        characterPresent[2] = i;
                        break;
                    case 4:
                        characterPresent[3] = i;
                        break;
                    case 5:
                        characterPresent[4] = i;
                        break;
                    case 6:
                        characterPresent[5] = i;
                        break;
                    case 7:
                        characterPresent[6] = i;
                        break;
                    case 8:
                        characterPresent[7] = i;
                        break;

                }
            }

            for(int i=0; i<characterPresent.length; i++){
                if(characterPresent[i] != -1){
                    nbTours++;

                    int joueurPos = characterPresent[i];

                    System.out.println("Tour "+nbTours+" - "+this.joueurs.get(joueurPos).getCharacter().getName());

                    if(joueurs.get(joueurPos).isAssassinated()){
                        System.out.println("O_o Vous êtes assasinés");
                        joueurs.get(joueurPos).setAssassinated(false);
                    } else {
                        Tour tour = new Tour();
                        tour.playTour(joueurs.get(joueurPos), joueurs);
                    }

                    gameOver = gameEnded();
                    if(gameOver){
                        int max = 0;
                        int posMax = 0;
                        for(int j=0; j<playerDatas.size(); j++){
                            if(playerDatas.get(j).getWealth() > max){
                                max = playerDatas.get(j).getWealth();
                                posMax = j;
                            }
                        }
                        System.out.println("Joueur "+this.joueurs.get(posMax)+" a gagné avec une fortune de "+playerDatas.get(posMax).getWealth()+" pièces");
                        break;
                    }


                    for (int j = 0; j<joueurs.size(); j++){
                        if(joueurs.get(j).getCharacter().getPosition() == 4)
                            joueurs.get(j).setKing(true);
                    }
                }

            }

            choixCarte();
        }
    }

    //Distribuer les cartes aux joueurs pour qu'ils fassent leur choix
    private void choixCarte(){
        List<Joueur> joueursClone = new ArrayList<>();
        int roi = -1;
        //Déterminer la position de la personne qui possède le couronne afin qu'il puisse choisir son personnage en premier
        for (int i = 0; i<joueurs.size(); i++){
            if(joueurs.get(i).isKing())
                roi = i;
        }


        if(roi == -1){
            //Personne ne possède la carte couronne
            for (int j=0; j<joueurs.size(); j++)
                joueursClone.add(choixCaractere(j));
        } else {
            //Un joueur possède déjà la carte couronne

            //Parcours du Roi à la fin
            for (int j=roi; j<joueurs.size(); j++)
                joueursClone.add(choixCaractere(j));

            //Parcours du debut jusqu'au Roi-1
            for (int j=0; j<roi; j++)
                joueursClone.add(choixCaractere(j));
        }

        this.joueurs = joueursClone;
        characteresChoisis = new ArrayList<>();
    }

    //Afficher la liste des personnages disponibles lors d'un choix de personnage
    private void afficherJoueursDisponobles(){
        for (int i=0; i<gameData.getJoueurs().size(); i++){
            if(!characteresChoisis.contains(i)){
                Joueur joueur = (Joueur)gameData.getJoueurs().get(i);
                System.out.println(i+" - "+joueur.getCharacter().getName());
            }
        }
    }

    //Choisir un personnage pour chaque tour
    private Joueur choixCaractere(int index){
        System.out.println("Joueur "+index+" : Veuillez choisir un personnage : ");
        afficherJoueursDisponobles();
        System.out.println("Choix : ");
        String input = scanner.nextLine();
        while (!Utils.isNumeric(input) && Integer.parseInt(input)<0 || Integer.parseInt(input)>=gameData.getJoueurs().size()){
            System.out.println("Veuillez entrer un choix valide");
            input = scanner.nextLine();
        }

        Joueur joueurChoisi = (Joueur) gameData.getJoueurs().get(Integer.parseInt(input));
        Joueur joueurCourrant = this.joueurs.get(index);
        joueurCourrant.setCharacter(joueurChoisi.getCharacter());


        characteresChoisis.add(joueurCourrant.getCharacter().getPosition()-1);
        System.out.println("Characteres déja choisis : "+characteresChoisis.toString());

        //Si on a un joueur qui a choisi la personnage Roi, lui donner la carte couronne
        if(joueurCourrant.getCharacter().getName().toUpperCase().equals("ROI")){
            for (int i=0; i<this.joueurs.size(); i++)
                this.joueurs.get(i).setKing(false);
            joueurCourrant.setKing(true);
        }

        return joueurCourrant;
    }

    //Determiner si le jeu est terminé
    private boolean gameEnded(){
        for(int i=0; i<nbJoueurs; i++){
            Joueur joueur = this.joueurs.get(i);
            int nbCons = joueur.getConstructions().size();
            int consPrice = 0;
            for(int j=0; j<nbCons; j++){
                consPrice += joueur.getConstructions().get(j).getPrice();
            }
            playerDatas.add(new PlayerData(nbCons, joueur.getCoins(), consPrice));
        }

        //Terminer le jeu si un joueur a au moins 8 biens construits dans son cité
        for (int i=0; i<playerDatas.size(); i++){
            if(playerDatas.get(i).getNbCons() >= 8){
                return true;
            }

        }
        return false;
    }

    //Démarrer une partie
    private void startGame(){
        instantiateGame();
        tour();
    }

    public static void main(String args[]){
        gameData = new GameData("assets/jsonData.json");

        if(gameData.readCSV())
            System.out.println("Data read successfully");

        Scanner scanner = new Scanner(System.in);
        System.out.println("Veuillez choisir le nombre de joueurs entre 2 et 8");
        String nbJoueurs = scanner.nextLine();
        while (!Utils.isNumeric(nbJoueurs) || Integer.parseInt(nbJoueurs)<2 || Integer.parseInt(nbJoueurs)>8){
            System.out.println("Choix invalide");
            nbJoueurs = scanner.nextLine();
        }
        Game game = new Game(Integer.parseInt(nbJoueurs));
        game.startGame();
    }


}
