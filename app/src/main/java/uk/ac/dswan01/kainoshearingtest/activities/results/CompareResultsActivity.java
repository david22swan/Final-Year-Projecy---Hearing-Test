package uk.ac.dswan01.kainoshearingtest.activities.results;
/**
 * Imports the necessary classes
 */
import android.content.Intent;
import android.graphics.Color;
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

import uk.ac.dswan01.kainoshearingtest.R;
import uk.ac.dswan01.kainoshearingtest.standalone.Global;

public class CompareResultsActivity extends AppCompatActivity {

    //Global variable declared
    private Global G;
    //Graph variable declared
    private GraphView graph;
    //Array variables declared
    private int[] _comparedResults;
    private int[] _comparisonResults;
    //Spinner Variable declared
    private Spinner _comparisonSpinner;
    //TextView variables declared
    private TextView _compareVolume;
    private TextView _averageVolume;
    //String variable declared
    private String _message;
    //Int variables declared
    private int _comparedTestId;
    private int _comparedTestVolume;
    private int _years;
    //Double variable declared
    private double _comparisonTestVolume;

    /**
     * OnCreate method for the Compare Results Activity.
     * Sets content to the compare_results_screen.
     * @param savedInstanceState - Instance of the application.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compare_results_screen);

        //Global variable referenced
        G = (Global)getApplication();

        //Code to get results to compare
        Bundle b=this.getIntent().getExtras();
        _comparedTestId = b.getInt("number");
        _comparedResults = G.getUser().get_storedResults().get(_comparedTestId).get_score();
        _comparedTestVolume = G.getUser().get_storedResults().get(_comparedTestId).get_volumeLevel();

        //Code to call and reference graph
        graph = (GraphView) findViewById(R.id.graph);

        //Spinner is referenced and populated
        _comparisonSpinner = (Spinner) findViewById(R.id.spnrCompare);
        populateList();

        //References the label which displays the recorded _volumeLbl level
        _compareVolume = (TextView)findViewById(R.id.lblVolumeMessage);
        _averageVolume = (TextView) findViewById(R.id.lblComparisonMessage);

        //Methods to set comparison and populate display are called
        setComparison();

        //Button variables are called and referenced
        Button _backButton = (Button) findViewById(R.id.btnBack);

        //Button method
        /**
         * Back Button - redirects user back to Stored Results screen
         */
        _backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CompareResultsActivity.this, StoredResultsActivity.class));
                finish();
            }
        });

        //Spinner method
        /**
         * Comparison Spinner - Calls the setComparison() method when the slection is changed.
         */
        _comparisonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                setComparison();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //Empty method
            }
        });

    }

    /**
     * Populate List Method - Populates the spinner with the possible comparison choices.
     */
    private void populateList() {
        // Create an ArrayAdapter using the stored compare array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.compare_array,
                android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        _comparisonSpinner.setAdapter(adapter);
        //Set original value
        _comparisonSpinner.setSelection(0);
    }

    /**
     * Set Comparison Method - Checks what selection the spinner is currently at and then sets the
     *  _comparisonResults array and _comparisonTestVolume to the appropriate values, before calling
     *  the populateDisplay() method.
     * For the Averages choice also calculates the users age and sex in order to get the correct
     *  set of averages.
     * For the other methods checks if they are possible and displays and error message if they are not.
     */
    private void setComparison() {
        int i = _comparisonSpinner.getSelectedItemPosition() + 1;
        switch (i) {
            case 1:
                if (_years == 0) {
                    _years = calculateAge();
                }
                if (_years <= 25) {
                    //20
                    _comparisonResults = (getResources().getIntArray(R.array.frequency_20));
                    _comparisonTestVolume = ((double)(getResources().getIntArray(R.array.volume_thresholds)[0]) / 100);
                } else if (_years > 25 && _years <= 35) {
                    //30
                    _comparisonResults = (getResources().getIntArray(R.array.frequency_30));
                    _comparisonTestVolume = ((double)(getResources().getIntArray(R.array.volume_thresholds)[1]) / 100);
                } else if (_years > 35 && _years <= 45) {
                    //40
                    _comparisonResults = (getResources().getIntArray(R.array.frequency_40));
                    _comparisonTestVolume = ((double)(getResources().getIntArray(R.array.volume_thresholds)[2]) / 100);
                } else if (_years > 45 && _years <= 55) {
                    //50
                    _comparisonResults = (getResources().getIntArray(R.array.frequency_50));
                    _comparisonTestVolume = ((double)(getResources().getIntArray(R.array.volume_thresholds)[3]) / 100);
                } else if (_years > 55) {
                    if (G.getUser().get_gender() == 1) {
                        //M60
                        _comparisonResults = (getResources().getIntArray(R.array.frequency_M60));
                        _comparisonTestVolume = ((double)(getResources().getIntArray(R.array.volume_thresholds)[4]) / 100);
                    } else {
                        //F60
                        _comparisonResults = (getResources().getIntArray(R.array.frequency_F60));
                        _comparisonTestVolume = ((double)(getResources().getIntArray(R.array.volume_thresholds)[5]) / 100);
                    }
                }
                _message = (getResources().getString(R.string.lbl_averageForAge));
                break;
            case 2:
                if (_comparedTestId == 0) {
                    Toast.makeText(CompareResultsActivity.this, R.string.lbl_comparisonError, Toast.LENGTH_SHORT).show();
                    return;
                }
                _comparisonResults = G.getUser().get_storedResults().get(0).get_score();
                _comparisonTestVolume = (double) G.getUser().get_storedResults().get(0).get_volumeLevel();
                _message = (getResources().getString(R.string.lbl_averageOther));
                break;
            case 3:
                if (_comparedTestId == (G.getUser().get_storedResults().size()) - 1) {
                    Toast.makeText(CompareResultsActivity.this, R.string.lbl_comparisonError, Toast.LENGTH_SHORT).show();
                    return;
                }
                _comparisonResults = G.getUser().get_storedResults().get((G.getUser().get_storedResults().size() - 1)).get_score();
                _comparisonTestVolume = (double) G.getUser().get_storedResults().get((G.getUser().get_storedResults().size() - 1)).get_volumeLevel();
                _message = (getResources().getString(R.string.lbl_averageOther));
                break;
            case 4:
                if (_comparedTestId == 0) {
                    Toast.makeText(CompareResultsActivity.this, R.string.lbl_comparisonError, Toast.LENGTH_SHORT).show();
                    return;
                }
                _comparisonResults = G.getUser().get_storedResults().get((_comparedTestId - 1)).get_score();
                _comparisonTestVolume = (double) G.getUser().get_storedResults().get((_comparedTestId - 1)).get_volumeLevel();
                _message = (getResources().getString(R.string.lbl_averageOther));
                break;
            default: //Blank default statement
                break;
        }
        populateDisplay();
    }

    /**
     * Populate Display Method - Method to populate the graph with both the results to be compared
     *  and the results that they are being compared against.
     * Also sets the volume labels.
    */
    public void populateDisplay() {
        LineGraphSeries<DataPoint> comparedSeries = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, _comparedResults[0]),
                new DataPoint(1, _comparedResults[1]),
                new DataPoint(2, _comparedResults[2]),
                new DataPoint(3, _comparedResults[3]),
                new DataPoint(4, _comparedResults[4]),
                new DataPoint(5, _comparedResults[5]),
                new DataPoint(6, _comparedResults[6]),
                new DataPoint(7, _comparedResults[7])
        });
        comparedSeries.setColor(Color.BLUE);
        LineGraphSeries<DataPoint> comparisonSeries = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, _comparisonResults[0]),
                new DataPoint(1, _comparisonResults[1]),
                new DataPoint(2, _comparisonResults[2]),
                new DataPoint(3, _comparisonResults[3]),
                new DataPoint(4, _comparisonResults[4]),
                new DataPoint(5, _comparisonResults[5]),
                new DataPoint(6, _comparisonResults[6]),
                new DataPoint(7, _comparisonResults[7])
        });
        //Remove all previous series and adds the new ones
        graph.removeAllSeries();
        comparedSeries.setThickness(10);
        graph.addSeries(comparedSeries);
        comparisonSeries.setThickness(10);
        comparisonSeries.setColor(Color.RED);
        graph.addSeries(comparisonSeries);
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
        //Set sound level labels
        _compareVolume.setText(R.string.lbl_volume);
        _compareVolume.append(" " + Integer.toString(_comparedTestVolume));
        _averageVolume.setText(_message);
        _averageVolume.append(" " + String.valueOf(_comparisonTestVolume));
        Toast.makeText(CompareResultsActivity.this, R.string.lbl_comparisonColor, Toast.LENGTH_SHORT).show();
    }

    /**
     * Calculate Age Method - Code to calculate the difference in years between the users given DoB
     *  and the time the test was taken at.
     * @return _yearDifference
     */
    public int calculateAge() {
        G.getUser().get_dateOfBirth().getYear();
        G.getUser().get_storedResults().get(_comparedTestId).get_dateOfTest();

        int _yearDifference = ((G.getUser().get_dateOfBirth().getYear()) -
                (G.getUser().get_storedResults().get(_comparedTestId).get_dateOfTest().getYear()));

        if ((G.getUser().get_dateOfBirth().getMonth()) >
                (G.getUser().get_storedResults().get(_comparedTestId).get_dateOfTest().getMonth())) {
            _yearDifference =- 1;
        } else if ((G.getUser().get_dateOfBirth().getMonth()) ==
                (G.getUser().get_storedResults().get(_comparedTestId).get_dateOfTest().getMonth())) {
            if ((G.getUser().get_dateOfBirth().getDay()) >
                    (G.getUser().get_storedResults().get(_comparedTestId).get_dateOfTest().getDay())) {
                _yearDifference =- 1;
            }
        }

        return _yearDifference;
    }

}
