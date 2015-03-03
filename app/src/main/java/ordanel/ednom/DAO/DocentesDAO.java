package ordanel.ednom.DAO;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import ordanel.ednom.Entity.AulaLocalE;
import ordanel.ednom.Entity.DocentesE;
import ordanel.ednom.Entity.InstrumentoE;
import ordanel.ednom.Entity.LocalE;
import ordanel.ednom.Library.ConstantsUtils;
import ordanel.ednom.MainActivity;

/**
 * Created by OrdNael on 05/11/2014.
 */
public class DocentesDAO extends BaseDAO {

    private static final String TAG = DocentesDAO.class.getSimpleName();
    private static DocentesDAO docentesDAO;

    DocentesE docentesE;

    ArrayList<DocentesE> docentesEArrayList;


    public synchronized static DocentesDAO getInstance( Context paramContext ) {

        if ( docentesDAO == null )
        {
            docentesDAO = new DocentesDAO( paramContext );
        }

        return docentesDAO;
    }

    private DocentesDAO( Context paramContext ) {
        initDBHelper( paramContext );
        initHttPostAux();
    }

    public DocentesE searchPerson( String paramConditional ) {

        Log.e( TAG, "start searchPerson" );

        docentesE = new DocentesE();

        try
        {
                openDBHelper();

                SQL = "SELECT nro_doc, ape_pat, ape_mat, nombres, nro_aula, lc.nombreLocal FROM docentes dc INNER JOIN local lc ON dc.cod_sede_operativa = lc.cod_sede_operativa AND dc.cod_local_sede = lc.cod_local_sede WHERE " + paramConditional;
                Log.e(TAG, "string sql : " + SQL);
                cursor = dbHelper.getDatabase().rawQuery(SQL, null);

                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {

                        LocalE localE = new LocalE();
                        localE.setNombreLocal(cursor.getString(cursor.getColumnIndex(LocalE.NOMBRE_LOCAL)));

                        AulaLocalE aulaLocalE = new AulaLocalE();
                        aulaLocalE.setLocalE(localE);
                        aulaLocalE.setNro_aula(cursor.getInt(cursor.getColumnIndex(AulaLocalE.NRO_AULA)));

                        docentesE.setNro_doc(cursor.getString(cursor.getColumnIndex(DocentesE.NRO_DOC)));
                        docentesE.setApe_pat(cursor.getString(cursor.getColumnIndex(DocentesE.APE_PAT)));
                        docentesE.setApe_mat(cursor.getString(cursor.getColumnIndex(DocentesE.APE_MAT)));
                        docentesE.setNombres(cursor.getString(cursor.getColumnIndex(DocentesE.NOMBRES)));
                        docentesE.setAulaLocalE(aulaLocalE);

                        cursor.moveToNext();
                    }

                    docentesE.setStatus(0);
                } else {
                    docentesE.setStatus(1);// alerta. sin datos;
                }
        }
          catch (Exception e)
        {
            e.printStackTrace();
            docentesE.setStatus( 2 ); // error al acceder.
        }
        finally
        {
            closeDBHelper();
            cursor.close();
        }

        Log.e( TAG, "end searchPerson" );

