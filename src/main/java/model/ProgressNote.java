package model;

public class ProgressNote {
    private String comment;
    private String date;

    public ProgressNote(String comment, String date){
        this.comment = comment;
        this.date = date;
    }

    public String getComment(){
        return comment;
    }

    public String date(){
        return date;
    }
}
