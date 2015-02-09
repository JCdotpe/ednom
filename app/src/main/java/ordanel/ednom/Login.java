package ordanel.ednom;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ordanel.ednom.Asyncs.LoginAsync;
import ordanel.ednom.Library.ConstantsUtils;

public class Login extends Activity {

    private static final String TAG = Login.class.getSimpleName();

    EditText txtPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        txtPassword = (EditText) findViewById(R.id.edtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

    }

    public void initLogin(View view) {
        String password = txtPassword.getText().toString();
        ConstantsUtils.getPass = password;

        if ( checkLoginData( password ) )
        {
            new LoginAsync( Login.this ).execute( password );
        }
        else
        {
            err_login();
        }

    }

    public boolean checkLoginData( String password ) {

        if ( password.equals("") )
        {
            Log.e( TAG, "check login data pass error!");
            return false;
        }
        else
        {
            return true;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(ev);

        if ( view instanceof EditText )
        {
            View view1 = getCurrentFocus();

            int scrcoords[] = new int[2];

            view1.getLocationOnScreen( scrcoords );

            float x = ev.getRawX() + view1.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view1.getTop() - scrcoords[1];

            if ( ev.getAction() == MotionEvent.ACTION_UP && ( x < view1.getLeft() || x >= view1.getRight() || y < view1.getTop() || y > view1.getBottom() ) )
            {
                InputMethodManager imm = (InputMethodManager) getSystemService( Context.INPUT_METHOD_SERVICE );
                imm.hideSoftInputFromWindow( getWindow().getCurrentFocus().getWindowToken(), 0 );
            }

        }

        return ret;
    }

    public void err_login() {

        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate( 200 );
        Toast.makeText( getApplicationContext(), getString( R.string.login_msg_error_password ), Toast.LENGTH_SHORT ).show();

    }

}