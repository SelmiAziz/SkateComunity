package beans;

public class TrickBean {
    private String nameTrick;
    private String difficulty;
    private String description;
    private String category;
    private String date;

    public TrickBean(String nameTrick){
        this.nameTrick = nameTrick;
    }
    public TrickBean(String nameTrick, String description, String difficulty, String category){
        this.nameTrick = nameTrick;
        this.description = description;
        this.difficulty = difficulty;
        this.category = category;
    }
    public TrickBean(String nameTrick, String description, String difficulty, String category, String date){
        this.nameTrick = nameTrick;
        this.description = description;
        this.difficulty = difficulty;
        this.category = category;
        this.date = date;
    }

    public TrickBean(String nameTrick, String category){
        this.nameTrick = nameTrick;
        this.category = category;
    }

    public String getCategory(){return this.category;}
    public String getDescription(){return this.description;}
    public String getNameTrick(){return this.nameTrick;}
    public String getDifficulty(){return this.difficulty;}
    public String getDate(){return this.date;}
}
