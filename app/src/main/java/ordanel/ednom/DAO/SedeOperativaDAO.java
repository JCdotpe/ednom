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

import ordanel.ednom.Entity.LocalE;
import ordanel.ednom.Entity.SedeOperativaE;
import ordanel.ednom.Entity.UsuarioLocalE;
import ordanel.ednom.Library.ConstantsUtils;

/**
 * Created by Leandro on 21/11/2014.
 */
public class SedeOperativaDAO extends BaseDAO {

    private static final String TAG = SedeOperativaDAO.class.getSimpleName();
    private static SedeOperativaDAO sedeOperativaDAO;

    String URL_Connect = ConstantsUtils.BASE_URL + "logeo"; // "acces.php" "logeo"
    Integer cod_sede_operativa, cod_local_sede;
    String SQL, Where;

    Cursor cursor = null;
    ContentValues contentValues = null;

    JSONObject jsonObject;
    JSONArray jsonArray;

    SedeOperativaE sedeOperativaE;

    ArrayList<LocalE> localEArrayList;
    ArrayList<UsuarioLocalE> usuarioLocalEArrayList;

    public static SedeOperativaDAO getInstance( Context paramContext ) {

        if ( sedeOperativaDAO == null )
        {
            sedeOperativaDAO = new SedeOperativaDAO( paramContext );
        }

        return sedeOperativaDAO;
    }

    private SedeOperativaDAO( Context paramContext ) {

        initDBHelper( paramContext );
        initHttPostAux();

    }

    public SedeOperativaE CheckLogin( String password ) {

        Log.e( TAG, "start CheckLogin" );

        ArrayList<NameValuePair> parametersPost = new ArrayList<NameValuePair>();
        parametersPost.add( new BasicNameValuePair( "sendPass", password ) ); // "password" "sendPass"

        jsonArray = httpPostAux.getServerData( parametersPost, URL_Connect );

        sedeOperativaE = new SedeOperativaE();

        if ( jsonArray != null )
        {
            if ( jsonArray.length() > 0 )
            {
                try
                {
                    // set array SEDE_OPERATIVA
                    for ( int i = 0; i < jsonArray.length(); i++ )
                    {
                        jsonObject = (JSONObject) jsonArray.get(i);

                        sedeOperativaE.setCod_sede_operativa( jsonObject.getInt( "cod_sede_operativa" ) );
                        sedeOperativaE.setSede_operativa( jsonObject.getString( "sede_operativa" ) );

                        localEArrayList = new ArrayList<LocalE>();

                        // set array LOCAL
                        for ( int j = 0; j < jsonArray.length(); j++ )
                        {
                            LocalE localE = new LocalE();
                            jsonObject = (JSONObject) jsonArray.get(j);

                            localE.setSedeOperativaE( sedeOperativaE );
                            localE.setCod_local_sede( jsonObject.getInt( "cod_local_sede" ) );
                            localE.setNombreLocal( jsonObject.getString( "nombreLocal" ) );
                            localE.setDireccion( jsonObject.getString( "direccion" ) );
                            localE.setNaula_t( jsonObject.getInt( "naula_t" ) );
                            localE.setNaula_n( jsonObject.getInt( "naula_n" ) );
                            localE.setNaula_discapacidad( jsonObject.getInt( "naula_discapacidad" ) );
                            localE.setNaula_contingencia( jsonObject.getInt( "naula_contingencia" ) );
                            localE.setNficha( jsonObject.getInt( "nficha" ) );
                            localE.setNcartilla( jsonObject.getInt( "ncartilla" ) );

                            usuarioLocalEArrayList = new ArrayList<UsuarioLocalE>();


                            // set array USUARIO_LOCAL
                            for ( int x = 0; x < jsonArray.length(); x++ )
                            {
                                UsuarioLocalE usuarioLocalE = new UsuarioLocalE();
                                jsonObject = (JSONObject) jsonArray.get(x);

                                usuarioLocalE.setIdUsuario( jsonObject.getInt( "idUsuario") );
                                usuarioLocalE.setUsuario(jsonObject.getString( "usuario" ) );
                                usuarioLocalE.setClave( jsonObject.getString( "clave") );
                                usuarioLocalE.setRol( jsonObject.getInt( "rol" ) );
                                usuarioLocalE.setLocalE( localE );

                                usuarioLocalEArrayList.add( usuarioLocalE );

                            }

                            localE.setUsuarioLocalEList( usuarioLocalEArrayList );
                            // .set array USUARIO_LOCAL

                            localEArrayList.add( localE );

                        }

                        sedeOperativaE.setLocalEList( localEArrayList );
                        // .set array LOCAL

                        sedeOperativaE.setStatus( 0 );

                    }
                    // .set array SEDE_OPERATIVA

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Log.e( TAG, e.toString() );
                    sedeOperativaE.setStatus( 3 ); // error en el CheckLogin //
                }
            }
            else
            {
                sedeOperativaE.setStatus( 2 ); // error en el Password //
            }
        }
        else
        {
            sedeOperativaE.setStatus( 1 ); // error en el HttpPostAux //
        }
        Log.e( TAG, "end CheckLogin" );

        return sedeOperativaE;

    }

