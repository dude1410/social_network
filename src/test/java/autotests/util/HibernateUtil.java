package autotests.util;

import javapro.model.Person;
import org.hibernate.Session;

import java.util.List;

public class HibernateUtil {
    public static Person getPersonByEmail(String email) {
        Session session = HibernateConnection.getSession();
        List<Person> personList = session.createQuery("from Person where e_mail = " + "'" + email + "'").getResultList();
        session.close();
        return personList.size() == 0 ? null : personList.get(0);
    }

}
