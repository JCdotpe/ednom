package ordanel.ednom.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ordanel.ednom.BD.DBHelper;
import ordanel.ednom.Entity.LocalE;
import ordanel.ednom.Entity.PadronE;
import ordanel.ednom.Entity.SedeOperativaE;
import ordanel.ednom.Entity.UsuarioLocalE;

/**
 * Created by OrdNael on 05/11/2014.
 */
public class LocalDAO {

    private static final String TAG = LocalDAO.class.getSimpleName();

    ArrayList<PadronE> arrayList;
    ArrayList<UsuarioLocalE> usuarioLocalEArrayList;

    Context context;

    public LocalDAO( Context context ) {
        this.context = context;
    }

    public ArrayList<PadronE> searchPerson( String number_dni )
    {

        DBHelper dbHelper = DBHelper.getUtilDb( this.context );

        Cursor cursor = null;
        ContentValues contentValues = null;
        arrayList = null;

        try
        {
            dbHelper.openDataBase();
            dbHelper.beginTransaction();

            String SQL = "SELECT ins_numdoc, apepat, apemat, nombres, local_aplicacion, aula FROM postulantes2014 WHERE ins_numdoc = '" + number_dni + "'";
            cursor = dbHelper.getDatabase().rawQuery( SQL, null );

            if ( cursor.moveToFirst() )
            {
                arrayList = new ArrayList<PadronE>();

                while ( !cursor.isAfterLast() )
                {
                    PadronE padronE = new PadronE();

                    padronE.setIns_numdoc( cursor.getString( cursor.getColumnIndex( "ins_numdoc" ) ) );
                    padronE.setApepat( cursor.getString( cursor.getColumnIndex( "apepat" ) ) );
                    padronE.setApemat( cursor.getString( cursor.getColumnIndex( "apemat" ) ) );
                    padronE.setNombres( cursor.getString( cursor.getColumnIndex( "nombres" ) ) );
                    padronE.setLocal_aplicacion( cursor.getString( cursor.getColumnIndex( "local_aplicacion" ) ) );
                    padronE.setAula( cursor.getString( cursor.getColumnIndex( "aula" ) ) );

                    arrayList.add( padronE );

                    cursor.moveToNext();
                }

                if ( arrayList.size() > 0 )
                {

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date();

                    contentValues =  new ContentValues();
                    contentValues.put( "estatus", 1 );
                    contentValues.put( "fecha_registro", dateFormat.format(date) );

                    SQL = "ins_numdoc = '" + number_dni + "'";

                    Integer exito = dbHelper.getDatabase().updateWithOnConflict( "postulantes2014", contentValues, SQL, null, SQLiteDatabase.CONFLICT_IGNORE );

                }

                dbHelper.setTransactionSuccessful();

            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            dbHelper.endTransaction();
            dbHelper.close();
            cursor.close();
        }

        return arrayList;

    }

    public ArrayList<PadronE> showPerson( String number_dni ) {

        Cursor cursor = null;

        DBHelper dbHelper = DBHelper.getUtilDb( this.context );

        try
        {
            dbHelper.openDataBase();
            dbHelper.beginTransaction();

            String SQL = "SELECT ins_numdoc, apepat, apemat, nombres, local_aplicacion, aula FROM postulantes2014 WHERE ins_numdoc = '" + number_dni + "'";
            cursor = dbHelper.getDatabase().rawQuery( SQL, null );

            if ( cursor.moveToFirst() )
            {
                arrayList = new ArrayList<PadronE>();

                while ( !cursor.isAfterLast() )
                {
                    PadronE padronE = new PadronE();

                    padronE.setIns_numdoc( cursor.getString( cursor.getColumnIndex( "ins_numdoc" ) ) );
                    padronE.setApepat( cursor.getString( cursor.getColumnIndex( "apepat" ) ) );
                    padronE.setApemat( cursor.getString( cursor.getColumnIndex( "apemat" ) ) );
                    padronE.setNombres( cursor.getString( cursor.getColumnIndex( "nombres" ) ) );
                    padronE.setLocal_aplicacion( cursor.getString( cursor.getColumnIndex( "local_aplicacion" ) ) );
                    padronE.setAula( cursor.getString( cursor.getColumnIndex( "aula" ) ) );

                    arrayList.add( padronE );

                    cursor.moveToNext();

                }

            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            dbHelper.endTransaction();
            dbHelper.close();
            cursor.close();
        }

        return arrayList;
    }

    public ArrayList<UsuarioLocalE> showInfoLocal() {

        Log.e(TAG, "start showInfoLocal");

        DBHelper dbHelper = DBHelper.getUtilDb( this.context );
        Cursor cursor = null;

        try
        {
            dbHelper.openDataBase();
            dbHelper.beginTransaction();

            String SQL = "SELECT sd.cod_sede_operativa, sd.sede_operativa, l.cod_local_sede, l.nombreLocal, l.direccion, l.naula_t, l.naula_n, l.naula_discapacidad, "+
                        " l.naula_contingencia, l.nficha, l.ncartilla, ul.usuario, ul.rol " +
                        "FROM sede_operativa sd " +
                        "INNER JOIN local l " +
                        "ON sd.cod_sede_operativa = l.cod_sede_operativa " +
                        "INNER JOIN usuario_local ul " +
                        "ON l.cod_sede_operativa = ul.cod_sede_operativa and l.cod_local_sede = ul.cod_local_sede";
            cursor = dbHelper.getDatabase().rawQuery( SQL, null );

            usuarioLocalEArrayList = new ArrayList<UsuarioLocalE>();

            if ( cursor.moveToFirst() )
            {

                while ( !cursor.isAfterLast() )
                {
                    SedeOperativaE sedeOperativaE = new SedeOperativaE();
                    LocalE localE = new LocalE();
                    UsuarioLocalE usuarioLocalE = new UsuarioLocalE();


                    sedeOperativaE.setCod_sede_operativa( cursor.getInt( cursor.getColumnIndex( "cod_sede_operativa" ) ) );
                    sedeOperativaE.setSede_operativa(cursor.getString(cursor.getColumnIndex("sede_operativa")));

                    localE.setSedeOperativaE( sedeOperativaE );
                    localE.setCod_local_sede( cursor.getInt( cursor.getColumnIndex( "cod_local_sede" ) ) );
                    localE.setNombreLocal( cursor.getString( cursor.getColumnIndex( "nombreLocal" ) ) );
                    localE.setDireccion( cursor.getString( cursor.getColumnIndex( "direccion" ) ) );
                    localE.setNaula_t( cursor.getInt( cursor.getColumnIndex( "naula_t" ) ) );
                    localE.setNaula_n( cursor.getInt( cursor.getColumnIndex( "naula_n" ) ) );
                    localE.setNaula_discapacidad( cursor.getInt( cursor.getColumnIndex("naula_discapacidad") ) );
                    localE.setNaula_contingencia( cursor.getInt( cursor.getColumnIndex( "naula_contingencia" ) ) );
                    localE.setNficha( cursor.getInt( cursor.getColumnIndex( "nficha" ) ) );
                    localE.setNcartilla( cursor.getInt( cursor.getColumnIndex( "ncartilla" ) ) );

                    usuarioLocalE.setUsuario( cursor.getString( cursor.getColumnIndex( "usuario" ) ) );
                    usuarioLocalE.setRol( cursor.getInt( cursor.getColumnIndex( "rol" ) ) );
                    usuarioLocalE.setLocalE( localE );

                    usuarioLocalEArrayList.add( usuarioLocalE );

                    cursor.moveToNext();
                }

            }

            return usuarioLocalEArrayList;

        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e( TAG, "error showInfoLocal : " + e.toString() );
            return null; // error en el showInfoUser //
        }
        finally
        {
            dbHelper.endTransaction();
            dbHelper.close();
            cursor.close();

            Log.e( TAG, "start showInfoLocal" );
        }

    }

}