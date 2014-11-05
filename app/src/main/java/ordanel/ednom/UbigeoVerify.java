package ordanel.ednom;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import ordanel.ednom.Asyncs.VersionAsync;
import ordanel.ednom.Entity.UbigeoE;

/**
 * Created by Leandro on 27/10/2014.
 */
public class UbigeoVerify extends Activity {

    private static final String TAG = UbigeoVerify.class.getSimpleName();

    String Departamento, Provincia, Distrito, Local;
    TextView txvDepartamento, txvProvincia, txvDistrito, txvLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ubigeo_verify);

        txvDepartamento = (TextView) findViewById( R.id.txtDepartamento);
        txvProvincia = (TextView) findViewById( R.id.txtProvincia);
        txvDistrito = (TextView) findViewById( R.id.txtDistrito);
        txvLocal = (TextView) findViewById( R.id.txtLocal);

        ArrayList<UbigeoE> arrayList = getIntent().getParcelableArrayListExtra( "listUbigeo" );

        Integer count = arrayList.size();
        Log.e( TAG, count.toString() );

        for (int i = 0; i < arrayList.size(); i++)
        {
            Departamento = arrayList.get(i).getDepartamento();
            Provincia = arrayList.get(i).getProvincia();
            Distrito = arrayList.get(i).getDistrito();
            Local = arrayList.get(i).getLocal();
        }

        txvDepartamento.setText( Departamento );
        txvProvincia.setText( Provincia );
        txvDistrito.setText( Distrito );
        txvLocal.setText( Local );
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
