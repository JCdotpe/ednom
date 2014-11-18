package ordanel.ednom.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ordanel.ednom.BD.DBHelper;
import ordanel.ednom.Entity.LocalE;
import ordanel.ednom.Entity.SedeOperativaE;
import ordanel.ednom.Entity.UsuarioLocalE;
import ordanel.ednom.Library.ConstantsUtils;
import ordanel.ednom.Library.HttpPostAux;

/**
 * Created by OrdNael on 28/10/2014.
 */
public class LoginDAO {

    private static final String TAG = LoginDAO.class.getSimpleName();

    /*String IP_Server = "jc.pe";
    String URL_Connect = "http://" + IP_Server + "/portafolio/ednom/acces.php";*/
    String URL_Connect = ConstantsUtils.BASE_URL + "acces.php";

    Integer error = 0;
    String SQL = "";
    Context context;
    Cursor cursor = null;


    DBHelper dbHelper;

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

                        SedeOperativaE sedeOperativaE = new SedeOperativaE();
                        LocalE localE = new LocalE();
                        UsuarioLocalE usuarioLocalE = new UsuarioLocalE();

                        sedeOperativaE.setCod_sede_operativa( jsonObject.getInt( "cod_sede_operativa" ) );
                        sedeOperativaE.setSede_operativa( jsonObject.getString( "sede_operativa" ) );

                        localE.setCod_sede_operativa( jsonObject.getInt("cod_sede_operativa") );
                        localE.setCod_local_sede( jsonObject.getInt( "cod_local_sede" ) );
                        localE.setNombreLocal( jsonObject.getString("nombreLocal") );
                        localE.setDireccion( jsonObject.getString( "direccion" ) );
                        localE.setNaula_t( jsonObject.getInt( "naula_t" ) );
                        localE.setNaula_n( jsonObject.getInt( "naula_n" ) );
                        localE.setNaula_discapacidad( jsonObject.getInt( "naula_discapacidad" ) );
                        localE.setNaula_contingencia( jsonObject.getInt( "naula_contingencia" ) );
                        localE.setNficha( jsonObject.getInt( "nficha" ) );
                        localE.setNcartilla( jsonObject.getInt( "ncartilla" ) );

                        usuarioLocalE.setIdUsuario( jsonObject.getInt( "idUsuario") );
                        usuarioLocalE.setUsuario(jsonObject.getString( "usuario" ) );
                        usuarioLocalE.setClave( jsonObject.getString( "clave") );
                        usuarioLocalE.setRol( jsonObject.getInt( "rol" ) );
                        usuarioLocalE.setCod_sede_operativa( jsonObject.getInt( "cod_sede_operativa") );
                        usuarioLocalE.setCod_local_sede( jsonObject.getInt( "cod_local_sede" ) );

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

        dbHelper = DBHelper.getUtilDb( this.context );

