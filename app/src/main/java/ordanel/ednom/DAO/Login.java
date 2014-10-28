package ordanel.ednom.DAO;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ordanel.ednom.Entity.Ubigeo;
import ordanel.ednom.libreria.HttpPostAux;

/**
 * Created by OrdNael on 28/10/2014.
 */
public class Login {

    String IP_Server = "172.16.100.45";
    String URL_Connect = "http://" + IP_Server + "/droidlogin/acces.php";

    HttpPostAux posteo = new HttpPostAux();
    ArrayList<Ubigeo> arrayList;

    public ArrayList<Ubigeo> CheckLogin(String usuario, String password){

        ArrayList<NameValuePair> parametersPost = new ArrayList<NameValuePair>();
        parametersPost.add( new BasicNameValuePair("usuario", usuario) );
        parametersPost.add( new BasicNameValuePair("password", password) );

        JSONArray jsonArray = posteo.getServerData( parametersPost, URL_Connect );

        if ( jsonArray != null && jsonArray.length() > 0 )
        {
            arrayList = new ArrayList<Ubigeo>();
            JSONObject jsonObject;

            Integer count = jsonArray.length();

            Log.e("LOGIN : " , count.toString() );

            try
            {
                for ( int i = 0; i < jsonArray.length(); i++ )
                {
                    jsonObject = (JSONObject) jsonArray.get(i);

                    Ubigeo ubigeo = new Ubigeo();
                    ubigeo.setDepartamento( jsonObject.getString("Departamento") );
                    ubigeo.setProvincia( jsonObject.getString("Provincia") );
                    ubigeo.setDistrito( jsonObject.getString("Distrito") );
                    ubigeo.setLocal( jsonObject.getString("Local") );

                    arrayList.add(ubigeo);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return arrayList;
        }
        else
        {
            return null;
        }

    }
}
