package ordanel.ednom.Business;

import android.content.Context;

import ordanel.ednom.DAO.UsuarioLocalDAO;
import ordanel.ednom.Entity.UsuarioLocalE;

/**
 * Created by OrdNael on 28/11/2014.
 */
public class UsuarioLocalBL {

    private static UsuarioLocalE usuarioLocalE;
    private static UsuarioLocalDAO usuarioLocalDAO;

    private static String password;
    private static Integer valueInteger;

    public UsuarioLocalBL( Context paramContext ) {
        usuarioLocalDAO = UsuarioLocalDAO.getInstance( paramContext );
    }

    public Integer checkLoginOffline( String... params ) {

        password = params[0];

        usuarioLocalE = usuarioLocalDAO.searchUserByPass( password );

        switch ( usuarioLocalE.getStatus() )
        {
            case 0:
                valueInteger = 0; //nothing, todo ok!
                break;

            case 1:
                valueInteger = 2; // no existe el usuario, clave incorrecta.
                break;

            case 2:
                valueInteger = 3; // problemas en la consulta.
                break;
        }

        return valueInteger;

    }

}