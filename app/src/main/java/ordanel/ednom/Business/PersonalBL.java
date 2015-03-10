package ordanel.ednom.Business;

import android.content.Context;

import ordanel.ednom.DAO.PersonalDAO;
import ordanel.ednom.Entity.PersonalE;

/**
 * Created by Cr-Diego on 09/03/2015.
 */
public class PersonalBL {

    private static PersonalDAO personalDAO;
    private static PersonalE personalE;

    public PersonalBL(Context paramContext) {
        personalDAO = PersonalDAO.getInstance(paramContext);
    }
}
