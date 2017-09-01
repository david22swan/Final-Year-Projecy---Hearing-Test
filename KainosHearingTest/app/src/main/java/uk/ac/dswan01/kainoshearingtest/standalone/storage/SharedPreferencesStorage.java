package uk.ac.dswan01.kainoshearingtest.standalone.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import uk.ac.dswan01.kainoshearingtest.standalone.objects.User;

public class SharedPreferencesStorage {

    /**
     * Code to store a user object within Shared Preferences as a String
     * @param _context - The applications context
     * @param _user - The user object to be stored
     * @throws IOException
     */
    public static void writeUser(Context _context, User _user) throws IOException {

        Gson _gson = new Gson();
        Type _type = new TypeToken<User>() {}.getType();
        String _json = _gson.toJson(_user, _type);

        SharedPreferences mySPrefs = PreferenceManager.getDefaultSharedPreferences(_context);
        SharedPreferences.Editor editor = mySPrefs.edit();
        editor.putString(_user.get_username(), _json);
        editor.apply();

    }

    /**
     * Method to retrieve a user object stored in Json format from shared preferences
     * @param _context - The applications context
     * @param _username - Key representing the stored screen
     * @return - User object is returned
     * @throws IOException
     */
    public static User readUser(Context _context, String _username) throws IOException {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(_context);
        String _json = preferences.getString(_username, "error");

        if (_json.equals("error")) {
            return null;
        }

        Gson _gson = new Gson();
        Type _type = new TypeToken<User>() {}.getType();
        return _gson.fromJson(_json, _type);

    }

    /**
     * Deletes a user object stored in Json format from shared preferences
     * @param _context - Applications context
     * @param _username - Key representing the stored String
     */
    public static void deleteUser(Context _context, String _username) {

        SharedPreferences mySPrefs = PreferenceManager.getDefaultSharedPreferences(_context);
        SharedPreferences.Editor editor = mySPrefs.edit();
        editor.remove(_username);
        editor.apply();

    }

    /**
     * Retrieves and arraylist of all usernames from SharedPreferences
     * @param _context - Application context
     * @return - Returns an arraylist of usernames
     * @throws IOException
     */
    public static ArrayList<String> readList(Context _context) throws IOException {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(_context);
        String _json = preferences.getString("//UsernameList//", "error");

        if (_json.equals("error")) {
            return null;
        }

        Gson _gson = new Gson();
        Type _type = new TypeToken<ArrayList<String>>() {}.getType();

        return _gson.fromJson(_json, _type);

    }

    /**
     * Code to retrieve stored username arraylist, initialising a new one if it does not exist, before
     *  adding the new username and passing the updated arrayList to the updateList method.
     * @param _context - Applications context
     * @param _username - The username to add to the list
     * @throws IOException
     */
    public static void addToList(Context _context, String _username) throws IOException {

        ArrayList<String> _usernameList = readList(_context);
        if (_usernameList == null) {
            _usernameList = new ArrayList<String>();
        }
        _usernameList.add(_username);

        updateList(_context, _usernameList);

    }

    /**
     * Code to retrieve stored username list, remove indicated entry and then pass the updated,
     *  arrayList to the updateList method.
     * @param _context - Applications context
     * @param _username - Username to remove from stored list
     * @throws IOException
     */
    public static void removeFromList(Context _context, String _username) throws IOException {

        ArrayList<String> _usernameList = readList(_context);
        if (_usernameList == null) {
            return;
        }
        _usernameList.remove(_username);

        updateList(_context, _usernameList);

    }

    /**
     * Code to store given ArrayList in Shared Preferences
     * @param _context - Application context
     * @param _usernameList - arrayList to store
     * @throws IOException
     */
    public static void updateList(Context _context, ArrayList<String> _usernameList) throws IOException {

        Gson _gson = new Gson();
        Type _type = new TypeToken<ArrayList<String>>() {}.getType();
        String _json = _gson.toJson(_usernameList, _type);

        SharedPreferences mySPrefs = PreferenceManager.getDefaultSharedPreferences(_context);
        SharedPreferences.Editor editor = mySPrefs.edit();
        editor.putString("//UsernameList//", _json);
        editor.apply();

    }

}
