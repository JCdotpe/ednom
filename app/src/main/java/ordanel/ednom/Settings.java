package ordanel.ednom;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import ordanel.ednom.Business.PadronBL;



public class Settings extends Activity {

    private static int rol;
    private static final int ROLDEFAULT = 1;
    private ListView listSettings;
    private PadronBL padronBL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Intent intent = getIntent();
        rol = intent.getIntExtra("ROL", ROLDEFAULT);
        String [] list = {getString(R.string.clear_data_local)};
        ArrayAdapter <String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listSettings = (ListView) findViewById(R.id.list_settings);
        listSettings.setAdapter(adapter);
        ListItemClick listItemClick = new ListItemClick();
        listSettings.setOnItemClickListener(listItemClick);
    }


    class ListItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String item = parent.getItemAtPosition(position).toString();
            if ( item.equals(getString(R.string.clear_data_local))){
                AlertDialog.Builder builder =  new AlertDialog.Builder(Settings.this);
                builder.setTitle("Borrar Datos");
                builder.setMessage("Deseas borrar los datos locales?");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            padronBL = new PadronBL(Settings.this);
                        padronBL.clearDataLocal();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(Settings.this, "No se han borrado los datos", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.create();
                builder.show();
            }
        }
    }



}
