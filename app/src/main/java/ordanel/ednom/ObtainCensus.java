package ordanel.ednom;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import ordanel.ednom.Asyncs.PadronAsync;

/**
 * Created by Leandro on 27/10/2014.
 */
public class ObtainCensus extends Activity {

    private static final String TAG = ObtainCensus.class.getSimpleName();

    Integer flag;

    Button btnPadron;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.obtain_census);

        btnPadron = (Button) findViewById( R.id.btnPadron );

        /*flag = getIntent().getIntExtra( "statusVersion", -1 );*/

    }

    public void downloadPadron(View view) {

        new PadronAsync( ObtainCensus.this ).execute();

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