package dao;

import model.Organizer;

public interface OrganizerDao {
    void addOrganizer(Organizer organizer);
    Organizer selectOrganizerByOrganizerName(String organizerName);
    void update(Organizer organizer);
}
