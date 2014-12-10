package ordanel.ednom.Business;

import android.content.Context;

import ordanel.ednom.DAO.SedeOperativaDAO;
import ordanel.ednom.Entity.SedeOperativaE;

/**
 * Created by Leandro on 21/11/2014.
 */
public class SedeOperativaBL {

    private static String password;

    private static SedeOperativaDAO sedeOperativaDAO;
    private static SedeOperativaE sedeOperativaE;

    public SedeOperativaBL( Context paramContext ) {

        sedeOperativaDAO = SedeOperativaDAO.getInstance( paramContext );
    }

    public Integer checkLogin( String... params ) {

        password = params[0];

        sedeOperativaE = sedeOperativaDAO.CheckLogin( password ); // data de la nube

        if ( sedeOperativaE.getStatus() == 0 )
        {
            sedeOperativaE = sedeOperativaDAO.registerLogin( sedeOperativaE ); // registro de datos
        }

        return sedeOperativaE.getStatus();

    }

    public SedeOperativaE showInfo() {

        sedeOperativaE = sedeOperativaDAO.showInfo();

        return sedeOperativaE;
    }

}
