package ordanel.ednom.Business;

import android.content.Context;

import ordanel.ednom.DAO.VersionDAO;
import ordanel.ednom.Entity.VersionE;

/**
 * Created by Leandro on 21/11/2014.
 */
public class VersionBL {

    public static VersionDAO versionDAO;
    public static VersionE versionE;

    public static Integer currentVersion;

    public VersionBL( Context paramContext ) {
        versionDAO = VersionDAO.getInstance( paramContext );
    }

    public static VersionE checkVersion() {

        currentVersion = versionDAO.currentVersion();

        if ( currentVersion != null )
        {
            versionE = versionDAO.checkVersion( currentVersion );
        }
        else
        {
            versionE.setStatus( 1 ); // error en currentVersion
        }

        return versionE;
    }


}
