package ordanel.ednom.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import ordanel.ednom.BD.DBHelper;
import ordanel.ednom.Entity.AulaLocalE;
import ordanel.ednom.Entity.DocentesE;
import ordanel.ednom.Library.ConstantsUtils;

/**
 * Created by OrdNael on 05/11/2014.
 */
public class LocalDAO {

    private static final String TAG = LocalDAO.class.getSimpleName();
    String SQL;
    Integer valueInteger;

    DBHelper dbHelper;

    ConstantsUtils constantsUtils;

    Context context;
    Cursor cursor = null;
    ContentValues contentValues = null;

    public LocalDAO( Context context ) {
        this.context = context;
    }

    public DocentesE searchPerson( String conditional ) {

        Log.e( TAG, "start searchPerson" );

        dbHelper = DBHelper.getUtilDb( this.context );

        DocentesE docentesE = new DocentesE();

        try
        {
            dbHelper.openDataBase();
            dbHelper.beginTransaction();

            String SQL = "SELECT nro_doc, ape_pat, ape_mat, nombres, nro_aula FROM docentes WHERE " + conditional;
            Log.e( TAG, "string sql : " + SQL );
            cursor = dbHelper.getDatabase().rawQuery( SQL, null );

            if ( cursor.moveToFirst() )
            {
                while ( !cursor.isAfterLast() )
                {

                    AulaLocalE aulaLocalE = new AulaLocalE();
                    aulaLocalE.setNro_aula( cursor.getInt( cursor.getColumnIndex( "nro_aula" ) ) );

                    docentesE.setNro_doc( cursor.getString( cursor.getColumnIndex( "nro_doc" ) ) );
                    docentesE.setApe_pat( cursor.getString( cursor.getColumnIndex( "ape_pat" ) ) );
                    docentesE.setApe_mat( cursor.getString( cursor.getColumnIndex( "ape_mat" ) ) );
                    docentesE.setNombres( cursor.getString( cursor.getColumnIndex( "nombres" ) ) );
                    docentesE.setAulaLocalE( aulaLocalE );

                    cursor.moveToNext();
                }

                docentesE.setStatus( 0 );
            }
            else
            {
                docentesE.setStatus( 1 );// alerta. sin datos;
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            docentesE.setStatus( 2 ); // error al acceder.
        }
        finally
        {
            dbHelper.endTransaction();
            dbHelper.close();
            cursor.close();
        }

        Log.e( TAG, "end searchPerson" );

        return docentesE;

    }

    public Integer asistenciaLocal( String number_doc ) {

        Log.e( TAG, "start asistenciaLocal" );

        dbHelper = DBHelper.getUtilDb( this.context );

        try
        {
            dbHelper.openDataBase();
            dbHelper.beginTransaction();

            contentValues =  new ContentValues();
            contentValues.put( "estado", 1 );
            contentValues.put( "f_registro", constantsUtils.fecha_registro() );

            SQL = "nro_doc = '" + number_doc + "'";

            valueInteger = dbHelper.getDatabase().updateWithOnConflict( "docentes", contentValues, SQL, null, SQLiteDatabase.CONFLICT_IGNORE );
            Log.e( TAG, "asistencia local : " + valueInteger.toString() );

            valueInteger = 0;

            dbHelper.setTransactionSuccessful();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            valueInteger = 3;// error al registra asistencia al local;
        }
        finally
        {
            dbHelper.endTransaction();
            dbHelper.close();
        }

        Log.e( TAG, "end asistenciaLocal" );

        return valueInteger;
    }

    /*public ArrayList<PadronE> searchPerson( String number_dni ) {

        dbHelper = DBHelper.getUtilDb( this.context );

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

                    *//*padronE.setIns_numdoc( cursor.getString( cursor.getColumnIndex( "ins_numdoc" ) ) );
                    padronE.setApepat( cursor.getString( cursor.getColumnIndex( "apepat" ) ) );
                    padronE.setApemat( cursor.getString( cursor.getColumnIndex( "apemat" ) ) );
                    padronE.setNombres( cursor.getString( cursor.getColumnIndex( "nombres" ) ) );
                    padronE.setLocal_aplicacion( cursor.getString( cursor.getColumnIndex( "local_aplicacion" ) ) );
                    padronE.setAula( cursor.getString( cursor.getColumnIndex( "aula" ) ) );*//*

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

    }*/

    /*public ArrayList<PadronE> showPerson( String number_dni ) {

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

                    *//*padronE.setIns_numdoc( cursor.getString( cursor.getColumnIndex( "ins_numdoc" ) ) );
                    padronE.setApepat( cursor.getString( cursor.getColumnIndex( "apepat" ) ) );
                    padronE.setApemat( cursor.getString( cursor.getColumnIndex( "apemat" ) ) );
                    padronE.setNombres( cursor.getString( cursor.getColumnIndex( "nombres" ) ) );
                    padronE.setLocal_aplicacion( cursor.getString( cursor.getColumnIndex( "local_aplicacion" ) ) );
                    padronE.setAula( cursor.getString( cursor.getColumnIndex( "aula" ) ) );*//*

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
    }*/

}