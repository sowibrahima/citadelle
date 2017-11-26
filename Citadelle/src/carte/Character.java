package carte;


public class Character implements Carte{

    private String name, description;
    private int position;

    public Character(String name, String description, int position){
        this.name = name;
        this.description = description;
        this.position = position;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    public int getPosition(){
        return this.position;
    }


}
