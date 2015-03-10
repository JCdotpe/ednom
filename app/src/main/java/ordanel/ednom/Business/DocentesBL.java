package ordanel.ednom.Business;

import android.content.Context;

import java.util.ArrayList;

import ordanel.ednom.DAO.AulaLocalDAO;
import ordanel.ednom.DAO.DocentesDAO;
import ordanel.ednom.Entity.DocentesE;

/**
 * Created by OrdNael on 24/11/2014.
 */
public class DocentesBL {

    private static DocentesDAO docentesDAO;
    private static DocentesE docentesE;
    private static AulaLocalDAO aulaLocalDAO;

    private static String conditional;
    private static int SIZE;

    ArrayList<DocentesE> docentesEArrayList;
    ArrayList<DocentesE> docentesETempList;

    public DocentesBL( Context paramContext ) {

        docentesDAO = DocentesDAO.getInstance( paramContext );
        aulaLocalDAO = AulaLocalDAO.getInstance( paramContext );

    }

    public DocentesE asistenciaLocal( String paramDNI ) {

        conditional = "nro_doc = '" + paramDNI + "'";

        docentesE = docentesDAO.searchPerson( conditional );

        if ( docentesE.getStatus() == 0 )
        {
            docentesE.setStatus( docentesDAO.asistenciaLocal( paramDNI ) );
        }
        return docentesE;

    }

    public DocentesE asistenciaAula( String paramDNI, Integer paramNroAula ) {

        Integer contingencia = aulaLocalDAO.searchTipoAula( paramNroAula );

        if ( contingencia == 0 ) // NO es del TIPO CONTINGENCIA
        {
            conditional = "nro_doc = '" + paramDNI + "' and nro_aula = " + paramNroAula;
        }
        else
        {
            conditional = "nro_doc = '" + paramDNI + "'";
        }

        docentesE = docentesDAO.searchPerson( conditional );

        if ( docentesE.getStatus() == 0 )
        {
            docentesE.setStatus( docentesDAO.asistenciaAula( paramDNI, paramNroAula, contingencia ) );

        }  else if (docentesE.getStatus() == 1){
            docentesE.setStatus (8);
        }


        return docentesE;
    }

    public ArrayList<DocentesE> listadoIngresoLocal( int offset, int limit ) {

        docentesETempList = docentesDAO.listadoIngresoLocal();
        SIZE = docentesETempList.size();

        docentesEArrayList = new ArrayList<>(limit);

        int end = offset + limit;

        if ( end > SIZE )
        {
            end = SIZE;
        }

        docentesEArrayList.addAll( docentesETempList.subList( offset, end ) );

        return docentesEArrayList;
    }

    public ArrayList<DocentesE> listadoAsistenciaAula( Integer paramNroAula, int offset, int limit ) {

        docentesETempList = docentesDAO.listadoAsistenciaAula( paramNroAula );
        SIZE = docentesETempList.size();

        docentesEArrayList = new ArrayList<>(limit);

        int end = offset + limit;

        if ( end > SIZE )
        {
            end = SIZE;
        }

        docentesEArrayList.addAll( docentesETempList.subList( offset, end ) );

        return docentesEArrayList;

    }


    public int getSIZE() {
        return SIZE;
    }

    public Integer getNroDatosSincronizados(String columnEstado, int nroAula){
        return docentesDAO.nroDatosSincronizados(columnEstado, nroAula);
    }

    public Integer getNroDatosRegistrados(String columnEstado, int nroAula){
        return docentesDAO.nroDatosRegistrados(columnEstado, nroAula);
    }
}
