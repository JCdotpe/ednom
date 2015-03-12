package ordanel.ednom.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import ordanel.ednom.Interfaces.MainI;
import ordanel.ednom.R;

public class IngresoLocal extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private String number_dni;

    EditText edtDNI;

    private MainI mListener;

    public IngresoLocal() {
    }

    public static IngresoLocal newInstance ( int sectionNumber ) {

        IngresoLocal fragment = new IngresoLocal();

        Bundle args = new Bundle();
        args.putInt( ARG_SECTION_NUMBER, sectionNumber );

        fragment.setArguments( args );

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate( R.layout.fragment_ingreso_local, container, false );
        mListener.onSectionAttached( getArguments().getInt( ARG_SECTION_NUMBER ) );

        edtDNI = (EditText) view.findViewById( R.id.edtDNI);

        edtDNI.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if ( s.length() == 8 )
                {
                    number_dni = s.toString();
                    mListener.asistenciaLocal( number_dni );
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
        catch (ClassCastException e)
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