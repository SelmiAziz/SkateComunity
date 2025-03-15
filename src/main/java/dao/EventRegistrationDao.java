package dao;

import model.EventRegistration;

public interface EventRegistrationDao {
    EventRegistration selectEventRegistrationById(int id);
    void addEventRegistration(EventRegistration eventRegistration);
}
