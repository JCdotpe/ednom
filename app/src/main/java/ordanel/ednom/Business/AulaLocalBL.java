package ordanel.ednom.Business;

import android.content.Context;

import java.util.ArrayList;

import ordanel.ednom.DAO.AulaLocalDAO;
import ordanel.ednom.Entity.AulaLocalE;

/**
 * Created by OrdNael on 24/11/2014.
 */
public class AulaLocalBL {

    private static AulaLocalDAO aulaLocalDAO;

    public AulaLocalBL( Context paramContext ) {
        aulaLocalDAO = AulaLocalDAO.getInstance( paramContext );
    }

    public ArrayList<AulaLocalE> getAllNroAula() {
        return  aulaLocalDAO.getAllNroAula();
    }

}