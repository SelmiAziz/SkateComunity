package dao.patternabstractfactory;

import dao.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public abstract class DaoFactory {

    public abstract WalletDao createWalletDao();

    public abstract UserDao createUserDao();

    public abstract CustomerDao createCustomerDao();

    public abstract OrganizerDao createOrganizerDao();

    public abstract CompetitionDao createCompetitionDao();

    public abstract RegistrationDao createRegistrationDao();

    public abstract TrickDao createTrickDao();

    public abstract BoardDao createBoardDao();

    public abstract OrderDao createCustomOrderDao();

    public abstract ProgressNoteDao createProgressNoteDao();

    public abstract DeliveryDestinationDao createDeliveryDestinationDao();


    private static DaoFactory instance;
    private static final String CONFIGHPATH= "src/main/resources/config.properties";

        public static synchronized DaoFactory getInstance()  {
            if (instance == null) {
                Properties props = new Properties();
                try (FileInputStream fis = new FileInputStream(CONFIGHPATH)) {
                    props.load(fis);
                } catch (IOException _) {
                    //exception not implemented
                }

                String mode = props.getProperty("mode");


                switch (mode.toLowerCase()) {
                    case "dbms":
                        instance = new DaoDbmsFactory();
                        break;
                    case "filesystem":
                        instance = new DaoFileSystemFactory();
                        break;
                    case "demo":
                        instance = new DaoDemoFactory();
                        break;
                    default:
                        instance = new DaoDemoFactory();

                }
            }
            return instance;

        }
}


