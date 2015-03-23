package ordanel.ednom.Fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import ordanel.ednom.Business.AulaLocalBL;
import ordanel.ednom.Entity.AulaLocalE;
import ordanel.ednom.Interfaces.MainI;
import ordanel.ednom.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CambiarCargo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CambiarCargo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CambiarCargo extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private MainI mListener;
    EditText editTextDni;
    Button btnCambioCargo;
    private String nroDni;
    private String cargo;

    public static CambiarCargo newInstance( int position ) {

        CambiarCargo fragment = new CambiarCargo();

        Bundle args = new Bundle();
        args.putInt( ARG_SECTION_NUMBER, position );

        fragment.setArguments( args );

        return  fragment;

    }

    public CambiarCargo() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cambiar_cargo, container, false);
        mListener.onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
        editTextDni = (EditText) view.findViewById(R.id.edtDNI);
        btnCambioCargo = (Button) view.findViewById(R.id.btn_cambio_cargo);

        final Spinner spinner = (Spinner) view.findViewById( R.id.spinner );

        // source de spinner
        List<String> lista = new ArrayList<String>();
        lista.add("ORIENTADOR");
        lista.add("APLICADOR");
        lista.add("OPERADOR INFORMÁTICO");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this.getActivity(), R.layout.selected_item, lista);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        // .source de spinner
        editTextDni.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 8){
                    nroDni = s.toString();
                    mListener.searchPersonalCambioCargo(nroDni);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 8){
                    editTextDni.setText("");
                }
            }
        });
        btnCambioCargo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargo = spinner.getSelectedItem().toString();
                mListener.registrarCambioCargo(nroDni, cargo);
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
