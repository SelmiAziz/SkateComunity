package dao;

import model.Organizer;

import java.io.IOException;

public interface OrganizerDao {
    void addOrganizer(Organizer organizer);
    Organizer selectOrganizerByUsername(String organizerName) throws IOException;

}