    public SedeOperativaE registerLogin( SedeOperativaE paramSedeOperativaE ) {

        Log.e( TAG, "start registerLogin" );

        try
        {
            openDBHelper();

            cod_sede_operativa = paramSedeOperativaE.getCod_sede_operativa();

            // registro de SEDE_OPERATIVA
            SQL = "SELECT * FROM sede_operativa WHERE cod_sede_operativa = " + cod_sede_operativa;
            cursor = dbHelper.getDatabase().rawQuery( SQL, null );

            contentValues = new ContentValues();
            contentValues.put( "cod_sede_operativa", cod_sede_operativa );
            contentValues.put( "sede_operativa", paramSedeOperativaE.getSede_operativa() );

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
            // .registro de SEDE_OPERATIVA


            // registro de LOCAL
            for ( LocalE localE : paramSedeOperativaE.getLocalEList() )
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


                // registro de USUARIO_LOCAL
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
                // .registro de USUARIO_LOCAL

            }
            // registro de LOCAL

            sedeOperativaE.setStatus( 0 );

            dbHelper.setTransactionSuccessful();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e( TAG, "error registerLogin : " + e.toString() );
            sedeOperativaE.setStatus( 4 ); // error en el registerLogin //
        }
        finally
        {
            closeDBHelper();
        }

        Log.e( TAG, "end registerLogin" );

        return sedeOperativaE;

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

    public SedeOperativaE showInfo() {

        Log.e(TAG, "start showInfo");

        sedeOperativaE = new SedeOperativaE();

        try
        {
            openDBHelper();

            String SQL = "SELECT cod_sede_operativa, sede_operativa FROM sede_operativa";
            cursor = dbHelper.getDatabase().rawQuery( SQL, null );

            // set data SEDE_OPERATIVA
            if ( cursor.moveToFirst() )
            {

                while ( !cursor.isAfterLast() )
                {
                    cod_sede_operativa = cursor.getInt( cursor.getColumnIndex( "cod_sede_operativa" ) );
                    sedeOperativaE.setCod_sede_operativa( cod_sede_operativa );
                    sedeOperativaE.setSede_operativa( cursor.getString( cursor.getColumnIndex( "sede_operativa" ) ) );


                    // set array LOCAL
                    SQL = "SELECT cod_local_sede, nombreLocal, direccion, naula_t, naula_n, naula_discapacidad, naula_contingencia, nficha, ncartilla " +
                            "FROM local " +
                            "WHERE cod_sede_operativa = " + cod_sede_operativa;
                    Cursor cursorLocal = dbHelper.getDatabase().rawQuery( SQL, null );

                    localEArrayList = new ArrayList<LocalE>();

                    cursorLocal.moveToFirst();
                    while ( !cursorLocal.isAfterLast() )
                    {
                        LocalE localE = new LocalE();

                        cod_local_sede = cursorLocal.getInt( cursorLocal.getColumnIndex( "cod_local_sede" ) );
                        localE.setCod_local_sede( cod_local_sede );
                        localE.setNombreLocal( cursorLocal.getString( cursorLocal.getColumnIndex( "nombreLocal" ) ) );
                        localE.setDireccion( cursorLocal.getString( cursorLocal.getColumnIndex( "direccion" ) ) );
                        localE.setNaula_t( cursorLocal.getInt( cursorLocal.getColumnIndex( "naula_t" ) ) );
                        localE.setNaula_n( cursorLocal.getInt( cursorLocal.getColumnIndex( "naula_n" ) ) );
                        localE.setNaula_discapacidad( cursorLocal.getInt( cursorLocal.getColumnIndex("naula_discapacidad") ) );
                        localE.setNaula_contingencia( cursorLocal.getInt( cursorLocal.getColumnIndex( "naula_contingencia" ) ) );
                        localE.setNficha( cursorLocal.getInt( cursorLocal.getColumnIndex( "nficha" ) ) );
                        localE.setNcartilla( cursorLocal.getInt( cursorLocal.getColumnIndex( "ncartilla" ) ) );


                        // set array USUARIO_LOCAL
                        SQL = "SELECT idUsuario, usuario, rol FROM usuario_local WHERE cod_sede_operativa = " + cod_sede_operativa + " and cod_local_sede = " + cod_local_sede;
                        Cursor cursorUsuarioLocal = dbHelper.getDatabase().rawQuery( SQL, null );

                        usuarioLocalEArrayList = new ArrayList<UsuarioLocalE>();

                        cursorUsuarioLocal.moveToFirst();
                        while ( !cursorUsuarioLocal.isAfterLast() )
                        {
                            UsuarioLocalE usuarioLocalE = new UsuarioLocalE();

                            usuarioLocalE.setIdUsuario( cursorUsuarioLocal.getInt( cursorUsuarioLocal.getColumnIndex( "idUsuario" ) ) );
                            usuarioLocalE.setUsuario( cursorUsuarioLocal.getString( cursorUsuarioLocal.getColumnIndex( "usuario" ) ) );
                            usuarioLocalE.setRol( cursorUsuarioLocal.getInt( cursorUsuarioLocal.getColumnIndex( "rol" ) ) );

                            usuarioLocalEArrayList.add( usuarioLocalE );
                            cursorUsuarioLocal.moveToNext();
                        }

                        localE.setUsuarioLocalEList( usuarioLocalEArrayList );
                        // .set array USUARIO_LOCAL

                        localEArrayList.add( localE );

                        cursorLocal.moveToNext();
                    }

                    sedeOperativaE.setLocalEList( localEArrayList );
                    // .set array LOCAL

                    cursor.moveToNext();
                }
                // .set data SEDE_OPERATIVA

                sedeOperativaE.setStatus( 0 );

            }
            else
            {
                sedeOperativaE.setStatus( 2 ); // no hay datos
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e( TAG, "error showInfo : " + e.toString() );
            sedeOperativaE.setStatus( 1 ); // error en el showInfoLocal //
        }
        finally
        {
            closeDBHelper();
            cursor.close();
        }

        Log.e( TAG, "end showInfo" );

        return sedeOperativaE;

    }
}