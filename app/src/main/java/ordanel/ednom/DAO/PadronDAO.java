package ordanel.ednom.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ordanel.ednom.Entity.AulaLocalE;
import ordanel.ednom.Entity.DiscapacidadE;
import ordanel.ednom.Entity.DocentesE;
import ordanel.ednom.Entity.InstrumentoE;
import ordanel.ednom.Entity.LocalE;
import ordanel.ednom.Entity.ModalidadE;
import ordanel.ednom.Entity.PadronE;
import ordanel.ednom.Entity.SedeOperativaE;
import ordanel.ednom.Library.ConstantsUtils;

/**
 * Created by OrdNael on 30/10/2014.
 */
public class PadronDAO extends BaseDAO {

    private static final String TAG = PadronDAO.class.getSimpleName();
    private static PadronDAO padronDAO;

    String URL_Connect = ConstantsUtils.BASE_URL + "padron"; // "padron.php" "padron"
    Integer cod_sede_operativa, cod_local_sede, nro_aula;
    Long valueLong;
    Integer valueInteger;

    Cursor cursor = null;
    ContentValues contentValues = null;

    JSONObject jsonObject;
    JSONArray jsonArray;
    JSONObject jsonObjectTemp;
    JSONArray jsonArrayAulaLocal, jsonArrayDiscapacidad, jsonArrayModalidad, jsonArrayInstrumento;

    LocalE localE;
    PadronE padronE;

    ArrayList<AulaLocalE> aulaLocalEArrayList;
    ArrayList<DocentesE> docentesEArrayList;
    ArrayList<DiscapacidadE> discapacidadEArrayList;
    ArrayList<ModalidadE> modalidadEArrayList;
    ArrayList<InstrumentoE> instrumentoEArrayList;


    public static PadronDAO getInstance( Context paramContext ) {

        if ( padronDAO == null )
        {
            padronDAO = new PadronDAO( paramContext );
        }

        return padronDAO;
    }


    private PadronDAO( Context paramContext ) {

        initDBHelper( paramContext );
        initHttPostAux();

    }

