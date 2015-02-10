package ordanel.ednom.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import ordanel.ednom.Business.UsuarioLocalBL;
import ordanel.ednom.Interfaces.MainI;
import ordanel.ednom.Library.ConstantsUtils;
import ordanel.ednom.R;


public class ReporteGeneral extends Fragment {
    String pass;
    UsuarioLocalBL usuarioLocalBL;
    private static final String ARG_SECTION_NUMBER = "section number";
    WebView webview;
    MainI mListener;
    public ReporteGeneral(){

    }



    public static ReporteGeneral newInstance ( int sectionNumber ) {

        ReporteGeneral fragment = new ReporteGeneral();

        Bundle args = new Bundle();
        args.putInt( ARG_SECTION_NUMBER, sectionNumber );

        fragment.setArguments( args );

        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mListener.onSectionAttached( getArguments().getInt( ARG_SECTION_NUMBER ) );
        View view = inflater.inflate(R.layout.fragment_reporte_general, container, false);
        webview = (WebView) view.findViewById(R.id.webView);
        pass = ConstantsUtils.getPass;
        webview.loadUrl(ConstantsUtils.URL_REPORTE_ACCESS + "?sendPass=" + pass);
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        if (savedInstanceState != null) {
            webview.restoreState(savedInstanceState);
        } else {
            webview.getSettings().setJavaScriptEnabled(true);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        webview.saveState(outState);
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
