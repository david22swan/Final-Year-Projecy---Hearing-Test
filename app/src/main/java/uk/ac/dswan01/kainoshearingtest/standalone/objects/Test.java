package uk.ac.dswan01.kainoshearingtest.standalone.objects;

/**
 * Import the necessary classes
 */
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Test implements Serializable {

    /**
     * Variables to store the test details
     */
    private Date _dateOfTest;
    private int[] _storedResults;
    private int _volumeLevel;

    /**
     * The constructor class for the Test object
     * The volume level is set automatically
     * @param _results - Sets the results array
     * @param _volumeLevel - Sets the volume level
     */
    public Test(int[] _results, int _volumeLevel) {
        this._storedResults = _results;
        this._dateOfTest = Calendar.getInstance().getTime();
        this._volumeLevel = _volumeLevel;
    }

    //Get/Set Methods
    //Score
    /**
     * Method to return the array of stored results
     * @return _storedResults
     */
    public int[] get_score() {
        return _storedResults;
    }

    //Date
    /**
     * Method to return the Date in SimpleDateFormat format
     * @return (SimpleDateFormat) _dateOfTest
     */
    public String get_Date() {
        SimpleDateFormat _sdf = new SimpleDateFormat("yyyy/MM/dd");
        return _sdf.format(_dateOfTest);
    }

    /**
     * Method to return the date
     * @return _dateOFTest
     */
    public Date get_dateOfTest() {
        return _dateOfTest;
    }

    //Volume Level
    /**
     * Method to return the volume level
     * @return _volumeLEvel
     */
    public int get_volumeLevel() {
        return _volumeLevel;
    }

}
