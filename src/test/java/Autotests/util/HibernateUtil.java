package Autotests.util;

import JavaPRO.model.Person;
import java.util.List;
import org.hibernate.Session;

public class HibernateUtil {
    public static Person getPersonByEmail(String email) {
        Session session = HibernateConnection.getSession();
        List<Person> personList = session.createQuery("from Person " +
            "where e_mail = '" + email + "'").getResultList();
        session.close();
        return personList.size() == 0 ? null : personList.get(0);
    }

}
