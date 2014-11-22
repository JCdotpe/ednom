package ordanel.ednom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ordanel.ednom.Asyncs.VersionAsync;
import ordanel.ednom.Business.SedeOperativaBL;
import ordanel.ednom.Entity.LocalE;
import ordanel.ednom.Entity.SedeOperativaE;
import ordanel.ednom.Entity.UsuarioLocalE;

/**
 * Created by Leandro on 27/10/2014.
 */
public class UbigeoVerify extends Activity {

    Integer error = 0;

    String Sede, Usuario, NombreLocal, Direccion;
    Integer NAulas_t, NAulas_n, NAulas_disc, NAulas_contin;
    TextView txtSede, txtUsuario, txtNombreLocal, txtDireccion, txtNAulas_t, txtNAulas_n, txtNAulas_disc, txtNAulas_contin;
    Button btnCorrecto;

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

        /*ArrayList<UsuarioLocalE> arrayList = getIntent().getParcelableArrayListExtra( "listUbigeo" ); // metodo para obtener arraylist */
        sedeOperativaE = new SedeOperativaBL( UbigeoVerify.this ).showInfo();

        error = setUbigeo( sedeOperativaE );

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

            txtSede.setText( Sede );
            txtUsuario.setText( Usuario );
            txtNombreLocal.setText( NombreLocal );
            txtDireccion.setText( Direccion );
            txtNAulas_t.setText( NAulas_n.toString() );
            txtNAulas_n.setText( NAulas_t.toString() );
            txtNAulas_disc.setText( NAulas_disc.toString() );
            txtNAulas_contin.setText( NAulas_contin.toString() );

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
}
