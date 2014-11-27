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

    String URL_Connect = ConstantsUtils.BASE_URL + "acces.php"; // "acces.php" "logeo"
    Integer cod_sede_operativa, cod_local_sede;
    String SQL, Where;
    Integer valueInteger;
    Long valueLong;

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
        parametersPost.add( new BasicNameValuePair( "password", password ) ); // "password" "sendPass"

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

                        sedeOperativaE.setCod_sede_operativa( jsonObject.getInt( sedeOperativaE.COD_SEDE_OPERATIVA ) );
                        sedeOperativaE.setSede_operativa( jsonObject.getString( sedeOperativaE.SEDE_OPERATIVA ) );

                        localEArrayList = new ArrayList<LocalE>();

                        // set array LOCAL
                        for ( int j = 0; j < jsonArray.length(); j++ )
                        {
                            LocalE localE = new LocalE();
                            jsonObject = (JSONObject) jsonArray.get(j);

                            localE.setSedeOperativaE( sedeOperativaE );
                            localE.setCod_local_sede( jsonObject.getInt( localE.COD_LOCAL_SEDE ) );
                            localE.setNombreLocal( jsonObject.getString( localE.NOMBRE_LOCAL ) );
                            localE.setDireccion( jsonObject.getString( localE.DIRECCION ) );
                            localE.setNaula_t( jsonObject.getInt( localE.NAULA_T ) );
                            localE.setNaula_n( jsonObject.getInt( localE.NAULA_N ) );
                            localE.setNaula_discapacidad( jsonObject.getInt( localE.NAULA_DISCAPACIDAD ) );
                            localE.setNaula_contingencia( jsonObject.getInt( localE.NAULA_CONTINGENCIA ) );
                            localE.setNficha( jsonObject.getInt( localE.NFICHA ) );
                            localE.setNcartilla( jsonObject.getInt( localE.NCARTILLA ) );

                            usuarioLocalEArrayList = new ArrayList<UsuarioLocalE>();


                            // set array USUARIO_LOCAL
                            for ( int x = 0; x < jsonArray.length(); x++ )
                            {
                                UsuarioLocalE usuarioLocalE = new UsuarioLocalE();
                                jsonObject = (JSONObject) jsonArray.get(x);

                                usuarioLocalE.setIdUsuario( jsonObject.getInt( usuarioLocalE.IDUSUARIO ) );
                                usuarioLocalE.setUsuario(jsonObject.getString( usuarioLocalE.USUARIO ) );
                                usuarioLocalE.setClave( jsonObject.getString( usuarioLocalE.CLAVE ) );
                                usuarioLocalE.setRol( jsonObject.getInt( usuarioLocalE.ROL ) );
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
            contentValues.put( sedeOperativaE.COD_SEDE_OPERATIVA , cod_sede_operativa );
            contentValues.put( sedeOperativaE.SEDE_OPERATIVA , paramSedeOperativaE.getSede_operativa() );

            if ( cursor.moveToFirst() )
            {
                Where = "cod_sede_operativa = " + cod_sede_operativa;

                valueInteger = dbHelper.getDatabase().updateWithOnConflict( "sede_operativa", contentValues, Where, null, SQLiteDatabase.CONFLICT_IGNORE );
                Log.e( TAG, "sede_operativa update : " + String.valueOf(valueInteger) );
            }
            else
            {
                this.CleanBD( 1 );/* Login con diferente sede operativa */

                valueLong = dbHelper.getDatabase().insertOrThrow( "sede_operativa", null, contentValues );
                Log.e( TAG, "sede_operativa insert : " + String.valueOf(valueLong) );

            }
            // .registro de SEDE_OPERATIVA


            // registro de LOCAL
            for ( LocalE localE : paramSedeOperativaE.getLocalEList() )
            {
                cod_local_sede = localE.getCod_local_sede();

                contentValues =  new ContentValues();
                contentValues.put( localE.getSedeOperativaE().COD_SEDE_OPERATIVA, cod_sede_operativa );
                contentValues.put( localE.COD_LOCAL_SEDE, cod_local_sede );
                contentValues.put( localE.NOMBRE_LOCAL, localE.getNombreLocal() );
                contentValues.put( localE.DIRECCION, localE.getDireccion() );
                contentValues.put( localE.NAULA_T, localE.getNaula_t() );
                contentValues.put( localE.NAULA_N, localE.getNaula_n() );
                contentValues.put( localE.NAULA_DISCAPACIDAD, localE.getNaula_discapacidad() );
                contentValues.put( localE.NAULA_CONTINGENCIA, localE.getNaula_contingencia() );
                contentValues.put( localE.NFICHA, localE.getNficha() );
                contentValues.put( localE.NCARTILLA, localE.getNcartilla() );

                SQL = "SELECT * FROM local WHERE cod_sede_operativa = " + cod_sede_operativa + " and cod_local_sede = " + cod_local_sede;
                cursor = dbHelper.getDatabase().rawQuery( SQL, null );

                if ( cursor.moveToFirst() )
                {
                    Where = "cod_sede_operativa = " + cod_sede_operativa + " AND cod_local_sede = " + cod_local_sede;

                    valueInteger = dbHelper.getDatabase().updateWithOnConflict( "local", contentValues, Where, null, SQLiteDatabase.CONFLICT_IGNORE );
                    Log.e( TAG, "Local update : " + String.valueOf(valueInteger) );
                }
                else
                {

                    this.CleanBD( 2 );/* Login con diferente local */

                    valueLong = dbHelper.getDatabase().insertOrThrow( "local", null, contentValues );
                    Log.e( TAG, "Local insert : " + String.valueOf(valueLong) );
                }


                // registro de USUARIO_LOCAL
                dbHelper.getDatabase().delete( "usuario_local", null, null );
                Log.e( TAG, "Se elimino usuario_local!" );

                for ( UsuarioLocalE usuarioLocalE : localE.getUsuarioLocalEList() )
                {

                    contentValues = new ContentValues();
                    contentValues.put( usuarioLocalE.IDUSUARIO, usuarioLocalE.getIdUsuario() );
                    contentValues.put( usuarioLocalE.USUARIO, usuarioLocalE.getUsuario() );
                    contentValues.put( usuarioLocalE.CLAVE, usuarioLocalE.getClave() );
                    contentValues.put( usuarioLocalE.ROL, usuarioLocalE.getRol() );
                    contentValues.put( sedeOperativaE.COD_SEDE_OPERATIVA, cod_sede_operativa );
                    contentValues.put( localE.COD_LOCAL_SEDE, cod_local_sede );

                    valueLong = dbHelper.getDatabase().insertOrThrow( "usuario_local", null, contentValues );
                    Log.e( TAG, "usuario_local insert : " + String.valueOf(valueLong) );

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
                    cod_sede_operativa = cursor.getInt( cursor.getColumnIndex( sedeOperativaE.COD_SEDE_OPERATIVA ) );
                    sedeOperativaE.setCod_sede_operativa( cod_sede_operativa );
                    sedeOperativaE.setSede_operativa( cursor.getString( cursor.getColumnIndex( sedeOperativaE.SEDE_OPERATIVA ) ) );


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

                        cod_local_sede = cursorLocal.getInt( cursorLocal.getColumnIndex( localE.COD_LOCAL_SEDE ) );
                        localE.setCod_local_sede( cod_local_sede );
                        localE.setNombreLocal( cursorLocal.getString( cursorLocal.getColumnIndex( localE.NOMBRE_LOCAL ) ) );
                        localE.setDireccion( cursorLocal.getString( cursorLocal.getColumnIndex( localE.DIRECCION ) ) );
                        localE.setNaula_t( cursorLocal.getInt( cursorLocal.getColumnIndex( localE.NAULA_T ) ) );
                        localE.setNaula_n( cursorLocal.getInt( cursorLocal.getColumnIndex( localE.NAULA_N ) ) );
                        localE.setNaula_discapacidad( cursorLocal.getInt( cursorLocal.getColumnIndex( localE.NAULA_DISCAPACIDAD ) ) );
                        localE.setNaula_contingencia( cursorLocal.getInt( cursorLocal.getColumnIndex( localE.NAULA_CONTINGENCIA ) ) );
                        localE.setNficha( cursorLocal.getInt( cursorLocal.getColumnIndex( localE.NFICHA ) ) );
                        localE.setNcartilla( cursorLocal.getInt( cursorLocal.getColumnIndex( localE.NCARTILLA ) ) );


                        // set array USUARIO_LOCAL
                        SQL = "SELECT idUsuario, usuario, rol FROM usuario_local WHERE cod_sede_operativa = " + cod_sede_operativa + " and cod_local_sede = " + cod_local_sede;
                        Cursor cursorUsuarioLocal = dbHelper.getDatabase().rawQuery( SQL, null );

                        usuarioLocalEArrayList = new ArrayList<UsuarioLocalE>();

                        cursorUsuarioLocal.moveToFirst();
                        while ( !cursorUsuarioLocal.isAfterLast() )
                        {
                            UsuarioLocalE usuarioLocalE = new UsuarioLocalE();

                            usuarioLocalE.setIdUsuario( cursorUsuarioLocal.getInt( cursorUsuarioLocal.getColumnIndex( usuarioLocalE.IDUSUARIO ) ) );
                            usuarioLocalE.setUsuario( cursorUsuarioLocal.getString( cursorUsuarioLocal.getColumnIndex( usuarioLocalE.USUARIO ) ) );
                            usuarioLocalE.setRol( cursorUsuarioLocal.getInt( cursorUsuarioLocal.getColumnIndex( usuarioLocalE.ROL ) ) );

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