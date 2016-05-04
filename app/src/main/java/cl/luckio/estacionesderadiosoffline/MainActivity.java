package cl.luckio.estacionesderadiosoffline;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ListView lvRadios;
    private String listaRadios[] = new String[]{"Radio Bio Bio", "ADN Radio"};
    private Integer[] imgid={
            R.drawable.biobio,
            R.drawable.adn
    };

    private ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RadiosListAdapter adapter = new RadiosListAdapter(this, listaRadios, imgid);
        lvRadios = (ListView) findViewById(R.id.lvRadios);
        lvRadios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShowResults(position, view);
            }
        });
        lvRadios.setAdapter(adapter);
    }

    private void ShowResults(int position, View view) {
        Intent i = new Intent(this, ResultsActivity.class);
        i.putExtra("position", position);
        startActivity(i);
    }
}
