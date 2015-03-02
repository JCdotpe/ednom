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

    public boolean isInformatico(String password){
        Log.e( TAG, "start isInformatico" );

        SQL = "SELECT rol FROM usuario_local WHERE clave = '" + password + "'";

        boolean isInformatico = false;
        int rol = 0;
        try
        {
            openDBHelper();
            cursor = dbHelper.getDatabase().rawQuery( SQL, null );

            if ( cursor.moveToFirst() )
            {
                rol = cursor.getInt(0);
                if (rol == 2){ // Rol 2 de informatico de local
                    isInformatico = true;
                }
            }
            Log.e( TAG, "end isInformatico" );
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e( TAG, "isInformatico : " + e.toString() );
        }
        finally
        {
            closeDBHelper();
            cursor.close();
        }
        return isInformatico;

    }

}
