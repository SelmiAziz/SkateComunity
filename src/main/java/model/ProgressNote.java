package model;

import java.time.LocalDate;
import java.util.UUID;

public class ProgressNote {
    private String id;
    private String comment;
    private LocalDate date;


    public ProgressNote(String id, String comment, LocalDate date){
        this.id = id;
        this.comment = comment;
        this.date = date;
    }

    public ProgressNote(String comment) {
        this.id = UUID.randomUUID().toString();
        this.comment = comment;
        this.date = LocalDate.now();
    }

    public String getId(){
        return this.id;
    }

    public ProgressNote(String comment, LocalDate date) {
        this.comment = comment;
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public LocalDate getDate() {
        return date;
    }
}

