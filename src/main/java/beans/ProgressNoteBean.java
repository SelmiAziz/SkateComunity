package beans;

public class ProgressNoteBean {
    private String comment;
    private String date;


    public ProgressNoteBean(){
        //empty
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }
}
