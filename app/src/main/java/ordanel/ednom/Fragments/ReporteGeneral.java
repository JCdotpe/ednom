package ordanel.ednom.Fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import ordanel.ednom.DAO.ReporteGeneralDAO;
import ordanel.ednom.Library.ConstantsUtils;
import ordanel.ednom.R;


public class ReporteGeneral extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section number";

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
        WebView webview;
        View view = inflater.inflate(R.layout.fragment_reporte_general, container, false);
        webview = (WebView) view.findViewById(R.id.webView);
        webview.loadUrl(ConstantsUtils.URL_REPORTE_ACCESS );
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }
}
