package uk.ac.dswan01.kainoshearingtest;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import uk.ac.dswan01.kainoshearingtest.standalone.Global;
import uk.ac.dswan01.kainoshearingtest.standalone.objects.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AccountTest {

    Global G = new Global();
    Date date;

    @Test
    public void addDeleteUser() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        date = null;

        try {
            date = sdf.parse("1992/10/22");
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        G.setUser(new User("Username1", "Password1", "Name1", "Email1", date, 1));

        assertEquals("Username1", G.getUser().get_username());
        assertEquals("Password1", G.getUser().get_password());
        assertEquals("Name1", G.getUser().get_name());
        assertEquals("Email1", G.getUser().get_email());
        assertEquals(date, G.getUser().get_dateOfBirth());
        assertEquals(1, G.getUser().get_gender());

        G.deleteUser();

        assertNull(G.getUser());

    }

    @Test
    public void addDeleteTest() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        date = null;

        try {
            date = sdf.parse("1992/10/22");
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        G.setUser(new User("Username1", "Password1", "Name1", "Email1", date, 1));

        int[] array = new int[]{0,1,2,3,4,5,6,7};
        date = Calendar.getInstance().getTime();

        G.getUser().addScore(array, 4);
//Can pass or fail depending on the time it takes to run the test (inaccurate)
//        assertEquals(G.getUser().get_storedResults().get(0).get_dateOfTest(), date);
        assertEquals(G.getUser().get_storedResults().get(0).get_volumeLevel(), 4);
        assertEquals(G.getUser().get_storedResults().get(0).get_score(), array);

        array = new int[]{8,9,10,11,12,13,14,15};
        date = Calendar.getInstance().getTime();

        G.getUser().addScore(array, 2);

        G.getUser().removeScore(0);

//Can pass or fail depending on the time it takes to run the test (inaccurate)
//        assertEquals(G.getUser().get_storedResults().get(0).get_dateOfTest(), date);
        assertEquals(G.getUser().get_storedResults().get(0).get_volumeLevel(), 2);
        assertEquals(G.getUser().get_storedResults().get(0).get_score(), array);

    }


}
