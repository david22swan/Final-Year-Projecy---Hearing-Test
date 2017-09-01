package uk.ac.dswan01.kainoshearingtest.activities.test;

/**
 * Necessary classes are imported
 */
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import java.util.Random;
import android.os.Handler;
import android.widget.Toast;

import uk.ac.dswan01.kainoshearingtest.R;
import uk.ac.dswan01.kainoshearingtest.activities.login.MainActivity;
import uk.ac.dswan01.kainoshearingtest.activities.results.ResultsActivity;
import uk.ac.dswan01.kainoshearingtest.activities.account.AccountActivity;
import uk.ac.dswan01.kainoshearingtest.standalone.Global;

public class TestActivity extends AppCompatActivity {

    //Array variables declared
    private int[] _results = new int[8];
    private int[] _soundFiles;
    //Sound Pool declared
    SoundPool _soundPool;
    //Audio manager declared
    AudioManager _manager;
    //Boolean variables declared
    volatile boolean _soundPlaying = false;
    volatile boolean _soundHeard = false;
    volatile boolean _cancelled = true;
    //Integer variables declared
    int _count = 0;
    int i = 0;
    int j = 0;
    int _delay;
    int _volume;
    //Float variable declared
    float _soundLevel;
    //Handler declared
    Handler _handler = new Handler();
    //Random Generator declared
    Random _randomGenerator = new Random();
    //Global variable declared
    Global G;

    /**
     * OnCreate method for the TestActivity.
     * Sets content to the test_screen.
     * @param savedInstanceState - Instance of the application.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_screen);

        //Global variable referenced
        G = (Global)getApplication();

        //Original sound level found and recorded
        _manager = (AudioManager) getSystemService(AUDIO_SERVICE);
        _volume = _manager.getStreamVolume(AudioManager.STREAM_MUSIC);

        //Buttons are declared and referenced
        Button _soundButton = (Button) findViewById(R.id.btnSound);
        Button _startTestButton = (Button) findViewById(R.id.btnStart);
        Button _cancelButton = (Button) findViewById(R.id.btnCancel);

        //Sound pool initialised
        //Each seperate sound file is loaded
        _soundFiles = new int[8];
        _soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        _soundFiles[0] = _soundPool.load(this, R.raw.sound125hz, 1);
        _soundFiles[1] = _soundPool.load(this, R.raw.sound250hz, 1);
        _soundFiles[2] = _soundPool.load(this, R.raw.sound500hz, 1);
        _soundFiles[3] = _soundPool.load(this, R.raw.sound1000hz, 1);
        _soundFiles[4] = _soundPool.load(this, R.raw.sound2000hz, 1);
        _soundFiles[5] = _soundPool.load(this, R.raw.sound4000hz, 1);
        _soundFiles[6] = _soundPool.load(this, R.raw.sound6000hz, 1);
        _soundFiles[7] = _soundPool.load(this, R.raw.sound8000hz, 1);

        //Button Methods
        /**
         * Cancel Button - Sets _cancelled to true, calls stop sound and then redirects user to either the home page,
         *  either the home page or account page depending if they are logged in or not.
         */
        _cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _cancelled = true;
                stopSound();
                if (G.getUser() == null) {
                    startActivity(new Intent(TestActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(TestActivity.this, AccountActivity.class));
                }
                finish();
            }
        });

        /**
         * Sound Button - If clicked while _soundPlaying is true and _cancelled is false,
         *  sets _SoundHeard to true.
         */
        _soundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_soundPlaying && !_cancelled) {
                    _soundHeard = true;
                }
            }
        });

        /**
         * Start Test Button - When clicked starts the recursive loop which handles the test and sets
         *  _cancelled to false.
         */
        _startTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_cancelled) {
                    Toast.makeText(TestActivity.this, R.string.lbl_testintruction, Toast.LENGTH_SHORT).show();
                    _cancelled = false;
                    testStart();
                }
            }
        });
    }

    //Test Methods
    /**
     * Test Start Method - This is the first half of the recursive loop that handles the test.
     * When called this method sets a random delay between 1 and 7 seconds before setting the
     *  sound playing variable to true and playing the sound.
     * The second half of the recursive test loop is then called.
     * If _cancelled is true, immediately calls return.
     */
    public void testStart() {
        if(_cancelled) return;

        _delay = _randomGenerator.nextInt(6) + 1;
        _handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Below code resets the _volumeLbl to the designated level
                _manager.setStreamVolume(AudioManager.STREAM_MUSIC, _volume, 0);
                _soundPlaying = true;
                //Code to calculate sound level
                _soundLevel = (float)(1 - (0.2 * j));
                //Code to play the sound file
                _soundPool.play(_soundFiles[i], _soundLevel, _soundLevel, 0, 0, 1);
                testContinued();
            }
        }, (1000 * _delay));

    }

    /**
     * Test Continue Method - This is the second half of the recursive loop that handles the test.
     * When called this method will set a 3 second delay before cancelling the sound and setting
     *  the Sound Playing variable back to false.
     * The method then checks the Sound Hear variable and then either sets it to heard in the results
     *  and moves onto the next sound, repeats the sound if missed for the first time and skips
     *  to the next hz if the user has missed the same sound twice.
     * If _cancelled is true, immediately calls return.
     */
    public void testContinued() {
        _handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(_cancelled) return;

                //Wait 3 seconds
                //cancel sound (just in case)
                stopSound();

                //Set the results
                if (_soundHeard) {
                        _results[i] = (j + 1);
                        _count = 0;
                        _soundHeard = false;
                } else if (!_soundHeard && _count != 1) {
                    _count += 1;
                    j -= 1;
                } else {
                    j = 5;
                    _count = 0;
                }
                //Set what happens next
                j += 1;
                if (j >= 5) {
                    i += 1;
                    j = 0;
                }
                if (i >= 8) {
                    testFinished();
                } else {
                    testStart();
                }
            }
        }, (3000));
    }

    /**
     * Test Finish Method - This is the final test method. It bundles the _results array and
     *  saved _volume level then passes it into the Result Screen and closes the Test Activity.
     */
    public void testFinished() {
        //Code to pass results to Result Screen
        Bundle b = new Bundle();
        b.putIntArray("array", _results);
        b.putInt("_volumeLbl", _volume);
        startActivity(new Intent(TestActivity.this, ResultsActivity.class).putExtras(b));
        finish();
    }

    /**
     * Stop Sound Method - If called when _soundPlaying is true, stops the current sound file and
     *  sets _soundPlaying to false.
     */
    private void stopSound(){
        if (_soundPlaying){
            _soundPool.stop(_soundFiles[i]);
            _soundPlaying = false;
        }

    }
}
