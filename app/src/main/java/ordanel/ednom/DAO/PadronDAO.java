package ordanel.ednom.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ordanel.ednom.BD.DBHelper;
import ordanel.ednom.Entity.AulaLocalE;
import ordanel.ednom.Entity.DiscapacidadE;
import ordanel.ednom.Entity.DocentesE;
import ordanel.ednom.Entity.LocalE;
import ordanel.ednom.Entity.ModalidadE;
import ordanel.ednom.Entity.PadronE;
import ordanel.ednom.Library.ConstantsUtils;
import ordanel.ednom.Library.HttpPostAux;

/**
 * Created by OrdNael on 30/10/2014.
 */
public class PadronDAO {

    private static final String TAG = PadronDAO.class.getSimpleName();

    String URL_Connect = ConstantsUtils.BASE_URL + "padron"; // "padron.php" "padron"

    Integer cod_sede_operativa, cod_local_sede, nro_aula;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Long valueLong;
    Integer valueInteger;

    DBHelper dbHelper;
    Context context;
    Cursor cursor = null;
    ContentValues contentValues;

    JSONObject jsonObject;
    JSONArray jsonArray;
    JSONObject jsonObjectTemp;
    JSONArray jsonArrayAulaLocal, jsonArrayDiscapacidad, jsonArrayModalidad;

    LocalE localE;
    PadronE padronE;
    ArrayList<AulaLocalE> aulaLocalEArrayList;
    ArrayList<DocentesE> docentesEArrayList;
    ArrayList<DiscapacidadE> discapacidadEArrayList;
    ArrayList<ModalidadE> modalidadEArrayList;


    public PadronDAO( Context context ) {
        this.context = context;
        Log.e( TAG, "start" );
    }

