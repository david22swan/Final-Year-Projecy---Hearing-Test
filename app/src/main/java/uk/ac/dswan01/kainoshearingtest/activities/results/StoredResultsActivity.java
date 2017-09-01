package uk.ac.dswan01.kainoshearingtest.activities.results;
/**
 * Imports the necessary classes
 */
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.IOException;
import java.util.ArrayList;

import uk.ac.dswan01.kainoshearingtest.R;
import uk.ac.dswan01.kainoshearingtest.activities.account.AccountActivity;
import uk.ac.dswan01.kainoshearingtest.standalone.Global;
import uk.ac.dswan01.kainoshearingtest.standalone.storage.SharedPreferencesStorage;

public class StoredResultsActivity extends AppCompatActivity {

    //Global variable declared
    Global G;
    //Graph variable declared
    GraphView graph;
    //Array variable declared
    int[] _results;
    //Array List variable declared
    private ArrayList<String> _list;
    //Spinner Variable declared
    private Spinner _resultsSpinner;
    //Textview variable declared
    TextView _volumeLbl;
    //Int variable declared
    int _volumeLevel;

    /**
     * OnCreate method for the Stored Results Activity.
     * Sets content to the stored_results_screen.
     * @param savedInstanceState - Instance of the application.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stored_results_screen);

        //Global variable referenced
        G = (Global)getApplication();

        //References the Array List which populates the spinner
        _list = new ArrayList<>();

        //Spinner is referenced and populated
        _resultsSpinner = (Spinner) findViewById(R.id.spnrTests);
        populateList();

        //References the label which displays the recorded _volumeLbl level
        _volumeLbl = (TextView)findViewById(R.id.lblVolumeMessage);

        //Code to call and reference graph
        graph = (GraphView) findViewById(R.id.graph);
        setResults();

        //Button variables are called and referenced
        Button _compareButton = (Button) findViewById(R.id.btnCompare);
        Button _deleteButton = (Button) findViewById(R.id.btnDelete);
        Button _backButton = (Button) findViewById(R.id.btnBack);

        /**
         * Compare Button - Bundles an int value representing the results to compare and then
         *  redirects the user to the Compare Results Screen.
         */
        _compareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Code to pass results to compare to the Compare Screen
                Bundle b = new Bundle();
                b.putInt("number", _resultsSpinner.getSelectedItemPosition());
                startActivity(new Intent(StoredResultsActivity.this, CompareResultsActivity.class).putExtras(b));
                finish();
            }
        });

        /**
         * Delete Button - Deletes the currently selected result, then calls the saveUser(),
         *  setResults() and populateList() methods.
         * If no more stored results exist, redirects the user to the Account screen.
         */
        _deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                G.getUser().removeScore(_resultsSpinner.getSelectedItemPosition());
                saveUser();
                if (G.getUser().getTestAmount() == 0) {
                    startActivity(new Intent(StoredResultsActivity.this, AccountActivity.class));
                    finish();
                    return;
                }
                populateList();
                setResults();
            }
        });

        /**
         * Back Button - redirects user back to account screen.
         */
        _backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StoredResultsActivity.this, AccountActivity.class));
                finish();
            }
        });

        //Spinner method
        /**
         * Results Spinner - Calls the setResults() method whenever the spinner selection is
         *  changed.
         */
        _resultsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                setResults();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //Empty method
            }
        });

    }

    /**
     * Populate List Method - Method to create a list containing the dates of all stored results and
     *  then populate the spinner with it. The default selection is then set.
     */
    public void populateList() {
        for (int i = 0; i < (G.getUser().getTestAmount()); i++) {
            _list.add(G.getUser().get_storedResults().get(i).get_Date());
        }
        // Create an ArrayAdapter using the _list arraylist and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, _list);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        _resultsSpinner.setAdapter(adapter);
        //Set original value
        _resultsSpinner.setSelection(0);
    }

    /**
     * Set Results Method - Code to set the _results array and _volumeLevel depending on what entry
     *  is selected in the spinner. the populateDisplay() method is then called.
     */
    public void setResults() {
        _results = G.getUser().get_storedResults().get(_resultsSpinner.getSelectedItemPosition()).get_score();
        _volumeLevel = G.getUser().get_storedResults().get(_resultsSpinner.getSelectedItemPosition()).get_volumeLevel();
        populateDisplay();
    }

    /**
     * Populate Graph Method - Creates and populates the graph with the stored _results.
     * Also sets _volumeLbl to display the volume level.
     */
    public void populateDisplay() {
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
        //Set Volume label
        _volumeLbl.setText(R.string.lbl_volume);
        _volumeLbl.append(" " + Integer.toString(_volumeLevel));
    }

    /**
     * Save User Method - When called saves the user to SharedPreferences and updates the stored
     *  list with the username.
     * Returns true if successful and false if unsuccessful.
     *  @return - true / false
     */
    public boolean saveUser() {
        try {
            // Save the list of entries to internal storage
            SharedPreferencesStorage.writeUser(this, G.getUser());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            try {
                G.setUser(SharedPreferencesStorage.readUser(this, G.getUser().get_username()));
            } catch (IOException e1) {
                e1.printStackTrace();
                Toast.makeText(StoredResultsActivity.this, R.string.lbl_reloadError, Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }

}
