package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProgressNote {
    private String comment;
    private String date;

    public ProgressNote() {
        this.comment = "Order chronology starts here!";
        this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public ProgressNote(String comment, String date) {
        this.comment = comment;
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public String getDate() {
        return date;
    }
}

