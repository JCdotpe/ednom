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
                sedeOperativaE.setCod_sede_operativa( cursor.getInt( cursor.getColumnIndex( sedeOperativaE.COD_SEDE_OPERATIVA ) )  );

                localE.setSedeOperativaE( sedeOperativaE );
                localE.setCod_local_sede( cursor.getInt( cursor.getColumnIndex( localE.COD_LOCAL_SEDE ) ) );

                valueInteger = cursor.getInt( cursor.getColumnIndex( localE.COD_LOCAL_SEDE ) );
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

        jsonArray = httpPostAux.getServerData( null, ConstantsUtils.URL_PADRON + "?cod_sede_operativa=" + cod_sede_operativa + "&cod_local_sede=" + cod_local_sede );

        if ( jsonArray != null )
        {
            if ( jsonArray.length() > 0 )
            {
                try
                {
                    jsonObject = jsonArray.getJSONObject(0);

                    // set array AULAS
                    jsonArrayAulaLocal = jsonObject.getJSONArray( "AULAS" );
                    aulaLocalEArrayList = new ArrayList<AulaLocalE>();

                    for ( int i = 0; i < jsonArrayAulaLocal.length(); i++ )
                    {
                        jsonObjectTemp = (JSONObject) jsonArrayAulaLocal.get(i);

                        AulaLocalE aulaLocalE = new AulaLocalE();
                        aulaLocalE.setLocalE( paramLocalE );
                        aulaLocalE.setNro_aula( jsonObjectTemp.getInt( aulaLocalE.NRO_AULA ) );
                        aulaLocalE.setTipo( jsonObjectTemp.getString( aulaLocalE.TIPO ) );
                        aulaLocalE.setCant_docente( jsonObjectTemp.getInt( aulaLocalE.CANT_DOCENTE ) );

                        // set array DOCENTES
                        jsonArray = (JSONArray) jsonObjectTemp.get( "DOCENTES" );
                        docentesEArrayList = new ArrayList<DocentesE>();

                        for ( int j = 0; j < jsonArray.length(); j++ )
                        {
                            jsonObjectTemp = (JSONObject) jsonArray.get(j);

                            DiscapacidadE discapacidadE = new DiscapacidadE();
                            discapacidadE.setCod_discap( jsonObjectTemp.getInt( discapacidadE.COD_DISCAP ) );

                            ModalidadE modalidadE = new ModalidadE();
                            modalidadE.setCod_modal( jsonObjectTemp.getInt( modalidadE.COD_MODAL ) );

                            DocentesE docentesE = new DocentesE();
                            docentesE.setAulaLocalE( aulaLocalE );
                            docentesE.setDiscapacidadE( discapacidadE );
                            docentesE.setModalidadE( modalidadE );
                            docentesE.setDre_des( jsonObjectTemp.getString( docentesE.DRE_DES ) );
                            docentesE.setUgel_des( jsonObjectTemp.getString( docentesE.UGEL_DES ) );
                            docentesE.setTipo_doc( jsonObjectTemp.getString( docentesE.TIPO_DOC ) );
                            docentesE.setNro_doc( jsonObjectTemp.getString( docentesE.NRO_DOC ) );
                            docentesE.setApe_pat( jsonObjectTemp.getString( docentesE.APE_PAT ) );
                            docentesE.setApe_mat( jsonObjectTemp.getString( docentesE.APE_MAT ) );
                            docentesE.setNombres( jsonObjectTemp.getString( docentesE.NOMBRES ) );
                            docentesE.setSexo( jsonObjectTemp.getString( docentesE.SEXO ) );
                            docentesE.setFecha_nac( jsonObjectTemp.getString( docentesE.FECHA_NAC ) );
                            docentesE.setEdad( jsonObjectTemp.getInt( docentesE.EDAD ) );
                            docentesE.setCod_ficha( jsonObjectTemp.getString( docentesE.COD_FICHA ) );
                            docentesE.setCod_cartilla( jsonObjectTemp.getString( docentesE.COD_CARTILLA ) );
                            docentesE.setEstado( jsonObjectTemp.getInt( docentesE.ESTADO ) );
                            docentesE.setF_registro( jsonObjectTemp.getString( docentesE.F_REGISTRO ) );
                            docentesE.setEstado_aula( jsonObjectTemp.getInt( docentesE.ESTADO_AULA ) );
                            docentesE.setF_aula( jsonObjectTemp.getString( docentesE.F_AULA ) );
                            docentesE.setEstado_ficha( jsonObjectTemp.getInt( docentesE.ESTADO_FICHA ) );
                            docentesE.setF_ficha( jsonObjectTemp.getString( docentesE.F_FICHA ) );
                            docentesE.setEstado_cartilla( jsonObjectTemp.getInt( docentesE.ESTADO_CARTILLA ) );
                            docentesE.setF_caritlla( jsonObjectTemp.getString( docentesE.F_CARTILLA ) );
                            docentesE.setNro_aula_cambio( jsonObjectTemp.getInt( docentesE.NRO_AULA_CAMBIO ) );

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
                    jsonArrayDiscapacidad = jsonObject.getJSONArray( "DISCAPACIDAD" );
                    discapacidadEArrayList = new ArrayList<DiscapacidadE>();

                    for ( int i = 0; i < jsonArrayDiscapacidad.length(); i++ )
                    {
                        jsonObjectTemp = (JSONObject) jsonArrayDiscapacidad.get(i);

                        DiscapacidadE discapacidadE = new DiscapacidadE();
                        discapacidadE.setCod_discap( jsonObjectTemp.getInt( discapacidadE.COD_DISCAP ) );
                        discapacidadE.setDiscapacidad( jsonObjectTemp.getString( discapacidadE.DISCAPACIDAD ) );

                        discapacidadEArrayList.add( discapacidadE );
                    }

                    valueInteger = discapacidadEArrayList.size();
                    Log.e( TAG, "cantidad de discapacidad : " + valueInteger.toString() );
                    padronE.setDiscapacidadEList( discapacidadEArrayList );
                    // .set array DISCAPACIDAD


                    // set array MODALIDAD
                    jsonArrayModalidad = jsonObject.getJSONArray( "MODALIDAD" );
                    modalidadEArrayList = new ArrayList<ModalidadE>();

                    for ( int i = 0; i < jsonArrayModalidad.length(); i++ )
                    {
                        jsonObjectTemp = (JSONObject) jsonArrayModalidad.get(i);

                        ModalidadE modalidadE = new ModalidadE();
                        modalidadE.setCod_modal( jsonObjectTemp.getInt( modalidadE.COD_MODAL ) );
                        modalidadE.setModalidad( jsonObjectTemp.getString( modalidadE.MODALIDAD ) );

                        modalidadEArrayList.add( modalidadE );
                    }

                    valueInteger = modalidadEArrayList.size();
                    Log.e( TAG, "cantidad de modalidad : " + valueInteger.toString() );
                    padronE.setModalidadEList( modalidadEArrayList );
                    // .set array MODALIDAD

                    // set array INSTRUMENTO
                    jsonArrayInstrumento = jsonObject.getJSONArray( "INSTRUMENTO" );
                    instrumentoEArrayList = new ArrayList<InstrumentoE>();

                    for ( int i = 0; i < jsonArrayInstrumento.length(); i++ )
                    {
                        jsonObjectTemp = (JSONObject) jsonArrayInstrumento.get(i);

                        InstrumentoE instrumentoE = new InstrumentoE();
                        instrumentoE.setId_inst( jsonObjectTemp.getInt( instrumentoE.ID_INST ) );
                        instrumentoE.setCod_ficha( jsonObjectTemp.getString( instrumentoE.COD_FICHA ) );
                        instrumentoE.setCod_cartilla( jsonObjectTemp.getString( instrumentoE.COD_CARTILLA ) );
                        instrumentoE.setNro_aula( jsonObjectTemp.getInt( instrumentoE.NRO_AULA ) );
                        instrumentoE.setEstado_ficha( jsonObjectTemp.getInt( instrumentoE.ESTADO_FICHA ) );
                        instrumentoE.setF_ficha( jsonObjectTemp.getString( instrumentoE.F_FICHA ) );
                        instrumentoE.setEstado_cartilla( jsonObjectTemp.getInt( instrumentoE.ESTADO_CARTILLA ) );
                        instrumentoE.setF_cartilla( jsonObjectTemp.getString( instrumentoE.F_CARTILLA ) );

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

                    contentValues.put( aulaLocalE.getLocalE().getSedeOperativaE().COD_SEDE_OPERATIVA, cod_sede_operativa );
                    contentValues.put( aulaLocalE.getLocalE().COD_LOCAL_SEDE, cod_local_sede);
                    contentValues.put( aulaLocalE.NRO_AULA, nro_aula);
                    contentValues.put( aulaLocalE.TIPO, aulaLocalE.getTipo() );
                    contentValues.put( aulaLocalE.CANT_DOCENTE, aulaLocalE.getCant_docente() );

                    valueLong = dbHelper.getDatabase().insertOrThrow( "aula_local", null, contentValues );
                    Log.e( TAG, "aula_local insert : " + String.valueOf(valueLong) );

                    // registro de Docentes
                    for ( DocentesE docentesE : aulaLocalE.getDocentesEList() )
                    {
                        contentValues = new ContentValues();

                        contentValues.put( docentesE.getAulaLocalE().getLocalE().getSedeOperativaE().COD_SEDE_OPERATIVA, cod_sede_operativa );
                        contentValues.put( docentesE.getAulaLocalE().getLocalE().COD_LOCAL_SEDE, cod_local_sede );
                        contentValues.put( docentesE.getAulaLocalE().NRO_AULA, nro_aula );
                        contentValues.put( docentesE.getDiscapacidadE().COD_DISCAP, docentesE.getDiscapacidadE().getCod_discap() );
                        contentValues.put( docentesE.getModalidadE().COD_MODAL, docentesE.getModalidadE().getCod_modal() );
                        contentValues.put( docentesE.DRE_DES, docentesE.getDre_des() );
                        contentValues.put( docentesE.UGEL_DES, docentesE.getUgel_des() );
                        contentValues.put( docentesE.TIPO_DOC, docentesE.getTipo_doc() );
                        contentValues.put( docentesE.NRO_DOC, docentesE.getNro_doc() );
                        contentValues.put( docentesE.APE_PAT, docentesE.getApe_pat() );
                        contentValues.put( docentesE.APE_MAT, docentesE.getApe_mat() );
                        contentValues.put( docentesE.NOMBRES, docentesE.getNombres() );
                        contentValues.put( docentesE.SEXO, docentesE.getSexo() );
                        contentValues.put( docentesE.FECHA_NAC, docentesE.getFecha_nac() );
                        contentValues.put( docentesE.EDAD, docentesE.getEdad() );
                        contentValues.put( docentesE.COD_FICHA, docentesE.getCod_ficha() );
                        contentValues.put( docentesE.COD_CARTILLA, docentesE.getCod_cartilla() );
                        contentValues.put( docentesE.ESTADO, docentesE.getEstado() );
                        contentValues.put( docentesE.F_REGISTRO, docentesE.getF_registro() );
                        contentValues.put( docentesE.ESTADO_AULA, docentesE.getEstado_aula() );
                        contentValues.put( docentesE.F_AULA, docentesE.getF_aula() );
                        contentValues.put( docentesE.ESTADO_FICHA, docentesE.getEstado_ficha() );
                        contentValues.put( docentesE.F_FICHA, docentesE.getF_ficha() );
                        contentValues.put( docentesE.ESTADO_CARTILLA, docentesE.getEstado_cartilla() );
                        contentValues.put( docentesE.F_CARTILLA, docentesE.getF_caritlla() );
                        contentValues.put( docentesE.NRO_AULA_CAMBIO, docentesE.getNro_aula_cambio() );

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

                    contentValues.put( discapacidadE.COD_DISCAP, discapacidadE.getCod_discap() );
                    contentValues.put( discapacidadE.DISCAPACIDAD, discapacidadE.getDiscapacidad() );

                    valueLong = dbHelper.getDatabase().insertOrThrow( "discapacidad", null, contentValues );
                    Log.e( TAG, "discapacidad insert : " + String.valueOf(valueLong) );
                }
                // .registro de DISCAPACIDAD

                // registro de MODALIDAD
                for ( ModalidadE modalidadE : modalidadEArrayList )
                {
                    contentValues = new ContentValues();

                    contentValues.put( modalidadE.COD_MODAL, modalidadE.getCod_modal() );
                    contentValues.put(  modalidadE.MODALIDAD, modalidadE.getModalidad() );

                    valueLong = dbHelper.getDatabase().insertOrThrow( "modalidad", null, contentValues );
                    Log.e( TAG, "modalidad insert : " + String.valueOf(valueLong) );
                }
                // .registro de MODALIDAD


                // registro de INSTRUMENTO
                for ( InstrumentoE instrumentoE : instrumentoEArrayList )
                {
                    contentValues = new ContentValues();

                    contentValues.put( instrumentoE.ID_INST, instrumentoE.getId_inst() );
                    contentValues.put( instrumentoE.getLocalE().getSedeOperativaE().COD_SEDE_OPERATIVA, instrumentoE.getLocalE().getSedeOperativaE().getCod_sede_operativa() );
                    contentValues.put( instrumentoE.getLocalE().COD_LOCAL_SEDE, instrumentoE.getLocalE().getCod_local_sede() );
                    contentValues.put( instrumentoE.COD_FICHA, instrumentoE.getCod_ficha() );
                    contentValues.put( instrumentoE.COD_CARTILLA, instrumentoE.getCod_cartilla() );
                    contentValues.put( instrumentoE.NRO_AULA, instrumentoE.getNro_aula() );
                    contentValues.put( instrumentoE.ESTADO_FICHA, instrumentoE.getEstado_ficha() );
                    contentValues.put( instrumentoE.F_FICHA, instrumentoE.getF_ficha() );
                    contentValues.put( instrumentoE.ESTADO_CARTILLA, instrumentoE.getEstado_cartilla() );
                    contentValues.put( instrumentoE.F_CARTILLA, instrumentoE.getF_cartilla() );

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