package cl.luckio.estacionesderadiosoffline;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Luckio on 03/05/2016.
 */
public class RadiosListAdapter extends ArrayAdapter<String>{

    private final Activity context;
    private final String[] itemname;
    private final String[] status;
    private final Integer[] integers;

    public RadiosListAdapter(Activity context, String[] itemname, String[] status, Integer[] integers) {
        super(context, R.layout.lista_radio, itemname);
        this.context = context;
        this.itemname = itemname;
        this.status = status;
        this.integers = integers;
    }

    public View getView(int position, View view, ViewGroup parent){

        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.lista_radio, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.texto_principal);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView etxDescription = (TextView) rowView.findViewById(R.id.texto_secundario);

        txtTitle.setText(itemname[position]);

        if(status[position] == "al aire") {
            etxDescription.setTextColor(Color.parseColor("#689F38"));
        }
        else{
            etxDescription.setTextColor(Color.parseColor("#FF5722"));
        }
        etxDescription.setText(status[position]);
        imageView.setImageResource(integers[position]);

        return rowView;
    }
}
