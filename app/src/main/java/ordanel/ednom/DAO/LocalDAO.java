package ordanel.ednom.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

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

            String SQL = "SELECT NumDoc, ApePaterno, ApeMaterno, Nombres FROM Padron WHERE NumDoc = '" + number_dni + "'";

            cursor = dbHelper.getDatabase().rawQuery( SQL, null );

            if ( cursor.moveToFirst() )
            {
                arrayList = new ArrayList<PadronE>();

                while ( !cursor.isAfterLast() )
                {
                    PadronE padronE = new PadronE();

                    padronE.setNumDoc( cursor.getString( cursor.getColumnIndex( "NumDoc" ) ) );
                    padronE.setApePaterno( cursor.getString( cursor.getColumnIndex( "ApePaterno" ) ) );
                    padronE.setApeMaterno( cursor.getString( cursor.getColumnIndex( "ApeMaterno" ) ) );
                    padronE.setNombres( cursor.getString( cursor.getColumnIndex( "Nombres" ) ) );

                    arrayList.add( padronE );

                    cursor.moveToNext();
                }

                if ( arrayList.size() > 0 )
                {
                    contentValues =  new ContentValues();
                    contentValues.put( "Status", 1 );

                    SQL = "NumDoc = '" + number_dni + "'";

                    Integer exito = dbHelper.getDatabase().updateWithOnConflict( "Padron", contentValues, SQL, null, SQLiteDatabase.CONFLICT_IGNORE );
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