    public LocalE searchNroLocal() {

        Log.e( TAG, "start searchNroLocal" );

        localE = new LocalE();

        try
        {
            openDBHelper();

            String SQL = "SELECT cod_sede_operativa, cod_local_sede FROM local";

            cursor = dbHelper.getDatabase().rawQuery( SQL, null );

            if ( cursor.moveToFirst() )
            {
                SedeOperativaE sedeOperativaE = new SedeOperativaE();
                sedeOperativaE.setCod_sede_operativa( cursor.getInt( cursor.getColumnIndex( "cod_sede_operativa" ) )  );

                localE.setSedeOperativaE( sedeOperativaE );
                localE.setCod_local_sede( cursor.getInt( cursor.getColumnIndex( "cod_local_sede" ) ) );

                valueInteger = cursor.getInt( cursor.getColumnIndex( "cod_local_sede" ) );
                Log.e( TAG, "numero de local : " + valueInteger.toString() );

                localE.setStatus( 0 );
            }
            else
            {
                localE.setStatus( 2 ); // no hay datos
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            localE.setStatus( 1 ); // error en busqueda
            Log.e( TAG, "error searchNroLocal : " + e.toString() );
        }
        finally
        {
            closeDBHelper();
            cursor.close();
        }

        Log.e( TAG, "end searchNroLocal" );

        return localE;

    }

    public PadronE padronNube( LocalE paramLocalE ) {

        Log.e( TAG, "start padronNube" );

        padronE = new PadronE();

        cod_sede_operativa = paramLocalE.getSedeOperativaE().getCod_sede_operativa();
        cod_local_sede = paramLocalE.getCod_local_sede();

        jsonArray = httpPostAux.getServerData( null, URL_Connect + "?cod_sede_operativa=" + cod_sede_operativa + "&cod_local_sede=" + cod_local_sede );

        if ( jsonArray != null )
        {
            if ( jsonArray.length() > 0 )
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

                            DiscapacidadE discapacidadE = new DiscapacidadE();
                            discapacidadE.setCod_discap( jsonObjectTemp.getInt( "cod_discap" ) );

                            ModalidadE modalidadE = new ModalidadE();
                            modalidadE.setCod_modal( jsonObjectTemp.getInt( "cod_modal" ) );

                            DocentesE docentesE = new DocentesE();
                            docentesE.setAulaLocalE( aulaLocalE );
                            docentesE.setDiscapacidadE( discapacidadE );
                            docentesE.setModalidadE( modalidadE );
                            docentesE.setDre_des( jsonObjectTemp.getString( "dre_des" ) );
                            docentesE.setUgel_des( jsonObjectTemp.getString( "ugel_des" ) );
                            docentesE.setTipo_doc( jsonObjectTemp.getString( "tipo_doc" ) );
                            docentesE.setNro_doc( jsonObjectTemp.getString( "nro_doc" ) );
                            docentesE.setApe_pat( jsonObjectTemp.getString( "ape_pat" ) );
                            docentesE.setApe_mat( jsonObjectTemp.getString( "ape_mat" ) );
                            docentesE.setNombres( jsonObjectTemp.getString( "nombres" ) );
                            docentesE.setSexo( jsonObjectTemp.getString( "sexo" ) );
                            docentesE.setFecha_nac( jsonObjectTemp.getString( "fecha_nac" ) );
                            docentesE.setEdad( jsonObjectTemp.getInt( "edad" ) );
                            docentesE.setCod_ficha( jsonObjectTemp.getString( "cod_ficha" ) );
                            docentesE.setCod_cartilla( jsonObjectTemp.getString( "cod_cartilla" ) );
                            docentesE.setEstado( jsonObjectTemp.getInt( "estado" ) );
                            docentesE.setF_registro( jsonObjectTemp.getString( "f_registro" ) );
                            docentesE.setEstado_aula( jsonObjectTemp.getInt( "estado_aula" ) );
                            docentesE.setF_aula( jsonObjectTemp.getString( "f_aula" ) );
                            docentesE.setEstado_ficha( jsonObjectTemp.getInt( "estado_ficha" ) );
                            docentesE.setF_ficha( jsonObjectTemp.getString( "f_ficha" ) );
                            docentesE.setEstado_cartilla( jsonObjectTemp.getInt( "estado_cartilla" ) );
                            docentesE.setF_caritlla( jsonObjectTemp.getString( "f_cartilla" ) );
                            docentesE.setNro_aula_cambio( jsonObjectTemp.getInt( "nro_aula_cambio" ) );

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

                    // set array INSTRUMENTO
                    jsonArrayInstrumento = jsonObject.getJSONArray("INSTRUMENTO");
                    instrumentoEArrayList = new ArrayList<InstrumentoE>();

                    for ( int i = 0; i < jsonArrayInstrumento.length(); i++ )
                    {
                        jsonObjectTemp = (JSONObject) jsonArrayInstrumento.get(i);

                        InstrumentoE instrumentoE = new InstrumentoE();
                        instrumentoE.setId_inst( jsonObjectTemp.getInt( "id_inst" ) );
                        instrumentoE.setCod_ficha( jsonObjectTemp.getString( "cod_ficha" ) );
                        instrumentoE.setCod_cartilla( jsonObjectTemp.getString( "cod_cartilla" ) );
                        instrumentoE.setNro_aula( jsonObjectTemp.getInt( "nro_aula" ) );
                        instrumentoE.setEstado_ficha( jsonObjectTemp.getInt( "estado_ficha" ) );
                        instrumentoE.setF_ficha( jsonObjectTemp.getString( "f_ficha" ) );
                        instrumentoE.setEstado_cartilla( jsonObjectTemp.getInt( "estado_cartilla" ) );
                        instrumentoE.setF_cartilla( jsonObjectTemp.getString( "f_cartilla" ) );

                        instrumentoE.setLocalE( paramLocalE );

                        instrumentoEArrayList.add( instrumentoE );

                    }

                    valueInteger = instrumentoEArrayList.size();
                    Log.e( TAG, "cantidad de modalidad : " + valueInteger.toString() );
                    padronE.setInstrumentoEList(instrumentoEArrayList);
                    // .set array INSTRUMENTO

                    padronE.setStatus( 0 );

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    padronE.setStatus( 4 ); // error en padron nube
                    Log.e( TAG, "error padronNube : " + e.toString() );
                }
            }
            else
            {
                padronE.setStatus( 3 ); // no hay datos
            }

        }
        else
        {
            padronE.setStatus( 2 ); // error en conexion
        }

        Log.e( TAG, "end padronNube" );

