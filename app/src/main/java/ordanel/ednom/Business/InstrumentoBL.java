package ordanel.ednom.Business;

import android.content.Context;

import ordanel.ednom.DAO.DocentesDAO;
import ordanel.ednom.DAO.InstrumentoDAO;
import ordanel.ednom.Entity.DocentesE;
import ordanel.ednom.Entity.InstrumentoE;

/**
 * Created by OrdNael on 02/12/2014.
 */
public class InstrumentoBL {

    private static DocentesDAO docentesDAO;
    private static InstrumentoDAO instrumentoDAO;
    private static InstrumentoE instrumentoE;

    private static String conditional;

    public InstrumentoBL(Context paramContext) {

        docentesDAO = DocentesDAO.getInstance( paramContext );
        instrumentoDAO = InstrumentoDAO.getInstance( paramContext );

    }

    public static InstrumentoE inventarioFicha( String paramCodFicha, Integer paramNroAula ) {

        conditional = "cod_ficha = '" + paramCodFicha +"'";
        instrumentoE = instrumentoDAO.searchInstrumentoDocente( conditional );

        if ( instrumentoE.getStatus() == 0 ) // encontro la ficha en docentes.
        {
            docentesDAO.inventarioFichaDocente( paramCodFicha ); // registra el inventario de ficha en docentes.
        }
        else if ( instrumentoE.getStatus() == 1 )
        {
            instrumentoE = instrumentoDAO.searchInstrumento( conditional );

            if ( instrumentoE.getStatus() == 0 ) // encontro la ficha en instrumento
            {
                instrumentoE.setStatus( instrumentoDAO.inventarioFicha( paramCodFicha, paramNroAula ) ); // registra la ficha en instrumento

                if ( instrumentoE.getStatus() == 0 )
                {
                    instrumentoE = instrumentoDAO.searchInstrumento( conditional ); // devuelve los datos a mostrar de la ficha.
                }
            }
        }

        return instrumentoE;

    }

}