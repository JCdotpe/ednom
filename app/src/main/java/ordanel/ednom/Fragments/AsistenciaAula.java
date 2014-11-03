package ordanel.ednom.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import ordanel.ednom.MainActivity;
import ordanel.ednom.R;

/**
 * Created by OrdNael on 03/11/2014.
 */
public class AsistenciaAula extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public AsistenciaAula() {
    }

    public static AsistenciaAula newInstance( int position ) {

        AsistenciaAula fragment = new AsistenciaAula();

        Bundle args = new Bundle();
        args.putInt( ARG_SECTION_NUMBER, position );

        fragment.setArguments( args );

        return  fragment;

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_asistencia_aula, container, false );

        Spinner spinner = (Spinner) view.findViewById( R.id.spinnerAula );
        ArrayAdapter adapter = ArrayAdapter.createFromResource( this.getActivity(), R.array.aulas, android.R.layout.simple_spinner_item );

        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        spinner.setAdapter( adapter );

        return view;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ( (MainActivity) activity ).onSectionAttached( getArguments().getInt(ARG_SECTION_NUMBER) );

    }
}
