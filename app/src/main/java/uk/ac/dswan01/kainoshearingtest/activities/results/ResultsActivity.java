package uk.ac.dswan01.kainoshearingtest.activities.results;

/**
 * Imports the necessary classes
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.IOException;

import uk.ac.dswan01.kainoshearingtest.R;
import uk.ac.dswan01.kainoshearingtest.activities.login.MainActivity;
import uk.ac.dswan01.kainoshearingtest.activities.account.AccountActivity;
import uk.ac.dswan01.kainoshearingtest.standalone.Global;
import uk.ac.dswan01.kainoshearingtest.standalone.storage.SharedPreferencesStorage;

public class ResultsActivity extends AppCompatActivity {
    //Array variable declared
    int[] _results;
    //Int variable declared
    int _volume;
    TextView _volumeLbl;
    //Global variable declared
    Global G;

    /**
     * OnCreate method for the Results Activity.
     * Sets content to the results_screen.
     * @param savedInstanceState - Instance of the application.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_screen);

        //Global variable referenced
        G = (Global)getApplication();

        //Declare and reference button
        Button _finishButton = (Button) findViewById(R.id.btnFinish);

        //Reference _volumeLbl label
        _volumeLbl = (TextView)findViewById(R.id.lblVolumeMessage);

        //Code to retrieve bundled _results and _volume
        Bundle b=this.getIntent().getExtras();
        _results = b.getIntArray("array");
        _volume = b.getInt("_volumeLbl");

        /**
         * Code to check is user is logged in, adding the score to the global variable if they are.
         * The saveUser() method is then called, an error message being displayed if it returns
         *  false, i.e. if it did not work.
         */
        if ((!G.getUser().get_username().isEmpty())){
            G.getUser().addScore(_results, _volume);
            if (!saveUser()) {
                Toast.makeText(ResultsActivity.this, R.string.lbl_updateError, Toast.LENGTH_SHORT).show();
            }
        }

        //Code to populate graph
        populateGraph();

        /**
         * Finish Button - When called redirects the user to either the Home Screen or Account
         *  Screen depending if they are logged in or not.
         */
        _finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (G.getUser() == null) {
                    startActivity(new Intent(ResultsActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(ResultsActivity.this, AccountActivity.class));
                }
                finish();
            }
        });

    }

    /**
     * Save User Method - Calls the writeUser() method, passing in the global User variable. Returns
     *  true is successful and false if unsuccessful.
     * Must be inside current activity and outside the 'on click' method.
     * @return true / false
     */
    public boolean saveUser() {
        try {
            // Save the list of entries to internal storage
            SharedPreferencesStorage.writeUser(this, G.getUser());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Populate Graph Method - Creates and populates the graph with the stored _results.
     * Also sets _volumeLbl to display the volume level.
     */
    public void populateGraph() {
        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, _results[0]),
                new DataPoint(1, _results[1]),
                new DataPoint(2, _results[2]),
                new DataPoint(3, _results[3]),
                new DataPoint(4, _results[4]),
                new DataPoint(5, _results[5]),
                new DataPoint(6, _results[6]),
                new DataPoint(7, _results[7])
        });
        //Formats line thickness
        series.setThickness(10);
        //Remove all previous series and adds the new ones
        graph.removeAllSeries();
        graph.addSeries(series);
        //Set manual y bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(5);
        //Set manual x bounds
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMaxX(0);
        graph.getViewport().setMaxX(7);
        //Set Label size
        graph.getGridLabelRenderer().setTextSize(30f);
        graph.getGridLabelRenderer().reloadStyles();
        //Add manual labels
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(getResources().getStringArray(R.array.horizontalAxis_array));
        staticLabelsFormatter.setVerticalLabels(getResources().getStringArray(R.array.verticalAxis_array));
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        //Sets Volume label
        _volumeLbl.setText(R.string.lbl_volume);
        _volumeLbl.append(String.valueOf(_volume));
    }

}
