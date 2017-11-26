package carte;

public class Construction implements Bien{

    private int price, character, specialPowerId, numOccur;
    private String name, description;
    private boolean isBuilt, isOwned;


    //Constructeur Batiment
    public Construction(String name, int price, int character, int numOccur, boolean isBuilt, boolean isOwned){
        this.name = name;
        this.description = null;
        this.specialPowerId = -1;
        this.price = price;
        this.character = character;
        this.numOccur = numOccur;
        this.isBuilt = isBuilt;
        this.isOwned = isOwned;
    }

    //Constructeur Merveille
    public Construction(int specialPowerId, String name, String description, int price, int numOccur, boolean isBuilt, boolean isOwned){
        this.specialPowerId = specialPowerId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.numOccur = numOccur;
        this.isBuilt = isBuilt;
        this.character = 0;
        this.isOwned = isOwned;
    }

    //Constructeur defaut
    public Construction(String name, String description, int price, int character, int specialPowerId, int numOccur, boolean isBuilt, boolean isOwned){
        this.specialPowerId = specialPowerId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.character = character;
        this.numOccur = numOccur;
        this.isBuilt = isBuilt;
        this.isOwned = isOwned;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public int getPrice() {
        return this.price;
    }

    @Override
    public int getCharacter() {
        return this.character;
    }

    @Override
    public int getSpecialPowerId() {
        return this.specialPowerId;
    }

    //Nombre d'occurrences
    @Override
    public int getNumberOccur() {
        return this.numOccur;
    }

    //Augmenter une occurrence de la carte
    public void addOccur(int occur){
        this.numOccur += occur;
    }

    //Diminuer une occurence de la carte
    public boolean removeOccur(int occur){
        if(numOccur < occur)
            return false;
        else
            this.numOccur = numOccur-occur;

        return true;
    }

    //Déterminer si la carte est construite ou pas
    public boolean isBuilt(){
        return isBuilt;
    }

    //Construite ou détruire la carte
    public void changeBuilding(){
        this.isBuilt = !this.isBuilt;
    }

    //Si la carte possède un propriétaire
    public boolean isOwned(){
        return isOwned;
    }

    //Modifier la possession de la carte
    public void changeOwnership(){
        this.isOwned = !this.isOwned;
    }

    public String toString(){
        return "Name : "+this.getName()+"\nDescription : "+this.getDescription()+"\nPrice : "+this.getPrice()+"\nCharacter : "+this.getCharacter()+"\nSpecialPower : "+this.getSpecialPowerId()+"\nOccurence : "+this.getNumberOccur();
    }
}
