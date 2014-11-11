package ordanel.ednom.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ordanel.ednom.BD.DBHelper;
import ordanel.ednom.Entity.PadronE;

/**
 * Created by OrdNael on 05/11/2014.
 */
public class LocalDAO {

    private static final String TAG = LocalDAO.class.getSimpleName();

    ArrayList<PadronE> arrayList;

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


                    /*SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date();
                    ContentValues initialValues = new ContentValues();
                    initialValues.put("date_created", dateFormat.format(date));
                    long rowId = mDb.insert(DATABASE_TABLE, null, initialValues);*/



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

}