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

    private static  String conditional;

    public PersonalBL(Context paramContext) {
        personalDAO = PersonalDAO.getInstance(paramContext);
    }

    public PersonalE asistenciaPersonal(String nroDni) {
        conditional = "dni = '" + nroDni + "'";
        personalE = personalDAO.searchPersonal(conditional);

        if ( personalE.getStatus() == 0 ){
            personalE.setStatus(personalDAO.asistenciaPersonal(nroDni));
        }
        return personalE;
    }


    public PersonalE searchPersonalCambio(String nroDni) {
        conditional = "dni = '" + nroDni + "'";
        personalE = personalDAO.searchPersonalCambio(conditional);

        return personalE;
    }
}
