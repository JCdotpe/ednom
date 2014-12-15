package ordanel.ednom.Fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import ordanel.ednom.Business.AulaLocalBL;
import ordanel.ednom.Entity.AulaLocalE;
import ordanel.ednom.Interfaces.MainI;
import ordanel.ednom.R;

/**
 * Created by OrdNael on 15/12/2014.
 */
public class ListadoAsistenciaAula extends ListFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static ListadoAsistenciaAula fragment;

    private Integer nro_aula;

    private MainI mListener;

    View view;

    public ListadoAsistenciaAula() {
    }

    public static ListadoAsistenciaAula newInstance( int position ) {

        if ( fragment == null )
        {
            fragment = new ListadoAsistenciaAula();

            Bundle args = new Bundle();
            args.putInt( ARG_SECTION_NUMBER, position );

            fragment.setArguments( args );
        }

        return fragment;
    }

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_listado_asistencia_aula, container, false );
        mListener.onSectionAttached( getArguments().getInt( ARG_SECTION_NUMBER ) );

        final Spinner spinner = (Spinner) view.findViewById( R.id.spinnerAula );

        // source de spinner
        ArrayList<AulaLocalE> stringArrayList = new AulaLocalBL( this.getActivity() ).getAllNroAula();
        ArrayAdapter<AulaLocalE> adapter = new ArrayAdapter<>( this.getActivity(),R.layout.selected_item, stringArrayList );

        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        spinner.setAdapter( adapter );
        // .source de spinner

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                nro_aula = ( (AulaLocalE) spinner.getSelectedItem() ).getNro_aula();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try
        {
            mListener = (MainI) activity;
        }
        catch ( Exception e )
        {
            throw new ClassCastException( activity.toString() + "must implement OnFragmentInteractionListener" );
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;

    }
}