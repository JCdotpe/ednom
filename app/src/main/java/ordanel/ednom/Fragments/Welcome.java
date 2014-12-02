package ordanel.ednom.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ordanel.ednom.MainActivity;
import ordanel.ednom.R;

/**
 * Created by OrdNael on 10/11/2014.
 */
public class Welcome extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public Welcome() {
    }

    public static Welcome newInstance( int position ) {

        Welcome fragment = new Welcome();

        Bundle args = new Bundle();
        args.putInt( ARG_SECTION_NUMBER, position );

        fragment.setArguments( args );

        return  fragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate( R.layout.fragment_welcome, container, false );

        Button btnJson = (Button) view.findViewById( R.id.btnJSON );
        btnJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pruebaJSon();
            }
        });

        return view;

    }

    public void pruebaJSon() {

        //new SyncAsync( getActivity() ).execute();

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ( (MainActivity) activity ).onSectionAttached( getArguments().getInt(ARG_SECTION_NUMBER) );
    }
}
