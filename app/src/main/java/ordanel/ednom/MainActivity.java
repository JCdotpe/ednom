package ordanel.ednom;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import ordanel.ednom.libreria.HttpPostAux;

public class MainActivity extends Activity {

    EditText txtUsuario;
    EditText txtPassword;
    Button btnLogin;
    HttpPostAux posteo;

    String IP_Server = "localhost";
    String URL_Connect = "http://" + IP_Server + "/droidlogin/acces.php";

    Boolean result_back;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        posteo = new HttpPostAux();

        txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String usuario = txtUsuario.getText().toString();
                String password = txtPassword.getText().toString();

                if ( !password.equals("") || !usuario.equals("") )
                {
                    new asynclogin().execute(usuario, password);
                }
                else
                {
                    err_login();
                }
            }
        });

    }

    public void err_login(){
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
        Toast toast = Toast.makeText( getApplicationContext(), "Error: El password es incorrecto", Toast.LENGTH_SHORT );
        toast.show();
    }

    public boolean loginstatus(String usuario, String password){

        int logstatus = -1;

        try {
            ArrayList<NameValuePair> parametersPost = new ArrayList<NameValuePair>();

            parametersPost.add(new BasicNameValuePair("usuario", usuario));
            parametersPost.add(new BasicNameValuePair("password", password));

            JSONArray jsonArray = posteo.getServerData(parametersPost, URL_Connect);

            SystemClock.sleep(950);

            if (jsonArray != null && jsonArray.length() > 0)
            {
                JSONObject jsonObject;

                try
                {
                    jsonObject = jsonArray.getJSONObject(0);
                    logstatus = jsonObject.getInt("logstatus");

                    Log.e("loginstatus", "logstatus = " + logstatus);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                if (logstatus == 0)
                {
                    Log.e("loginstatus", "invalido");
                    return  false;
                }
                else
                {
                    Log.e("loginstatus", "valido");
                    return  true;
                }
            }
            else
            {
                Log.e("JSON", "ERROR");
                return false;
            }

        }
        catch (Exception e)
        {
            Log.e("Login Status = ", "genero error");
            return false;
        }

    }

    class asynclogin extends AsyncTask <String, String, String> {

        String usuario;
        String password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Autenticando...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            usuario = params[0];
            password = params[1];

            if (loginstatus(usuario, password) == true)
            {
                return "ok";
            }
            else
            {
                return "err";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            dialog.dismiss();
            Log.e("onPostExecute = ", result);

            if ( result.equals("ok") )
            {
                Toast msgToast = Toast.makeText( getApplicationContext(), "El logeo fue correcto", Toast.LENGTH_SHORT );
                msgToast.show();
            }
            else
            {
                err_login();
            }
        }

    }

}

