package dao;

import exceptions.DataAccessException;
import model.Organizer;


public interface OrganizerDao {
    void addOrganizer(Organizer organizer) throws DataAccessException;
    Organizer selectOrganizerByUsername(String organizerName) throws DataAccessException;

}
