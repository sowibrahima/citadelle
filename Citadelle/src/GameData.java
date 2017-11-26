import carte.*;
import carte.Character;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GameData {

    //VARIABLES
    private List<Joueur> joueurs;
    private List<Bien> biens;
    private String path;

    //Constructeur avec le chemin des données
    public GameData(String path){
        this.path = path;
        this.biens = new ArrayList<>();
        this.joueurs = new ArrayList<>();
    }

    //Lire le fichier JSON contenant les données
    public static JSONObject parseJSONFile(String filename) throws JSONException, IOException {
        String content = new String(Files.readAllBytes(Paths.get(filename)));
        return new JSONObject(content);
    }

    //Obtenir la liste de tout les biens dans le jeu
    public List getBiens(){
        return biens;
    }


    //Obtenir la liste de tout les personnages sur le jeu
    public List getJoueurs(){
        return joueurs;
    }

    //Read the CSV file data
    public boolean readCSV(){
        try {
            JSONObject obj = parseJSONFile(path);

            //CONSTRUCTIONS
            JSONArray arr = obj.getJSONArray("CONSTRUCTIONS");
            for (int i = 0; i < arr.length(); i++)
            {
                String name = arr.getJSONObject(i).getString("name");
                int price = arr.getJSONObject(i).getInt("price");
                int numOccur = arr.getJSONObject(i).getInt("num");

                if(arr.getJSONObject(i).has("specialPowerID")){
                    int specPow = arr.getJSONObject(i).getInt("specialPowerID");
                    String desc = arr.getJSONObject(i).getString("desc");
                    biens.add(new Merveille(specPow, name, desc, price, numOccur, false, false));
                } else {
                    int role = arr.getJSONObject(i).getInt("role");
                    biens.add(new Batiment(name, price, role, numOccur, false, false));
                }
            }

            //CHARACTERS
            JSONArray arrChars = obj.getJSONArray("CHARACTERS");
            for (int i = 0; i < arrChars.length(); i++)
            {
                String name = arrChars.getJSONObject(i).getString("name");
                String desc = arrChars.getJSONObject(i).getString("desc");
                joueurs.add(new Joueur(new Character(name, desc, i+1), false, new ArrayList<>(), 2));
            }


            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
