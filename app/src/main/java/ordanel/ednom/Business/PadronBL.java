package ordanel.ednom.Business;

import android.content.Context;

import ordanel.ednom.DAO.PadronDAO;
import ordanel.ednom.Entity.LocalE;
import ordanel.ednom.Entity.PadronE;

/**
 * Created by Leandro on 21/11/2014.
 */
public class PadronBL {

    private static PadronDAO padronDAO;
    private static LocalE localE;
    private static PadronE padronE;

    public static Integer asyncPadron( Context context ) {

        padronDAO = new PadronDAO( context );

        localE = padronDAO.searchNroLocal();

        if ( localE != null )
        {
            padronE = padronDAO.padronNube( localE );
            if ( padronE != null && padronE.getStatus() == 0 )
            {
                padronE = padronDAO.registrarPadron( padronE );
            }
            else
            {
                // error al obtener de la nube;
            }
        }
        else
        {
            // error en busqueda de local;
        }

        return padronE.getStatus();
    }


}
