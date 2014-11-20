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

    String URL_Connect = ConstantsUtils.BASE_URL + "acces.php"; // "acces.php" "logeo"

    Integer error = 0;
    String SQL = "";
    String Where = "";
    Integer cod_sede_operativa, cod_local_sede;

    Context context;
    Cursor cursor = null;
    ContentValues contentValues = null;

    DBHelper dbHelper;
    HttpPostAux posteo = new HttpPostAux();

    ArrayList<UsuarioLocalE> usuarioLocalEArrayList;
    ArrayList<LocalE> localEArrayList;

    public LoginDAO( Context context ) {
        this.context = context;
        Log.e( TAG, "start" );
    }

    public Integer CheckLogin( String password ) {

        Log.e( TAG, "start CheckLogin" );

        ArrayList<NameValuePair> parametersPost = new ArrayList<NameValuePair>();
        parametersPost.add( new BasicNameValuePair( "password", password ) ); // "password" "sendPass"

        JSONArray jsonArray = posteo.getServerData( parametersPost, URL_Connect );

        if ( jsonArray != null )
        {
            if ( jsonArray.length() > 0 )
            {
                try
                {
                    JSONObject jsonObject;

                    for ( int i = 0; i < jsonArray.length(); i++ )
                    {
                        SedeOperativaE sedeOperativaE = new SedeOperativaE();
                        jsonObject = (JSONObject) jsonArray.get(i);

                        sedeOperativaE.setCod_sede_operativa( jsonObject.getInt( "cod_sede_operativa" ) );
                        sedeOperativaE.setSede_operativa( jsonObject.getString( "sede_operativa" ) );

                        localEArrayList = new ArrayList<LocalE>();

                        for ( int j = 0; j < jsonArray.length(); j++ )
                        {
                            LocalE localE = new LocalE();
                            jsonObject = (JSONObject) jsonArray.get(j);

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

                            usuarioLocalEArrayList = new ArrayList<UsuarioLocalE>();

                            for ( int x = 0; x < jsonArray.length(); x++ )
                            {
                                UsuarioLocalE usuarioLocalE = new UsuarioLocalE();
                                jsonObject = (JSONObject) jsonArray.get(x);

                                usuarioLocalE.setIdUsuario( jsonObject.getInt( "idUsuario") );
                                usuarioLocalE.setUsuario(jsonObject.getString( "usuario" ) );
                                usuarioLocalE.setClave( jsonObject.getString( "clave") );
                                usuarioLocalE.setRol( jsonObject.getInt( "rol" ) );
                                usuarioLocalE.setCod_sede_operativa( jsonObject.getInt( "cod_sede_operativa") );
                                usuarioLocalE.setCod_local_sede( jsonObject.getInt( "cod_local_sede" ) );

                                usuarioLocalEArrayList.add( usuarioLocalE );

                            }

                            localE.setUsuarioLocalEList( usuarioLocalEArrayList );

                            localEArrayList.add( localE );

                        }

                        sedeOperativaE.setLocalEList( localEArrayList );

                        this.registerLogin( sedeOperativaE );

                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Log.e( TAG, e.toString() );
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

    public void registerLogin( SedeOperativaE sedeOperativaE ) {

        Log.e( TAG, "start registerLogin" );

        dbHelper = DBHelper.getUtilDb( this.context );

        try
        {
            dbHelper.openDataBase();
            dbHelper.beginTransaction();

            cod_sede_operativa = sedeOperativaE.getCod_sede_operativa();

            SQL = "SELECT * FROM sede_operativa WHERE cod_sede_operativa = " + cod_sede_operativa;
            cursor = dbHelper.getDatabase().rawQuery( SQL, null );

            contentValues = new ContentValues();
            contentValues.put( "cod_sede_operativa", cod_sede_operativa );
            contentValues.put( "sede_operativa", sedeOperativaE.getSede_operativa() );

            if ( cursor.moveToFirst() )
            {
                Where = "cod_sede_operativa = " + cod_sede_operativa;

                Integer exitoSedeOperativa = dbHelper.getDatabase().updateWithOnConflict( "sede_operativa", contentValues, Where, null, SQLiteDatabase.CONFLICT_IGNORE );
                Log.e( TAG, "sede_operativa update : " + String.valueOf(exitoSedeOperativa) );
            }
            else
            {
                this.CleanBD( 1 );/* Login con diferente sede operativa */

                Long exitoSedeOperativa = dbHelper.getDatabase().insertOrThrow( "sede_operativa", null, contentValues );
                Log.e( TAG, "sede_operativa insert : " + String.valueOf(exitoSedeOperativa) );

            }

            for ( LocalE localE : sedeOperativaE.getLocalEList() )
            {
                cod_local_sede = localE.getCod_local_sede();

                contentValues =  new ContentValues();
                contentValues.put( "cod_sede_operativa", cod_sede_operativa );
                contentValues.put( "cod_local_sede", cod_local_sede );
                contentValues.put( "nombreLocal", localE.getNombreLocal() );
                contentValues.put( "direccion", localE.getDireccion() );
                contentValues.put( "naula_t", localE.getNaula_t() );
                contentValues.put( "naula_n", localE.getNaula_n() );
                contentValues.put( "naula_discapacidad", localE.getNaula_discapacidad() );
                contentValues.put( "naula_contingencia", localE.getNaula_contingencia() );
                contentValues.put( "nficha", localE.getNficha() );
                contentValues.put( "ncartilla", localE.getNcartilla() );

                SQL = "SELECT * FROM local WHERE cod_sede_operativa = " + cod_sede_operativa + " and cod_local_sede = " + cod_local_sede;
                cursor = dbHelper.getDatabase().rawQuery( SQL, null );

                if ( cursor.moveToFirst() )
                {
                    Where = "cod_sede_operativa = " + cod_sede_operativa + " AND cod_local_sede = " + cod_local_sede;

                    Integer exitoLocal = dbHelper.getDatabase().updateWithOnConflict( "local", contentValues, Where, null, SQLiteDatabase.CONFLICT_IGNORE );
                    Log.e( TAG, "Local update : " + String.valueOf(exitoLocal) );
                }
                else
                {

                    this.CleanBD( 2 );/* Login con diferente local */

                    Long exitoLocal = dbHelper.getDatabase().insertOrThrow( "local", null, contentValues );
                    Log.e( TAG, "Local insert : " + String.valueOf(exitoLocal) );
                }


                dbHelper.getDatabase().delete( "usuario_local", null, null );
                Log.e( TAG, "Se elimino usuario_local!" );

                for ( UsuarioLocalE usuarioLocalE : localE.getUsuarioLocalEList() )
                {

                    contentValues = new ContentValues();
                    contentValues.put( "idUsuario", usuarioLocalE.getIdUsuario() );
                    contentValues.put( "usuario", usuarioLocalE.getUsuario() );
                    contentValues.put( "clave", usuarioLocalE.getClave() );
                    contentValues.put( "rol", usuarioLocalE.getRol() );
                    contentValues.put( "cod_sede_operativa", cod_sede_operativa );
                    contentValues.put( "cod_local_sede", cod_local_sede );

                    Long exitoUsuarioLocal = dbHelper.getDatabase().insertOrThrow( "usuario_local", null, contentValues );
                    Log.e( TAG, "usuario_local insert : " + String.valueOf(exitoUsuarioLocal) );

                }

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

    public void CleanBD( Integer CaseOfClean ) {

        Log.e( TAG, "start CleanBD" );

        try
        {

            // Tablas Dependientes //
            dbHelper.getDatabase().delete( "docentes", null, null );
            Log.e( TAG, "Se elimino docentes!" );

            dbHelper.getDatabase().delete( "aula_local", null, null );
            Log.e( TAG, "Se elimino aula_local!" );

            dbHelper.getDatabase().delete( "local", null, null );
            Log.e( TAG, "Se elimino usuario_local!" );

            dbHelper.getDatabase().delete( "instrumento", null, null );
            Log.e( TAG, "Se elimino instrumento!" );

            if ( CaseOfClean == 1 )
            {
                dbHelper.getDatabase().delete( "sede_operativa", null, null );
                Log.e( TAG, "Se elimino sede_operativa!" );
            }

            // Tablas Independientes //
            dbHelper.getDatabase().delete( "discapacidad", null, null );
            Log.e( TAG, "Se elimino discapacidad!" );

            dbHelper.getDatabase().delete( "modalidad", null, null );
            Log.e( TAG, "Se elimino modalidad!" );

            dbHelper.getDatabase().delete( "version", null, null );
            Log.e( TAG, "Se elimino version!" );

        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e( TAG, "error CleanBD : " + e.toString() );
        }

        Log.e( TAG, "end CleanBD" );

    }

}