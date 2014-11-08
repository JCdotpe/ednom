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
import ordanel.ednom.Entity.PadronE;
import ordanel.ednom.Library.HttpPostAux;

/**
 * Created by OrdNael on 30/10/2014.
 */
public class PadronDAO {

    private static final String TAG = PadronDAO.class.getSimpleName();

    String IP_Server = "jc.pe";
    String URL_Connect = "http://" + IP_Server + "/portafolio/ednom/padron.php";
    /*String IP_Server = "172.16.100.45";
    String URL_Connect = "http://" + IP_Server + "/droidlogin/padron.php";*/

    Boolean status = true;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    DBHelper dbHelper;
    Context context;
    Cursor cursor = null;
    ContentValues contentValues;

    ArrayList<PadronE> arrayList;
    Integer nro_local = 0;

    public PadronDAO( Context context ) {
        this.context = context;
        Log.e( TAG, "start" );
    }

    public Integer searchNroLocal() {

        dbHelper = DBHelper.getUtilDb( this.context );

        try
        {
            dbHelper.openDataBase();
            dbHelper.beginTransaction();

            String SQL = "SELECT nro_local FROM usuario_local";

            cursor = dbHelper.getDatabase().rawQuery( SQL, null );

            if ( cursor.moveToFirst() )
            {
                nro_local = cursor.getInt( cursor.getColumnIndex( "nro_local" ) );
                Log.e( TAG, "numero de local : " + nro_local.toString() );
            }
            else
            {
                Log.e( TAG, "error in searchNroLocal!" );
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

        return nro_local;

    }

    public ArrayList<PadronE> padronNube() {

        nro_local = this.searchNroLocal();

        HttpPostAux httpPostAux = new HttpPostAux();

        ArrayList<NameValuePair> parametersPost = new ArrayList<NameValuePair>();
        parametersPost.add( new BasicNameValuePair( "nro_local", nro_local.toString() ) );
//        parametersPost.add( new BasicNameValuePair( "nro_local", "1" ) );

        JSONArray jsonArray = httpPostAux.getServerData( parametersPost, URL_Connect );

        if ( jsonArray != null && jsonArray.length() > 0 )
        {
            try
            {
                arrayList = new ArrayList<PadronE>();
                JSONObject jsonObject;

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
            }
            catch (Exception e)
            {
                e.printStackTrace();
                arrayList = null;
            }

            return arrayList;

        }
        else
        {
            return null;
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

    }


    public Integer padronLocal( Integer idVersion ) {

        dbHelper = DBHelper.getUtilDb( this.context );

        String success = "0";

        arrayList = this.padronNube();

        try
        {
            dbHelper.openDataBase();
            dbHelper.beginTransaction();

            String SQL = "SELECT COUNT(codigo) AS numberRows FROM postulantes2014";

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

            }

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

            if ( !success.equals("0") && !success.equals("-1") )
            {
                dbHelper.getDatabase().delete( "Version", null, null );

                contentValues =  new ContentValues();
                contentValues.put( "idVersion" , idVersion );


                Long exito = dbHelper.getDatabase().insertOrThrow( "Version", null, contentValues );
                success = String.valueOf(exito);

            }

            dbHelper.setTransactionSuccessful();
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

        return Integer.valueOf(success);
    }

}