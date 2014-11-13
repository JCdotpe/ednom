package ordanel.ednom.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import ordanel.ednom.Interfaces.MainI;
import ordanel.ednom.R;

/**
 * Created by OrdNael on 03/11/2014.
 */
public class IngresoLocal extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private MainI mListener;

    EditText edtDNI_Local;


    public IngresoLocal() {
    }

    public static IngresoLocal newInstance ( int sectionNumber ) {

        IngresoLocal fragment = new IngresoLocal();

        Bundle args = new Bundle();
        args.putInt( ARG_SECTION_NUMBER, sectionNumber );

        fragment.setArguments( args );

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate( R.layout.fragment_ingreso_local, container, false );
        mListener.onSectionAttached( getArguments().getInt( ARG_SECTION_NUMBER ) );

        edtDNI_Local = (EditText) view.findViewById( R.id.edtDNI_Local );
        edtDNI_Local.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if ( s.length() == 8 )
                {
                    mListener.searchPerson();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

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