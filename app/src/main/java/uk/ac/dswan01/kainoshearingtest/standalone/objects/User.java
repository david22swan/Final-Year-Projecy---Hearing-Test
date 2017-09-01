package uk.ac.dswan01.kainoshearingtest.standalone.objects;

/**
 * Import the necessary classes
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class User implements Serializable {

    /**
     * Variables to store the User details
     */
    private String _username;
    private String _password;
    private String _name;
    private String _email;
    private Date _dateOfBirth;

    /**
     * A enumerated value representing the sex of the user
     */
    private enum _genderEnum {
        MALE, FEMALE
    }
    private _genderEnum _gender;

    /**
     * An arraylist which stores all test results
     */
    private ArrayList<Test> _storedResults = new ArrayList<>();

    /**
     * The constructor class for the User object
     * @param _username - Sets the username
     * @param _password  - Sets the password
     * @param _name - Sets the actual name @param _email
     * @param _dateOfBirth - Sets the date of birth
     * @param _gender - Sets the gender
     */
    public User(String _username, String _password, String _name, String _email, Date _dateOfBirth, int _gender){

        this._username = _username;
        this._password = _password;
        this._name = _name;
        this._email = _email;
        this._dateOfBirth = _dateOfBirth;
        if (_gender == 1) {
            this._gender = _genderEnum.MALE;
        } else {
            this._gender = _genderEnum.FEMALE;
        }

    }

    //Get and Set Methods
    //User Method
    /**
     * A setter method for the entire, allows the updating of all variables simultaneously while leaving
     *  the _storedResults untouched.
     * @param _username - Sets the username
     * @param _password  - Sets the password
     * @param _name - Sets the actual name @param _email
     * @param _email - Sets the actual name @param _email
     * @param _dateOfBirth - Sets the date of birth
     * @param _gender - Sets the gender
     */
    public void setUser(String _username, String _password, String _name, String _email, Date _dateOfBirth, int _gender) {

        this._username = _username;
        this._password = _password;
        this._name = _name;
        this._email = _email;
        this._dateOfBirth = _dateOfBirth;
        if (_gender == 1) {
            this._gender = _genderEnum.MALE;
        } else {
            this._gender = _genderEnum.FEMALE;
        }

    }

    //Score Methods
    /**
     * Method to store the results of a test within the array list
     * @param _results - The test results to be added
     * @param _volumeLevel - The volume level of the test
     */
    public void addScore(int[] _results, int _volumeLevel) {
        _storedResults.add(new Test(_results, _volumeLevel));
    }

    /**
     * Method to remove a designated test from the array list
     * @param _index - Designates the test to be removed
     */
    public void removeScore(int _index) {
        _storedResults.remove(_index);
    }

    /**
     * Method to retrieve the array list
     * @return _storedResults
     */
    public ArrayList<Test> get_storedResults() {
        return _storedResults;
    }

    /**
     * Method to retrieve the amount of stored tests
     * @return (int) _storedResults.size()
     */
    public int getTestAmount() {
        return _storedResults.size();
    }

    //Username Methods
    /**
     * Method to return the username
     * @return _username
     */
    public String get_username() {
        return _username;
    }

    //Password Methods

    /**
     * Method to return the password
     * @return _password
     */
    public String get_password() {
        return _password;
    }

    //Name Methods
    /**
     * Method to return the name
     * @return _name
     */
    public String get_name() {
        return _name;
    }

    //Email
    /**
     * Method to return the email
     * @return _email
     */
    public String get_email() {
        return _email;
    }

    //Date of Birth
    /**
     * Method to return the Date of Birth
     * @return _dateOfBirth
     */
    public Date get_dateOfBirth() {
        return _dateOfBirth;
    }

    //Gender
    /**
     * Method to return an integer alue representing the gender
     * @return (int) gender representive
     */
    public int get_gender() {
        if (_gender == _genderEnum.MALE) {
            return 1;
        } else {
            return 2;
        }
    }

}
