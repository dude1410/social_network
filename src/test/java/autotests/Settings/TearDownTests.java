package autotests.Settings;

import autotests.util.HibernateConnection;
import org.testng.annotations.AfterSuite;

public class TearDownTests {

    @AfterSuite
    public void tearDown() {
        SetUpTests.driver.quit();
        HibernateConnection.closeSessionFactory();
    }
}
