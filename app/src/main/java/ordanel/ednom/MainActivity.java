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

public class MainActivity extends Activity {

    EditText txtUsuario;
    EditText txtPassword;
    Button btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

    }

    public void InitLogin(View view){
        String usuario = txtUsuario.getText().toString();
        String password = txtPassword.getText().toString();

        if ( checkLoginData( usuario, password ) == true )
        {
            new LoginAsync(MainActivity.this).execute( usuario, password );
        }
        else
        {
            err_login();
        }
    }

    public boolean checkLoginData(String userName, String password){

        if ( userName.equals("") || password.equals("") )
        {
            Log.e("Login UI", "check login data user or pass error!");
            return false;
        }
        else
        {
            return true;
        }
    }

    public void err_login(){
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
        Toast toast = Toast.makeText( getApplicationContext(), "Error: El password es incorrecto", Toast.LENGTH_SHORT );
        toast.show();
    }

}

