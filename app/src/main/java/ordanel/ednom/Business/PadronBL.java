package ordanel.ednom.Business;

import android.content.Context;
import android.widget.Toast;

import ordanel.ednom.DAO.PadronDAO;
import ordanel.ednom.DAO.UsuarioLocalDAO;
import ordanel.ednom.DAO.VersionDAO;
import ordanel.ednom.Entity.LocalE;
import ordanel.ednom.Entity.PadronE;
import ordanel.ednom.Entity.VersionE;
import ordanel.ednom.Library.ConstantsUtils;
import ordanel.ednom.Library.NetworkUtils;


public class PadronBL {

    private static PadronDAO padronDAO;
    private static PadronE padronE;
    private static VersionDAO versionDAO;
    private static LocalE localE;
    private static UsuarioLocalDAO usuarioLocalDAO;

    private static NetworkUtils networkUtils;
    private Context context;

    private static Boolean connection;

    public PadronBL( Context paramContext ) {

        padronDAO = PadronDAO.getInstance( paramContext );
        versionDAO = VersionDAO.getInstance( paramContext );
        usuarioLocalDAO = UsuarioLocalDAO.getInstance(paramContext);

        this.context = paramContext;

    }

    public Integer asyncPadron( VersionE versionE ) {

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

    public boolean getAllforSync() {
        boolean isSync = false;
        networkUtils = new NetworkUtils();
        connection = networkUtils.haveNetworkConnection( this.context );

        if ( connection )
        {
            isSync = padronDAO.getAllforSync();

        }
        return isSync;
    }

    public void clearDataLocal(){
        String password = ConstantsUtils.getPass;

        if (usuarioLocalDAO.isInformatico(password)){
            padronDAO.clearDataLocal();
            Toast.makeText(context, "Se han borrado los datos de manera local", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Solo el informático de local puede realizar esta función", Toast.LENGTH_SHORT).show();
        }
    }

}