package ordanel.ednom.Business;

import android.content.Context;

import ordanel.ednom.DAO.PadronDAO;
import ordanel.ednom.DAO.VersionDAO;
import ordanel.ednom.Entity.LocalE;
import ordanel.ednom.Entity.PadronE;
import ordanel.ednom.Entity.VersionE;

/**
 * Created by Leandro on 21/11/2014.
 */
public class PadronBL {

    private static PadronDAO padronDAO;
    private static PadronE padronE;
    private static VersionDAO versionDAO;
    private static LocalE localE;


    public PadronBL( Context paramContext ) {

        padronDAO = PadronDAO.getInstance( paramContext );
        versionDAO = VersionDAO.getInstance( paramContext );

    }

    public static Integer asyncPadron( VersionE versionE ) {

        localE = padronDAO.searchNroLocal();

        if ( localE.getStatus() == 0 )
        {
            padronE = padronDAO.padronNube( localE ); // set data nube
            if ( padronE.getStatus() == 0 )
            {
                padronE = padronDAO.registrarPadron( padronE ); // register data in local

                if ( padronE.getStatus() == 0 )
                {
                    if ( versionDAO.registerVersion( versionE ) != 0 ) // register version
                    {
                        padronE.setStatus( 7 ); // error register version
                    }
                }
            }

        }
        else
        {
           padronE.setStatus( 1 ); // error en busqueda de Nro Local;
        }

        return padronE.getStatus();
    }


}
