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

    String URL_Connect = ConstantsUtils.BASE_URL + "padron.php";

    Integer error = 0;
    Integer cod_sede_operativa, cod_local_sede, nro_aula;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    DBHelper dbHelper;
    Context context;
    Cursor cursor = null;
    ContentValues contentValues;

    LocalE localE;
    ArrayList<PadronE> arrayList;


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

                Integer nro_local = cursor.getInt( cursor.getColumnIndex( "cod_local_sede" ) );
                Log.e( TAG, "numero de local : " + nro_local.toString() );

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

    public PadronE padronNube( LocalE localE ) {

        //localE = this.searchNroLocal();

        Log.e( TAG, "start padronNubeJson" );
        PadronE padronE = new PadronE();

        if ( localE != null )
        {
            HttpPostAux httpPostAux = new HttpPostAux();

            cod_sede_operativa = localE.getCod_sede_operativa();
            cod_local_sede = localE.getCod_local_sede();

            ArrayList<NameValuePair> parametersPost = new ArrayList<NameValuePair>();
            parametersPost.add( new BasicNameValuePair( "cod_sede_operativa", cod_sede_operativa.toString() ) );
            parametersPost.add( new BasicNameValuePair( "cod_local_sede", cod_local_sede.toString() ) );

            JSONArray jsonArray = httpPostAux.getServerData( parametersPost, URL_Connect );

            if ( jsonArray != null && jsonArray.length() > 0 )
            {
                ArrayList<AulaLocalE> aulaLocalEArrayList = new ArrayList<AulaLocalE>();

                try
                {
                    JSONObject jsonObjectTemp;
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    JSONArray aula_local = jsonObject.getJSONArray("AULAS");

                    for ( int i = 0; i < aula_local.length(); i++ )
                    {
                        jsonObjectTemp = (JSONObject) aula_local.get(i);

                        AulaLocalE aulaLocalE = new AulaLocalE();
                        aulaLocalE.setLocalE( localE );
                        aulaLocalE.setNro_aula( jsonObjectTemp.getInt( "nro_aula" ) );
                        aulaLocalE.setTipo( jsonObjectTemp.getString("tipo") );
                        aulaLocalE.setCant_docente( jsonObjectTemp.getInt( "cant_docente" ) );

                        JSONArray jsonArrayTemp = (JSONArray) jsonObjectTemp.get("DOCENTES");

                        ArrayList<DocentesE> docentesEArrayList = new ArrayList<DocentesE>();

                        for ( int j = 0; j < jsonArrayTemp.length(); j++ )
                        {
                            jsonObjectTemp = (JSONObject) jsonArrayTemp.get(j);
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

                        aulaLocalEArrayList.add( aulaLocalE );

                    }

                    Integer cantidad;
                    cantidad = aulaLocalEArrayList.size();
                    Log.e( TAG, "cantidad de aula_local : " + cantidad.toString() );
                    padronE.setAulaLocalEList( aulaLocalEArrayList );

                    JSONArray discapacidad = jsonObject.getJSONArray("DISCAPACIDAD");
                    ArrayList<DiscapacidadE> discapacidadEArrayList = new ArrayList<DiscapacidadE>();

                    for ( int i = 0; i < discapacidad.length(); i++ )
                    {
                        jsonObjectTemp = (JSONObject) discapacidad.get(i);

                        DiscapacidadE discapacidadE = new DiscapacidadE();
                        discapacidadE.setCod_discap( jsonObjectTemp.getInt( "cod_discap" ) );
                        discapacidadE.setDiscapacidad( jsonObjectTemp.getString( "discapacidad" ) );

                        discapacidadEArrayList.add( discapacidadE );
                    }

                    cantidad = discapacidadEArrayList.size();
                    Log.e( TAG, "cantidad de discapacidad : " + cantidad.toString() );
                    padronE.setDiscapacidadEList( discapacidadEArrayList );

                    JSONArray modalidad = jsonObject.getJSONArray("MODALIDAD");
                    ArrayList<ModalidadE> modalidadEArrayList = new ArrayList<ModalidadE>();

                    for ( int i = 0; i < modalidad.length(); i++ )
                    {
                        jsonObjectTemp = (JSONObject) modalidad.get(i);

                        ModalidadE modalidadE = new ModalidadE();
                        modalidadE.setCod_modal( jsonObjectTemp.getInt( "cod_modal" ) );
                        modalidadE.setModalidad( jsonObjectTemp.getString( "modalidad" ) );

                        modalidadEArrayList.add( modalidadE );
                    }

                    cantidad = modalidadEArrayList.size();
                    Log.e( TAG, "cantidad de modalidad : " + cantidad.toString() );
                    padronE.setModalidadEList( modalidadEArrayList );

                    padronE.setStatus( 0 );

                    //this.registrarPadron( aulaLocalEArrayList, discapacidadEArrayList, modalidadEArrayList );

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    padronE.setStatus( 1 );
                    Log.e( TAG, "error padronNubeJson : " + e.toString() );
                }

            }

        }

        Log.e( TAG, "end padronNubeJson" );

        return padronE;

    }

    public PadronE registrarPadron( PadronE padronE ) {

        Log.e( TAG, "start registrarPadron" );

        dbHelper = DBHelper.getUtilDb( this.context );

        try
        {
            dbHelper.openDataBase();
            dbHelper.beginTransaction();

            dbHelper.getDatabase().delete( "aula_local", null, null );
            Log.e( TAG, "Se elimino aula_local!" );

            dbHelper.getDatabase().delete( "docentes", null, null );
            Log.e( TAG, "Se elimino docentes!" );

            dbHelper.getDatabase().delete( "discapacidad", null, null );
            Log.e( TAG, "Se elimino discapacidad!" );

            dbHelper.getDatabase().delete( "modalidad", null, null );
            Log.e( TAG, "Se elimino modalidad!" );

            ArrayList<AulaLocalE> aulaLocalEArrayList = (ArrayList) padronE.getAulaLocalEList();
            ArrayList<DiscapacidadE> discapacidadEArrayList = (ArrayList) padronE.getDiscapacidadEList();
            ArrayList<ModalidadE> modalidadEArrayList = (ArrayList) padronE.getModalidadEList();

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

                Long exitoAula_Local = dbHelper.getDatabase().insertOrThrow( "aula_local", null, contentValues );
                Log.e( TAG, "aula_local insert : " + String.valueOf(exitoAula_Local) );

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

                    Long exitoDocentes = dbHelper.getDatabase().insertOrThrow( "docentes", null, contentValues );
                    Log.e( TAG, "docentes insert : " + String.valueOf(exitoDocentes) );

                }

            }

            for ( DiscapacidadE discapacidadE : discapacidadEArrayList )
            {
                contentValues = new ContentValues();

                contentValues.put( "cod_discap", discapacidadE.getCod_discap() );
                contentValues.put( "discapacidad", discapacidadE.getDiscapacidad() );

                Long exitoDocentes = dbHelper.getDatabase().insertOrThrow( "discapacidad", null, contentValues );
                Log.e( TAG, "discapacidad insert : " + String.valueOf(exitoDocentes) );
            }

            for ( ModalidadE modalidadE : modalidadEArrayList )
            {
                contentValues = new ContentValues();

                contentValues.put( "cod_modal", modalidadE.getCod_modal() );
                contentValues.put( "modalidad", modalidadE.getModalidad() );

                Long exitoDocentes = dbHelper.getDatabase().insertOrThrow( "modalidad", null, contentValues );
                Log.e( TAG, "modalidad insert : " + String.valueOf(exitoDocentes) );
            }

            dbHelper.setTransactionSuccessful();

            padronE.setStatus( 0 );

        }
        catch (Exception e)
        {
            e.printStackTrace();
            padronE.setStatus( 2 );
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


    /*public ArrayList<PadronE> padronNube() {

//        nro_local = this.searchNroLocal();
        localE = this.searchNroLocal();

        Log.e( TAG, "start padronNube" );

        if ( localE != null )
        {
            HttpPostAux httpPostAux = new HttpPostAux();

            cod_sede_operativa = localE.getCod_sede_operativa();
            cod_local_sede = localE.getCod_local_sede();

            ArrayList<NameValuePair> parametersPost = new ArrayList<NameValuePair>();
            parametersPost.add( new BasicNameValuePair( "cod_sede_operativa", cod_sede_operativa.toString() ) );
            parametersPost.add( new BasicNameValuePair( "cod_local_sede", cod_local_sede.toString() ) );

            JSONArray jsonArray = httpPostAux.getServerData( parametersPost, URL_Connect );


            if ( jsonArray != null && jsonArray.length() > 0 )
            {

                arrayList = new ArrayList<PadronE>();

                try
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    JSONObject jsonObjectTemp;

//                    jsonObject = jsonArray.getJSONObject(0);
                    JSONArray aula_local = jsonObject.getJSONArray("aula_local");

                    for ( int i = 0; i < aula_local.length(); i++ )
                    {
                        jsonObjectTemp = (JSONObject) aula_local.get(i);

                        SedeOperativaE sedeOperativaE = new SedeOperativaE();
                        sedeOperativaE.setCod_sede_operativa( jsonObjectTemp.getInt( "cod_sede_operativa" ) );

                        LocalE localE = new LocalE();
                        // localE.setSedeOperativaE( sedeOperativaE );
                        localE.setCod_local_sede( jsonObjectTemp.getInt( "cod_local_sede" ) );

                        AulaLocalE aulaLocalE = new AulaLocalE();
                        aulaLocalE.setLocalE( localE );
                        aulaLocalE.setNro_aula( jsonObjectTemp.getInt( "nro_aula") );
                        aulaLocalE.setTipo( jsonObjectTemp.getString( "tipo" ) );
                        aulaLocalE.setCant_docente( jsonObjectTemp.getInt( "cant_docente" ) );


                    }

                    JSONArray discapacidad = jsonObject.getJSONArray("discapacidad");

                    for ( int i = 0; i < discapacidad.length(); i++ )
                    {
                        jsonObjectTemp = (JSONObject) discapacidad.get(i);

                        DiscapacidadE discapacidadE = new DiscapacidadE();
                        discapacidadE.setCod_discap( jsonObjectTemp.getInt( "cod_discap" ) );
                        discapacidadE.setDiscapacidad( jsonObjectTemp.getString( "discapacidad") );


                    }


                    for ( int i = 0; i < jsonArray.length(); i++ )
                    {
                        jsonObject = (JSONObject) jsonArray.get(i);

                        PadronE padronE = new PadronE();
                        padronE.setCodigo( jsonObject.getInt( "codigo" ) );
                        padronE.setSede( jsonObject.getString( "sede" ) );
                        padronE.setNro_local( jsonObject.getInt( "nro_local" ) );
                        padronE.setLocal_aplicacion( jsonObject.getString( "local_aplicacion" ) );
                        padronE.setAula( jsonObject.getString( "aula" ) );
                        padronE.setIns_numdoc( jsonObject.getString( "ins_numdoc" ) );
                        padronE.setApepat( jsonObject.getString( "apepat" ) );
                        padronE.setApemat( jsonObject.getString( "apemat" ) );
                        padronE.setNombres( jsonObject.getString( "nombres" ) );
                        padronE.setEstatus( jsonObject.getInt( "estatus" ) );
//                    padronE.setFecha_registro( this.setearFecha( jsonObject.getString( "fecha_registro" ) ) );
                        padronE.setS_aula( jsonObject.getInt( "s_aula" ) );
//                    padronE.setF_aula( this.setearFecha( jsonObject.getString( "f_aula" ) ) );
                        padronE.setS_ficha( jsonObject.getInt("s_ficha"));
//                    padronE.setF_ficha( this.setearFecha( jsonObject.getString( "f_ficha" ) ) );
                        padronE.setS_cartilla( jsonObject.getInt( "s_cartilla" ) );
//                    padronE.setF_cartilla( this.setearFecha( jsonObject.getString( "f_cartilla" ) ) );
                        padronE.setId_local( jsonObject.getInt( "id_local" ) );
                        padronE.setId_aula( jsonObject.getInt( "id_aula" ) );
                        padronE.setDireccion( jsonObject.getString( "direccion" ) );
                        padronE.setCodFicha( jsonObject.getString( "codFicha" ) );
                        padronE.setCodCartilla( jsonObject.getString( "codCartilla" ) );
                        padronE.setAula_ficha( jsonObject.getString( "aula_ficha" ) );
                        padronE.setAula_cartilla( jsonObject.getString( "aula_cartilla" ) );
                        padronE.setSf_cartilla( jsonObject.getString( "sf_cartilla" ) );
                        padronE.setSf_aula( jsonObject.getString( "sf_aula" ) );
                        padronE.setSf_ficha( jsonObject.getString( "sf_ficha" ) );
                        padronE.setSfecha_registro( jsonObject.getString( "sfecha_registro" ) );
                        padronE.setNew_aula( jsonObject.getString( "new_aula" ) );
                        padronE.setNew_local( jsonObject.getString( "new_local" ) );
                        padronE.setCant_ficha( jsonObject.getInt( "cant_ficha" ) );
                        padronE.setCodCartilla( jsonObject.getString( "tipo" ) );

                        arrayList.add( padronE );
                    }

                    return arrayList;

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Log.e( TAG, "error padronNube : " + e.toString() );
                }

            }

        }

        Log.e( TAG, "end padronNube" );
        return null;

    }*/

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

    }


    /*public Integer padronLocal() {

        *//*arrayList = this.padronNube();*//*
        this.padronNubeJson();
        arrayList = null;

        Log.e( TAG, "start padronLocal" );

        dbHelper = DBHelper.getUtilDb( this.context );

        if ( arrayList != null )
        {
            if ( arrayList.size() > 0 )
            {
                try
                {
                    dbHelper.openDataBase();
                    dbHelper.beginTransaction();

                *//*String SQL = "SELECT COUNT(codigo) AS numberRows FROM postulantes2014";

                cursor = dbHelper.getDatabase().rawQuery( SQL, null );
                Integer count = 0;

                if ( cursor.moveToFirst() )
                {
                    count = cursor.getInt( cursor.getColumnIndex( "numberRows" ) );
                }
                else
                {
                    Log.e( TAG, "error in numberRows postulantes2014!");
                }

                if ( count > 0 )
                {
                    Integer exito = dbHelper.getDatabase().delete( "postulantes2014", null, null );
                    Log.e( TAG, "Se elimino Padron! : " + exito.toString() );

                }*//*

                    Integer rowsNumber;
                    String success = "0";

                    rowsNumber = dbHelper.getDatabase().delete( "postulantes2014", null, null );
                    Log.e( TAG, "Se elimino Padron! : " + rowsNumber.toString() );

                    for ( int i = 0; i < arrayList.size(); i++ )
                    {
                        contentValues = new ContentValues();

                        contentValues.put( "codigo", arrayList.get(i).getCodigo() );
                        contentValues.put( "sede", arrayList.get(i).getSede() );
                        contentValues.put( "nro_local", arrayList.get(i).getNro_local() );
                        contentValues.put( "local_aplicacion", arrayList.get(i).getLocal_aplicacion() );
                        contentValues.put( "aula", arrayList.get(i).getAula() );
                        contentValues.put( "ins_numdoc", arrayList.get(i).getIns_numdoc() );
                        contentValues.put( "apepat", arrayList.get(i).getApepat() );
                        contentValues.put( "apemat", arrayList.get(i).getApemat() );
                        contentValues.put( "nombres", arrayList.get(i).getNombres() );
                        contentValues.put( "estatus", arrayList.get(i).getEstatus() );
//                contentValues.put( "fecha_registro", simpleDateFormat.format( arrayList.get(i).getFecha_registro() ) );
                        contentValues.put( "s_aula", arrayList.get(i).getS_aula() );
//                contentValues.put( "f_aula", simpleDateFormat.format( arrayList.get(i).getF_aula() ) );
                        contentValues.put( "s_ficha", arrayList.get(i).getS_ficha() );
//                contentValues.put( "f_ficha", simpleDateFormat.format( arrayList.get(i).getF_ficha() ) );
                        contentValues.put( "s_cartilla", arrayList.get(i).getS_cartilla() );
//                contentValues.put( "f_cartilla", simpleDateFormat.format( arrayList.get(i).getF_cartilla() ) );
                        contentValues.put( "id_local", arrayList.get(i).getId_local() );
                        contentValues.put( "id_aula", arrayList.get(i).getId_aula() );
                        contentValues.put( "direccion", arrayList.get(i).getDireccion() );
                        contentValues.put( "codFicha", arrayList.get(i).getCodFicha() );
                        contentValues.put( "codCartilla", arrayList.get(i).getCodCartilla() );
                        contentValues.put( "aula_ficha", arrayList.get(i).getAula_ficha() );
                        contentValues.put( "aula_cartilla", arrayList.get(i).getAula_cartilla() );
                        contentValues.put( "sf_cartilla", arrayList.get(i).getSf_cartilla() );
                        contentValues.put( "sf_aula", arrayList.get(i).getSf_aula() );
                        contentValues.put( "sf_ficha", arrayList.get(i).getSf_ficha() );
                        contentValues.put( "sfecha_registro", arrayList.get(i).getSfecha_registro() );
                        contentValues.put( "new_aula", arrayList.get(i).getNew_aula() );
                        contentValues.put( "new_local", arrayList.get(i).getNew_local() );
                        contentValues.put( "cant_ficha", arrayList.get(i).getCant_ficha() );
                        contentValues.put( "tipo", arrayList.get(i).getTipo() );

                        Long exito = dbHelper.getDatabase().insertOrThrow( "postulantes2014", null, contentValues );
                        success = String.valueOf(exito);
                    }

                    if ( success.equals("0") && success.equals("-1") )
                    {
                        *//*dbHelper.getDatabase().delete( "Version", null, null );

                        contentValues =  new ContentValues();
                        contentValues.put( "idVersion" , idVersion );


                        Long exito = dbHelper.getDatabase().insertOrThrow( "Version", null, contentValues );
                        success = String.valueOf(exito);*//*
                        rowsNumber = dbHelper.getDatabase().delete( "Version", null, null );
                        Log.e( TAG, "Se elimino Padron! : " + rowsNumber.toString() );
                        error = 3; // error en padronLocal //

                    }

                    dbHelper.setTransactionSuccessful();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Log.e( TAG, "error padronLocal : " + e.toString() );
                    error = 3; // error en padronLocal //
                }
                finally
                {
                    dbHelper.endTransaction();
                    dbHelper.close();
                    cursor.close();
                }
            }
            else
            {
                error = 2; // error en data //
            }

        }
        else
        {
            error = 1; // error en padronNube //
        }

        return error;
    }*/

}