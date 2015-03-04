package ordanel.ednom.Fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
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

import ordanel.ednom.Adapter.InstrumentoArrayAdapter;
import ordanel.ednom.Business.AulaLocalBL;
import ordanel.ednom.Business.InstrumentoBL;
import ordanel.ednom.Entity.AulaLocalE;
import ordanel.ednom.Entity.DocentesE;
import ordanel.ednom.Entity.InstrumentoE;
import ordanel.ednom.Interfaces.MainI;
import ordanel.ednom.R;

/**
 * Created by Leandro on 18/12/2014.
 */
public class ListadoInventarioCuadernillo extends ListFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static ListadoInventarioCuadernillo fragment;
    private static final int PAGESIZE = 10;

    private Integer nro_aula;
    protected boolean loading = false;

    View view;
    View footerView;
    InstrumentoBL instrumentoBL;

    private MainI mListener;

    public ListadoInventarioCuadernillo() {
    }

    public static ListadoInventarioCuadernillo newInstance( int position ) {

        if ( fragment == null )
        {
            fragment = new ListadoInventarioCuadernillo();

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
                /*mListener.listadoInventarioFicha( nro_aula );*/

                instrumentoBL = new InstrumentoBL( getActivity().getApplicationContext() );
                footerView = ( (LayoutInflater) getActivity().getSystemService( Context.LAYOUT_INFLATER_SERVICE ) ).inflate( R.layout.footer, null, false );

                getListView().addFooterView( footerView, null, false );
                setListAdapter( new InstrumentoArrayAdapter( getActivity().getApplicationContext(), instrumentoBL.listadoInventarioCuadernillo( nro_aula, 0, PAGESIZE ) ) );
                getListView().removeFooterView( footerView );

                getListView().setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {

                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                        boolean lastItem = ( firstVisibleItem + visibleItemCount == totalItemCount );
                        boolean moreRows = getListAdapter().getCount() < instrumentoBL.getSIZE();

                        if (!loading && lastItem && moreRows) {
                            loading = true;
                            getListView().addFooterView(footerView, null, false);
                            (new ListadoInventarioCuadernilloAsync()).execute( nro_aula, getListAdapter().getCount(), PAGESIZE );
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_listado_inventario_ficha, container, false );
        mListener.onSectionAttached( getArguments().getInt( ARG_SECTION_NUMBER ) );

        return view;

    }

    private void updateDisplayingTextView() {

        TextView textViewDisplaying = (TextView) view.findViewById( R.id.displaying );
        String text = getString( R.string.display );
        text = String.format( text, getListAdapter().getCount(), instrumentoBL.getSIZE()  );
        textViewDisplaying.setText( text );
        TextView textViewSincronizado = (TextView) view.findViewById(R.id.txt_sincronizado);
        textViewSincronizado.setText( instrumentoBL.getNroDatosSincronizados(InstrumentoE.ESTADO_CARTILLA)+ " cuadernillos sincronizados");
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

    private class ListadoInventarioCuadernilloAsync extends AsyncTask<Integer, Void, String> {

        private ArrayList<DocentesE> newData = null;

        @Override
        protected String doInBackground(Integer... params) {

            Integer nro_aula, offset, limit;

            nro_aula = params[0];
            offset = params[1];
            limit = params[2];

            try
            {
                Thread.sleep( 1500 );
            }
            catch ( Exception e )
            {
                Log.e("ListadoInventarioFicha", e.getMessage());
            }

            newData = instrumentoBL.listadoInventarioCuadernillo( nro_aula, offset, limit );

            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            InstrumentoArrayAdapter docentesArrayAdapter = ( (InstrumentoArrayAdapter) getListAdapter() );

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