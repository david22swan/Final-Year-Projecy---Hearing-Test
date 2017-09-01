package uk.ac.dswan01.kainoshearingtest.activities.account;

/**
 * Imports the necessary classes
 */
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import uk.ac.dswan01.kainoshearingtest.R;
import uk.ac.dswan01.kainoshearingtest.activities.login.MainActivity;
import uk.ac.dswan01.kainoshearingtest.activities.test.PreTestActivity;
import uk.ac.dswan01.kainoshearingtest.activities.results.StoredResultsActivity;
import uk.ac.dswan01.kainoshearingtest.standalone.Global;

public class AccountActivity extends AppCompatActivity {

    //Global variable declared
    Global G;

    /**
     * OnCreate method for the Account Activity.
     * Sets content to the account_screen.
     * @param savedInstanceState - Instance of the application.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_screen);

        //Global variable referenced
        G = (Global)getApplication();

        //Button variables are called and referenced
        Button _startTestButton = (Button) findViewById(R.id.btnStartTest);
        Button _savedTestsButton = (Button) findViewById(R.id.btnSavedResults);
        Button _accountInfoButton = (Button) findViewById(R.id.btnAccountInfo);
        Button _logoutButton = (Button) findViewById(R.id.btnLogOut);

        //Sets the labels which display basic user information
        String placeholder;
        TextView actualName = (TextView)findViewById(R.id.lblName);
        placeholder = ((getResources().getString(R.string.lbl__nameStub)) + " " + (G.getUser().get_name()));
        actualName.setText(placeholder);

        TextView username = (TextView)findViewById(R.id.lblUsername);
        placeholder = ((getResources().getString(R.string.lbl__usernameStub)) + " " + (G.getUser().get_username()));
        username.setText(placeholder);

        TextView email = (TextView)findViewById(R.id.lblEmail);
        email.setText((G.getUser().get_email()));

        //Button methods
        /**
         * Start Test Button - Redirects the user to the pre-test screen
         */
        _startTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountActivity.this, PreTestActivity.class));
                finish();
            }
        });

        /**
         * Saved Results Button - Redirects the user to the saved result screen on the condition
         *  that there are results stored.
         * If there are no stored results displays an error message instead.
         */
        _savedTestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (G.getUser().getTestAmount() == 0) {
                    Toast.makeText(AccountActivity.this, R.string.lbl_noResults, Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(AccountActivity.this, StoredResultsActivity.class));
                    finish();
                }
            }
        });

        /**
         * Account Information Button - Redirects the user to the account info screen
         */
        _accountInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountActivity.this, AccountInfoActivity.class));
                finish();
            }
        });

        /**
         * Logout Button - Sets the global variable User to null and redirects the user to the login screen
         */
        _logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                G.deleteUser();
                startActivity(new Intent(AccountActivity.this, MainActivity.class));
                finish();
            }
        });

    }
}
