package ordanel.ednom.Business;

import android.content.Context;

import ordanel.ednom.DAO.AulaLocalDAO;
import ordanel.ednom.DAO.DocentesDAO;
import ordanel.ednom.DAO.InstrumentoDAO;
import ordanel.ednom.Entity.InstrumentoE;

/**
 * Created by OrdNael on 02/12/2014.
 */
public class InstrumentoBL {

    private static AulaLocalDAO aulaLocalDAO;
    private static DocentesDAO docentesDAO;
    private static InstrumentoDAO instrumentoDAO;
    private static InstrumentoE instrumentoE;

    private static String conditional_docente;
    private static String conditional_instrumento;

    public InstrumentoBL(Context paramContext) {

        aulaLocalDAO = AulaLocalDAO.getInstance( paramContext );
        docentesDAO = DocentesDAO.getInstance( paramContext );
        instrumentoDAO = InstrumentoDAO.getInstance( paramContext );

    }

    public InstrumentoE inventarioFicha( String paramCodFicha, Integer paramNroAula ) {

        Integer contingencia = aulaLocalDAO.searchTipoAula( paramNroAula );

        if ( contingencia == 0 ) // NO es del TIPO CONTINGENCIA
        {
            conditional_docente = "cod_ficha = '" + paramCodFicha + "' and nro_aula = " + paramNroAula;
        }
        else
        {
            conditional_docente = "cod_ficha = '" + paramCodFicha + "'";
        }

        conditional_instrumento = "cod_ficha = '" + paramCodFicha + "'";

        instrumentoE = instrumentoDAO.searchInstrumentoDocente( conditional_docente );

        if ( instrumentoE.getStatus() == 0 ) // encontro la ficha en docentes.
        {
            instrumentoE.setStatus( docentesDAO.inventarioFichaDocente( paramCodFicha ) ); // registra el inventario de ficha en docentes.
        }
        else if ( instrumentoE.getStatus() == 1 )
        {
            instrumentoE = instrumentoDAO.searchInstrumento( conditional_instrumento );

            if ( instrumentoE.getStatus() == 0 ) // encontro la ficha en instrumento
            {
                instrumentoE.setStatus( instrumentoDAO.inventarioFicha( paramCodFicha, paramNroAula ) ); // registra la ficha en instrumento

                if ( instrumentoE.getStatus() == 0 )
                {
                    instrumentoE = instrumentoDAO.searchInstrumento( conditional_instrumento ); // devuelve los datos a mostrar de la ficha.
                }
            }
        }

        return instrumentoE;

    }

    public InstrumentoE inventarioCuadernillo( String paramCodCuadernillo, Integer paramNroAula ) {

        Integer contingencia = aulaLocalDAO.searchTipoAula( paramNroAula );

        if ( contingencia == 0 ) // NO es del TIPO CONTINGENCIA
        {
            conditional_docente = "cod_cartilla = '" + paramCodCuadernillo + "' and nro_aula = " + paramNroAula;
        }
        else
        {
            conditional_docente = "cod_cartilla = '" + paramCodCuadernillo + "'";
        }

        conditional_instrumento = "cod_cartilla = '" + paramCodCuadernillo + "'";

        instrumentoE = instrumentoDAO.searchInstrumentoDocente( conditional_docente );

        if ( instrumentoE.getStatus() == 0 ) // encontro el cuadernillo en docentes.
        {
            instrumentoE.setStatus( docentesDAO.inventarioCuadernilloDocente( paramCodCuadernillo ) ); // registra el inventario de cuadernillo en docentes.
        }
        else if ( instrumentoE.getStatus() == 1 )
        {
            instrumentoE = instrumentoDAO.searchInstrumento( conditional_instrumento );

            if ( instrumentoE.getStatus() == 0 ) // encontro el cuadernillo en instrumento
            {
                instrumentoE.setStatus( instrumentoDAO.inventarioCuadernillo( paramCodCuadernillo ) ); // registra el cuadernillo en instrumento

                if ( instrumentoE.getStatus() == 0 )
                {
                    instrumentoE = instrumentoDAO.searchInstrumento( conditional_instrumento ); // devuelve los datos a mostrar del cuadernillo.
                }
            }
        }


        return instrumentoE;
    }

}