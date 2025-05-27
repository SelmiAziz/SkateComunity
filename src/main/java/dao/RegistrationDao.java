package dao;

import exceptions.DataAccessException;
import model.Registration;


public interface RegistrationDao {
    Registration selectRegistrationById(int id) throws DataAccessException;
    void addRegistration(Registration competitionRegistration) throws DataAccessException;
}