        try
        {
            dbHelper.openDataBase();
            dbHelper.beginTransaction();

            dbHelper.getDatabase().delete( "usuario_local", null, null );
            Log.e( TAG, "Se elimino usuario_local!" );

            for ( int i = 0; i < usuarioLocalEArrayList.size(); i++ )
            {
                /*Integer cod_sede_operativa = usuarioLocalEArrayList.get(i).getLocalE().getSedeOperativaE().getCod_sede_operativa();
                Integer cod_local_sede = usuarioLocalEArrayList.get(i).getLocalE().getCod_local_sede();*/

                ContentValues contentSedeOperativa = new ContentValues();
                /*contentSedeOperativa.put( "cod_sede_operativa", cod_sede_operativa );*/
                /*contentSedeOperativa.put( "sede_operativa", usuarioLocalEArrayList.get(i).getLocalE().getSedeOperativaE().getSede_operativa() );*/

                ContentValues contentLocal =  new ContentValues();
                /*contentLocal.put( "cod_sede_operativa", cod_sede_operativa );
                contentLocal.put( "cod_local_sede", cod_local_sede );*/
                /*contentLocal.put( "nombreLocal", usuarioLocalEArrayList.get(i).getLocalE().getNombreLocal() );
                contentLocal.put( "direccion", usuarioLocalEArrayList.get(i).getLocalE().getDireccion() );
                contentLocal.put( "naula_t", usuarioLocalEArrayList.get(i).getLocalE().getNaula_t() );
                contentLocal.put( "naula_n", usuarioLocalEArrayList.get(i).getLocalE().getNaula_n() );
                contentLocal.put( "naula_discapacidad", usuarioLocalEArrayList.get(i).getLocalE().getNaula_discapacidad() );
                contentLocal.put( "naula_contingencia", usuarioLocalEArrayList.get(i).getLocalE().getNaula_contingencia() );
                contentLocal.put( "nficha", usuarioLocalEArrayList.get(i).getLocalE().getNficha() );
                contentLocal.put( "ncartilla", usuarioLocalEArrayList.get(i).getLocalE().getNcartilla() );

                SQL = "SELECT * FROM local WHERE cod_sede_operativa = " + cod_sede_operativa + " AND cod_local_sede = " + cod_local_sede;*/
                cursor = dbHelper.getDatabase().rawQuery( SQL, null );

                if ( cursor.moveToFirst() )
                {
                    /*String Where = "cod_sede_operativa = " + cod_sede_operativa + " AND cod_local_sede = " + cod_local_sede;*/

                    /*Integer exitoLocal = dbHelper.getDatabase().updateWithOnConflict( "local", contentLocal, Where, null, SQLiteDatabase.CONFLICT_IGNORE );
                    Log.e( TAG, "Local update : " + String.valueOf(exitoLocal) );*/
                }
                else
                {
                    ResetBD();

                    Long exitoSedeOperativa = dbHelper.getDatabase().insertOrThrow( "sede_operativa", null, contentSedeOperativa );
                    Log.e( TAG, "sede_operativa insert : " + String.valueOf(exitoSedeOperativa) );

                    Long exitoLocal = dbHelper.getDatabase().insertOrThrow( "local", null, contentLocal );
                    Log.e( TAG, "Local insert : " + String.valueOf(exitoLocal) );
                }

                ContentValues contentUsuarioLocal = new ContentValues();
                contentUsuarioLocal.put( "idUsuario", usuarioLocalEArrayList.get(i).getIdUsuario() );
                contentUsuarioLocal.put( "usuario", usuarioLocalEArrayList.get(i).getUsuario() );
                contentUsuarioLocal.put( "clave", usuarioLocalEArrayList.get(i).getClave() );
                contentUsuarioLocal.put( "rol", usuarioLocalEArrayList.get(i).getRol() );
                /*contentUsuarioLocal.put( "cod_sede_operativa", cod_sede_operativa );
                contentUsuarioLocal.put( "cod_local_sede", cod_local_sede );*/

                Long exito = dbHelper.getDatabase().insertOrThrow( "usuario_local", null, contentUsuarioLocal );
                Log.e( TAG, "usuario_local insert : " + String.valueOf(exito) );

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

    public void ResetBD() {

        Log.e( TAG, "start ResetBD" );

        try
        {
            dbHelper.getDatabase().delete( "docentes", null, null );
            Log.e( TAG, "Se elimino docentes!" );

            dbHelper.getDatabase().delete( "aula_local", null, null );
            Log.e( TAG, "Se elimino aula_local!" );

            dbHelper.getDatabase().delete( "local", null, null );
            Log.e( TAG, "Se elimino usuario_local!" );

            dbHelper.getDatabase().delete( "sede_operativa", null, null );
            Log.e( TAG, "Se elimino sede_operativa!" );

            dbHelper.getDatabase().delete( "instrumento", null, null );
            Log.e( TAG, "Se elimino instrumento!" );

            dbHelper.getDatabase().delete( "version", null, null );
            Log.e( TAG, "Se elimino version!" );

        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e( TAG, "error ResetBD : " + e.toString() );
        }

        Log.e( TAG, "end ResetBD" );

    }

}