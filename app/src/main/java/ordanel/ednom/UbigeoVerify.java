package ordanel.ednom;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ordanel.ednom.Asyncs.VersionAsync;
import ordanel.ednom.DAO.LoginDAO;
import ordanel.ednom.Entity.UsuarioLocalE;

/**
 * Created by Leandro on 27/10/2014.
 */
public class UbigeoVerify extends Activity {

    private static final String TAG = UbigeoVerify.class.getSimpleName();

    Integer error = 0;

    String Usuario, NombreLocal, NAulas, Sede;
    Integer NContingencia;
    TextView txtUsuario, txtNombreLocal, txtNAulas, txtNContingencia, txtSede;
    Button btnCorrecto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ubigeo_verify);

        txtUsuario = (TextView) findViewById( R.id.txtUsuario);
        txtNombreLocal = (TextView) findViewById( R.id.txtNombreLocal);
        txtNAulas = (TextView) findViewById( R.id.txtNAulas);
        txtNContingencia = (TextView) findViewById( R.id.txtNContingencia);
        txtSede = (TextView) findViewById( R.id.txtSede );
        btnCorrecto = (Button) findViewById( R.id.btnCorrecto );

        /*ArrayList<UsuarioLocalE> arrayList = getIntent().getParcelableArrayListExtra( "listUbigeo" );*/
        ArrayList<UsuarioLocalE> arrayList = new LoginDAO( UbigeoVerify.this ).showInfoUser();

        error = setUbigeo( arrayList );

        if ( error > 0 )
        {
            err_UbigeoVerify( error );
        }

    }

    public Integer setUbigeo( ArrayList<UsuarioLocalE> arrayList ) {

        if ( arrayList != null )
        {
            Integer count = arrayList.size();

            if ( count > 0 )
            {
                for (int i = 0; i < arrayList.size(); i++)
                {
                    Usuario = arrayList.get(i).getUsuario();
                    NombreLocal = arrayList.get(i).getNombreLocal();
                    NAulas = arrayList.get(i).getNaulas();
                    NContingencia = arrayList.get(i).getNcontingencia();
                    Sede = arrayList.get(i).getSede();
                }

                txtUsuario.setText( Usuario );
                txtNombreLocal.setText( NombreLocal );
                txtNAulas.setText( NAulas );
                txtNContingencia.setText( NContingencia.toString() );
                txtSede.setText( Sede );
            }
            else
            {
                error = 2;
            }

            Log.e( TAG, "setUbigeo count : " + count.toString() );
        }
        else
        {
            error = 1;
        }

        return error;

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

        Toast toast = Toast.makeText( UbigeoVerify.this, msg_error, Toast.LENGTH_SHORT );
        toast.show();
    }

    public void clickCorrecto(View view) {

        new VersionAsync( UbigeoVerify.this ).execute();

    }

    public void clickIncorrecto(View view) {
        finish();
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
