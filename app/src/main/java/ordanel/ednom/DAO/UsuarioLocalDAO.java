package ordanel.ednom.DAO;

import android.content.Context;
import android.util.Log;

import ordanel.ednom.Entity.UsuarioLocalE;

/**
 * Created by OrdNael on 28/11/2014.
 */
public class UsuarioLocalDAO extends BaseDAO {

    private static final String TAG = UsuarioLocalDAO.class.getSimpleName();
    private static UsuarioLocalDAO usuarioLocalDAO;

    public UsuarioLocalE usuarioLocalE;

    public synchronized static UsuarioLocalDAO getInstance( Context paramContext ) {

        if ( usuarioLocalDAO == null )
        {
            usuarioLocalDAO = new UsuarioLocalDAO( paramContext );
        }

        return usuarioLocalDAO;

    }

    private UsuarioLocalDAO( Context paramContext ) {
        initDBHelper( paramContext );
    }

    public UsuarioLocalE searchUserByPass( String paramPassword ) {

        Log.e( TAG, "start searchUserByPass" );

        SQL = "SELECT idUsuario, usuario, rol FROM usuario_local WHERE clave = '" + paramPassword + "'";

        usuarioLocalE = new UsuarioLocalE();

        try
        {
            openDBHelper();

            cursor = dbHelper.getDatabase().rawQuery( SQL, null );

            if ( cursor.moveToFirst() )
            {
                usuarioLocalE.setIdUsuario( cursor.getInt( cursor.getColumnIndex( UsuarioLocalE.IDUSUARIO ) ) );
                usuarioLocalE.setUsuario( cursor.getString( cursor.getColumnIndex( UsuarioLocalE.USUARIO ) ) );
                usuarioLocalE.setRol( cursor.getInt( cursor.getColumnIndex( UsuarioLocalE.ROL ) ) );

                usuarioLocalE.setStatus( 0 ); // todo bien
            }
            else
            {
                usuarioLocalE.setStatus( 1 ); // no hay usuario
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e( TAG, "searchUserByPass : " + e.toString() );
            usuarioLocalE.setStatus( 2 ); // error en la consulta
        }
        finally
        {
            closeDBHelper();
            cursor.close();
        }

        Log.e( TAG, "end searchUserByPass" );

        return usuarioLocalE;
    }

}
