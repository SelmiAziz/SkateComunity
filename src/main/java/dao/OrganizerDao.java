package dao;

import model.Organizer;

public interface OrganizerDao {
    void addOrganizer(Organizer organizer);
    Organizer selectOrganizerByUsername(String organizerName);

}
