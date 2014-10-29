package ordanel.ednom.DAO;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ordanel.ednom.Entity.UbigeoE;
import ordanel.ednom.libreria.HttpPostAux;

/**
 * Created by OrdNael on 28/10/2014.
 */
public class Login {

    private static final String TAG = Login.class.getSimpleName();

    String IP_Server = "jc.pe";
    String URL_Connect = "http://" + IP_Server + "/portafolio/ednom/acces.php";

    HttpPostAux posteo = new HttpPostAux();
    ArrayList<UbigeoE> arrayList;

    public ArrayList<UbigeoE> CheckLogin( String password ) {

        ArrayList<NameValuePair> parametersPost = new ArrayList<NameValuePair>();
        parametersPost.add( new BasicNameValuePair( "password", password ) );

        JSONArray jsonArray = posteo.getServerData( parametersPost, URL_Connect );

        if ( jsonArray != null && jsonArray.length() > 0 )
        {
            arrayList = new ArrayList<UbigeoE>();
            JSONObject jsonObject;

            Integer count = jsonArray.length();

            Log.e( TAG , count.toString() );

            try
            {
                for ( int i = 0; i < jsonArray.length(); i++ )
                {
                    jsonObject = (JSONObject) jsonArray.get(i);

                    UbigeoE ubigeoE = new UbigeoE();
                    ubigeoE.setDepartamento( jsonObject.getString( "Departamento" ) );
                    ubigeoE.setProvincia( jsonObject.getString( "Provincia" ) );
                    ubigeoE.setDistrito( jsonObject.getString( "Distrito" ) );
                    ubigeoE.setLocal( jsonObject.getString( "Local" ) );

                    arrayList.add(ubigeoE);
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