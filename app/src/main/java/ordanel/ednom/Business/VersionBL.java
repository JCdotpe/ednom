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

    public static VersionE checkVersionOffline() {

        currentVersion = versionDAO.currentVersion();
        versionE = new VersionE();

        if ( currentVersion != null )
        {

            if ( currentVersion == 0 )
            {
                versionE.setStatus( 5 ); // no hay datos ni padron
            }
            else
            {
                versionE.setStatus( 100 ); // tod bien
            }

        }
        else
        {
            versionE.setStatus( 1 ); // error en currentVersion
        }

        return versionE;
    }

}