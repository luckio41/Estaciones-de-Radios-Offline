package cl.luckio.estacionesderadiosoffline;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class ResultsActivity extends AppCompatActivity {

    private int id;
    private TextView tvCoordinates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvCoordinates = (TextView) findViewById(R.id.tvCoordinates);

        Bundle extras = getIntent().getExtras();
        id = extras.getInt("position");
        tvCoordinates.setText( String.valueOf(id));
    }

}
