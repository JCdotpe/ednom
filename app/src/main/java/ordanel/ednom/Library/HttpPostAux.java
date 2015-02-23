package ordanel.ednom.Library;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class HttpPostAux {

    private static final String TAG = HttpPostAux.class.getSimpleName();
    private static HttpPostAux httpPostAux;

    InputStream inputStream = null;
    String result = "";
    Integer error  = 0;

    public static HttpPostAux getInstance() {

        if ( httpPostAux == null )
        {
            Log.e( TAG, "start HttpPostAux" );
            httpPostAux = new HttpPostAux();
        }

        return httpPostAux;
    }

    private HttpPostAux() {
    }

    public JSONArray getServerData( ArrayList<NameValuePair> parameters, String urlWebServer ) {

        httpPostConnect( parameters, urlWebServer );

        if ( error == 0 && inputStream != null )
        {
            getPostResponse();
            if ( error == 0 )
            {
                return getJsonArray();
            }
            else
            {
                return null;
            }
        }
        else {
            return null;
        }

    }

    private void httpPostConnect( ArrayList<NameValuePair> parameters, String urlWebServer ) {

        try
        {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost( urlWebServer );

            if ( parameters != null )
            {
                httpPost.setEntity( new UrlEncodedFormEntity( parameters ) );
            }

            HttpResponse httpResponse = httpClient.execute( httpPost );
            HttpEntity httpEntity = httpResponse.getEntity();

            inputStream = httpEntity.getContent();
        }
        catch (Exception e)
        {
            Log.e( TAG, "Error in the connection : " + e.toString() );
            error = 1;
        }


    }

    public void getPostResponse() {

        try
        {
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( inputStream, "iso-8859-1" ), 8 );
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ( ( line = bufferedReader.readLine() ) != null )
            {
                stringBuilder.append( line + "\n" );
            }

            bufferedReader.close();
            inputStream.close();

            result = stringBuilder.toString();

            Log.e( TAG, "result : " + stringBuilder.toString() );
        }
        catch (Exception e)
        {
            Log.e( TAG, "Error converting result : " + e.toString() );
            error = 1;
        }

    }

    public JSONArray getJsonArray(){

        try
        {
            JSONArray jsonArray = new JSONArray( result );
            return jsonArray;
        }
        catch (Exception e)
        {
            Log.e( TAG, "Error parsing data : " + e.toString() );
            return null;
        }

    }

}