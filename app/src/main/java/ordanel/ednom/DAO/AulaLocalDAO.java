package ordanel.ednom.DAO;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import ordanel.ednom.Entity.AulaLocalE;

public class AulaLocalDAO extends BaseDAO {

    private static final String TAG = AulaLocalDAO.class.getSimpleName();
    private static AulaLocalDAO aulaLocalDAO;

    AulaLocalE aulaLocalE;
    ArrayList<AulaLocalE> aulaLocalEArrayList;

    public static synchronized AulaLocalDAO getInstance( Context paramContext ) {

        if ( aulaLocalDAO == null )
        {
            aulaLocalDAO = new AulaLocalDAO( paramContext );
        }

        return aulaLocalDAO;

    }

    private AulaLocalDAO ( Context paramContext ) {
        initDBHelper( paramContext );
    }


    public ArrayList<AulaLocalE> getAllNroAula() {

        Log.e( TAG, "start getAllNroAula" );

        aulaLocalEArrayList = new ArrayList<>();

        try
        {
            openDBHelper();

            SQL = "SELECT nro_aula, tipo FROM aula_local ORDER BY nro_aula ASC";
            cursor = dbHelper.getDatabase().rawQuery( SQL, null );

            if ( cursor.moveToFirst() )
            {
                while ( !cursor.isAfterLast() )
                {
                    aulaLocalE = new AulaLocalE();

                    aulaLocalE.setNro_aula( cursor.getInt( cursor.getColumnIndex( AulaLocalE.NRO_AULA ) ) );
                    aulaLocalE.setTipo( cursor.getString( cursor.getColumnIndex( AulaLocalE.TIPO ) ) );

                    aulaLocalEArrayList.add( aulaLocalE );

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
            closeDBHelper();
            cursor.close();
        }

        Log.e( TAG, "end getAllNroAula" );

        return aulaLocalEArrayList;
    }

    public Integer searchTipoAula( Integer paramNroAula ) {

        Log.e( TAG, "start searchTipoAula" );

        try
        {
            openDBHelper();

            SQL = "SELECT tipo FROM aula_local WHERE nro_aula = " + paramNroAula;
            cursor = dbHelper.getDatabase().rawQuery( SQL, null );

            if ( cursor.moveToFirst() )
            {
                valueString = cursor.getString( cursor.getColumnIndex( AulaLocalE.TIPO ) );
            }

            if ( valueString.equals( "C" ) ) // tipo CONTINGENCIA
            {
                valueInteger = 1;
            }
            else
            {
                valueInteger = 0;
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            closeDBHelper();
            cursor.close();
        }

        Log.e( TAG, "end searchTipoAula" );


        return valueInteger;
    }

}
