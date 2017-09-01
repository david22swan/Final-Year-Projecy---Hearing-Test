package uk.ac.dswan01.kainoshearingtest.activities.login;

/**
 * Import the necessary classes
 */
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import uk.ac.dswan01.kainoshearingtest.R;
import uk.ac.dswan01.kainoshearingtest.activities.account.AccountActivity;
import uk.ac.dswan01.kainoshearingtest.activities.test.PreTestActivity;
import uk.ac.dswan01.kainoshearingtest.standalone.Global;
import uk.ac.dswan01.kainoshearingtest.standalone.storage.SharedPreferencesStorage;

public class MainActivity extends AppCompatActivity {
    //Text box variables declared
    private EditText _usernameEdit;
    private EditText _passwordEdit;
    //Global variable declared
    Global G;

    /**
     * The on create method for the main activity.
     * Sets content to the login_screen.
     * @param savedInstanceState - Instance of the application.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        //Global variable referenced
        G = (Global)getApplication();

        //Text boxes are referenced
        _usernameEdit = (EditText) findViewById(R.id.txtUsername);
        _passwordEdit = (EditText) findViewById(R.id.txtPassword);
        //Buttons are declared and referenced
        Button _loginButton = (Button) findViewById(R.id.btnLogin);
        Button _registerButton = (Button) findViewById(R.id.btnRegister);
        Button _forgotPasswordButton = (Button) findViewById(R.id.btnForgotPassword);
        Button _blindTestButton = (Button) findViewById(R.id.btnBlindTest);

        //Button methods
        /**
         * Login Button - Calls the load user method, and if it returns true, redirects the
         *  user to the Account activity.
         */
        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loadUser()) {
                    startActivity(new Intent(MainActivity.this, AccountActivity.class));
                    finish();
                } else {
                    _passwordEdit.setText("");
                }
            }
        });

        /**
         * Register Button - Redirects the user to the Register Activity
         */
        _registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                finish();
            }
        });

        /**
         * Forgot Password Button - Calls the sendEmail() method
         */
        _forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });

        /**
         * Blind Test Button - Redirects the user to the pre-test screen
         */
        _blindTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PreTestActivity.class));
                finish();
            }
        });
    }

    /**
     * loadUser() method
     * Tries to load the a profile associated with the entered username, and if successful then compares
     *  the profile's stored password with the entered password.
     * If both actions are successful returns true.
     * If one is unsuccessful, wipes loaded data and returns false.
     * @return true / false
     */
    public boolean loadUser() {
        try {
            G.setUser(SharedPreferencesStorage.readUser(this, _usernameEdit.getText().toString()));
            if (_passwordEdit.getText().toString().equals(G.getUser().get_password())) {
                return true;
            } else {
                G.deleteUser();
                Toast.makeText(MainActivity.this, R.string.lbl_loginError, Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, R.string.lbl_loginError, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * sendEmail() method
     * Attempts to send an email containing the profiles password if the given username exists.
     * If the username exists, searches for a matching application to send the email and then
     *  loads it with a pre-prepared intent.
     */
    public void sendEmail() {
        try {
            G.setUser(SharedPreferencesStorage.readUser(this, _usernameEdit.getText().toString()));

            if (G.getUser() == null) {
                Toast.makeText(MainActivity.this, R.string.lbl_forgotPasswordError, Toast.LENGTH_SHORT).show();
                return;
            }

            sendMail(G.getUser().get_email(), "Password Reminder", "Username: " +
                    G.getUser().get_username() + " - Password" + G.getUser().get_password());

            G.deleteUser();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, R.string.lbl_forgotPasswordError, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * sendMail() method
     * This method creates the session and message and then calls the execute message to send
     * @param email - The email address that is being sent to
     * @param subject - The subject of the email
     * @param messageBody - The main body of the email
     */
    private void sendMail(String email, String subject, String messageBody) {
        Session session = createSessionObject();
        try {
            Message message = createMessage(email, subject, messageBody, session);
            new SendMailTask().execute(message);
        } catch (UnsupportedEncodingException | MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * createMessage() Method
     * Creates the message that will be sent
     * @param email - The email that will recieve the message
     * @param subject - The subject heading of the email
     * @param messageBody - The message body of the email
     * @param session - The session used to connect with the GMail server
     * @return - The message to be sent is returned
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    private Message createMessage(String email, String subject, String messageBody, Session session) throws MessagingException, UnsupportedEncodingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("ds212923@gmail.com", "Hearing Test Application"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email, email));
        message.setSubject(subject);
        message.setText(messageBody);
        return message;
    }

    /**
     * createSession() method
     * Creates the session object used in order to access the GMail server
     * @return - The session that will send the email is returned
     */
    private Session createSessionObject() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        return Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("ds212923@gmail.com", "HearAppTest183");
            }
        });
    }

    /**
     * SendMailTask private class
     * Email is sent from a private class in order to prevent a runtime exception
     * This class handles the connection with the GMail servers
     */
    private class SendMailTask extends AsyncTask<Message, Void, Void> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MainActivity.this, "Please wait", "Sending mail", true, false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }

        @Override
        protected Void doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
