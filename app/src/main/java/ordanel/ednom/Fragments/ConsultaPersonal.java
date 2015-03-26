package ordanel.ednom.Fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ordanel.ednom.Interfaces.MainI;
import ordanel.ednom.Library.ConstantsUtils;
import ordanel.ednom.R;


public class ConsultaPersonal extends Fragment {


    private static final String ARG_SECTION_NUMBER = "section_number";
    private MainI mListener;
    WebView webview;
    String pass;

    public static ConsultaPersonal newInstance( int position ) {

        ConsultaPersonal fragment = new ConsultaPersonal();

        Bundle args = new Bundle();
        args.putInt( ARG_SECTION_NUMBER, position );

        fragment.setArguments( args );

        return  fragment;

    }

    public ConsultaPersonal() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consulta_personal, container, false);
        mListener.onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
        webview = (WebView) view.findViewById(R.id.webView);
        pass = "sngen";
        webview.loadUrl(ConstantsUtils.URL_SEARCH + "?sendPass=" + pass);
        webview.setWebChromeClient(new WebChromeClient());
        Log.i("Url cargada: ", ConstantsUtils.URL_SEARCH + "?sendPass=" + pass);
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith(ConstantsUtils.URL_SEARCH)){
                    url = ConstantsUtils.URL_REPORTE_SEARCH_PERSONAL;
                }
                view.loadUrl(url);
                return true;
            }
        });

        webview.getSettings().setJavaScriptEnabled(true);
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
            throw new ClassCastException( activity.toString() + "must implement OnFragmentInteractionListener" );
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