        return padronE;

    }

    public PadronE registrarPadron( PadronE paramPadronE ) {

        Log.e( TAG, "start registrarPadron" );

        try
        {
            openDBHelper();

            if ( this.clearPadron() )// limpio padron
            {
                // set de Arrays
                aulaLocalEArrayList = (ArrayList) paramPadronE.getAulaLocalEList();
                discapacidadEArrayList = (ArrayList) paramPadronE.getDiscapacidadEList();
                modalidadEArrayList = (ArrayList) paramPadronE.getModalidadEList();
                instrumentoEArrayList = (ArrayList) paramPadronE.getInstrumentoEList();
                // .set de Arrays

                // registro de AULAS
                for ( AulaLocalE aulaLocalE : aulaLocalEArrayList )
                {
                    cod_sede_operativa = aulaLocalE.getLocalE().getSedeOperativaE().getCod_sede_operativa();
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
                        contentValues.put( "nro_aula", nro_aula );
                        contentValues.put( "cod_discap", docentesE.getDiscapacidadE().getCod_discap() );
                        contentValues.put( "cod_modal", docentesE.getModalidadE().getCod_modal() );
                        contentValues.put( "dre_des", docentesE.getDre_des() );
                        contentValues.put( "ugel_des", docentesE.getUgel_des() );
                        contentValues.put( "tipo_doc", docentesE.getTipo_doc() );
                        contentValues.put( "nro_doc", docentesE.getNro_doc() );
                        contentValues.put( "ape_pat", docentesE.getApe_pat() );
                        contentValues.put( "ape_mat", docentesE.getApe_mat() );
                        contentValues.put( "nombres", docentesE.getNombres() );
                        contentValues.put( "sexo", docentesE.getSexo() );
                        contentValues.put( "fecha_nac", docentesE.getFecha_nac() );
                        contentValues.put( "edad", docentesE.getEdad() );
                        contentValues.put( "cod_ficha", docentesE.getCod_ficha() );
                        contentValues.put( "cod_cartilla", docentesE.getCod_cartilla() );
                        contentValues.put( "estado", docentesE.getEstado() );
                        contentValues.put( "f_registro", docentesE.getF_registro() );
                        contentValues.put( "estado_aula", docentesE.getEstado_aula() );
                        contentValues.put( "f_aula", docentesE.getF_aula() );
                        contentValues.put( "estado_ficha", docentesE.getEstado_ficha() );
                        contentValues.put( "f_ficha", docentesE.getF_ficha() );
                        contentValues.put( "estado_cartilla", docentesE.getEstado_cartilla() );
                        contentValues.put( "f_cartilla", docentesE.getF_caritlla() );
                        contentValues.put( "nro_aula_cambio", docentesE.getNro_aula_cambio() );

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


                // registro de INSTRUMENTO
                for ( InstrumentoE instrumentoE : instrumentoEArrayList )
                {
                    contentValues = new ContentValues();

                    contentValues.put( "id_inst", instrumentoE.getId_inst() );
                    contentValues.put( "cod_sede_operativa", instrumentoE.getLocalE().getSedeOperativaE().getCod_sede_operativa() );
                    contentValues.put( "cod_local_sede", instrumentoE.getLocalE().getCod_local_sede() );
                    contentValues.put( "cod_ficha", instrumentoE.getCod_ficha() );
                    contentValues.put( "cod_cartilla", instrumentoE.getCod_cartilla() );
                    contentValues.put( "nro_aula", instrumentoE.getNro_aula() );
                    contentValues.put( "estado_ficha", instrumentoE.getEstado_ficha() );
                    contentValues.put( "f_ficha", instrumentoE.getF_ficha() );
                    contentValues.put( "estado_cartilla", instrumentoE.getEstado_cartilla() );
                    contentValues.put( "f_cartilla", instrumentoE.getF_cartilla() );

                    valueLong = dbHelper.getDatabase().insertOrThrow( "instrumento", null, contentValues );
                    Log.e( TAG, "instrumento insert : " + String.valueOf(valueLong) );
                }
                // .registro de INSTRUMENTO

                dbHelper.setTransactionSuccessful();

                padronE.setStatus( 0 );

            }
            else
            {
                padronE.setStatus( 5 ); // error cleanPadron
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            padronE.setStatus( 6 ); // error register padron
            Log.e( TAG, "error registrarPadron : " + e.toString() );
        }
        finally
        {
            closeDBHelper();
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

            dbHelper.getDatabase().delete( "instrumento", null, null );
            Log.e( TAG, "Se elimino instrumento!" );

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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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