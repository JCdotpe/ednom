package ordanel.ednom.Fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ordanel.ednom.Business.PersonalBL;
import ordanel.ednom.Entity.PersonalE;
import ordanel.ednom.Interfaces.MainI;
import ordanel.ednom.Library.Item;
import ordanel.ednom.R;

public class ReporteAsistencia extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    String dni, nombre, asistencia, cargo, dniReemplazo, nombreReemplazo;
    TextView txtDni, txtNombre, txtAsistencia, txtCargo, txtDniReemplazo, txtNombreReemplazo;
    ListView listView;
    PersonalBL personalBL;
    private MainI mListener;

    public static ReporteAsistencia newInstance( int position ) {

        ReporteAsistencia fragment = new ReporteAsistencia();

        Bundle args = new Bundle();
        args.putInt( ARG_SECTION_NUMBER, position );

        fragment.setArguments( args );

        return  fragment;

    }

    public ReporteAsistencia() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mListener.onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
        List items = new ArrayList();
        View view = inflater.inflate(R.layout.fragment_reporte_asistencia, container, false);
        personalBL = new PersonalBL(getActivity().getApplicationContext());
        listView = (ListView) view.findViewById(R.id.list_view_personal);
        txtDni = (TextView) view.findViewById(R.id.txt_list_dni);
        txtNombre = (TextView) view.findViewById(R.id.txt_list_nombre);
        txtAsistencia = (TextView) view.findViewById(R.id.txt_list_asistencia);
        txtCargo = (TextView) view.findViewById(R.id.txt_list_cargo);
        txtDniReemplazo = (TextView) view.findViewById(R.id.txt_list_dni_reemp);
        txtNombreReemplazo = (TextView) view.findViewById(R.id.txt_list_nombre_reemp);
        ArrayList<PersonalE> personalEArrayList = personalBL.listadoAsistencia();
        for (PersonalE personalE : personalEArrayList){
            items.add(new PersonalE(personalE.getDni(), personalE.getNombre_completo(), personalE.getAsistencia(), personalE.getCargo(), personalE.getR_dni(), personalE.getR_nombre_completo()));
        }
        listView.setAdapter(new ItemAdapter(getActivity(), items));

        return view;
    }

    public void putData(PersonalE personalE){
        dni = personalE.getDni();
        nombre = personalE.getNombre_completo();
        asistencia = personalE.getAsistencia();
        cargo = personalE.getCargoE().getCargoRes();
        dniReemplazo = personalE.getR_dni();
        nombreReemplazo = personalE.getR_nombre_completo();
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

    public class ItemAdapter extends BaseAdapter{

        private Context context;
        private List<PersonalE> items;

        public ItemAdapter(Context context, List<PersonalE> items) {
            this.context = context;
            this.items = items;
        }
        @Override
        public int getCount() {
            return this.items.size();
        }

        @Override
        public Object getItem(int position) {
            return this.items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = convertView;
            if (convertView == null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_item_2, parent, false);
            }

            txtDni = (TextView) view.findViewById(R.id.txt_list_dni);
            txtNombre = (TextView) view.findViewById(R.id.txt_list_nombre);
            txtAsistencia = (TextView) view.findViewById(R.id.txt_list_asistencia);
            txtCargo = (TextView) view.findViewById(R.id.txt_list_cargo);
            txtDniReemplazo = (TextView) view.findViewById(R.id.txt_list_dni_reemp);
            txtNombreReemplazo = (TextView) view.findViewById(R.id.txt_list_nombre_reemp);

            PersonalE personalE = this.items.get(position);
            txtDni.setText(personalE.getDni());
            txtNombre.setText(personalE.getNombre_completo());
            txtAsistencia.setText(personalE.getAsistencia());
            txtCargo.setText(personalE.getCargo());
            txtDniReemplazo.setText(personalE.getR_dni());
            txtNombreReemplazo.setText(personalE.getR_nombre_completo());
            return view;
        }
    }
}
