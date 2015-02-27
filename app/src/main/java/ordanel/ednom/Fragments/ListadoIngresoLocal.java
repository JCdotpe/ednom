package ordanel.ednom.Fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import java.util.ArrayList;

import ordanel.ednom.Adapter.DocentesArrayAdapter;
import ordanel.ednom.Business.DocentesBL;
import ordanel.ednom.Business.PadronBL;
import ordanel.ednom.Entity.DocentesE;
import ordanel.ednom.Interfaces.MainI;
import ordanel.ednom.R;

/**
 * Created by OrdNael on 11/12/2014.
 */
public class ListadoIngresoLocal extends ListFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static ListadoIngresoLocal fragment;
    private static final int PAGESIZE = 10;

    protected boolean loading = false;

    View view;
    View footerView;
    DocentesBL docentesBL;

    private MainI mListener;

    public ListadoIngresoLocal(){
    }

    public static ListadoIngresoLocal newInstance( int position ) {

        if ( fragment == null )
        {
            fragment = new ListadoIngresoLocal();

            Bundle args = new Bundle();
            args.putInt( ARG_SECTION_NUMBER, position );

            fragment.setArguments( args );
        }

        return fragment;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        docentesBL = new DocentesBL( getActivity().getApplicationContext() );

        footerView = ( (LayoutInflater) getActivity().getSystemService( getActivity().getApplicationContext().LAYOUT_INFLATER_SERVICE ) ).inflate( R.layout.footer, null, false );
        getListView().addFooterView( footerView, null, false );
        setListAdapter( new DocentesArrayAdapter( getActivity(), docentesBL.listadoIngresoLocal( 0, PAGESIZE ) ) );
        getListView().removeFooterView( footerView );

        getListView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                boolean lastItem = ( firstVisibleItem + visibleItemCount == totalItemCount );
                boolean moreRows = getListAdapter().getCount() < docentesBL.getSIZE();

                if ( !loading && lastItem && moreRows )
                {
                    loading = true;
                    getListView().addFooterView( footerView, null, false );
                    (new ListadoIngresoLocalAsync()).execute("");
                }

            }
        });
        updateDisplayingTextView();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate( R.layout.fragment_listado_ingreso_local, container, false );
        mListener.onSectionAttached( getArguments().getInt( ARG_SECTION_NUMBER ) );


        return view;

    }

    private void updateDisplayingTextView() {

        TextView textViewDisplaying = (TextView) view.findViewById( R.id.displaying );
        String text = getString( R.string.display );
        text = String.format( text, getListAdapter().getCount(), docentesBL.getSIZE()  );
        textViewDisplaying.setText( text );
        TextView textViewSincronizado = (TextView) view.findViewById(R.id.txt_sincronizado);
        textViewSincronizado.setText( docentesBL.getNroDatosSincronizados(DocentesE.ESTADO)+ " docentes sincronizados");

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

    private class ListadoIngresoLocalAsync extends AsyncTask<String, Void, String> {

        private ArrayList<DocentesE> newData = null;

        @Override
        protected String doInBackground(String... params) {
            try
            {
                Thread.sleep(1500);

            }
            catch (Exception e)
            {
                Log.e( "LoadNextPage", e.getMessage() );
            }

            newData = docentesBL.listadoIngresoLocal( getListAdapter().getCount(), PAGESIZE );

            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            DocentesArrayAdapter docentesArrayAdapter = ( (DocentesArrayAdapter) getListAdapter() );

            for ( DocentesE value : newData )
            {
                docentesArrayAdapter.add( value );
            }

            docentesArrayAdapter.notifyDataSetChanged();

            getListView().removeFooterView(footerView);
            updateDisplayingTextView();
            loading = false;

        }

    }

}