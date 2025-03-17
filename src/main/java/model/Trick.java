package model;

import utils.Difficulty;

public class Trick {
    private String nameTrick;
    private String description;
    private Difficulty difficultyTrick;

    public Trick(String nameTrick, String description, Difficulty difficultyTrick){
        this.nameTrick = nameTrick;
        this.description = description;
        this.difficultyTrick = difficultyTrick;
    }

    public String getDescription(){return this.description;}
    public String getNameTrick(){return this.nameTrick;}
    public Difficulty getDifficultyTrick(){return this.difficultyTrick;}

    void setDescription(String description){this.description = description;}
    void setNameTrick(String nameTrick){this.nameTrick = nameTrick;}
    void setDifficultyTrick(Difficulty difficultyTrick){this.difficultyTrick = difficultyTrick;}
}
