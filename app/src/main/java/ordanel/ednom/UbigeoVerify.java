package ordanel.ednom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ordanel.ednom.Asyncs.VersionAsync;
import ordanel.ednom.Business.SedeOperativaBL;
import ordanel.ednom.Entity.LocalE;
import ordanel.ednom.Entity.SedeOperativaE;
import ordanel.ednom.Entity.UsuarioLocalE;
import ordanel.ednom.Library.Item;

/**
 * Created by Leandro on 27/10/2014.
 */
public class UbigeoVerify extends Activity {

    Integer error = 0;

    String Sede, Usuario, NombreLocal, Direccion;
    Integer NAulas_t, NAulas_n, NAulas_disc, NAulas_contin;
    TextView txtSede, txtUsuario, txtNombreLocal, txtDireccion, txtNAulas_t, txtNAulas_n, txtNAulas_disc, txtNAulas_contin;
    Button btnCorrecto;
    ListView listView;
    SedeOperativaE sedeOperativaE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ubigeo_verify);

        txtUsuario = (TextView) findViewById( R.id.txtUsuario);
        txtNombreLocal = (TextView) findViewById( R.id.txtNombreLocal);
        txtDireccion = (TextView) findViewById( R.id.txtDirecion);
        txtNAulas_t = (TextView) findViewById( R.id.txtNAulas_t);
        txtNAulas_n = (TextView) findViewById( R.id.txtNAulas_n);
        txtNAulas_disc = (TextView) findViewById( R.id.txtNAulas_disc);
        txtNAulas_contin = (TextView) findViewById( R.id.txtNAulas_contin);
        txtSede = (TextView) findViewById( R.id.txtSede );
        btnCorrecto = (Button) findViewById( R.id.btnCorrecto );
        listView = (ListView) findViewById(R.id.listView);

        /*ArrayList<UsuarioLocalE> arrayList = getIntent().getParcelableArrayListExtra( "listUbigeo" ); // metodo para obtener arraylist */
        sedeOperativaE = new SedeOperativaBL( UbigeoVerify.this ).showInfo();

        error = setUbigeo( sedeOperativaE );

        List items = new ArrayList();
        items.add(new Item(R.drawable.home, "Sede", Sede));
        items.add(new Item(R.drawable.user, "Usuario", Usuario));
        items.add(new Item(R.drawable.school, "Local de Aplicación", NombreLocal));
        items.add(new Item(R.drawable.location, "Dirección", Direccion));
        /*
        items.add(new Item(R.drawable.classroom, "Número Totales de Aulas", Integer.toString(NAulas_t)));
        items.add(new Item(R.drawable.classroom, "Número de Aulas de Profesores sin Discapacidad", Integer.toString(NAulas_n)));
        items.add(new Item(R.drawable.classroom, "Número de Aulas de Profesores con Discapacidad", Integer.toString(NAulas_disc)));
        items.add(new Item(R.drawable.classroom, "Número de Aulas de Contingencia", Integer.toString(NAulas_contin)));
        */
        listView.setAdapter(new ItemAdapter(this, items));

        if ( error > 0 )
        {
            err_UbigeoVerify( error );
        }

    }

    public Integer setUbigeo( SedeOperativaE paramSedeOperativaE ) {

        if ( paramSedeOperativaE != null )
        {
            Sede = paramSedeOperativaE.getSede_operativa();

            // set data LOCAL
            for ( LocalE localE : paramSedeOperativaE.getLocalEList() )
            {
                NombreLocal = localE.getNombreLocal();
                Direccion = localE.getDireccion();
                NAulas_t = localE.getNaula_t();
                NAulas_n = localE.getNaula_n();
                NAulas_disc = localE.getNaula_discapacidad();
                NAulas_contin = localE.getNaula_contingencia();

                // set data USUARIO_LOCAL
                for ( UsuarioLocalE usuarioLocalE : localE.getUsuarioLocalEList() )
                {
                    Usuario = usuarioLocalE.getUsuario();
                }
                // .set data USUARIO_LOCAL

            }
            // .set data LOCAL
/*
            txtSede.setText( Sede );
            txtUsuario.setText( Usuario );
            txtNombreLocal.setText( NombreLocal );
            txtDireccion.setText( Direccion );
            txtNAulas_t.setText( NAulas_n.toString() );
            txtNAulas_n.setText( NAulas_t.toString() );
            txtNAulas_disc.setText( NAulas_disc.toString() );
            txtNAulas_contin.setText( NAulas_contin.toString() );
*/
        }

        return paramSedeOperativaE.getStatus();

    }

    public void err_UbigeoVerify( Integer error ) {

        String msg_error = "";

        switch ( error )
        {
            case 1:
                msg_error = getString( R.string.verify_error_query );
                break;

            case 2:
                msg_error = getString( R.string.verify_error_data );
                break;
        }

        btnCorrecto.setEnabled( false );

        Toast.makeText( UbigeoVerify.this, msg_error, Toast.LENGTH_SHORT ).show();
    }

    public void clickCorrecto(View view) {

        new VersionAsync( UbigeoVerify.this ).execute();

    }

    public void clickIncorrecto(View view) {

        Intent intent = new Intent( getApplicationContext(), Login.class );
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 )
        {
            return true;
        }

        return super.onKeyDown( keyCode, event );
    }

    public class ItemAdapter extends BaseAdapter{
        private Context context;
        private List<Item> items;

        public ItemAdapter(Context context, List<Item> items) {
            this.context = context;
            this.items = items;
        }
        @Override
        public int getCount() {
            return this.items.size();
        }

        @Override
        public Object getItem(int position) {
            return this.items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = convertView;
            if (convertView == null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_item, parent, false);
            }

            ImageView image = (ImageView) view.findViewById(R.id.imageView);
            TextView textViewTitle = (TextView) view.findViewById(R.id.txt_title);
            TextView textViewDescription = (TextView) view.findViewById(R.id.txt_description);

            Item item = this.items.get(position);
            textViewTitle.setText(item.getTitle());
            textViewDescription.setText(item.getDescription());
            image.setImageResource(item.getImage());

            return view;
        }
    }
}
