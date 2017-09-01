package uk.ac.dswan01.kainoshearingtest.activities.test;

/**
 * Imports the necessary classes
 */
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import uk.ac.dswan01.kainoshearingtest.R;
import uk.ac.dswan01.kainoshearingtest.activities.login.MainActivity;
import uk.ac.dswan01.kainoshearingtest.activities.account.AccountActivity;
import uk.ac.dswan01.kainoshearingtest.standalone.Global;

public class PreTestActivity extends AppCompatActivity {
    //Button variables declared
    Button _testVolumeButton;
    Button _beginTestButton;
    Button _cancelButton;
    //Check box variables declared
    private CheckBox _volumeCheck;
    private CheckBox _headphoneCheck;
    //Boolean variables declared
    private boolean _volumeSet;
    private boolean _headphoneSet;
    //Media Player variable declared
    MediaPlayer _mediaPlayer;
    //Global variable declared
    Global G;

    /**
     * On create method for the Pre-Test Activity
     * Sets content to the pre_test_screen
     * @param savedInstanceState - Instance of the application.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pretest_screen);

        //Global variable referenced
        G = (Global)getApplication();

        //Buttons are defined and referenced
        _testVolumeButton = (Button) findViewById(R.id.btnTestVolume);
        _beginTestButton = (Button) findViewById(R.id.btnStartTest);
        _cancelButton = (Button) findViewById(R.id.btnCancel);

        //Check boxes are referenced
        _volumeCheck = (CheckBox) findViewById(R.id.chkVolume);
        _headphoneCheck = (CheckBox) findViewById(R.id.chkHeadphones);


        //Checkbox methods
        /**
         * Volume check method - Sets '_volumeSet' to true when checked and false when unchecked
         */
        _volumeCheck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                _volumeSet = _volumeCheck.isChecked();
            }
        });

        /**
         * Headphone check method - Sets 'headphoneSet' to true when checked and false when unchecked
         */
        _headphoneCheck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                _headphoneSet = _headphoneCheck.isChecked();
            }
        });

        //Button methods
        /**
         * Test _volumeLbl Button - Plays a set test _volumeLbl of 1000hz at the current sound level
         */
        _testVolumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mediaPlayer = MediaPlayer.create(getApplicationContext() ,R.raw.sound1000hz);
                _mediaPlayer.start();
            }
        });

        /**
         * Begin test Button - Redirects the user to the test screen if both booleans are set to true,
         *  displays differing error message if either is false.
         */
        _beginTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_volumeSet) {
                    if (_headphoneSet) {
                        startActivity(new Intent(PreTestActivity.this, TestActivity.class));
                        finish();
                    } else {
                        Toast.makeText(PreTestActivity.this, R.string.tst_checkheadphones, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PreTestActivity.this, R.string.tst_checkvolume, Toast.LENGTH_SHORT).show();
                }
            }
        });

        /**
         * Cancel Button - Redirects user to either the home page or account page depending
         *  if they are logged in or not.
         */
        _cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (G.getUser() == null){
                    startActivity(new Intent(PreTestActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(PreTestActivity.this, AccountActivity.class));
                }
                finish();
            }
        });

    }

}
