package ordanel.ednom.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ordanel.ednom.Interfaces.MainI;
import ordanel.ednom.R;


public class InventarioCuadernillo extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private MainI mListener;

    public static InventarioCuadernillo newInstance( int position ) {

        InventarioCuadernillo fragment = new InventarioCuadernillo();

        Bundle args = new Bundle();

        args.putInt(ARG_SECTION_NUMBER, position);

        fragment.setArguments( args );

        return fragment;
    }

    public InventarioCuadernillo() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate( R.layout.fragment_inventario_cuadernillo, container, false);
        mListener.onSectionAttached( getArguments().getInt( ARG_SECTION_NUMBER ) );

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
            throw new ClassCastException( activity.toString()
                    + " must implement OnFragmentInteractionListener" );
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }

}