package uk.ac.dswan01.kainoshearingtest.activities.account;

/**
 * Imports the necessary classes
 */
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.ac.dswan01.kainoshearingtest.R;
import uk.ac.dswan01.kainoshearingtest.standalone.Global;
import uk.ac.dswan01.kainoshearingtest.standalone.storage.SharedPreferencesStorage;
import uk.ac.dswan01.kainoshearingtest.standalone.objects.User;

public class AccountInfoActivity extends AppCompatActivity {

    //Text box variables declared
    private EditText _usernameEdit;
    private EditText _passwordEdit;
    private EditText _nameEdit;
    private EditText _emailEdit;
    private EditText _dateEdit;
    //Spinner Variable declared
    private Spinner _genderSpinner;
    //String variables declared
    private String _username;
    private String _usernameOld;
    private String _password;
    private String _name;
    private String _email;
    //Date variable declared
    private Date _dateOfBirth;
    //Integer variable declared
    private int _gender;
    //Arraylist variable declared
    ArrayList<String> _usernameList;
    //Global variable declared
    Global G;

    /**
     * OnCreate method for the Account Information Activity.
     * Sets content to the account_info_screen.
     * @param savedInstanceState - Instance of the application.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_info_screen);

        //Global variable referenced
        G = (Global)getApplication();

        //Text boxes are referenced
        _usernameEdit = (EditText) findViewById(R.id.txtUsername);
        _passwordEdit = (EditText) findViewById(R.id.txtPassword);
        _nameEdit = (EditText) findViewById(R.id.txtName);
        _emailEdit = (EditText) findViewById(R.id.txtEmail);
        _dateEdit = (EditText) findViewById(R.id.txtDate);
        //Buttons are referenced - Text for Register is changed to update
        Button _updateButton = (Button) findViewById(R.id.btnUpdate);
        Button _cancelButton = (Button) findViewById(R.id.btnCancel);

        //Spinner is referenced and populated
        _genderSpinner = (Spinner) findViewById(R.id.spnrGender);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        _genderSpinner.setAdapter(adapter);

        //Current user data is loaded
        loadUserData();

        //Button methods
        /**
         * Cancel Button - Redirects the user to the Account Screen
         */
        _cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountInfoActivity.this, AccountActivity.class));
                finish();
            }
        });

        /**
         * Update button - Calls the validate() method.
         */
        _updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

    }

    /**
     * Load User Data Method - When called populates the various Text Boxes and the Spinner with
     *  the information stored in the global variable.
     */
    public void loadUserData() {
        //Populate display with the original data
        _usernameEdit.setText(G.getUser().get_username());
        _nameEdit.setText(G.getUser().get_name());
        _emailEdit.setText(G.getUser().get_email());
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        _dateEdit.setText(df.format(G.getUser().get_dateOfBirth()));
        _genderSpinner.setSelection(G.getUser().get_gender());
    }

    /**
     * Validate Method - Compares the information entered into the text boxes and selected in the spinner
     *  against preset conditions, displaying an error message if any one of them is found to be invalid.
     * If all validation passes the update() method is called.
     */
    public void validate() {
        //Validate username
        String pattern = "^[a-zA-Z0-9]*$";
        _username = _usernameEdit.getText().toString();
        if (!(_username.matches(pattern)) || _username.isEmpty()) {
            Toast.makeText(AccountInfoActivity.this, R.string.lbl_usernameValidate, Toast.LENGTH_SHORT).show();
            return;
        }
        //If the username is being changed, checks whether the new one has already been taken
        if (!(_username.equals(G.getUser().get_username()))) {
            try {
                _usernameList = SharedPreferencesStorage.readList(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (_usernameList != null) {
                for (int i = 0; i < _usernameList.size(); i++) {
                    if (_usernameList.get(i).equals(_username)) {
                        Toast.makeText(AccountInfoActivity.this, R.string.lbl_usernameTaken, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        }

        //Validate password
        _password = _passwordEdit.getText().toString();
        if (!(_password.matches(pattern)) || _password.isEmpty()) {
            Toast.makeText(AccountInfoActivity.this, R.string.lbl_passwordValidate, Toast.LENGTH_SHORT).show();
            return;
        }

        //Validate name
        pattern = "^[a-zA-Z0-9 ]*$";
        _name = _nameEdit.getText().toString();
        if (!(_name.matches(pattern)) || _name.isEmpty()) {
            Toast.makeText(AccountInfoActivity.this, R.string.lbl_nameValidate, Toast.LENGTH_SHORT).show();
            return;
        }

        //Validate email
        _email = _emailEdit.getText().toString();
        pattern = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern emailPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        CharSequence inputStr = _email;
        Matcher matcher = emailPattern.matcher(inputStr);
        if (TextUtils.isEmpty(_email) || !(matcher.matches())) {
            Toast.makeText(AccountInfoActivity.this, R.string.lbl_emailValidate, Toast.LENGTH_SHORT).show();
            return;
        }

        //Validate date of birth
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        try {
            _dateOfBirth = sdf.parse(_dateEdit.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        if (_dateOfBirth == null) {
            Toast.makeText(AccountInfoActivity.this, R.string.lbl_dobValidate, Toast.LENGTH_SHORT).show();
            return;
        }

        //Validate gender
        if (_genderSpinner.getSelectedItem().toString().equals("Male")) {
            _gender = 1;
        } else if (_genderSpinner.getSelectedItem().toString().equals("Female")) {
            _gender = 2;
        } else {
            Toast.makeText(AccountInfoActivity.this, R.string.lbl_genderValidate, Toast.LENGTH_SHORT).show();
            return;
        }
        update();
    }

    /**
     * Update Method - Code to update the user object stored in the global variable. The saveUser()
     *  method is then called, if it is successful the user is redirect to the Account Screen. If it
     *  is unsuccessful and error message is displayed and the original user files are reloaded.
     */
    public void update() {

        //Original username is stored, just in case
        _usernameOld = G.getUser().get_username();

        //Update User
        G.getUser().setUser(_username, _password, _name, _email, _dateOfBirth, _gender);

        //Validate that saved fle was created, if it wasn't the original user file is reloaded
        if (!saveUser()) {
            Toast.makeText(AccountInfoActivity.this, R.string.lbl_saveError, Toast.LENGTH_SHORT).show();
            loadUser();
            return;
        }

        //If all validation checks passed and save file was created the user will be redirected to the account screen
        startActivity(new Intent(AccountInfoActivity.this, AccountActivity.class));
        finish();
    }

    /**
     * Save User Method - When called saves the user to SharedPreferences and updates the stored
     *  list with the username. If the username has been changed, removes the old username from the
     *  list and deletes the duplicate User file this has created.
     * Returns true if successful and false if unsuccessful.
     *  @return - true / false
     */
    public boolean saveUser() {
        try {
            // Save the list of entries to internal storage
            SharedPreferencesStorage.writeUser(this, G.getUser());
            //If username was updated, removes old one from list and adds new one while removing duplicated user profile
            if (_usernameOld != null) {
                SharedPreferencesStorage.addToList(this, G.getUser().get_username());
                SharedPreferencesStorage.removeFromList(this, _usernameOld);
                SharedPreferencesStorage.deleteUser(this, _usernameOld);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * loadUser() method
     * Tries to restore the original User files.
     * If action is successful returns true.
     * If one is unsuccessful displays error message.
     */
    public void loadUser() {
        try {
            G.setUser(SharedPreferencesStorage.readUser(this, _usernameOld));
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(AccountInfoActivity.this, R.string.lbl_reloadError, Toast.LENGTH_SHORT).show();
        }
    }

}
