package ordanel.ednom.Fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ordanel.ednom.Interfaces.MainI;
import ordanel.ednom.R;

public class ReemplazarPersonal extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private MainI mListener;

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reemplazar_personal, container, false);
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
