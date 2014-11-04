package ordanel.ednom.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import ordanel.ednom.MainActivity;
import ordanel.ednom.R;

/**
 * Created by OrdNael on 03/11/2014.
 */
public class IngresoLocal extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private OnFragmentInteractionListener mListener;


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


        Button button = (Button) view.findViewById( R.id.btnLocal );
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction( v );
            }
        });


        return view;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

//        ( (MainActivity) activity ).onSectionAttached( getArguments().getInt( ARG_SECTION_NUMBER ) );
        try
        {
            mListener = (OnFragmentInteractionListener) activity;
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