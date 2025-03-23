package model;

import utils.DifficultyTrick;

public class Trick {
    private String nameTrick;
    private String description;
    private DifficultyTrick difficultyTrick;
    private String date;
    private String category;

    public Trick(String nameTrick, String description, DifficultyTrick difficultyTrick, String category, String date){
        this.nameTrick = nameTrick;
        this.description = description;
        this.difficultyTrick = difficultyTrick;
        this.category = category;
        this.date = date;
    }

    public String getDescription(){return this.description;}
    public String getNameTrick(){return this.nameTrick;}
    public DifficultyTrick getDifficultyTrick(){return this.difficultyTrick;}
    public String getDataCaricamento(){return this.date;}
    public String getCategory(){return this.category;}

    void setDescription(String description){this.description = description;}
    void setNameTrick(String nameTrick){this.nameTrick = nameTrick;}
    void setDifficultyTrick(DifficultyTrick difficultyTrick){this.difficultyTrick = difficultyTrick;}
    void setDate(String date){this.date = date;}
    void setCategory(String category){this.category = category;}
}
