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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import ordanel.ednom.Business.AulaLocalBL;
import ordanel.ednom.Interfaces.MainI;
import ordanel.ednom.R;

/**
 * Created by OrdNael on 27/11/2014.
 */
public class InventarioFicha extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    String cod_ficha;
    Integer nro_aula;

    EditText edtFicha;

    private MainI mListener;

    public InventarioFicha() {
    }

    public static InventarioFicha newInstance( int position ) {

        InventarioFicha fragment = new InventarioFicha();

        Bundle args = new Bundle();
        args.putInt( ARG_SECTION_NUMBER, position );

        fragment.setArguments( args );

        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate( R.layout.fragment_registro_ficha, container, false );
        mListener.onSectionAttached( getArguments().getInt( ARG_SECTION_NUMBER ) );

        final Spinner spinner = (Spinner) view.findViewById( R.id.spinnerAula );
        edtFicha = (EditText) view.findViewById( R.id.edtFicha );

        // source de spinner
        ArrayList<String> stringArrayList = new AulaLocalBL( this.getActivity() ).getAllNroAula();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>( this.getActivity(), R.layout.selected_item, stringArrayList );

        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        spinner.setAdapter( adapter );
        // .source de spinner

        edtFicha.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if ( s.length() == 7 )
                {
                    cod_ficha = s.toString();
                    nro_aula = Integer.parseInt( spinner.getSelectedItem().toString() );
                    mListener.inventarioFicha( cod_ficha, nro_aula );
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

                if ( s.length() == 7 )
                {
                    edtFicha.setText( "" );
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
            throw  new ClassCastException( activity.toString() + "must implement OnFragmentInteractionListener" );
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }
}
