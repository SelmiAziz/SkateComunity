package Dao;

import model.Costumer;
import model.Organizer;

import java.util.List;

public interface OrganizerDao {
    public void addOrganizer(Organizer organizer);
    public Organizer getOrganizerByUsername(String username);
    public List<Organizer> getAllOrganizers();
}
