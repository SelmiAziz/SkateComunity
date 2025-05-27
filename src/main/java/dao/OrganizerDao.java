package dao;

import exceptions.DataAccessException;
import model.Organizer;

import java.io.IOException;

public interface OrganizerDao {
    void addOrganizer(Organizer organizer) throws DataAccessException;
    Organizer selectOrganizerByUsername(String organizerName) throws DataAccessException;

}
