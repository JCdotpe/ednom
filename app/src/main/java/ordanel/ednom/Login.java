package ordanel.ednom;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ordanel.ednom.Asyncs.LoginAsync;

public class Login extends Activity {

    EditText txtPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

    }

    public void initLogin(View view) {
        String password = txtPassword.getText().toString();

        if ( checkLoginData( password ) )
        {
            new LoginAsync(Login.this).execute( password );
        }
        else
        {
            err_login();
        }
    }

    public boolean checkLoginData( String password ) {

        if ( password.equals("") )
        {
            Log.e("Login UI", "check login data pass error!");
            return false;
        }
        else
        {
            return true;
        }
    }

    public void err_login() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate( 200 );
        Toast toast = Toast.makeText( getApplicationContext(), getString( R.string.login_msg_error ), Toast.LENGTH_SHORT );
        toast.show();
    }

}