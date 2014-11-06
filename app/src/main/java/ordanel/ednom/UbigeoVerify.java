package ordanel.ednom;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import ordanel.ednom.Asyncs.VersionAsync;
import ordanel.ednom.Entity.UsuarioLocalE;

/**
 * Created by Leandro on 27/10/2014.
 */
public class UbigeoVerify extends Activity {

    private static final String TAG = UbigeoVerify.class.getSimpleName();

    String Usuario, NombreLocal, NAulas, Sede;
    Integer NContingencia;
    TextView txtUsuario, txtNombreLocal, txtNAulas, txtNContingencia, txtSede;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ubigeo_verify);

        txtUsuario = (TextView) findViewById( R.id.txtUsuario);
        txtNombreLocal = (TextView) findViewById( R.id.txtNombreLocal);
        txtNAulas = (TextView) findViewById( R.id.txtNAulas);
        txtNContingencia = (TextView) findViewById( R.id.txtNContingencia);
        txtSede = (TextView) findViewById( R.id.txtSede );

        ArrayList<UsuarioLocalE> arrayList = getIntent().getParcelableArrayListExtra( "listUbigeo" );

        setUbigeo( arrayList );

    }

    public void setUbigeo( ArrayList<UsuarioLocalE> arrayList ) {

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
            Log.e( TAG, "setUbigeo count : " + count.toString() );
        }

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
