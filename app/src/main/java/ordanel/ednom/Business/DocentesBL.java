package ordanel.ednom.Business;

import android.content.Context;

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

        }

        return docentesE;
    }

}