        return docentesE;

    }

    public Integer asistenciaLocal( String paramDNI ) {

        Log.e( TAG, "start asistenciaLocal" );

        try
        {
            openDBHelper();
            if (isEstadoRegistroDocente(paramDNI, DocentesE.ESTADO)){
                valueInteger = 6 ;
            } else {
            contentValues =  new ContentValues();
            contentValues.put( DocentesE.ESTADO, 1 );
            contentValues.put( DocentesE.F_REGISTRO, ConstantsUtils.fecha_registro() );

            SQL = "nro_doc = '" + paramDNI + "'";

            valueInteger = dbHelper.getDatabase().updateWithOnConflict( "docentes", contentValues, SQL, null, SQLiteDatabase.CONFLICT_IGNORE );
            Log.e( TAG, "asistencia local : " + valueInteger.toString() );

            valueInteger = 0;

            dbHelper.setTransactionSuccessful(); }
        }
        catch (SQLiteDatabaseLockedException l){
            l.printStackTrace();
            valueInteger = 7;
            Log.e(TAG, "Error: " + l);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            valueInteger = 3;// error al registra asistencia al local;
            Log.e(TAG, "Error: " + e);
        }
        finally
        {
            closeDBHelper();
        }

        Log.e( TAG, "end asistenciaLocal" );

        return valueInteger;
    }


    public Integer asistenciaAula( String number_doc, Integer nro_aula, Integer paramContingencia ) {

        Log.e( TAG, "start asistenciaAula" );

        try
        {
            openDBHelper();
            if (isEstadoRegistroDocente(number_doc, DocentesE.ESTADO_AULA)){
                valueInteger = 6 ;
            } else {
                contentValues = new ContentValues();
                contentValues.put(DocentesE.ESTADO_AULA, 1);
                contentValues.put(DocentesE.F_AULA, ConstantsUtils.fecha_registro());

                if (paramContingencia == 0) {
                    SQL = "nro_doc = '" + number_doc + "' and nro_aula = " + nro_aula + " and estado > 0";
                } else {
                    SQL = "nro_doc = '" + number_doc + "' and estado > 0";
                    contentValues.put(DocentesE.NRO_AULA_CAMBIO, nro_aula);
                }

                valueInteger = dbHelper.getDatabase().updateWithOnConflict("docentes", contentValues, SQL, null, SQLiteDatabase.CONFLICT_IGNORE);
                Log.e(TAG, "asistencia aula : " + valueInteger.toString());

                if (!valueInteger.equals(0)) // el registro es correcto
                {
                    valueInteger = 0;
                } else {
                    valueInteger = 5; // el docente no fue registrado al ingreso del local
                }

                dbHelper.setTransactionSuccessful();
            }
        }
        catch (SQLiteDatabaseLockedException l){
            l.printStackTrace();
            valueInteger = 7;
            Log.e(TAG, "Error: " + l);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            valueInteger = 4;// error al registrar asistencia al aula;
            Log.e(TAG, "Error: " + e);
        }
        finally
        {
            closeDBHelper();
        }

        Log.e( TAG, "end asistenciaAula" );

        return valueInteger;
    }

    public Integer inventarioFichaDocente( String paramCodFicha ) {

        Log.e( TAG, "start inventarioFichaDocente" );

        try
        {
            openDBHelper();

            if (isEstadoRegistroInstrumento(paramCodFicha, DocentesE.ESTADO_FICHA, DocentesE.COD_FICHA)) {
                valueInteger = 4;
            } else {
                contentValues = new ContentValues();
                contentValues.put(DocentesE.ESTADO_FICHA, 1);
                contentValues.put(DocentesE.F_FICHA, ConstantsUtils.fecha_registro());

                Where = "cod_ficha = '" + paramCodFicha + "'";

                valueInteger = dbHelper.getDatabase().updateWithOnConflict("docentes", contentValues, Where, null, SQLiteDatabase.CONFLICT_IGNORE);
                Log.e(TAG, "inventario ficha docente : " + valueInteger.toString());

                if (!valueInteger.equals(0)) // el registro es correcto
                {
                    valueInteger = 0;
                } else {
                    valueInteger = 3; // error al registrar inventario de ficha
                }

                dbHelper.setTransactionSuccessful();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e( TAG, e.toString() );
            valueInteger = 3;// error al registrar inventario de ficha;
        }
        finally
        {
            closeDBHelper();
        }

        Log.e( TAG, "end inventarioFichaDocente" );

        return valueInteger;

    }

    public Integer inventarioCuadernilloDocente( String paramCodCuadernillo ) {

        Log.e( TAG, "start inventarioCuadernilloDocente" );

        try
        {
            openDBHelper();

            if (isEstadoRegistroInstrumento(paramCodCuadernillo, DocentesE.ESTADO_CARTILLA, DocentesE.COD_CARTILLA)) {
                valueInteger = 4;
            } else {
                contentValues = new ContentValues();
                contentValues.put(DocentesE.ESTADO_CARTILLA, 1);
                contentValues.put(DocentesE.F_CARTILLA, ConstantsUtils.fecha_registro());

                Where = "cod_cartilla = '" + paramCodCuadernillo + "'";

                valueInteger = dbHelper.getDatabase().updateWithOnConflict("docentes", contentValues, Where, null, SQLiteDatabase.CONFLICT_IGNORE);
                Log.e(TAG, "inventario cuadernillo docente : " + valueInteger.toString());

                if (!valueInteger.equals(0)) // el registro es correcto
                {
                    valueInteger = 0;
                } else {
                    valueInteger = 3; // error al registrar inventario de cuadernillo
                }

                dbHelper.setTransactionSuccessful();
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            Log.e( TAG, e.toString() );
            valueInteger = 3;// error al registrar inventario de cuadernillo;
        }
        finally
        {
            closeDBHelper();
        }

        Log.e( TAG, "end inventarioCuadernilloDocente" );

        return valueInteger;
    }

    public ArrayList<DocentesE> listadoIngresoLocal() {

        Log.e( TAG, "start listadoIngresoLocal" );

        docentesEArrayList = new ArrayList<>();

        try
        {
            openDBHelper();

            SQL = "SELECT nro_doc, ape_pat, ape_mat, nombres, nro_aula, estado, f_registro  FROM docentes WHERE estado > 0 ORDER BY nro_aula";

            cursor = dbHelper.getDatabase().rawQuery( SQL, null );

            if ( cursor.moveToFirst() )
            {
                while ( !cursor.isAfterLast() )
                {
                    AulaLocalE aulaLocalE = new AulaLocalE();
                    aulaLocalE.setNro_aula( cursor.getInt( cursor.getColumnIndex( AulaLocalE.NRO_AULA ) ) );

                    docentesE = new DocentesE();
                    docentesE.setAulaLocalE( aulaLocalE );
                    docentesE.setNro_doc( cursor.getString( cursor.getColumnIndex( DocentesE.NRO_DOC ) ) );
                    docentesE.setApe_pat( cursor.getString( cursor.getColumnIndex( DocentesE.APE_PAT ) ) );
                    docentesE.setApe_mat( cursor.getString( cursor.getColumnIndex( DocentesE.APE_MAT ) ) );
                    docentesE.setNombres( cursor.getString( cursor.getColumnIndex( DocentesE.NOMBRES ) ) );
                    docentesE.setEstado( cursor.getInt( cursor.getColumnIndex( DocentesE.ESTADO ) ) );
                    docentesE.setF_registro( cursor.getString( cursor.getColumnIndex( DocentesE.F_REGISTRO ) ) );

                    docentesEArrayList.add( docentesE );

                    cursor.moveToNext();

                }
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            Log.e( TAG, e.toString() );
        }
        finally
        {
            closeDBHelper();
            cursor.close();
        }

        Log.e( TAG, "end listadoIngresoLocal" );

        return docentesEArrayList;
    }

    public ArrayList<DocentesE> listadoAsistenciaAula( Integer paramNroAula ) {

        Log.e( TAG, "start listadoAsistenciaAula" );

        docentesEArrayList = new ArrayList<>();

        try
        {
            openDBHelper();

            SQL = "SELECT nro_doc, ape_pat, ape_mat, nombres, nro_aula, estado_aula, f_aula  FROM docentes WHERE estado_aula > 0 and nro_aula = " + paramNroAula + " ORDER BY nro_aula";

            cursor = dbHelper.getDatabase().rawQuery( SQL, null );

            if ( cursor.moveToFirst() )
            {
                while ( !cursor.isAfterLast() )
                {
                    AulaLocalE aulaLocalE = new AulaLocalE();
                    aulaLocalE.setNro_aula( cursor.getInt( cursor.getColumnIndex( AulaLocalE.NRO_AULA ) ) );

                    docentesE = new DocentesE();
                    docentesE.setAulaLocalE( aulaLocalE );
                    docentesE.setNro_doc( cursor.getString( cursor.getColumnIndex( DocentesE.NRO_DOC ) ) );
                    docentesE.setApe_pat( cursor.getString( cursor.getColumnIndex( DocentesE.APE_PAT ) ) );
                    docentesE.setApe_mat( cursor.getString( cursor.getColumnIndex( DocentesE.APE_MAT ) ) );
                    docentesE.setNombres( cursor.getString( cursor.getColumnIndex( DocentesE.NOMBRES ) ) );
                    docentesE.setEstado_aula( cursor.getInt( cursor.getColumnIndex( DocentesE.ESTADO_AULA ) ) );
                    docentesE.setF_registro( cursor.getString( cursor.getColumnIndex( DocentesE.F_AULA ) ) );

                    docentesEArrayList.add( docentesE );

                    cursor.moveToNext();

                }
            }
        }
        catch ( Exception  e )
        {
            e.printStackTrace();
            Log.e( TAG, e.toString() );
        }
        finally
        {
            closeDBHelper();
            cursor.close();
        }

        Log.e( TAG, "end listadoAsistenciaAula" );

        return docentesEArrayList;

    }

    public ArrayList<DocentesE> listadoInventarioFicha ( Integer paramNroAula ) {

        Log.e( TAG, "start listadoInventarioFicha" );

        docentesEArrayList = new ArrayList<>();

        try
        {
            openDBHelper();

            SQL = "SELECT nro_doc, ape_pat, ape_mat, nombres, nro_aula, cod_ficha, estado_ficha, f_ficha  FROM docentes WHERE estado_ficha > 0 and nro_aula = " + paramNroAula + " ORDER BY nro_aula";

            cursor = dbHelper.getDatabase().rawQuery( SQL, null );

            if ( cursor.moveToFirst() )
            {
                while ( !cursor.isAfterLast() )
                {
                    AulaLocalE aulaLocalE = new AulaLocalE();
                    aulaLocalE.setNro_aula( cursor.getInt( cursor.getColumnIndex( AulaLocalE.NRO_AULA ) ) );

                    docentesE = new DocentesE();
                    docentesE.setAulaLocalE( aulaLocalE );
                    docentesE.setNro_doc( cursor.getString( cursor.getColumnIndex( DocentesE.NRO_DOC ) ) );
                    docentesE.setApe_pat( cursor.getString( cursor.getColumnIndex( DocentesE.APE_PAT ) ) );
                    docentesE.setApe_mat( cursor.getString( cursor.getColumnIndex( DocentesE.APE_MAT ) ) );
                    docentesE.setNombres( cursor.getString( cursor.getColumnIndex( DocentesE.NOMBRES ) ) );
                    docentesE.setCod_ficha( cursor.getString( cursor.getColumnIndex( DocentesE.COD_FICHA ) ) );
                    docentesE.setEstado_ficha(cursor.getInt(cursor.getColumnIndex(DocentesE.ESTADO_FICHA)));
                    docentesE.setF_ficha( cursor.getString( cursor.getColumnIndex( DocentesE.F_FICHA ) ) );

                    docentesEArrayList.add( docentesE );

                    cursor.moveToNext();

                }
            }

            cursor = null;

            SQL = "SELECT nro_aula, cod_ficha,  estado_ficha, f_ficha FROM instrumento WHERE estado_ficha > 0 and nro_aula = " + paramNroAula + " ORDER BY nro_aula";

            cursor = dbHelper.getDatabase().rawQuery( SQL, null );

            if ( cursor.moveToFirst() )
            {
                while ( !cursor.isAfterLast() )
                {
                    AulaLocalE aulaLocalE = new AulaLocalE();
                    aulaLocalE.setNro_aula( cursor.getInt( cursor.getColumnIndex( InstrumentoE.NRO_AULA ) ) );

                    docentesE = new DocentesE();
                    docentesE.setAulaLocalE( aulaLocalE );

                    docentesE.setNro_doc( "-" );
                    docentesE.setApe_pat( "" );
                    docentesE.setApe_mat( "-" );
                    docentesE.setNombres( "" );

                    docentesE.setCod_ficha( cursor.getString( cursor.getColumnIndex( InstrumentoE.COD_FICHA ) ) );
                    docentesE.setEstado_ficha( cursor.getInt( cursor.getColumnIndex( InstrumentoE.ESTADO_FICHA ) ) );
                    docentesE.setF_ficha( cursor.getString( cursor.getColumnIndex( InstrumentoE.F_FICHA ) ) );

                    docentesEArrayList.add( docentesE );

                    cursor.moveToNext();

                }
            }

        }
        catch ( Exception e )
        {
            e.printStackTrace();
            Log.e( TAG, e.toString() );
        }
        finally
        {
            closeDBHelper();
            cursor.close();
        }

        Log.e( TAG, "end listadoInventarioFicha" );

        return docentesEArrayList;
    }

    public ArrayList<DocentesE> listadoInventarioCuadernillo ( Integer paramNroAula ) {

        Log.e( TAG, "start listadoInventarioCuadernillo" );

        docentesEArrayList = new ArrayList<>();

        try
        {
            openDBHelper();

            SQL = "SELECT nro_doc, ape_pat, ape_mat, nombres, nro_aula, cod_cartilla, estado_cartilla, f_cartilla FROM docentes WHERE estado_cartilla > 0 and nro_aula = " + paramNroAula + " ORDER BY nro_aula";

            cursor = dbHelper.getDatabase().rawQuery( SQL, null );

            if ( cursor.moveToFirst() )
            {
                while ( !cursor.isAfterLast() )
                {
                    AulaLocalE aulaLocalE = new AulaLocalE();
                    aulaLocalE.setNro_aula( cursor.getInt( cursor.getColumnIndex( AulaLocalE.NRO_AULA ) ) );

                    docentesE = new DocentesE();
                    docentesE.setAulaLocalE( aulaLocalE );
                    docentesE.setNro_doc( cursor.getString( cursor.getColumnIndex( DocentesE.NRO_DOC ) ) );
                    docentesE.setApe_pat( cursor.getString( cursor.getColumnIndex( DocentesE.APE_PAT ) ) );
                    docentesE.setApe_mat( cursor.getString( cursor.getColumnIndex( DocentesE.APE_MAT ) ) );
                    docentesE.setNombres( cursor.getString( cursor.getColumnIndex( DocentesE.NOMBRES ) ) );
                    docentesE.setCod_ficha( cursor.getString( cursor.getColumnIndex( DocentesE.COD_CARTILLA ) ) );
                    docentesE.setEstado_ficha(cursor.getInt(cursor.getColumnIndex(DocentesE.ESTADO_CARTILLA ) ) );
                    docentesE.setF_ficha( cursor.getString( cursor.getColumnIndex( DocentesE.F_CARTILLA ) ) );

                    docentesEArrayList.add( docentesE );

                    cursor.moveToNext();

                }
            }

            cursor = null;

            SQL = "SELECT nro_aula, cod_cartilla,  estado_cartilla, f_cartilla FROM instrumento WHERE estado_cartilla > 0 and nro_aula = " + paramNroAula + " ORDER BY nro_aula";

            cursor = dbHelper.getDatabase().rawQuery( SQL, null );

            if ( cursor.moveToFirst() )
            {
                while ( !cursor.isAfterLast() )
                {
                    AulaLocalE aulaLocalE = new AulaLocalE();
                    aulaLocalE.setNro_aula( cursor.getInt( cursor.getColumnIndex( InstrumentoE.NRO_AULA ) ) );

                    docentesE = new DocentesE();
                    docentesE.setAulaLocalE( aulaLocalE );

                    docentesE.setNro_doc( "-" );
                    docentesE.setApe_pat( "" );
                    docentesE.setApe_mat( "-" );
                    docentesE.setNombres( "" );

                    docentesE.setCod_ficha( cursor.getString( cursor.getColumnIndex( InstrumentoE.COD_CARTILLA ) ) );
                    docentesE.setEstado_ficha( cursor.getInt( cursor.getColumnIndex( InstrumentoE.ESTADO_CARTILLA ) ) );
                    docentesE.setF_ficha( cursor.getString( cursor.getColumnIndex( InstrumentoE.F_CARTILLA ) ) );

                    docentesEArrayList.add( docentesE );

                    cursor.moveToNext();

                }
            }

        }
        catch ( Exception e )
        {
            e.printStackTrace();
            Log.e( TAG, e.toString() );
        }
        finally
        {
            closeDBHelper();
            cursor.close();
        }

        Log.e( TAG, "end listadoInventarioCuadernillo" );

        return docentesEArrayList;
    }

    public Boolean isEstadoRegistroDocente (String dni, String column){
        boolean isDate = false;

        SQL = "SELECT " + column + " from docentes where nro_doc like '" + dni + "'";
        cursor = dbHelper.getDatabase().rawQuery( SQL, null );
        if ( cursor.moveToFirst() ) {
            while (!cursor.isAfterLast()) {
                int estado = 0;
                estado = cursor.getInt(cursor.getColumnIndex(column));
                if (estado != 0) {
                    isDate = true;
                }
                Log.i(TAG, column + ": " + Integer.toString(estado));
            cursor.moveToNext();
            }
        }
        return isDate;
    }

    public Boolean isEstadoRegistroInstrumento (String codigo, String column_estado, String column_codigo ){
        boolean isDate = false;

        SQL = "SELECT " + column_estado + " from docentes where " + column_codigo + " like '" + codigo + "'";
        cursor = dbHelper.getDatabase().rawQuery( SQL, null );
        if ( cursor.moveToFirst() ) {
            while (!cursor.isAfterLast()) {
                int estado = 0;
                estado = cursor.getInt(cursor.getColumnIndex(column_estado));
                if (estado != 0) {
                    isDate = true;
                }
                Log.i(TAG, column_estado + ": " + Integer.toString(estado));
                cursor.moveToNext();
            }
        }
        return isDate;
    }

    public Integer nroDatosSincronizados(String columnEstado){
        int datosSincronizados = 0;
        try {
            openDBHelper();
            SQL = "SELECT count(*) FROM docentes WHERE " + columnEstado + " = 2";
            cursor = dbHelper.getDatabase().rawQuery( SQL, null );
            cursor.moveToFirst();
            datosSincronizados = cursor.getInt(0);
        }catch (Exception e){
            e.printStackTrace();
            Log.e( TAG, e.toString() );
        }
        finally {
            closeDBHelper();
            cursor.close();
        }
        return datosSincronizados;
    }

}