package ordanel.ednom;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Leandro on 27/10/2014.
 */
public class ObtainCensus extends Activity {

    private static final String TAG = ObtainCensus.class.getSimpleName();

    Button btnPadron;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.obtain_census);

        btnPadron = (Button) findViewById( R.id.btnPadron );

        Integer flag = getIntent().getIntExtra( "statusVersion", -1 );

        if ( flag == 1 )
        {
            btnPadron.setText( getString( R.string.padron_msg_new ) );
        }
        else if ( flag == 2 )
        {
            btnPadron.setText( getString( R.string.padron_msg_update ) );
        }

    }

    public void downloadPadron(View view) {
        Toast toast = Toast.makeText( getApplicationContext(), "Padrón descargado", Toast.LENGTH_SHORT );
        toast.show();
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