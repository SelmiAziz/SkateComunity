package model;

import java.time.LocalDate;

public class ProgressNote {
    private String comment;
    private LocalDate date;

    public ProgressNote(String comment) {
        this.comment = comment;
        this.date = LocalDate.now();
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

