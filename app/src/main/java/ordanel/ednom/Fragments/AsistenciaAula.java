package ordanel.ednom.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import ordanel.ednom.Business.AulaLocalBL;
import ordanel.ednom.Entity.AulaLocalE;
import ordanel.ednom.Interfaces.MainI;
import ordanel.ednom.R;

/**
 * Created by OrdNael on 03/11/2014.
 */
public class AsistenciaAula extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private String number_dni;
    private Integer nro_aula;

    EditText edtDNI;

    private MainI mListener;

    public AsistenciaAula() {
    }

    public static AsistenciaAula newInstance( int position ) {

        AsistenciaAula fragment = new AsistenciaAula();

        Bundle args = new Bundle();
        args.putInt( ARG_SECTION_NUMBER, position );

        fragment.setArguments( args );

        return  fragment;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_asistencia_aula, container, false );
        mListener.onSectionAttached( getArguments().getInt( ARG_SECTION_NUMBER ) );

        final Spinner spinner = (Spinner) view.findViewById( R.id.spinnerAula );
        edtDNI = (EditText) view.findViewById( R.id.edtDNI );

        // source de spinner
        ArrayList<AulaLocalE> stringArrayList = new AulaLocalBL( this.getActivity() ).getAllNroAula();
        ArrayAdapter<AulaLocalE> adapter = new ArrayAdapter<>( this.getActivity(),R.layout.selected_item, stringArrayList );

        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        spinner.setAdapter( adapter );
        // .source de spinner

        edtDNI.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if ( s.length() == 8 )
                {
                    number_dni = s.toString();
                    nro_aula = ( (AulaLocalE) spinner.getSelectedItem() ).getNro_aula();
                    mListener.asistenciaAula( number_dni, nro_aula );
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ( s.length() == 8 )
                {
                    edtDNI.setText( "" );
                }
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
        catch (Exception e)
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