    public LocalE searchNroLocal() {

        Log.e( TAG, "start searchNroLocal" );

        dbHelper = DBHelper.getUtilDb( this.context );

        try
        {
            dbHelper.openDataBase();
            dbHelper.beginTransaction();

            String SQL = "SELECT cod_sede_operativa, cod_local_sede FROM local";

            cursor = dbHelper.getDatabase().rawQuery( SQL, null );

            if ( cursor.moveToFirst() )
            {
                localE = new LocalE();

                localE.setCod_sede_operativa( cursor.getInt( cursor.getColumnIndex( "cod_sede_operativa" ) ) );
                localE.setCod_local_sede( cursor.getInt( cursor.getColumnIndex( "cod_local_sede" ) ) );
                localE.setOperation_status( 0 );

                valueInteger = cursor.getInt( cursor.getColumnIndex( "cod_local_sede" ) );
                Log.e( TAG, "numero de local : " + valueInteger.toString() );

                return localE;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e( TAG, "error searchNroLocal : " + e.toString() );
        }
        finally
        {
            dbHelper.endTransaction();
            dbHelper.close();
            cursor.close();
        }

        Log.e( TAG, "end searchNroLocal" );

        return null;

    }

    public PadronE padronNube( LocalE paramLocalE ) {

        Log.e( TAG, "start padronNubeJson" );
        padronE = new PadronE();

        if ( paramLocalE != null )
        {
            HttpPostAux httpPostAux = new HttpPostAux();

            cod_sede_operativa = paramLocalE.getCod_sede_operativa();
            cod_local_sede = paramLocalE.getCod_local_sede();

            jsonArray = httpPostAux.getServerData( null, URL_Connect + "?cod_sede_operativa=" + cod_sede_operativa + "&cod_local_sede=" + cod_local_sede );

            if ( jsonArray != null && jsonArray.length() > 0 )
            {

                try
                {
                    jsonObject = jsonArray.getJSONObject(0);

                    // set array AULAS
                    jsonArrayAulaLocal = jsonObject.getJSONArray("AULAS");
                    aulaLocalEArrayList = new ArrayList<AulaLocalE>();

                    for ( int i = 0; i < jsonArrayAulaLocal.length(); i++ )
                    {
                        jsonObjectTemp = (JSONObject) jsonArrayAulaLocal.get(i);

                        AulaLocalE aulaLocalE = new AulaLocalE();
                        aulaLocalE.setLocalE( paramLocalE );
                        aulaLocalE.setNro_aula( jsonObjectTemp.getInt( "nro_aula" ) );
                        aulaLocalE.setTipo( jsonObjectTemp.getString("tipo") );
                        aulaLocalE.setCant_docente( jsonObjectTemp.getInt( "cant_docente" ) );

                        // set array DOCENTES
                        jsonArray = (JSONArray) jsonObjectTemp.get("DOCENTES");
                        docentesEArrayList = new ArrayList<DocentesE>();

                        for ( int j = 0; j < jsonArray.length(); j++ )
                        {
                            jsonObjectTemp = (JSONObject) jsonArray.get(j);
                            DocentesE docentesE = new DocentesE();

                            docentesE.setAulaLocalE( aulaLocalE );
                            docentesE.setTipo_doc( jsonObjectTemp.getString( "tipo_doc" ) );
                            docentesE.setNro_doc( jsonObjectTemp.getString( "nro_doc" ) );
                            docentesE.setApe_pat( jsonObjectTemp.getString( "ape_pat" ) );
                            docentesE.setApe_mat( jsonObjectTemp.getString( "ape_mat" ) );
                            docentesE.setNombres( jsonObjectTemp.getString( "nombres" ) );

                            docentesEArrayList.add( docentesE );

                        }

                        aulaLocalE.setDocentesEList( docentesEArrayList );
                        // .set array DOCENTES

                        aulaLocalEArrayList.add( aulaLocalE );

                    }

                    valueInteger = aulaLocalEArrayList.size();
                    Log.e( TAG, "cantidad de aula_local : " + valueInteger.toString() );
                    padronE.setAulaLocalEList( aulaLocalEArrayList );
                    // .set array AULAS


                    // set array DISCAPACIDAD
                    jsonArrayDiscapacidad = jsonObject.getJSONArray("DISCAPACIDAD");
                    discapacidadEArrayList = new ArrayList<DiscapacidadE>();

                    for ( int i = 0; i < jsonArrayDiscapacidad.length(); i++ )
                    {
                        jsonObjectTemp = (JSONObject) jsonArrayDiscapacidad.get(i);

                        DiscapacidadE discapacidadE = new DiscapacidadE();
                        discapacidadE.setCod_discap( jsonObjectTemp.getInt( "cod_discap" ) );
                        discapacidadE.setDiscapacidad( jsonObjectTemp.getString( "discapacidad" ) );

                        discapacidadEArrayList.add( discapacidadE );
                    }

                    valueInteger = discapacidadEArrayList.size();
                    Log.e( TAG, "cantidad de discapacidad : " + valueInteger.toString() );
                    padronE.setDiscapacidadEList( discapacidadEArrayList );
                    // .set array DISCAPACIDAD


                    // set array MODALIDAD
                    jsonArrayModalidad = jsonObject.getJSONArray("MODALIDAD");
                    modalidadEArrayList = new ArrayList<ModalidadE>();

                    for ( int i = 0; i < jsonArrayModalidad.length(); i++ )
                    {
                        jsonObjectTemp = (JSONObject) jsonArrayModalidad.get(i);

                        ModalidadE modalidadE = new ModalidadE();
                        modalidadE.setCod_modal( jsonObjectTemp.getInt( "cod_modal" ) );
                        modalidadE.setModalidad( jsonObjectTemp.getString( "modalidad" ) );

                        modalidadEArrayList.add( modalidadE );
                    }

                    valueInteger = modalidadEArrayList.size();
                    Log.e( TAG, "cantidad de modalidad : " + valueInteger.toString() );
                    padronE.setModalidadEList( modalidadEArrayList );
                    // .set array MODALIDAD


                    padronE.setStatus( 0 );

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    padronE.setStatus( 1 ); // error en padron nube
                    Log.e( TAG, "error padronNubeJson : " + e.toString() );
                }

            }

        }

        Log.e( TAG, "end padronNubeJson" );

        return padronE;

    }

    public PadronE registrarPadron( PadronE paramPadronE ) {

        Log.e( TAG, "start registrarPadron" );

        dbHelper = DBHelper.getUtilDb( this.context );

        try
        {
            dbHelper.openDataBase();
            dbHelper.beginTransaction();

            if ( this.clearPadron() )// limpio padron
            {
                // set de Arrays
                aulaLocalEArrayList = (ArrayList) paramPadronE.getAulaLocalEList();
                discapacidadEArrayList = (ArrayList) paramPadronE.getDiscapacidadEList();
                modalidadEArrayList = (ArrayList) paramPadronE.getModalidadEList();
                // .set de Arrays

                // registro de AULAS
                for ( AulaLocalE aulaLocalE : aulaLocalEArrayList )
                {
                    cod_sede_operativa = aulaLocalE.getLocalE().getCod_sede_operativa();
                    cod_local_sede = aulaLocalE.getLocalE().getCod_local_sede();
                    nro_aula = aulaLocalE.getNro_aula();

                    contentValues = new ContentValues();

                    contentValues.put( "cod_sede_operativa", cod_sede_operativa );
                    contentValues.put( "cod_local_sede", cod_local_sede);
                    contentValues.put( "nro_aula", nro_aula);
                    contentValues.put( "tipo", aulaLocalE.getTipo() );
                    contentValues.put( "cant_docente", aulaLocalE.getCant_docente() );

                    valueLong = dbHelper.getDatabase().insertOrThrow( "aula_local", null, contentValues );
                    Log.e( TAG, "aula_local insert : " + String.valueOf(valueLong) );

                    // registro de Docentes
                    for ( DocentesE docentesE : aulaLocalE.getDocentesEList() )
                    {
                        contentValues = new ContentValues();

                        contentValues.put( "cod_sede_operativa", cod_sede_operativa );
                        contentValues.put( "cod_local_sede", cod_local_sede );
                        contentValues.put( "tipo_doc", docentesE.getTipo_doc() );
                        contentValues.put( "nro_doc", docentesE.getNro_doc() );
                        contentValues.put( "ape_pat", docentesE.getApe_pat() );
                        contentValues.put( "ape_mat", docentesE.getApe_mat() );
                        contentValues.put( "nombres", docentesE.getNombres() );
                        contentValues.put( "nro_aula", nro_aula );

                        valueLong = dbHelper.getDatabase().insertOrThrow( "docentes", null, contentValues );
                        Log.e( TAG, "docentes insert : " + String.valueOf(valueLong) );

                    }
                    // .registro de Docentes

                }
                // .registro de AULAS

                // registro de DISCAPACIDAD
                for ( DiscapacidadE discapacidadE : discapacidadEArrayList )
                {
                    contentValues = new ContentValues();

                    contentValues.put( "cod_discap", discapacidadE.getCod_discap() );
                    contentValues.put( "discapacidad", discapacidadE.getDiscapacidad() );

                    valueLong = dbHelper.getDatabase().insertOrThrow( "discapacidad", null, contentValues );
                    Log.e( TAG, "discapacidad insert : " + String.valueOf(valueLong) );
                }
                // .registro de DISCAPACIDAD

                // registro de MODALIDAD
                for ( ModalidadE modalidadE : modalidadEArrayList )
                {
                    contentValues = new ContentValues();

                    contentValues.put( "cod_modal", modalidadE.getCod_modal() );
                    contentValues.put( "modalidad", modalidadE.getModalidad() );

                    valueLong = dbHelper.getDatabase().insertOrThrow( "modalidad", null, contentValues );
                    Log.e( TAG, "modalidad insert : " + String.valueOf(valueLong) );
                }
                // .registro de MODALIDAD

                dbHelper.setTransactionSuccessful();

                padronE.setStatus( 0 );

            }
            else
            {
                padronE.setStatus( 2 ); // error cleanPadron
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            padronE.setStatus( 3 ); // error register padron
            Log.e( TAG, "error registrarPadron : " + e.toString() );
        }
        finally
        {
            dbHelper.endTransaction();
            dbHelper.close();
        }

        Log.e( TAG, "end registrarPadron" );

        return padronE;

    }

    public Boolean clearPadron() {

        try
        {

            dbHelper.getDatabase().delete( "aula_local", null, null );
            Log.e( TAG, "Se elimino aula_local!" );

            dbHelper.getDatabase().delete( "docentes", null, null );
            Log.e( TAG, "Se elimino docentes!" );

            dbHelper.getDatabase().delete( "discapacidad", null, null );
            Log.e( TAG, "Se elimino discapacidad!" );

            dbHelper.getDatabase().delete( "modalidad", null, null );
            Log.e( TAG, "Se elimino modalidad!" );

            return true;

        }
        catch (Exception e)
        {
            Log.e( TAG, "clear padron error : " + e.toString() );
            return false;
        }

    }

    public Date setearFecha( String stringFecha ) {

        Date dateFecha = null;

        try
        {
            dateFecha = simpleDateFormat.parse( stringFecha );
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return dateFecha;

        // padronE.setFecha_registro( this.setearFecha( jsonObject.getString( "fecha_registro" ) ) );
        // contentValues.put( "fecha_registro", simpleDateFormat.format( arrayList.get(i).getFecha_registro() ) );

    }

}