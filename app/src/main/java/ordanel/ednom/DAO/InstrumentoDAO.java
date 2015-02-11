package ordanel.ednom.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import ordanel.ednom.Entity.InstrumentoE;
import ordanel.ednom.Entity.LocalE;
import ordanel.ednom.Library.ConstantsUtils;

/**
 * Created by OrdNael on 02/12/2014.
 */
public class InstrumentoDAO extends BaseDAO {

    private static final String TAG = InstrumentoDAO.class.getSimpleName();

    private static InstrumentoDAO instrumentoDAO;
    private InstrumentoE instrumentoE;

    public static InstrumentoDAO getInstance( Context paramContext ) {

        if ( instrumentoDAO == null )
        {
            instrumentoDAO = new InstrumentoDAO( paramContext );
        }

        return instrumentoDAO;

    }

    private InstrumentoDAO( Context paramContext ) {
        initDBHelper( paramContext );
    }

    public InstrumentoE searchInstrumentoDocente( String paramConditional ) {

        Log.e(TAG, "start searchInstrumentoDocente");

        instrumentoE = new InstrumentoE();

        try
        {
            openDBHelper();

            SQL = "SELECT dc.nro_aula, dc.cod_ficha, dc.cod_cartilla, dc.estado_ficha, dc.f_ficha, dc.estado_cartilla, dc.f_cartilla, lc.nombreLocal FROM docentes dc INNER JOIN local lc ON dc.cod_sede_operativa = lc.cod_sede_operativa AND dc.cod_local_sede = lc.cod_local_sede WHERE " + paramConditional;

            cursor = dbHelper.getDatabase().rawQuery( SQL, null );

            if ( cursor.moveToFirst() )
            {
                while ( !cursor.isAfterLast() )
                {
                    LocalE localE = new LocalE();
                    localE.setNombreLocal( cursor.getString( cursor.getColumnIndex( LocalE.NOMBRE_LOCAL ) ) );

                    instrumentoE.setLocalE( localE );
                    instrumentoE.setNro_aula( cursor.getInt(cursor.getColumnIndex(InstrumentoE.NRO_AULA)) );
                    instrumentoE.setCod_ficha( cursor.getString( cursor.getColumnIndex( InstrumentoE.COD_FICHA ) ) );
                    instrumentoE.setCod_cartilla( cursor.getString( cursor.getColumnIndex( InstrumentoE.COD_CARTILLA ) ) );

                    cursor.moveToNext();
                }

                instrumentoE.setStatus( 0 );

            }
            else
            {
                instrumentoE.setStatus( 1 );// alerta. sin datos;
            }

        }
        catch ( Exception e )
        {
            e.printStackTrace();
            Log.e( TAG, e.toString() );
            instrumentoE.setStatus( 2 ); // error al acceder.
        }
        finally
        {
            closeDBHelper();
        }

        Log.e(TAG, "end searchInstrumentoDocente");

        return instrumentoE;

    }

    public InstrumentoE searchInstrumento( String paramConditional ) {

        Log.e(TAG, "start searchInstrumento");

        instrumentoE = new InstrumentoE();

        try
        {
            openDBHelper();

            SQL = "SELECT i.id_inst, i.cod_sede_operativa, i.cod_local_sede, i.cod_ficha, i.cod_cartilla, i.nro_aula, i.estado_ficha, i.f_ficha, i.estado_cartilla, i.f_cartilla, lc.nombreLocal FROM instrumento i INNER JOIN local lc ON i.cod_sede_operativa = lc.cod_sede_operativa AND i.cod_local_sede = lc.cod_local_sede  WHERE " + paramConditional;

            cursor = dbHelper.getDatabase().rawQuery( SQL, null );

            if ( cursor.moveToFirst() )
            {
                while ( !cursor.isAfterLast() )
                {
                    LocalE localE = new LocalE();
                    localE.setNombreLocal( cursor.getString( cursor.getColumnIndex( LocalE.NOMBRE_LOCAL ) ) );

                    instrumentoE.setLocalE( localE );
                    instrumentoE.setNro_aula( cursor.getInt(cursor.getColumnIndex ( InstrumentoE.NRO_AULA ) ) );
                    instrumentoE.setCod_ficha( cursor.getString( cursor.getColumnIndex( InstrumentoE.COD_FICHA ) ) );
                    instrumentoE.setCod_cartilla( cursor.getString( cursor.getColumnIndex( InstrumentoE.COD_CARTILLA ) ) );

                    cursor.moveToNext();
                }

                instrumentoE.setStatus( 0 );
            }
            else
            {
                instrumentoE.setStatus( 1 );// alerta. sin datos;
            }

        }
        catch ( Exception e )
        {
            e.printStackTrace();
            Log.e( TAG, e.toString() );
            instrumentoE.setStatus(2); // error al acceder.
        }
        finally
        {
            closeDBHelper();
            cursor.close();
        }

        Log.e(TAG, "end searchInstrumento");

        return instrumentoE;
    }

