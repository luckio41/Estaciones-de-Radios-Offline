package cl.luckio.estacionesderadiosoffline;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

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
        lvRadios.setAdapter(adapter);
    }
}
