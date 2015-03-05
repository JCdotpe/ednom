package ordanel.ednom.Fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import ordanel.ednom.Adapter.DocentesArrayAdapter;
import ordanel.ednom.Business.AulaLocalBL;
import ordanel.ednom.Business.DocentesBL;
import ordanel.ednom.Entity.AulaLocalE;
import ordanel.ednom.Entity.DocentesE;
import ordanel.ednom.Interfaces.MainI;
import ordanel.ednom.R;

/**
 * Created by OrdNael on 15/12/2014.
 */
public class ListadoAsistenciaAula extends ListFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static ListadoAsistenciaAula fragment;
    private static final int PAGESIZE = 10;

    private Integer nro_aula;
    protected boolean loading = false;

    View view;
    View footerView;
    DocentesBL docentesBL;

    private MainI mListener;

    public ListadoAsistenciaAula() {
    }

    public static ListadoAsistenciaAula newInstance( int position ) {

        if ( fragment == null )
        {
            fragment = new ListadoAsistenciaAula();

            Bundle args = new Bundle();
            args.putInt( ARG_SECTION_NUMBER, position );

            fragment.setArguments( args );
        }

        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Spinner spinner = (Spinner) view.findViewById( R.id.spinnerAula );

        // source de spinner
        ArrayList<AulaLocalE> stringArrayList = new AulaLocalBL( this.getActivity() ).getAllNroAula();
        ArrayAdapter<AulaLocalE> adapter = new ArrayAdapter<>( this.getActivity(),R.layout.selected_item, stringArrayList );

        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        spinner.setAdapter( adapter );
        // .source de spinner

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                nro_aula = ( (AulaLocalE) spinner.getSelectedItem() ).getNro_aula();
                /*mListener.listadoAsistenciaAula( nro_aula );*/

                docentesBL = new DocentesBL( getActivity().getApplicationContext() );
                footerView = ( (LayoutInflater) getActivity().getSystemService( Context.LAYOUT_INFLATER_SERVICE ) ).inflate( R.layout.footer, null, false );

                getListView().addFooterView(footerView, null, false);
                setListAdapter( new DocentesArrayAdapter(getActivity().getApplicationContext(), docentesBL.listadoAsistenciaAula(nro_aula, 0, PAGESIZE)));
                getListView().removeFooterView(footerView);

                getListView().setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {

                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                        boolean lastItem = ( firstVisibleItem + visibleItemCount == totalItemCount );
                        boolean moreRows = getListAdapter().getCount() < docentesBL.getSIZE();

                        if (!loading && lastItem && moreRows) {
                            loading = true;
                            getListView().addFooterView(footerView, null, false);
                            (new ListadoAsistenciaAulaAsync()).execute( nro_aula );
                        }

                    }
                });

                updateDisplayingTextView();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_listado_asistencia_aula, container, false );
        mListener.onSectionAttached( getArguments().getInt( ARG_SECTION_NUMBER ) );

        return view;

    }

    private void updateDisplayingTextView() {

        TextView textViewDisplaying = (TextView) view.findViewById( R.id.displaying );
        String text = getString( R.string.display );
        text = String.format( text, getListAdapter().getCount(), docentesBL.getSIZE()  );
        textViewDisplaying.setText( text );
        TextView textViewSincronizado = (TextView) view.findViewById(R.id.txt_sincronizado);
        textViewSincronizado.setText( docentesBL.getNroDatosSincronizados(DocentesE.ESTADO_AULA, nro_aula)+ " docentes sincronizados");

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
            throw new ClassCastException( activity.toString() + "must implement OnFragmentInteractionListener" );
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;

    }

    private class ListadoAsistenciaAulaAsync extends AsyncTask<Integer, Void, String> {


        private ArrayList<DocentesE> newData = null;

        @Override
        protected String doInBackground(Integer... params) {
            try
            {
                Thread.sleep(1500);

            }
            catch (Exception e)
            {
                Log.e("LoadNextPage", e.getMessage());
            }

            newData = docentesBL.listadoAsistenciaAula( params[0], getListAdapter().getCount(), PAGESIZE );

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