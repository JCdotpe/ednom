package ordanel.ednom.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ordanel.ednom.BD.DBHelper;
import ordanel.ednom.Entity.UsuarioLocalE;
import ordanel.ednom.Library.HttpPostAux;

/**
 * Created by OrdNael on 28/10/2014.
 */
public class LoginDAO {

    private static final String TAG = LoginDAO.class.getSimpleName();

    String IP_Server = "jc.pe";
    String URL_Connect = "http://" + IP_Server + "/portafolio/ednom/acces.php";

    Integer error = 0;
    Context context;

    HttpPostAux posteo = new HttpPostAux();
    ArrayList<UsuarioLocalE> arrayList;

    public LoginDAO( Context context ) {
        this.context = context;
        Log.e( TAG, "start" );
    }

    /*public ArrayList<UsuarioLocalE> CheckLogin( String password ) {*/
    public Integer CheckLogin( String password ) {

        Log.e( TAG, "start CheckLogin" );

        ArrayList<NameValuePair> parametersPost = new ArrayList<NameValuePair>();
        parametersPost.add( new BasicNameValuePair( "password", password ) );

        JSONArray jsonArray = posteo.getServerData( parametersPost, URL_Connect );

        if ( jsonArray != null )
        {
            if ( jsonArray.length() > 0 )
            {
                try
                {
                    arrayList = new ArrayList<UsuarioLocalE>();
                    JSONObject jsonObject;

                    for ( int i = 0; i < jsonArray.length(); i++ )
                    {
                        jsonObject = (JSONObject) jsonArray.get(i);

                        UsuarioLocalE usuarioLocalE = new UsuarioLocalE();

                        usuarioLocalE.setIdUsuario( jsonObject.getInt( "idUsuario") );
                        usuarioLocalE.setUsuario(jsonObject.getString( "usuario" ) );
                        usuarioLocalE.setClave( jsonObject.getString( "clave") );
                        usuarioLocalE.setRol( jsonObject.getInt( "rol" ) );
                        usuarioLocalE.setNro_local( jsonObject.getInt( "nro_local" ) );
                        usuarioLocalE.setNombreLocal( jsonObject.getString( "nombreLocal" ) );
                        usuarioLocalE.setNaulas( jsonObject.getString( "naulas" ) );
                        usuarioLocalE.setNcontingencia( jsonObject.getInt( "ncontingencia" ) );
                        usuarioLocalE.setSede( jsonObject.getString( "sede" ) );

                        arrayList.add( usuarioLocalE );
                    }

                    if ( arrayList.size() > 0 )
                    {
                        this.registerLogin( arrayList );
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    error = 3; // error en el CheckLogin //
                }
            }
            else
            {
                error  = 2; // error en el Password //
            }
        }
        else
        {
            error = 1; // error en el HttpPostAux //
        }
        Log.e( TAG, "end CheckLogin" );

        return error;

    }

    public void registerLogin( ArrayList<UsuarioLocalE> usuarioLocalEArrayList ) {

        Log.e( TAG, "start registerLogin" );

        DBHelper dbHelper = DBHelper.getUtilDb( this.context );
        ContentValues contentValues;

        try
        {
            dbHelper.openDataBase();
            dbHelper.beginTransaction();

            dbHelper.getDatabase().delete( "usuario_local", null, null );
            Log.e( TAG, "Se elimino usuario_local!" );

            for ( int i = 0; i < usuarioLocalEArrayList.size(); i++ )
            {
                contentValues = new ContentValues();

                contentValues.put( "idUsuario", usuarioLocalEArrayList.get(i).getIdUsuario() );
                contentValues.put( "usuario", usuarioLocalEArrayList.get(i).getUsuario() );
                contentValues.put( "clave", usuarioLocalEArrayList.get(i).getClave() );
                contentValues.put( "rol", usuarioLocalEArrayList.get(i).getRol() );
                contentValues.put( "nro_local", usuarioLocalEArrayList.get(i).getNro_local() );
                contentValues.put( "nombreLocal", usuarioLocalEArrayList.get(i).getNombreLocal() );
                contentValues.put( "naulas", usuarioLocalEArrayList.get(i).getNaulas() );
                contentValues.put( "ncontingencia", usuarioLocalEArrayList.get(i).getNcontingencia() );
                contentValues.put( "sede", usuarioLocalEArrayList.get(i).getSede() );

                Long exito = dbHelper.getDatabase().insertOrThrow( "usuario_local", null, contentValues );
                Log.e( TAG, "ingreso : " + String.valueOf(exito) );
            }

            dbHelper.setTransactionSuccessful();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e( TAG, "error registerLogin : " + e.toString() );
            error = 4; // error en el registerLogin //
        }
        finally
        {
            dbHelper.endTransaction();
            dbHelper.close();
        }

        Log.e( TAG, "end registerLogin" );

    }

    public ArrayList<UsuarioLocalE> showInfoUser() {

        Log.e( TAG, "start showInfoUser" );

        DBHelper dbHelper = DBHelper.getUtilDb( this.context );
        Cursor cursor = null;

        try
        {
            dbHelper.openDataBase();
            dbHelper.beginTransaction();

            String SQL = "SELECT idUsuario, usuario, clave, rol, nro_local, nombreLocal, naulas, ncontingencia, sede FROM usuario_local";
            cursor = dbHelper.getDatabase().rawQuery( SQL, null );

            arrayList = new ArrayList<UsuarioLocalE>();

            if ( cursor.moveToFirst() )
            {

                while ( !cursor.isAfterLast() )
                {
                    UsuarioLocalE usuarioLocalE = new UsuarioLocalE();

                    usuarioLocalE.setIdUsuario( cursor.getInt(cursor.getColumnIndex("idUsuario")) );
                    usuarioLocalE.setUsuario( cursor.getString( cursor.getColumnIndex( "usuario" ) ) );
                    usuarioLocalE.setClave( cursor.getString( cursor.getColumnIndex( "clave" ) ) );
                    usuarioLocalE.setRol( cursor.getInt(cursor.getColumnIndex("rol")) );
                    usuarioLocalE.setNro_local( cursor.getInt(cursor.getColumnIndex("nro_local")) );
                    usuarioLocalE.setNombreLocal( cursor.getString( cursor.getColumnIndex( "nombreLocal" ) ) );
                    usuarioLocalE.setNaulas( cursor.getString( cursor.getColumnIndex( "naulas" ) ) );
                    usuarioLocalE.setNcontingencia( cursor.getInt(cursor.getColumnIndex("ncontingencia")) );
                    usuarioLocalE.setSede( cursor.getString( cursor.getColumnIndex( "sede" ) ) );

                    arrayList.add( usuarioLocalE );

                    cursor.moveToNext();
                }

            }

            return arrayList;

        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e( TAG, "error showInfoUser : " + e.toString() );
            return null; // error en el showInfoUser //
        }
        finally
        {
            dbHelper.endTransaction();
            dbHelper.close();
            cursor.close();

            Log.e( TAG, "start showInfoUser" );
        }

    }

}