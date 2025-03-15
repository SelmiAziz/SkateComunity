package Dao;

import model.Costumer;
import model.Organizer;

import java.util.List;

public interface OrganizerDao {
    void addOrganizer(Organizer organizer);
    Organizer selectOrganizerByOrganizerName(String organizerName);
    void update(Organizer organizer);
}
