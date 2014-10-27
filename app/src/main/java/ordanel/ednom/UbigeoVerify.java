package ordanel.ednom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Leandro on 27/10/2014.
 */
public class UbigeoVerify extends Activity {

    String Departamento, Provincia, Distrito, Local;
    TextView txvDepartamento, txvProvincia, txvDistrito, txvLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ubigeo_verify);

        txvDepartamento = (TextView) findViewById(R.id.txvDepartamento);
        txvProvincia = (TextView) findViewById(R.id.txvProvincia);
        txvDistrito = (TextView) findViewById(R.id.txvDistrito);
        txvLocal = (TextView) findViewById(R.id.txvLocal);

        Departamento = "AMAZONAS";
        Provincia = "CHACHAPOYAS";
        Distrito = "CHACHAPOYAS";
        Local = "I.E. 0019";

        txvDepartamento.setText(Departamento);
        txvProvincia.setText(Provincia);
        txvDistrito.setText(Distrito);
        txvLocal.setText(Local);

    }

    public void clickCorrecto(View view){
        Toast toast = Toast.makeText(getApplicationContext(), "Acepto los Datos", Toast.LENGTH_SHORT );
        toast.show();

        Intent intent = new Intent(UbigeoVerify.this, ObtainCensus.class);
        startActivity(intent);
    }

    public void clickIncorrecto(View view){
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 )
        {
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
