package ordanel.ednom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
               padronBL = new PadronBL(getApplicationContext());
               padronBL.clearDataLocal();
            }
        }
    }



}
