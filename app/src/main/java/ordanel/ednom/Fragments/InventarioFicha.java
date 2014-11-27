package ordanel.ednom.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ordanel.ednom.Interfaces.MainI;
import ordanel.ednom.R;

/**
 * Created by OrdNael on 27/11/2014.
 */
public class InventarioFicha extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

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
