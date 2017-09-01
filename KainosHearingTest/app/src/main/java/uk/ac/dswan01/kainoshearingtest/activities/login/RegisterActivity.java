package uk.ac.dswan01.kainoshearingtest.activities.login;
/**
 * Imports the necessary classes
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
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

public class RegisterActivity extends AppCompatActivity {

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
    private String _password;
    private String _name;
    private String _email;
    private Date _dateOfBirth;
    //Int variable declared
    private int _gender;
    //Arraylist variable declared
    ArrayList<String> _usernameList;
    //Global variable declared
    Global G;

    /**
     * OnCreate method for the Register Activity.
     * Sets content to the register_screen.
     * @param savedInstanceState - Instance of the application.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_screen);

        //Global variable referenced
        G = (Global)getApplication();

        //Text boxes are referenced
        _usernameEdit = (EditText) findViewById(R.id.txtUsername);
        _passwordEdit = (EditText) findViewById(R.id.txtPassword);
        _nameEdit = (EditText) findViewById(R.id.txtName);
        _emailEdit = (EditText) findViewById(R.id.txtEmail);
        _dateEdit = (EditText) findViewById(R.id.txtDate);
        //Buttons are referenced
        Button _registerButton = (Button) findViewById(R.id.btnRegister);
        Button _cancelButton = (Button) findViewById(R.id.btnCancel);
        //Spinner is referenced
        _genderSpinner = (Spinner) findViewById(R.id.spnrGender);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        _genderSpinner.setAdapter(adapter);
        _genderSpinner.setSelection(0);

        //Button methods
        /**
         * Cancel Button - Redirects user to the Home Screen
         */
        _cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                finish();
            }
        });

        /**
         *Register Button - Calls the validate() method
         */
        _registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

    }

    /**
     * Validate Method - Compares the information entered into the text boxes and selected in the spinner
     *  against preset conditions, displaying an error message if any one of them is found to be invalid.
     * If all validation passes the create() method is called.
     */
    public void validate() {
        //Validate username
        String pattern = "^[a-zA-Z0-9]*$";
        _username = _usernameEdit.getText().toString();
        if (!(_username.matches(pattern)) || _username.isEmpty()) {
            Toast.makeText(RegisterActivity.this, R.string.lbl_usernameValidate, Toast.LENGTH_SHORT).show();
            return;
        }
        //Retrieves list of taken usernames to check if the selected username is taken
        try {
            _usernameList = SharedPreferencesStorage.readList(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (_usernameList != null) {
            for (int i = 0; i < _usernameList.size(); i++) {
                if (_usernameList.get(i).equals(_username)) {
                    Toast.makeText(RegisterActivity.this, R.string.lbl_usernameTaken, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }

        //Validate password
        _password = _passwordEdit.getText().toString();
        if (!(_password.matches(pattern)) || _password.isEmpty()) {
            Toast.makeText(RegisterActivity.this, R.string.lbl_passwordValidate, Toast.LENGTH_SHORT).show();
            return;
        }

        //Validate name
        pattern = "^[a-zA-Z0-9 ]*$";
        _name = _nameEdit.getText().toString();
        if (!(_name.matches(pattern)) || _name.isEmpty()) {
            Toast.makeText(RegisterActivity.this, R.string.lbl_nameValidate, Toast.LENGTH_SHORT).show();
            return;
        }

        //Validate email
        _email = _emailEdit.getText().toString();
        pattern = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern emailPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        CharSequence inputStr = _email;
        Matcher matcher = emailPattern.matcher(inputStr);
        if (TextUtils.isEmpty(_email) || !(matcher.matches())) {
            Toast.makeText(RegisterActivity.this, R.string.lbl_emailValidate, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(RegisterActivity.this, R.string.lbl_dobValidate, Toast.LENGTH_SHORT).show();
            return;
        }

        //Validate gender
        if (_genderSpinner.getSelectedItem().toString().equals("Male")) {
            _gender = 1;
        } else if (_genderSpinner.getSelectedItem().toString().equals("Female")) {
            _gender = 2;
        } else {
            Toast.makeText(RegisterActivity.this, R.string.lbl_genderValidate, Toast.LENGTH_SHORT).show();
            return;
        }
        create();
    }

    /**
     * Create Method - Code to create User variable and store it in the global variable. If variable
     *  is successfully created the saveUser() method is then called. If it is successful the user
     *  is redirected to the Home Screen. If it is not an error message is displayed. In either case
     *  the global variable is wiped.
     */
    public void create() {
        //Create class and validate it
        G.setUser(new User(_username, _password, _name, _email, _dateOfBirth, _gender));
        if (G.getUser() == null) {
            Toast.makeText(RegisterActivity.this, R.string.lbl_classError, Toast.LENGTH_SHORT).show();
            return;
        }

        //Validate that saved fle was created
        if (!saveUser()) {
            Toast.makeText(RegisterActivity.this, R.string.lbl_saveError, Toast.LENGTH_SHORT).show();
            G.deleteUser();
            return;
        }

        //If all validation checks passed and save file was created the user will be redirected to the login screen
        G.deleteUser();
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        finish();
    }

    //Code to call write method
    //Must be inside current activity and outside the 'on click' method

    /**
     * Save User Method - When called saves the user to SharedPreferences and updates the stored
     *  list with the username.
     * Returns true if successful and false if unsuccessful.
     *  @return - true / false
     */
    public boolean saveUser() {
        try {
            // Save the list of entries to internal storage
            SharedPreferencesStorage.addToList(this, G.getUser().get_username());
            SharedPreferencesStorage.writeUser(this, G.getUser());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
