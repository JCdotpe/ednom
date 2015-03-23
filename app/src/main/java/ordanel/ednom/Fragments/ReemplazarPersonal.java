package ordanel.ednom.Fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ordanel.ednom.Interfaces.MainI;
import ordanel.ednom.R;

public class ReemplazarPersonal extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private MainI mListener;
    EditText editTextDni;
    private String nroDni;
    Button btnReemplazar;
    EditText txtDniCambio;
    EditText txtNombreCambio;

    public static ReemplazarPersonal newInstance( int position ) {

        ReemplazarPersonal fragment = new ReemplazarPersonal();

        Bundle args = new Bundle();
        args.putInt( ARG_SECTION_NUMBER, position );

        fragment.setArguments( args );

        return  fragment;

    }

    public ReemplazarPersonal() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reemplazar_personal, container, false);
        mListener.onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
        txtDniCambio= (EditText) view.findViewById(R.id.txt_dni_cambio);
        txtNombreCambio = (EditText) view.findViewById(R.id.txt_nombre_cambio);
        editTextDni = (EditText) view.findViewById(R.id.edtDNI);
        editTextDni.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 8){
                    nroDni = s.toString();
                    mListener.searchPersonalCambio(nroDni);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 8){
                    editTextDni.setText("");
                }
            }
        });

        btnReemplazar = (Button) view.findViewById(R.id.btn_reemplazar);
        btnReemplazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dniCambio = txtDniCambio.getText().toString();
                String nombreCambio = txtNombreCambio.getText().toString();
                if (dniCambio.length() != 8 || nombreCambio.isEmpty()) {
                    Toast.makeText(getActivity(), "Falta llenar campos", Toast.LENGTH_SHORT).show();
                    txtDniCambio.setText("");
                    txtNombreCambio.setText("");
                }else {
                    mListener.reemplazarPersonal(nroDni, dniCambio, nombreCambio);
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
