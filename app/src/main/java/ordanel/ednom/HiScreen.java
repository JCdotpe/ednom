package ordanel.ednom;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Leandro on 26/10/2014.
 */
public class HiScreen extends Activity {

    String user;
    TextView txvUserName, txvLogOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_screen);

        txvUserName = (TextView) findViewById(R.id.txvUserName);
        txvLogOff = (TextView) findViewById(R.id.txvLogOff);

        Bundle bundleExtras = getIntent().getExtras();

        if ( bundleExtras != null )
        {
            user = bundleExtras.getString("user");
        }
        else
        {
            user = "error";
        }

        txvUserName.setText(user);

        txvLogOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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