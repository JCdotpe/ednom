package ordanel.ednom.Business;

import android.content.Context;

import java.util.ArrayList;

import ordanel.ednom.DAO.PersonalDAO;
import ordanel.ednom.Entity.PersonalE;

/**
 * Created by Cr-Diego on 09/03/2015.
 */
public class PersonalBL {

    private static PersonalDAO personalDAO;
    private static PersonalE personalE;

    private static  String conditional;
    ArrayList<PersonalE> personalEArrayList;
    ArrayList<PersonalE> personalETempList;


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
        personalE = personalDAO.searchPersonal(conditional);
        if (personalE.getStatus() == 0 ){
            switch (personalE.getId_cargo()){
                case 11:case 12: case 13:
                    personalE.setStatus(4);
                    break;
                default:
                    personalE.setStatus(15);
            };
        }
        return personalE;
    }


    public PersonalE reemplazarPersonal(String dni, String dniCambio, String nombreCambio, int cargoCambio) {
        personalE.setStatus(personalDAO.reemplazarPersonal(dni, dniCambio, nombreCambio, cargoCambio));
        return personalE;
    }


    public PersonalE registrarCambioCargo(String nroDni, String cargo) {
        personalE.setStatus(personalDAO.cambiarCargo(nroDni,cargo));
        return personalE;
    }

    public PersonalE searchPersonalCambioCargo(String nroDni){
        conditional = "dni = '" + nroDni + "'";
        personalE = personalDAO.searchPersonal(conditional);
        if (personalE.getStatus() == 1 || !personalE.getEstadoReemplazo().equals("") || !personalE.getEstadoReemplazo().isEmpty() ) {
            conditional = personalE.getStatus() == 1 ? "r_dni = '" + nroDni + "'" : "r_dni = '" + personalE.getR_dni() + "'";
            personalE = personalDAO.searchPersonalCambioCargo(conditional);
            if (personalE.getStatus() == 0){
                switch (personalE.getId_cargo()){
                    case 11:case 12: case 13:
                        personalE.setStatus(13);
                        break;
                    default:
                        personalE.setStatus(14);
                        break;
                }
            }
        } else {
            if (personalE.getStatus() == 0){
                switch (personalE.getId_cargo()){
                    case 11:case 12: case 13:
                        personalE.setStatus(4);
                        break;
                    default:
                        personalE.setStatus(14);
                        break;
                }
            }
        }
        return personalE;
    }

    public ArrayList<PersonalE> listadoAsistencia(){
        personalEArrayList = new ArrayList<PersonalE>();
        personalEArrayList = personalDAO.listadoPersonal();
        return personalEArrayList;
    }

    public String searchNombreReserva(String nroDni) {
        return personalDAO.searchNombreReserva(nroDni);
    }
}
