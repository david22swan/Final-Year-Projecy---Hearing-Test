package uk.ac.dswan01.kainoshearingtest.standalone;

import android.app.Application;

import uk.ac.dswan01.kainoshearingtest.standalone.objects.User;

public class Global extends Application {

    /**
     * Stores an instance of the class _user
     */
    private User _user = null;

    /**
     * Method to set the global variable _user
     * @param _user - value is stored in global variable
     */
    public void setUser(User _user) {
        this._user = _user;
    }

    /**
     * Method to return the stored global class
     * @return _user
     */
    public User getUser() {
        return this._user;
    }

    /**
     * Method to delete the stored global variable if it exists
     */
    public void deleteUser() {
        if (_user != null) {
            this._user = null;
        }
    }

}
