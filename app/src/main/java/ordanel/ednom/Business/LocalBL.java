package ordanel.ednom.Business;

import android.content.Context;

import ordanel.ednom.DAO.LocalDAO;
import ordanel.ednom.Entity.DocentesE;

/**
 * Created by OrdNael on 24/11/2014.
 */
public class LocalBL {

    private static LocalDAO localDAO;
    private static DocentesE docentesE;

    public LocalBL( Context paramContext ) {

        localDAO = new LocalDAO( paramContext );

    }

    public static DocentesE searchPerson( String conditional ) {

        docentesE = localDAO.searchPerson( conditional );

        if ( docentesE.getStatus() == 0 )
        {
            String number_dni = docentesE.getNro_doc();
            docentesE.setStatus( localDAO.asistenciaLocal( number_dni ) );
        }

        return docentesE;

    }

}
