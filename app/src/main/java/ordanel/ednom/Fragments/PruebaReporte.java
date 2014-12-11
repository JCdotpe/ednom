package ordanel.ednom.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import ordanel.ednom.Interfaces.MainI;
import ordanel.ednom.ListView.CustomArrayAdapter;
import ordanel.ednom.ListView.DataSource;
import ordanel.ednom.R;

/**
 * Created by OrdNael on 11/12/2014.
 */
public class PruebaReporte extends ListFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private static final int PAGESIZE = 10;

    View view;
    View footerView;
    ListView listView;
    DataSource dataSource;

    protected boolean loading = false;

    private MainI mListener;

    public PruebaReporte(){
    }

    public static PruebaReporte newInstance( int position ) {

        PruebaReporte fragment = new PruebaReporte();

        Bundle args = new Bundle();
        args.putInt( ARG_SECTION_NUMBER, position );

        fragment.setArguments( args );

        return fragment;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dataSource = DataSource.getInstance();

        footerView = ( (LayoutInflater) getActivity().getSystemService( getActivity().getApplicationContext().LAYOUT_INFLATER_SERVICE ) ).inflate( R.layout.footer, null, false );
        getListView().addFooterView( footerView, null, false );
        setListAdapter( new CustomArrayAdapter( getActivity(), dataSource.getData( 0, PAGESIZE ) ) );
        getListView().removeFooterView( footerView );

        getListView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                boolean lastItem = ( firstVisibleItem + visibleItemCount == totalItemCount );
                boolean moreRows = getListAdapter().getCount() < dataSource.getSize();

                if ( !loading && lastItem && moreRows )
                {
                    loading = true;
                    getListView().addFooterView( footerView, null, false );
                    (new LoadNextPage()).execute("");
                }

            }
        });

        updateDisplayingTextView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /*return super.onCreateView(inflater, container, savedInstanceState);*/

        view = inflater.inflate( R.layout.endless, container, false );
        mListener.onSectionAttached( getArguments().getInt( ARG_SECTION_NUMBER ) );

        /*listView.addFooterView( footerView, null, false );
        listView.setAdapter( new CustomArrayAdapter( getActivity(), R.layout.listview, dataSource.getData( 0, PAGESIZE ) ) );
        listView.removeFooterView( footerView );

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                boolean lastItem = ( firstVisibleItem + visibleItemCount == totalItemCount );
                boolean moreRows = listView.getAdapter().getCount() < dataSource.getSize();

                if ( !loading && lastItem && moreRows )
                {
                    loading = true;
                    listView.addFooterView( footerView, null, false );
                    (new LoadNextPage()).execute("");
                }

            }
        });*/

        return view;

    }

    private void updateDisplayingTextView() {

        TextView textViewDisplaying = (TextView) view.findViewById( R.id.displaying );
        String text = getString( R.string.display );
        text = String.format( text, getListAdapter().getCount(), dataSource.getSize()  );
        textViewDisplaying.setText( text );

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

    private class LoadNextPage extends AsyncTask<String, Void, String> {

        private List<String> newData = null;

        @Override
        protected String doInBackground(String... params) {
            try
            {
                Thread.sleep(1500);

            }
            catch (Exception e)
            {
                Log.e( "LoadNexPage", e.getMessage() );
            }

            /*newData = dataSource.getData( listView.getAdapter().getCount(), PAGESIZE );*/
            newData = dataSource.getData( getListAdapter().getCount(), PAGESIZE );

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            /*CustomArrayAdapter customArrayAdapter = (CustomArrayAdapter) listView.getAdapter();*/
            CustomArrayAdapter customArrayAdapter = ( (CustomArrayAdapter) getListAdapter() );

            for ( String value : newData )
            {
                customArrayAdapter.add( value );
            }

            customArrayAdapter.notifyDataSetChanged();

            getListView().removeFooterView(footerView);
            updateDisplayingTextView();
            loading = false;

        }

    }


}