package ordanel.ednom;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ordanel.ednom.Asyncs.SyncAsync;
import ordanel.ednom.Business.DocentesBL;
import ordanel.ednom.Business.InstrumentoBL;
import ordanel.ednom.Business.PersonalBL;
import ordanel.ednom.Entity.DocentesE;
import ordanel.ednom.Entity.InstrumentoE;
import ordanel.ednom.Entity.PersonalE;
import ordanel.ednom.Fragments.AsistenciaAula;
import ordanel.ednom.Fragments.BusquedaDocentes;
import ordanel.ednom.Fragments.CambiarCargo;
import ordanel.ednom.Fragments.ConsultaPersonal;
import ordanel.ednom.Fragments.IngresoLocal;
import ordanel.ednom.Fragments.InventarioCuadernillo;
import ordanel.ednom.Fragments.InventarioFicha;
import ordanel.ednom.Fragments.ListadoAsistenciaAula;
import ordanel.ednom.Fragments.ListadoIngresoLocal;
import ordanel.ednom.Fragments.ListadoInventarioCuadernillo;
import ordanel.ednom.Fragments.ListadoInventarioFicha;
import ordanel.ednom.Fragments.RedAdministrativa;
import ordanel.ednom.Fragments.ReemplazarPersonal;
import ordanel.ednom.Fragments.ReporteAsistencia;
import ordanel.ednom.Fragments.ReporteGeneral;
import ordanel.ednom.Fragments.Welcome;
import ordanel.ednom.Interfaces.MainI;
import ordanel.ednom.Library.ConstantsUtils;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, MainI {

    ProgressDialog progressDialog;
    DocentesE docentesE;
    DocentesBL docentesBL;
    InstrumentoE instrumentoE;
    InstrumentoBL instrumentoBL;
    PersonalE personalE;
    PersonalBL personalBL;
    String dni;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        docentesBL = new DocentesBL( MainActivity.this );
        instrumentoBL = new InstrumentoBL( MainActivity.this );
        personalBL = new PersonalBL(MainActivity.this);

       /* newData = getIntent().getParcelableArrayListExtra( "listadoInventarioFicha" );
        if ( newData != null )
        {
            updateDataListadoInventarioFicha();
        }*/

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = null;
        if (ConstantsUtils.getRol == 1){
            switch ( position )
            {
                case 0:
                    fragment = Welcome.newInstance( position + 1 );
                    break;

                case 1:
                    fragment = IngresoLocal.newInstance( position + 1 );
                    break;

                case 2:
                    fragment = AsistenciaAula.newInstance( position + 1 );
                    break;

                case 3:
                    fragment = InventarioFicha.newInstance( position + 1 );
                    break;

                case 4:
                    fragment = InventarioCuadernillo.newInstance( position + 1 );
                    break;

                case 5:
                    fragment = ListadoIngresoLocal.newInstance( position + 1 );
                    break;

                case 6:
                    fragment = ListadoAsistenciaAula.newInstance( position + 1 );
                    break;

                case 7:
                    fragment = ListadoInventarioFicha.newInstance( position + 1 );
                    break;

                case 8:
                    fragment = ListadoInventarioCuadernillo.newInstance( position + 1 );
                    break;
                case 9:
                    fragment = BusquedaDocentes.newInstance(position + 1);
                    break;
            }
        } else if (ConstantsUtils.getRol == 2){
        switch ( position )
        {
            case 0:
                fragment = Welcome.newInstance( position + 1 );
                break;

            case 1:
                fragment = IngresoLocal.newInstance( position + 1 );
                break;

            case 2:
                fragment = AsistenciaAula.newInstance( position + 1 );
                break;

            case 3:
                fragment = InventarioFicha.newInstance( position + 1 );
                break;

            case 4:
                fragment = InventarioCuadernillo.newInstance( position + 1 );
                break;

            case 5:
                fragment = ListadoIngresoLocal.newInstance( position + 1 );
                break;

            case 6:
                fragment = ListadoAsistenciaAula.newInstance( position + 1 );
                break;

            case 7:
                fragment = ListadoInventarioFicha.newInstance( position + 1 );
                break;

            case 8:
                fragment = ListadoInventarioCuadernillo.newInstance( position + 1 );
                break;
            case 9:
                fragment = ReporteGeneral.newInstance(position + 1);
                break;
            case 10:
                fragment = BusquedaDocentes.newInstance(position + 1);
                break;
        }} else if (ConstantsUtils.getRol == 6){
            switch ( position )
            {
                case 0:
                    fragment =  BusquedaDocentes.newInstance(position + 1);
                    break;
            }
        } else if (ConstantsUtils.getRol == 9) {
            switch (position) {
                case 0:
                    fragment = RedAdministrativa.newInstance(position + 1);
                    break;
                case 1:
                    fragment = ReemplazarPersonal.newInstance(position + 1);
                    break;
                case 2:
                    fragment = CambiarCargo.newInstance(position + 1);
                    break;
                case 3:
                    fragment =  ReporteAsistencia.newInstance(position + 1);
                    break;
                case 4:
                    fragment = ConsultaPersonal.newInstance(position + 1);
                    break;
            }
        }

        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

        /*fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();*/
    }

    @Override
    public void onSectionAttached(int number) {
        if (ConstantsUtils.getRol == 1){
            switch (number) {
                case 1:
                    mTitle = getString(R.string.title_section1);
                    break;
                case 2:
                    mTitle = getString(R.string.title_section2);
                    break;
                case 3:
                    mTitle = getString(R.string.title_section3);
                    break;
                case 4:
                    mTitle = getString( R.string.title_section4 );
                    break;
                case 5:
                    mTitle = getString( R.string.title_section5 );
                    break;
                case 6:
                    mTitle = getString( R.string.title_section6 );
                    break;

                case 7:
                    mTitle = getString( R.string.title_section7 );
                    break;

                case 8:
                    mTitle = getString( R.string.title_section8 );
                    break;

                case 9:
                    mTitle = getString( R.string.title_section9 );
                    break;
                case 10:
                    mTitle = getString(R.string.title_section11);
                    break;

            }
        } else if (ConstantsUtils.getRol == 2){
            switch (number) {
                case 1:
                    mTitle = getString(R.string.title_section1);
                    break;
                case 2:
                    mTitle = getString(R.string.title_section2);
                    break;
                case 3:
                    mTitle = getString(R.string.title_section3);
                    break;
                case 4:
                    mTitle = getString( R.string.title_section4 );
                    break;
                case 5:
                    mTitle = getString( R.string.title_section5 );
                    break;
                case 6:
                    mTitle = getString( R.string.title_section6 );
                    break;

                case 7:
                    mTitle = getString( R.string.title_section7 );
                    break;

                case 8:
                    mTitle = getString( R.string.title_section8 );
                    break;

                case 9:
                    mTitle = getString( R.string.title_section9 );
                    break;
                case 10:
                    mTitle = getString(R.string.title_section10);
                    break;
                case 11:
                    mTitle = getString(R.string.title_section11);
                    break;
            }
        } else if (ConstantsUtils.getRol == 6){
            switch (number) {
                case 1:
                    mTitle = getString(R.string.title_section11);
                    break;
            }
        } else if (ConstantsUtils.getRol == 9){
            switch (number) {
                case 1:
                    mTitle = getString(R.string.title_section_12);
                    break;
                case 2:
                    mTitle = getString(R.string.title_section_13);
                    break;
                case 3:
                    mTitle = getString(R.string.title_section_14);
                    break;
                case 4:
                    mTitle = getString(R.string.title_section_15);
                    break;
                case 5:
                    mTitle = getString(R.string.title_section_16);
                    break;
            }
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem settitng = menu.findItem(R.id.action_settings);
        if(ConstantsUtils.getRol == 2)
        {
            settitng.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_logout:
                logOut();
                break;
            case R.id.action_settings:
                settings();
                break;
            case R.id.action_sync:
                new SyncAsync( this ).execute();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void logOut() {
        Intent intent = new Intent( getApplicationContext(), Login.class );
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void settings(){
        Intent intent = new Intent(getApplicationContext(), Settings.class);
        startActivity(intent);
    }

    public void showDialog( String paramMessage ) {

        progressDialog = new ProgressDialog( MainActivity.this );
        progressDialog.setMessage( paramMessage );
        progressDialog.setIndeterminate( false );
        progressDialog.setCancelable( false );
        progressDialog.show();

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(ev);

        if ( view instanceof EditText )
        {
            View view1 = getCurrentFocus();

            int scrcoords[] = new int[2];

            view1.getLocationOnScreen( scrcoords );

            float x = ev.getRawX() + view1.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view1.getTop() - scrcoords[1];

            if ( ev.getAction() == MotionEvent.ACTION_UP && ( x < view1.getLeft() || x >= view1.getRight() || y < view1.getTop() || y > view1.getBottom() ) )
            {
                InputMethodManager imm = (InputMethodManager) getSystemService( Context.INPUT_METHOD_SERVICE );
                imm.hideSoftInputFromWindow( getWindow().getCurrentFocus().getWindowToken(), 0 );
            }

        }

        return ret;
    }

    @Override
    public void asistenciaLocal( String paramDNI ) {

        this.showDialog( "Buscando Docente!" );
        docentesE = docentesBL.asistenciaLocal( paramDNI );

        this.showPerson( docentesE );

    }

    @Override
    public void asistenciaAula( String paramDNI, Integer paramNroAula ) {

        this.showDialog( "Buscando Docente!" );
        docentesE = docentesBL.asistenciaAula( paramDNI, paramNroAula );

        this.showPerson( docentesE );
    }

    public void showPerson( DocentesE docentesE ) {

        progressDialog.dismiss();

        TextView txtDni = (TextView) findViewById( R.id.txtDNI );
        TextView txtApellidos = (TextView) findViewById( R.id.txtApellidos );
        TextView txtNombres = (TextView) findViewById( R.id.txtNombres );
        TextView txtLocalAplicacion = (TextView) findViewById( R.id.txtLocalAplicacion );
        TextView txtAula = (TextView) findViewById( R.id.txtAula );
        txtDni.setText( "" );
        txtApellidos.setText( "" );
        txtNombres.setText( "" );
        txtLocalAplicacion.setText( "" );
        txtAula.setText( "" );


        if ( docentesE.getStatus() == 0 || docentesE.getStatus() == 6 )
        {
            txtDni.setText( docentesE.getNro_doc() );
            txtApellidos.setText( docentesE.getApe_pat() + ' ' + docentesE.getApe_mat() );
            txtNombres.setText( docentesE.getNombres());
            txtAula.setText( docentesE.getAulaLocalE().getNro_aula().toString() );
            txtLocalAplicacion.setText( docentesE.getAulaLocalE().getLocalE().getNombreLocal() );
        }
        showMessageDocente(docentesE.getStatus());

    }

    public void showMessageDocente( Integer status ) {

        String msg = "";
        View view = findViewById(R.id.layout_datos);
        TextView textView = (TextView) findViewById(R.id.label_mensaje);
        switch ( status )
        {
            case 0:
                msg = getString(R.string.docente_register);
                view.setBackgroundColor(getResources().getColor(R.color.correct));
                textView.setVisibility(View.VISIBLE);
                textView.setTextColor(getResources().getColor(R.color.correct));
                textView.setText(msg);
                break;

            case 1:
                msg = getString( R.string.docente_not_found );
                view.setBackgroundColor(getResources().getColor(R.color.error));
                textView.setVisibility(View.VISIBLE);
                textView.setTextColor(getResources().getColor(R.color.error));
                textView.setText(msg);
                break;

            case 2:
                msg = getString( R.string.docente_not_access );
                view.setBackgroundColor(getResources().getColor(R.color.error));
                textView.setVisibility(View.VISIBLE);
                textView.setTextColor(getResources().getColor(R.color.error));
                textView.setText(msg);
                break;

            case 3:
                msg = getString( R.string.docente_not_register_local );
                view.setBackgroundColor(getResources().getColor(R.color.error));
                textView.setVisibility(View.VISIBLE);
                textView.setTextColor(getResources().getColor(R.color.error));
                textView.setText(msg);
                break;

            case 4:
                msg = getString( R.string.docente_not_register_aula);
                view.setBackgroundColor(getResources().getColor(R.color.error));
                textView.setVisibility(View.VISIBLE);
                textView.setTextColor(getResources().getColor(R.color.error));
                textView.setText(msg);
                break;

            case 5:
                msg = getString( R.string.docente_not_register_local );
                view.setBackgroundColor(getResources().getColor(R.color.error));
                textView.setVisibility(View.VISIBLE);
                textView.setTextColor(getResources().getColor(R.color.error));
                textView.setText(msg);
                break;

            case 6:
                msg = getString(R.string.docente_double_register);
                view.setBackgroundColor(getResources().getColor(R.color.warning));
                textView.setVisibility(View.VISIBLE);
                textView.setTextColor(getResources().getColor(R.color.warning));
                textView.setText(msg);
                break;

            case 7:
                msg = getString(R.string.docente_sync);
                view.setBackgroundColor(getResources().getColor(R.color.warning));
                ProgressDialog progressDialog = ProgressDialog.show(getApplicationContext(), "Sincronizando", msg);
                progressDialog.setMax(12000);
                progressDialog.dismiss();
                break;

            case 8:
                msg = getString(R.string.docente_aula_not_found);
                view.setBackgroundColor(getResources().getColor(R.color.error));
                textView.setVisibility(View.VISIBLE);
                textView.setTextColor(getResources().getColor(R.color.error));
                textView.setText(msg);
                break;
        }
    }

    @Override
    public void inventarioFicha(String paramCodFicha, Integer paramNroAula) {

        this.showDialog( "Buscando Ficha!" );
        instrumentoE = instrumentoBL.inventarioFicha( paramCodFicha, paramNroAula );

        this.showInstrumentoFicha( instrumentoE );

    }

    public void showInstrumentoFicha( InstrumentoE instrumentoE ) {

        progressDialog.dismiss();

        TextView txtFicha = (TextView) findViewById( R.id.txtFicha );
        TextView txtAula = (TextView) findViewById( R.id.txtAula );
        TextView txtLocalAplicacion = (TextView) findViewById( R.id.txtLocalAplicacion );

        txtFicha.setText( "" );
        txtLocalAplicacion.setText( "" );
        txtAula.setText( "" );

        if ( instrumentoE.getStatus() == 0 || instrumentoE.getStatus() == 4 )
        {
            txtFicha.setText( instrumentoE.getCod_ficha() );
            txtAula.setText( instrumentoE.getNro_aula().toString() );
            txtLocalAplicacion.setText( instrumentoE.getLocalE().getNombreLocal() );
        }

        showMessageInstrumento( instrumentoE.getStatus() );

    }

    @Override
    public void inventarioCuadernillo(String paramCodCuadernillo, Integer paramNroAula) {

        this.showDialog( "Buscando Cuadernillo!" );
        instrumentoE = instrumentoBL.inventarioCuadernillo( paramCodCuadernillo, paramNroAula );

        this.showInstrumentoCuadernillo( instrumentoE );

    }

    @Override
    public void asistenciaPersonal(String nroDni) {
        this.showDialog( "Buscando personal..." );
        personalE = personalBL.asistenciaPersonal(nroDni);
        this.showPersonal(personalE);

    }

    @Override
    public void reemplazarPersonal(String nroDni, String dniCambio, String nombreCambio) {
        personalBL.reemplazarPersonal(dni, dniCambio, nombreCambio);
        this.showPersonal(personalE);
    }

    @Override
    public void searchPersonalCambio(String nroDni) {
        this.showDialog( "Buscando personal..." );
        personalE = personalBL.searchPersonalCambio(nroDni);
        this.showPersonal(personalE);
        View layoutDatosCambio = findViewById(R.id.layout_datos_cambio);
        if ( personalE.getStatus() == 4) {
            layoutDatosCambio.setVisibility(View.VISIBLE);
        } else {
            layoutDatosCambio.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void registrarCambioCargo(String nroDni, String cargo) {
        this.showDialog( "Registrando personal..." );
        View view = findViewById(R.id.layout_datos_cambio);
        personalE = personalBL.registrarCambioCargo(nroDni, cargo);
        if (personalE.getStatus() == 0){
            view.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "Se cambio de cargo correctamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "No se pudo cambiar de cargo", Toast.LENGTH_SHORT).show();
        }
        this.showPersonal(personalE);
        progressDialog.dismiss();

    }

    @Override
    public void searchPersonalCambioCargo(String nroDni) {
        this.showDialog( "Buscando personal..." );
        View view = findViewById(R.id.layout_datos_cambio);
        personalE = personalBL.searchPersonalCambioCargo(nroDni);
        switch (personalE.getStatus()){
            case 13: case 4:
                view.setVisibility(View.VISIBLE);
                break;
            default:
                view.setVisibility(View.INVISIBLE);
                break;
        }
        this.showPersonal(personalE);
    }

    @Override
    public String searchNombreReserva(String nroDni) {
        return personalBL.searchNombreReserva(nroDni);
    }

    private void showPersonal(PersonalE personalE) {
        progressDialog.dismiss();

        TextView txtDni = (TextView) findViewById( R.id.txtDNI );
        TextView txtNombreCompleto = (TextView) findViewById( R.id.txt_nombre_completo );
        TextView txtLocalAplicacion = (TextView) findViewById( R.id.txtLocalAplicacion );
        TextView txtCargo = (TextView) findViewById( R.id.txt_cargo );
        txtDni.setText( "" );
        txtNombreCompleto.setText( "" );
        txtCargo.setText( "" );
        txtLocalAplicacion.setText( "" );
        dni = personalE.getDni();
        switch (personalE.getStatus()){
            case 0: case 4: case 6: case 8: case 9: case 11: case 12: case 13: case 14: case 15: case 16:
                txtDni.setText(dni);
                txtNombreCompleto.setText( personalE.getNombre_completo() );
                txtCargo.setText( personalE.getCargoE().getCargo());
                txtLocalAplicacion.setText( personalE.getLocalE().getNombreLocal() );
                break;
        }
        showMessagePersonal(personalE.getStatus());
    }

    private void showMessagePersonal(int status) {
        String msg = "";
        View view = findViewById(R.id.layout_datos);
        View layoutDatosCambio = findViewById(R.id.layout_datos_cambio);
        TextView textView = (TextView) findViewById(R.id.label_mensaje);
        TextView textViewDniCambio = (TextView) findViewById(R.id.txt_dni_cambio);
        TextView textViewNommbreCambio = (TextView) findViewById(R.id.txt_nombre_cambio);

        switch ( status )
        {
            case 0:
                msg = getString(R.string.personal_register);
                view.setBackgroundColor(getResources().getColor(R.color.correct));
                textView.setVisibility(View.VISIBLE);
                textView.setTextColor(getResources().getColor(R.color.correct));
                textView.setText(msg);
                break;

            case 1:
                msg = getString( R.string.personal_not_found );
                view.setBackgroundColor(getResources().getColor(R.color.error));
                textView.setVisibility(View.VISIBLE);
                textView.setTextColor(getResources().getColor(R.color.error));
                textView.setText(msg);
                break;

            case 2:
                msg = getString( R.string.personal_not_access );
                view.setBackgroundColor(getResources().getColor(R.color.error));
                textView.setVisibility(View.VISIBLE);
                textView.setTextColor(getResources().getColor(R.color.error));
                textView.setText(msg);
                break;

            case 4:
                msg = "Se encontro al personal";
                view.setBackgroundColor(getResources().getColor(R.color.correct));
                textView.setVisibility(View.VISIBLE);
                textView.setTextColor(getResources().getColor(R.color.correct));
                textView.setText(msg);
                break;

            case 3:
                msg = getString( R.string.personal_not_register_local );
                view.setBackgroundColor(getResources().getColor(R.color.error));
                textView.setVisibility(View.VISIBLE);
                textView.setTextColor(getResources().getColor(R.color.error));
                textView.setText(msg);
                break;

            case 5:
                msg = getString( R.string.personal_not_register_local );
                view.setBackgroundColor(getResources().getColor(R.color.error));
                textView.setVisibility(View.VISIBLE);
                textView.setTextColor(getResources().getColor(R.color.error));
                textView.setText(msg);
                break;

            case 6:
                msg = getString(R.string.personal_double_register);
                view.setBackgroundColor(getResources().getColor(R.color.warning));
                textView.setVisibility(View.VISIBLE);
                textView.setTextColor(getResources().getColor(R.color.warning));
                textView.setText(msg);
                break;

            case 7:
                msg = getString(R.string.personal_sync);
                view.setBackgroundColor(getResources().getColor(R.color.warning));
                ProgressDialog progressDialog = ProgressDialog.show(getApplicationContext(), "Sincronizando", msg);
                progressDialog.setMax(12000);
                progressDialog.dismiss();
                break;

            case 8:
                msg = getString(R.string.personal_correct);
                view.setBackgroundColor(getResources().getColor(R.color.correct));
                textView.setVisibility(View.VISIBLE);
                textView.setTextColor(getResources().getColor(R.color.correct));
                textView.setText(msg);
                break;

            case 9:
                msg = getString(R.string.personal_double_reemp);
                view.setBackgroundColor(getResources().getColor(R.color.warning));
                textView.setVisibility(View.VISIBLE);
                textView.setTextColor(getResources().getColor(R.color.warning));
                textView.setText(msg);
                break;

            case 10:
                Toast.makeText(this.getApplicationContext(), "Se reemplazó correctamente al personal", Toast.LENGTH_SHORT).show();
                textView.setVisibility(View.INVISIBLE);
                textViewDniCambio.setText("");
                textViewNommbreCambio.setText("");
                break;

            case 11:
                Toast.makeText(this.getApplicationContext(), "No se reemplazó al personal", Toast.LENGTH_SHORT).show();
                textView.setVisibility(View.INVISIBLE);
                textViewDniCambio.setText("");
                textViewNommbreCambio.setText("");
                break;

            case 12:
                msg = "Este personal no puede reemplazar";
                view.setBackgroundColor(getResources().getColor(R.color.error));
                textView.setVisibility(View.VISIBLE);
                textView.setTextColor(getResources().getColor(R.color.error));
                textView.setText(msg);
                layoutDatosCambio.setVisibility(View.INVISIBLE);
                textViewDniCambio.setText("");
                textViewNommbreCambio.setText("");
                break;

            case 13:
                msg = "El personal de reemplazo es: ";
                view.setBackgroundColor(getResources().getColor(R.color.warning));
                textView.setVisibility(View.VISIBLE);
                textView.setTextColor(getResources().getColor(R.color.warning));
                textView.setText(msg);
                break;

            case 14:
                msg = "No se puede cambiar de cargo a este personal: ";
                view.setBackgroundColor(getResources().getColor(R.color.warning));
                textView.setVisibility(View.VISIBLE);
                textView.setTextColor(getResources().getColor(R.color.warning));
                textView.setText(msg);
                layoutDatosCambio.setVisibility(View.INVISIBLE);
                break;

            case 15:
                msg = "No se puede reeemplazar a este personal: ";
                view.setBackgroundColor(getResources().getColor(R.color.warning));
                textView.setVisibility(View.VISIBLE);
                textView.setTextColor(getResources().getColor(R.color.warning));
                textView.setText(msg);
                textViewDniCambio.setText("");
                textViewNommbreCambio.setText("");
                break;

            case 16:
                msg = "Este personal ya fue reemplazado";
                view.setBackgroundColor(getResources().getColor(R.color.warning));
                textView.setVisibility(View.VISIBLE);
                textView.setTextColor(getResources().getColor(R.color.warning));
                textView.setText(msg);
                break;

            case 17:
                msg = "El personal de reserva no ha marcado asistencia";
                view.setBackgroundColor(getResources().getColor(R.color.warning));
                textView.setVisibility(View.VISIBLE);
                textView.setTextColor(getResources().getColor(R.color.warning));
                textView.setText(msg);
                textViewDniCambio.setText("");
                textViewNommbreCambio.setText("");
                break;

            case 18:
                msg = "El personal no ha marcado asistencia";
                view.setBackgroundColor(getResources().getColor(R.color.warning));
                textView.setVisibility(View.VISIBLE);
                textView.setTextColor(getResources().getColor(R.color.warning));
                textView.setText(msg);
                break;
        }
    }

    public void showInstrumentoCuadernillo( InstrumentoE instrumentoE ) {

        progressDialog.dismiss();

        TextView txtCuadernillo = (TextView) findViewById( R.id.txtCuadernillo );
        TextView txtAula = (TextView) findViewById( R.id.txtAula );
        TextView txtLocalAplicacion = (TextView) findViewById( R.id.txtLocalAplicacion );

        txtCuadernillo.setText( "" );
        txtLocalAplicacion.setText( "" );
        txtAula.setText( "" );

        if ( instrumentoE.getStatus() == 0 || instrumentoE.getStatus() == 4 )
        {
            txtCuadernillo.setText( instrumentoE.getCod_cartilla() );
            txtAula.setText( instrumentoE.getNro_aula().toString() );
            txtLocalAplicacion.setText( instrumentoE.getLocalE().getNombreLocal() );
        }
         showMessageInstrumento( instrumentoE.getStatus() );
    }

    public void showMessageInstrumento( Integer status ) {

        String msg = "";
        View view = findViewById(R.id.layout_datos);
        TextView textView = (TextView) findViewById(R.id.label_mensaje);
        switch ( status ) {
            case 0:
                msg = getString(R.string.instrumento_register);
                view.setBackgroundColor(getResources().getColor(R.color.correct));
                textView.setVisibility(View.VISIBLE);
                textView.setTextColor(getResources().getColor(R.color.correct));
                textView.setText(msg);
                break;

            case 1:
                msg = getString(R.string.instrumento_not_found);
                view.setBackgroundColor(getResources().getColor(R.color.error));
                textView.setVisibility(View.VISIBLE);
                textView.setTextColor(getResources().getColor(R.color.error));
                textView.setText(msg);
                break;

            case 2:
                msg = getString(R.string.instrumento_not_access);
                view.setBackgroundColor(getResources().getColor(R.color.error));
                textView.setVisibility(View.VISIBLE);
                textView.setTextColor(getResources().getColor(R.color.error));
                textView.setText(msg);
                break;

            case 3:
                msg = getString(R.string.instrumento_not_register);
                view.setBackgroundColor(getResources().getColor(R.color.error));
                textView.setVisibility(View.VISIBLE);
                textView.setTextColor(getResources().getColor(R.color.error));
                textView.setText(msg);
                break;

            case 4:
                msg = getString(R.string.instrumento_double_register);
                view.setBackgroundColor(getResources().getColor(R.color.warning));
                textView.setVisibility(View.VISIBLE);
                textView.setTextColor(getResources().getColor(R.color.warning));
                textView.setText(msg);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 )
        {
            return true;
        }
        return super.onKeyDown( keyCode, event );
    }

}