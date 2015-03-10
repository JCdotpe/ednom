package ordanel.ednom.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ordanel.ednom.Entity.AulaLocalE;
import ordanel.ednom.Entity.DiscapacidadE;
import ordanel.ednom.Entity.DocentesE;
import ordanel.ednom.Entity.InstrumentoE;
import ordanel.ednom.Entity.LocalE;
import ordanel.ednom.Entity.ModalidadE;
import ordanel.ednom.Entity.PadronE;
import ordanel.ednom.Entity.SedeOperativaE;
import ordanel.ednom.Entity.UsuarioLocalE;
import ordanel.ednom.Library.ConstantsUtils;

/**
 * Created by OrdNael on 30/10/2014.
 */
public class PadronDAO extends BaseDAO {

    private static final String TAG = PadronDAO.class.getSimpleName();
    private static PadronDAO padronDAO;

    Integer nro_aula;

    JSONObject jsonObjectTemp;
    JSONArray jsonArrayAulaLocal, jsonArrayDiscapacidad, jsonArrayModalidad, jsonArrayInstrumento, jsonArrayUsuarioLocal;

    LocalE localE;
    PadronE padronE;
    DiscapacidadE discapacidadE;
    AulaLocalE aulaLocalE;
    ModalidadE modalidadE;
    DocentesE docentesE;
    InstrumentoE instrumentoE;
    UsuarioLocalE usuarioLocalE;

    ArrayList<AulaLocalE> aulaLocalEArrayList;
    ArrayList<DocentesE> docentesEArrayList;
    ArrayList<DiscapacidadE> discapacidadEArrayList;
    ArrayList<ModalidadE> modalidadEArrayList;
    ArrayList<InstrumentoE> instrumentoEArrayList;
    ArrayList<UsuarioLocalE> usuarioLocalEArrayList;


