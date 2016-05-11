package cl.luckio.estacionesderadiosoffline;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class AcercaDe extends AppCompatActivity {

    private TextView tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);

        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvEmail.setText(Html.fromHtml("Escríbeme a <a href=\"mailto:soporte@luckio.cl\">soporte@luckio.cl</a> y la agrego."));
    }
}
