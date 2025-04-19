package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProgressNote {
    private String comment;
    private String date;

    public ProgressNote() {
        this.comment = "Order chronology starts here!";
        this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public ProgressNote(String comment) {
        this.comment = comment;
        this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String getComment() {
        return comment;
    }

    public String getDate() {
        return date;
    }
}