    public synchronized static PadronDAO getInstance( Context paramContext ) {

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

            SQL = "SELECT cod_sede_operativa, cod_local_sede FROM local";

            cursor = dbHelper.getDatabase().rawQuery( SQL, null );

            if ( cursor.moveToFirst() )
            {
                SedeOperativaE sedeOperativaE = new SedeOperativaE();
                sedeOperativaE.setCod_sede_operativa( cursor.getInt( cursor.getColumnIndex( SedeOperativaE.COD_SEDE_OPERATIVA ) )  );

                localE.setSedeOperativaE( sedeOperativaE );
                localE.setCod_local_sede( cursor.getInt( cursor.getColumnIndex( LocalE.COD_LOCAL_SEDE ) ) );

                valueInteger = cursor.getInt( cursor.getColumnIndex( LocalE.COD_LOCAL_SEDE ) );
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

                        aulaLocalE = new AulaLocalE();
                        aulaLocalE.setLocalE( paramLocalE );
                        aulaLocalE.setNro_aula( jsonObjectTemp.getInt( AulaLocalE.NRO_AULA ) );
                        aulaLocalE.setTipo( jsonObjectTemp.getString( AulaLocalE.TIPO ) );
                        aulaLocalE.setCant_docente( jsonObjectTemp.getInt( AulaLocalE.CANT_DOCENTE ) );

                        // set array DOCENTES
                        jsonArray = (JSONArray) jsonObjectTemp.get( "DOCENTES" );
                        docentesEArrayList = new ArrayList<DocentesE>();

                        for ( int j = 0; j < jsonArray.length(); j++ )
                        {
                            jsonObjectTemp = (JSONObject) jsonArray.get(j);

                            discapacidadE = new DiscapacidadE();
                            discapacidadE.setCod_discap( jsonObjectTemp.getInt( DiscapacidadE.COD_DISCAP ) );

                            modalidadE = new ModalidadE();
                            modalidadE.setCod_modal( jsonObjectTemp.getInt( ModalidadE.COD_MODAL ) );

                            docentesE = new DocentesE();
                            docentesE.setAulaLocalE( aulaLocalE );
                            docentesE.setDiscapacidadE( discapacidadE );
                            docentesE.setModalidadE( modalidadE );
                            docentesE.setDre_des( jsonObjectTemp.getString( DocentesE.DRE_DES ) );
                            docentesE.setUgel_des( jsonObjectTemp.getString( DocentesE.UGEL_DES ) );
                            docentesE.setTipo_doc( jsonObjectTemp.getString( DocentesE.TIPO_DOC ) );
                            docentesE.setNro_doc( jsonObjectTemp.getString( DocentesE.NRO_DOC ) );
                            docentesE.setApe_pat( jsonObjectTemp.getString( DocentesE.APE_PAT ) );
                            docentesE.setApe_mat( jsonObjectTemp.getString( DocentesE.APE_MAT ) );
                            docentesE.setNombres( jsonObjectTemp.getString( DocentesE.NOMBRES ) );
                            docentesE.setSexo( jsonObjectTemp.getString( DocentesE.SEXO ) );
                            docentesE.setFecha_nac( jsonObjectTemp.getString( DocentesE.FECHA_NAC ) );
                            docentesE.setEdad( jsonObjectTemp.getInt( DocentesE.EDAD ) );
                            docentesE.setCod_ficha( jsonObjectTemp.getString( DocentesE.COD_FICHA ) );
                            docentesE.setCod_cartilla( jsonObjectTemp.getString( DocentesE.COD_CARTILLA ) );
                            docentesE.setEstado( jsonObjectTemp.getInt( DocentesE.ESTADO ) );
                            docentesE.setF_registro( jsonObjectTemp.getString( DocentesE.F_REGISTRO ) );
                            docentesE.setEstado_aula( jsonObjectTemp.getInt( DocentesE.ESTADO_AULA ) );
                            docentesE.setF_aula( jsonObjectTemp.getString( DocentesE.F_AULA ) );
                            docentesE.setEstado_ficha( jsonObjectTemp.getInt( DocentesE.ESTADO_FICHA ) );
                            docentesE.setF_ficha( jsonObjectTemp.getString( DocentesE.F_FICHA ) );
                            docentesE.setEstado_cartilla( jsonObjectTemp.getInt( DocentesE.ESTADO_CARTILLA ) );
                            docentesE.setF_caritlla( jsonObjectTemp.getString( DocentesE.F_CARTILLA ) );
                            docentesE.setNro_aula_cambio( jsonObjectTemp.getInt( DocentesE.NRO_AULA_CAMBIO ) );

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

                        discapacidadE = new DiscapacidadE();
                        discapacidadE.setCod_discap( jsonObjectTemp.getInt( DiscapacidadE.COD_DISCAP ) );
                        discapacidadE.setDiscapacidad( jsonObjectTemp.getString( DiscapacidadE.DISCAPACIDAD ) );

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

                        modalidadE = new ModalidadE();
                        modalidadE.setCod_modal( jsonObjectTemp.getInt( ModalidadE.COD_MODAL ) );
                        modalidadE.setModalidad( jsonObjectTemp.getString( ModalidadE.MODALIDAD ) );

                        modalidadEArrayList.add( modalidadE );
                    }

                    valueInteger = modalidadEArrayList.size();
                    Log.e( TAG, "cantidad de modalidad : " + valueInteger.toString() );
                    padronE.setModalidadEList( modalidadEArrayList );
                    // .set array MODALIDAD
/*
                    // set array INSTRUMENTO
                    jsonArrayInstrumento = jsonObject.getJSONArray( "INSTRUMENTO" );
                    instrumentoEArrayList = new ArrayList<InstrumentoE>();

                    for ( int i = 0; i < jsonArrayInstrumento.length(); i++ )
                    {
                        jsonObjectTemp = (JSONObject) jsonArrayInstrumento.get(i);

                        instrumentoE = new InstrumentoE();
                        instrumentoE.setId_inst( jsonObjectTemp.getInt( InstrumentoE.ID_INST ) );
                        instrumentoE.setCod_ficha( jsonObjectTemp.getString( InstrumentoE.COD_FICHA ) );
                        instrumentoE.setCod_cartilla( jsonObjectTemp.getString( InstrumentoE.COD_CARTILLA ) );
                        instrumentoE.setNro_aula( jsonObjectTemp.getInt( InstrumentoE.NRO_AULA ) );
                        instrumentoE.setEstado_ficha( jsonObjectTemp.getInt( InstrumentoE.ESTADO_FICHA ) );
                        instrumentoE.setF_ficha( jsonObjectTemp.getString( InstrumentoE.F_FICHA ) );
                        instrumentoE.setEstado_cartilla( jsonObjectTemp.getInt( InstrumentoE.ESTADO_CARTILLA ) );
                        instrumentoE.setF_cartilla( jsonObjectTemp.getString( InstrumentoE.F_CARTILLA ) );

                        instrumentoE.setLocalE( paramLocalE );

                        instrumentoEArrayList.add( instrumentoE );

                    }

                    valueInteger = instrumentoEArrayList.size();
                    Log.e( TAG, "cantidad de modalidad : " + valueInteger.toString() );
                    padronE.setInstrumentoEList(instrumentoEArrayList);
                    // .set array INSTRUMENTO

                    // set array USUARIOS
                    jsonArrayUsuarioLocal = jsonObject.getJSONArray( "USUARIOS" );
                    usuarioLocalEArrayList = new ArrayList<UsuarioLocalE>();

                    for ( int i = 0; i < jsonArrayUsuarioLocal.length(); i++ )
                    {
                        jsonObjectTemp = (JSONObject) jsonArrayUsuarioLocal.get(i);

                        usuarioLocalE = new UsuarioLocalE();
                        usuarioLocalE.setIdUsuario( jsonObjectTemp.getInt( UsuarioLocalE.IDUSUARIO ) );
                        usuarioLocalE.setClave( jsonObjectTemp.getString( UsuarioLocalE.CLAVE ) );
                        usuarioLocalE.setRol( jsonObjectTemp.getInt( UsuarioLocalE.ROL ) );
                        usuarioLocalE.setUsuario( jsonObjectTemp.getString( UsuarioLocalE.USUARIO ) );

                        usuarioLocalE.setLocalE( paramLocalE );

                        usuarioLocalEArrayList.add( usuarioLocalE );

                    }

                    valueInteger = usuarioLocalEArrayList.size();
                    Log.e( TAG, "cantidad de usuarios : " + valueInteger.toString() );
                    padronE.setUsuarioLocalEList(usuarioLocalEArrayList);
                    // .set array USUARIOS
*/
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
                aulaLocalEArrayList = (ArrayList<AulaLocalE>) paramPadronE.getAulaLocalEList();
                discapacidadEArrayList = (ArrayList<DiscapacidadE>) paramPadronE.getDiscapacidadEList();
                modalidadEArrayList = (ArrayList<ModalidadE>) paramPadronE.getModalidadEList();
                instrumentoEArrayList = (ArrayList<InstrumentoE>) paramPadronE.getInstrumentoEList();
                usuarioLocalEArrayList = (ArrayList<UsuarioLocalE>) paramPadronE.getUsuarioLocalEList();
                // .set de Arrays

                // registro de AULAS
                for ( AulaLocalE aulaLocalE : aulaLocalEArrayList )
                {
                    cod_sede_operativa = aulaLocalE.getLocalE().getSedeOperativaE().getCod_sede_operativa();
                    cod_local_sede = aulaLocalE.getLocalE().getCod_local_sede();
                    nro_aula = aulaLocalE.getNro_aula();

                    contentValues = new ContentValues();

                    contentValues.put( SedeOperativaE.COD_SEDE_OPERATIVA, cod_sede_operativa );
                    contentValues.put( LocalE.COD_LOCAL_SEDE, cod_local_sede);
                    contentValues.put( AulaLocalE.NRO_AULA, nro_aula);
                    contentValues.put( AulaLocalE.TIPO, aulaLocalE.getTipo() );
                    contentValues.put( AulaLocalE.CANT_DOCENTE, aulaLocalE.getCant_docente() );

                    valueLong = dbHelper.getDatabase().insertOrThrow( "aula_local", null, contentValues );
                    Log.e( TAG, "aula_local insert : " + String.valueOf(valueLong) );


                    // registro de Docentes
                    for ( DocentesE docentesE : aulaLocalE.getDocentesEList() )
                    {
                        contentValues = new ContentValues();

                        contentValues.put( SedeOperativaE.COD_SEDE_OPERATIVA, cod_sede_operativa );
                        contentValues.put( LocalE.COD_LOCAL_SEDE, cod_local_sede );
                        contentValues.put( AulaLocalE.NRO_AULA, nro_aula );
                        contentValues.put( DiscapacidadE.COD_DISCAP, docentesE.getDiscapacidadE().getCod_discap() );
                        contentValues.put( ModalidadE.COD_MODAL, docentesE.getModalidadE().getCod_modal() );
                        contentValues.put( DocentesE.DRE_DES, docentesE.getDre_des() );
                        contentValues.put( DocentesE.UGEL_DES, docentesE.getUgel_des() );
                        contentValues.put( DocentesE.TIPO_DOC, docentesE.getTipo_doc() );
                        contentValues.put( DocentesE.NRO_DOC, docentesE.getNro_doc() );
                        contentValues.put( DocentesE.APE_PAT, docentesE.getApe_pat() );
                        contentValues.put( DocentesE.APE_MAT, docentesE.getApe_mat() );
                        contentValues.put( DocentesE.NOMBRES, docentesE.getNombres() );
                        contentValues.put( DocentesE.SEXO, docentesE.getSexo() );
                        contentValues.put( DocentesE.FECHA_NAC, docentesE.getFecha_nac() );
                        contentValues.put( DocentesE.EDAD, docentesE.getEdad() );
                        contentValues.put( DocentesE.COD_FICHA, docentesE.getCod_ficha() );
                        contentValues.put( DocentesE.COD_CARTILLA, docentesE.getCod_cartilla() );
                        contentValues.put( DocentesE.ESTADO, docentesE.getEstado() );
                        contentValues.put( DocentesE.F_REGISTRO, docentesE.getF_registro() );
                        contentValues.put( DocentesE.ESTADO_AULA, docentesE.getEstado_aula() );
                        contentValues.put( DocentesE.F_AULA, docentesE.getF_aula() );
                        contentValues.put( DocentesE.ESTADO_FICHA, docentesE.getEstado_ficha() );
                        contentValues.put( DocentesE.F_FICHA, docentesE.getF_ficha() );
                        contentValues.put( DocentesE.ESTADO_CARTILLA, docentesE.getEstado_cartilla() );
                        contentValues.put( DocentesE.F_CARTILLA, docentesE.getF_caritlla() );
                        contentValues.put( DocentesE.NRO_AULA_CAMBIO, docentesE.getNro_aula_cambio() );

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

                    contentValues.put( DiscapacidadE.COD_DISCAP, discapacidadE.getCod_discap() );
                    contentValues.put( DiscapacidadE.DISCAPACIDAD, discapacidadE.getDiscapacidad() );

                    valueLong = dbHelper.getDatabase().insertOrThrow( "discapacidad", null, contentValues );
                    Log.e( TAG, "discapacidad insert : " + String.valueOf(valueLong) );
                }
                // .registro de DISCAPACIDAD

                // registro de MODALIDAD
                for ( ModalidadE modalidadE : modalidadEArrayList )
                {
                    contentValues = new ContentValues();

                    contentValues.put( ModalidadE.COD_MODAL, modalidadE.getCod_modal() );
                    contentValues.put(  ModalidadE.MODALIDAD, modalidadE.getModalidad() );

                    valueLong = dbHelper.getDatabase().insertOrThrow( "modalidad", null, contentValues );
                    Log.e( TAG, "modalidad insert : " + String.valueOf(valueLong) );
                }
                // .registro de MODALIDAD

/*
                // registro de INSTRUMENTO
                for ( InstrumentoE instrumentoE : instrumentoEArrayList )
                {
                    contentValues = new ContentValues();

                    contentValues.put( InstrumentoE.ID_INST, instrumentoE.getId_inst() );
                    contentValues.put( SedeOperativaE.COD_SEDE_OPERATIVA, instrumentoE.getLocalE().getSedeOperativaE().getCod_sede_operativa() );
                    contentValues.put( LocalE.COD_LOCAL_SEDE, instrumentoE.getLocalE().getCod_local_sede() );
                    contentValues.put( InstrumentoE.COD_FICHA, instrumentoE.getCod_ficha() );
                    contentValues.put( InstrumentoE.COD_CARTILLA, instrumentoE.getCod_cartilla() );
                    contentValues.put( InstrumentoE.NRO_AULA, instrumentoE.getNro_aula() );
                    contentValues.put( InstrumentoE.ESTADO_FICHA, instrumentoE.getEstado_ficha() );
                    contentValues.put( InstrumentoE.F_FICHA, instrumentoE.getF_ficha() );
                    contentValues.put( InstrumentoE.ESTADO_CARTILLA, instrumentoE.getEstado_cartilla() );
                    contentValues.put( InstrumentoE.F_CARTILLA, instrumentoE.getF_cartilla() );

                    valueLong = dbHelper.getDatabase().insertOrThrow( "instrumento", null, contentValues );
                    Log.e( TAG, "instrumento insert : " + String.valueOf(valueLong) );
                }
                // .registro de INSTRUMENTO

                // registrar Usuario_Local
                for (UsuarioLocalE usuarioLocalE : usuarioLocalEArrayList){
                    cod_sede_operativa = usuarioLocalE.getLocalE().getSedeOperativaE().getCod_sede_operativa();
                    cod_local_sede = aulaLocalE.getLocalE().getCod_local_sede();

                    contentValues = new ContentValues();

                    contentValues.put( SedeOperativaE.COD_SEDE_OPERATIVA, cod_sede_operativa );
                    contentValues.put( LocalE.COD_LOCAL_SEDE, cod_local_sede);
                    contentValues.put( UsuarioLocalE.IDUSUARIO, usuarioLocalE.getIdUsuario());
                    contentValues.put( UsuarioLocalE.CLAVE, usuarioLocalE.getClave());
                    contentValues.put( UsuarioLocalE.ROL, usuarioLocalE.getRol());
                    contentValues.put( UsuarioLocalE.USUARIO, usuarioLocalE.getUsuario());

                    valueLong = dbHelper.getDatabase().insertOrThrow( "usuario_local", null, contentValues );
                    Log.e( TAG, "usuario_local insert : " + String.valueOf(valueLong) );

                }

                // registrar Usuario_Local
*/
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

    public void getAllforSync() {

        Log.e( TAG, "start getAllforSync" );

        try
        {
            openDBHelper();

            SQL = "SELECT nro_doc, estado, f_registro, estado_aula, f_aula, estado_ficha, f_ficha, estado_cartilla, f_cartilla, nro_aula_cambio FROM docentes WHERE estado = 1 or estado_aula = 1 or estado_ficha = 1 or estado_cartilla = 1";
            Log.e( TAG, "string sql docentes : " + SQL );
            cursor = dbHelper.getDatabase().rawQuery( SQL, null );

            jsonObject = new JSONObject();

            jsonArray = new JSONArray();

            Boolean syncronizar = false;

            if ( cursor.moveToFirst() )
            {
                while ( !cursor.isAfterLast() )
                {
                    jsonObjectTemp = new JSONObject();

                    jsonObjectTemp.put( DocentesE.NRO_DOC, cursor.getString( cursor.getColumnIndex( DocentesE.NRO_DOC ) ) );
                    jsonObjectTemp.put( DocentesE.ESTADO, cursor.getInt( cursor.getColumnIndex( DocentesE.ESTADO ) ) );
                    jsonObjectTemp.put( DocentesE.F_REGISTRO, cursor.getString( cursor.getColumnIndex( DocentesE.F_REGISTRO ) ) );
                    jsonObjectTemp.put( DocentesE.ESTADO_AULA, cursor.getInt( cursor.getColumnIndex( DocentesE.ESTADO_AULA ) ) );
                    jsonObjectTemp.put( DocentesE.F_AULA, cursor.getString( cursor.getColumnIndex( DocentesE.F_AULA ) ) );
                    jsonObjectTemp.put( DocentesE.ESTADO_FICHA, cursor.getInt( cursor.getColumnIndex( DocentesE.ESTADO_FICHA ) ) );
                    jsonObjectTemp.put( DocentesE.F_FICHA, cursor.getString( cursor.getColumnIndex( DocentesE.F_FICHA ) ) );
                    jsonObjectTemp.put( DocentesE.ESTADO_CARTILLA, cursor.getInt( cursor.getColumnIndex( DocentesE.ESTADO_CARTILLA ) ) );
                    jsonObjectTemp.put( DocentesE.F_CARTILLA, cursor.getString( cursor.getColumnIndex( DocentesE.F_CARTILLA ) ) );
                    jsonObjectTemp.put( DocentesE.NRO_AULA_CAMBIO, cursor.getInt( cursor.getColumnIndex( DocentesE.NRO_AULA_CAMBIO ) ) );

                    jsonArray.put( jsonObjectTemp );

                    cursor.moveToNext();

                }

                jsonObject.put( "DOCENTES", jsonArray );
                syncronizar = true;
            }
            Log.e(TAG, "DOCENTES: " + jsonArray.toString());

/*
            SQL = "SELECT id_inst, cod_sede_operativa, cod_local_sede, cod_ficha, cod_cartilla, nro_aula, estado_ficha, f_ficha, estado_cartilla, f_cartilla FROM instrumento WHERE estado_ficha = 1 or estado_cartilla = 1";
            Log.e( TAG, "string sql instrumento : " + SQL );

            cursor = dbHelper.getDatabase().rawQuery( SQL, null );

            jsonArray = new JSONArray();

            if ( cursor.moveToFirst() )
            {
                while ( !cursor.isAfterLast() )
                {
                    JSONObject jsonObjectTemp = new JSONObject();

                    jsonObjectTemp.put( InstrumentoE.ID_INST, cursor.getInt( cursor.getColumnIndex( InstrumentoE.ID_INST ) ) );
                    jsonObjectTemp.put( SedeOperativaE.COD_SEDE_OPERATIVA, cursor.getInt( cursor.getColumnIndex( SedeOperativaE.COD_SEDE_OPERATIVA ) ) );
                    jsonObjectTemp.put( LocalE.COD_LOCAL_SEDE, cursor.getInt( cursor.getColumnIndex( LocalE.COD_LOCAL_SEDE ) ) );
                    jsonObjectTemp.put( InstrumentoE.COD_FICHA, cursor.getString( cursor.getColumnIndex( InstrumentoE.COD_FICHA ) ) );
                    jsonObjectTemp.put( InstrumentoE.COD_CARTILLA, cursor.getString( cursor.getColumnIndex( InstrumentoE.COD_CARTILLA ) ) );
                    jsonObjectTemp.put( InstrumentoE.NRO_AULA, cursor.getInt(cursor.getColumnIndex(InstrumentoE.NRO_AULA)) );
                    jsonObjectTemp.put( InstrumentoE.ESTADO_FICHA, cursor.getInt(cursor.getColumnIndex(InstrumentoE.ESTADO_FICHA)) );
                    jsonObjectTemp.put( InstrumentoE.F_FICHA, cursor.getString( cursor.getColumnIndex( InstrumentoE.F_FICHA ) ) );
                    jsonObjectTemp.put( InstrumentoE.ESTADO_CARTILLA, cursor.getInt(cursor.getColumnIndex(InstrumentoE.ESTADO_CARTILLA)) );
                    jsonObjectTemp.put( InstrumentoE.F_CARTILLA, cursor.getString( cursor.getColumnIndex( InstrumentoE.F_CARTILLA ) ) );

                    jsonArray.put( jsonObjectTemp );

                    cursor.moveToNext();

                }

                jsonObject.put( "INSTRUMENTO", jsonArray );

                syncronizar = true;
            }
            Log.e(TAG, "INSTRUMENTO: " + jsonArray.toString());
*/

            if ( syncronizar )
            {
                initHttPostAux();

                jsonArray = new JSONArray();
                jsonArray.put( jsonObject );
                String json = jsonArray.toString();

                ArrayList<NameValuePair> parametersPost = new ArrayList<NameValuePair>();
                parametersPost.add( new BasicNameValuePair( "data", json ) );

                Log.e( "Welcome", "json : " + json );

                JSONArray jsonArrayGet = httpPostAux.getServerData( parametersPost, ConstantsUtils.URL_SYNC );
                Log.e( "Welcome", "get json : " + jsonArrayGet.toString() );

                if ( jsonArrayGet.length() > 0 )
                {
                    jsonObject = jsonArrayGet.getJSONObject(0);

                    // set array DOCENTES
                    jsonArray = jsonObject.getJSONArray( "DOCENTE" );

                    for ( int i = 0; i < jsonArray.length(); i++ )
                    {
                        jsonObjectTemp = (JSONObject) jsonArray.get(i) ;

                        contentValues = new ContentValues();
                        contentValues.put( DocentesE.ESTADO, jsonObjectTemp.getInt( DocentesE.ESTADO ) );
                        contentValues.put( DocentesE.ESTADO_AULA, jsonObjectTemp.getInt( DocentesE.ESTADO_AULA ) );
                        contentValues.put( DocentesE.ESTADO_FICHA, jsonObjectTemp.getInt( DocentesE.ESTADO_FICHA ) );
                        contentValues.put( DocentesE.ESTADO_CARTILLA, jsonObjectTemp.getInt( DocentesE.ESTADO_CARTILLA ) );

                        String nro_doc = jsonObjectTemp.getString( DocentesE.NRO_DOC );
                        Where = "nro_doc = '" + nro_doc + "'";
                        Log.e( TAG, "where : " + Where );
                        valueInteger = dbHelper.getDatabase().updateWithOnConflict( "docentes", contentValues, Where, null, SQLiteDatabase.CONFLICT_IGNORE );
                        Log.e( TAG, "sync update docentes : " + String.valueOf( valueInteger ) );

                    }
                    // .set array DOCENTES

/*
                    // set array INSTRUMENTOS
                    jsonArray = jsonObject.getJSONArray( "INSTRUMENTO" );

                    for ( int i = 0; i < jsonArray.length(); i++ )
                    {
                        jsonObjectTemp = (JSONObject) jsonArray.get(i);

                        contentValues = new ContentValues();
                        contentValues.put( InstrumentoE.ESTADO_FICHA, jsonObjectTemp.getInt( InstrumentoE.ESTADO_FICHA ) );
                        contentValues.put( InstrumentoE.ESTADO_CARTILLA, jsonObjectTemp.getInt( InstrumentoE.ESTADO_CARTILLA ) );

                        String cod_ficha, cod_cartilla;
                        cod_ficha = jsonObjectTemp.getString( InstrumentoE.COD_FICHA );
                        cod_cartilla = jsonObjectTemp.getString( InstrumentoE.COD_CARTILLA );

                        Where = InstrumentoE.COD_FICHA + " = '" + cod_ficha + "' and " + InstrumentoE.COD_CARTILLA + " = '" + cod_cartilla + "'";
                        Log.e( TAG, "where : " + Where );
                        valueInteger = dbHelper.getDatabase().updateWithOnConflict( "instrumento", contentValues, Where, null, SQLiteDatabase.CONFLICT_IGNORE );
                        Log.e( TAG, "sync update instrumento : " + String.valueOf( valueInteger ) );

                    }
                    // .set array INSTRUMENTOS
*/
                    dbHelper.setTransactionSuccessful();
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e( TAG, "getAllforSync : " + e.toString() );
        }
        finally
        {
            closeDBHelper();
            cursor.close();
        }

        Log.e( TAG, "end getAllforSync" );

    }

    public Boolean clearPadron() {

        try
        {

            dbHelper.getDatabase().delete( "aula_local", null, null );
            Log.e( TAG, "Se elimino aula_local!" );

            dbHelper.getDatabase().delete( "usuario_local", null, null );
            Log.e( TAG, "Se elimino usuario local!" );

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

    public void clearDataLocal(){
        try{
            Log.e(TAG, "Eliminando registro locales");
            openDBHelper();

            // Update docentes Table
            ContentValues docentesValues = new ContentValues();
            docentesValues.putNull(DocentesE.ESTADO);
            docentesValues.putNull(DocentesE.ESTADO_AULA);
            docentesValues.putNull(DocentesE.F_AULA);
            docentesValues.putNull(DocentesE.ESTADO_FICHA);
            docentesValues.putNull(DocentesE.ESTADO_CARTILLA);
            docentesValues.putNull(DocentesE.F_CARTILLA);
            docentesValues.putNull(DocentesE.NRO_AULA_CAMBIO);

            dbHelper.getDatabase().update("docentes", docentesValues, null, null);
            // Finish Update docentes table

            // Update instrumento Table
            ContentValues instrumentoValues = new ContentValues();
            instrumentoValues.putNull(InstrumentoE.NRO_AULA);
            instrumentoValues.putNull(InstrumentoE.ESTADO_FICHA);
            instrumentoValues.putNull(InstrumentoE.F_FICHA);
            instrumentoValues.putNull(InstrumentoE.ESTADO_CARTILLA);
            instrumentoValues.putNull(InstrumentoE.F_CARTILLA);

            dbHelper.getDatabase().update("instrumento", instrumentoValues, null, null);
            // Finish Update instrumento table

            Log.e(TAG, "Se eliminó registro locales");

            dbHelper.setTransactionSuccessful();
        }catch (SQLiteDatabaseLockedException l){
            l.printStackTrace();
            Log.e(TAG, "Error: " + l);
        } catch(Exception e){
            e.printStackTrace();
            Log.e(TAG, "Error: " + e);
        } finally {
            closeDBHelper();
        }

    }


    /*public Date setearFecha( String stringFecha ) {
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

    }*/

}