package CompetitionTest;

import model.Competition;
import model.Registration;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class CompetitionTest {
    @Test
    void testAddCompetitionRegistration(){
        Competition competition = new Competition();
        Registration registration = new Registration();
        competition.addCompetitionRegistration(registration);
        assertEquals(1,competition.getRegistrationsNumber());
    }

}
