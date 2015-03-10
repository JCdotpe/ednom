package ordanel.ednom.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ordanel.ednom.Asyncs.SyncAsync;
import ordanel.ednom.Business.DocentesBL;
import ordanel.ednom.Business.InstrumentoBL;
import ordanel.ednom.Entity.DocentesE;
import ordanel.ednom.MainActivity;
import ordanel.ednom.R;

/**
 * Created by OrdNael on 10/11/2014.
 */
public class Welcome extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private TextView nroDocentesIngresosRegistrados;
    private TextView nroDocentesIngresosSincronizados;
    private TextView nroDocentesAulaRegistrados;
    private TextView nroDocentesAulaSincronizados;
    private TextView nroFichasRegistradas;
    private TextView nroFichasSincronizadas;
    private TextView nroCuadernillosRegistrados;
    private TextView nroCuadernillosSincronizados;
    DocentesBL docentesBL;
    InstrumentoBL instrumentoBL;

    public Welcome() {
    }

    public static Welcome newInstance( int position ) {

        Welcome fragment = new Welcome();

        Bundle args = new Bundle();
        args.putInt( ARG_SECTION_NUMBER, position );

        fragment.setArguments( args );

        return  fragment;

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate( R.layout.fragment_welcome, container, false );
        docentesBL = new DocentesBL( getActivity().getApplicationContext() );
        instrumentoBL = new InstrumentoBL( getActivity().getApplicationContext() );

        nroDocentesIngresosRegistrados = (TextView) view.findViewById(R.id.txt_nro_docentes_ingresos_registrados);
        nroDocentesIngresosSincronizados = (TextView) view.findViewById(R.id.txt_nro_docentes_ingresos_sincronizados);
        nroDocentesAulaRegistrados = (TextView) view.findViewById(R.id.txt_nro_docentes_aula_registrados);
        nroDocentesAulaSincronizados = (TextView) view.findViewById(R.id.txt_nro_docentes_aula_sincronizados);
        nroFichasRegistradas = (TextView) view.findViewById(R.id.txt_nro_fichas_registradas);
        nroFichasSincronizadas = (TextView) view.findViewById(R.id.txt_nro_fichas_sincronizados);
        nroCuadernillosRegistrados = (TextView) view.findViewById(R.id.txt_nro_cuadernillos_registrados);
        nroCuadernillosSincronizados = (TextView) view.findViewById(R.id.txt_nro_cuadernillos_sincronizados);

        // Datos sincronizados
        nroDocentesIngresosSincronizados.setText(String.valueOf(docentesBL.getNroDatosSincronizados(DocentesE.ESTADO, 0)));
        //nroDocentesAulaSincronizados.setText(String.valueOf(docentesBL.getNroDatosSincronizados(DocentesE.ESTADO_AULA, 0)));
        //nroFichasSincronizadas.setText(String.valueOf(instrumentoBL.getNroDatosSincronizados(DocentesE.ESTADO_FICHA, 0)));
        //nroCuadernillosSincronizados.setText(String.valueOf(instrumentoBL.getNroDatosSincronizados(DocentesE.ESTADO_CARTILLA, 0)));

        // Datos Registrados
        nroDocentesIngresosRegistrados.setText(String.valueOf(docentesBL.getNroDatosRegistrados(DocentesE.ESTADO, 0)));
        //nroDocentesAulaRegistrados.setText(String.valueOf(docentesBL.getNroDatosRegistrados(DocentesE.ESTADO_AULA, 0)));
        //nroFichasRegistradas.setText(String.valueOf(instrumentoBL.getNroDatosRegistrados(DocentesE.ESTADO_FICHA, 0)));
        //nroCuadernillosRegistrados.setText(String.valueOf(instrumentoBL.getNroDatosRegistrados(DocentesE.ESTADO_CARTILLA, 0)));

        return view;
        
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ( (MainActivity) activity ).onSectionAttached( getArguments().getInt(ARG_SECTION_NUMBER) );
    }
}