    public Integer inventarioFicha( String paramCodFicha, Integer paramNroAula ) {

        Log.e( TAG, "start inventarioFicha" );

        try
        {
            openDBHelper();

            if (isFechaRegistroInstrumento(paramCodFicha, "f_ficha", "cod_ficha")) {
                valueInteger = 4;
            } else {
                contentValues = new ContentValues();
                contentValues.put(InstrumentoE.ESTADO_FICHA, 1);
                contentValues.put(InstrumentoE.F_FICHA, ConstantsUtils.fecha_registro());
                contentValues.put(InstrumentoE.NRO_AULA, paramNroAula);

                Where = "cod_ficha = '" + paramCodFicha + "'";

                valueInteger = dbHelper.getDatabase().updateWithOnConflict("instrumento", contentValues, Where, null, SQLiteDatabase.CONFLICT_IGNORE);
                Log.e(TAG, "inventario ficha : " + valueInteger.toString());

                if (!valueInteger.equals(0)) // el registro es correcto
                {
                    valueInteger = 0;
                } else {
                    valueInteger = 3;// error al registrar inventario de ficha;
                }

                dbHelper.setTransactionSuccessful();
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            Log.e( TAG, e.toString() );
            valueInteger = 3;// error al registrar inventario de ficha;
        }
        finally
        {
            closeDBHelper();
        }

        Log.e( TAG, "end inventarioFicha" );

        return valueInteger;
    }

    public Integer inventarioCuadernillo( String paramCodCuadernillo ) {

        Log.e( TAG, "start inventarioCuadernillo" );

        try
        {
            openDBHelper();
            if (isFechaRegistroInstrumento(paramCodCuadernillo, "f_cartilla", "cod_cartilla")) {
                valueInteger = 4;
            } else {

                contentValues = new ContentValues();
                contentValues.put(InstrumentoE.ESTADO_CARTILLA, 1);
                contentValues.put(InstrumentoE.F_CARTILLA, ConstantsUtils.fecha_registro());

                Where = "cod_cartilla = '" + paramCodCuadernillo + "'";

                valueInteger = dbHelper.getDatabase().updateWithOnConflict("instrumento", contentValues, Where, null, SQLiteDatabase.CONFLICT_IGNORE);
                Log.e(TAG, "inventario cuadernillo : " + valueInteger.toString());

                if (!valueInteger.equals(0)) // el registro es correcto
                {
                    valueInteger = 0;
                } else {
                    valueInteger = 3;// error al registrar inventario de ficha;
                }

                dbHelper.setTransactionSuccessful();
            }

        }
        catch ( Exception e )
        {
            e.printStackTrace();
            Log.e( TAG, e.toString() );
            valueInteger = 3;// error al registrar inventario de ficha;
        }
        finally
        {
            closeDBHelper();
        }

        Log.e( TAG, "end inventarioCuadernillo" );

        return valueInteger;
    }

    public Boolean isFechaRegistroInstrumento (String codigo, String column_fecha, String column_codigo ){
        boolean isDate = false;

        SQL = "SELECT " + column_fecha + " from instrumento where " + column_codigo + " like '" + codigo + "'";
        cursor = dbHelper.getDatabase().rawQuery( SQL, null );
        if ( cursor.moveToFirst() ) {
            while (!cursor.isAfterLast()) {
                String date = "vacio";
                date = cursor.getString(cursor.getColumnIndex(column_fecha));
                if (!date.isEmpty()) {
                    Log.i(TAG, "fecha: " + date);
                    isDate = true;
                }
                cursor.moveToNext();
            }
        }
        return isDate;
    }

}