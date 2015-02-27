package ordanel.ednom;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ordanel.ednom.Business.DocentesBL;
import ordanel.ednom.Business.InstrumentoBL;
import ordanel.ednom.Entity.DocentesE;
import ordanel.ednom.Entity.InstrumentoE;
import ordanel.ednom.Fragments.AsistenciaAula;
import ordanel.ednom.Fragments.BusquedaDocentes;
import ordanel.ednom.Fragments.IngresoLocal;
import ordanel.ednom.Fragments.InventarioCuadernillo;
import ordanel.ednom.Fragments.InventarioFicha;
import ordanel.ednom.Fragments.ListadoAsistenciaAula;
import ordanel.ednom.Fragments.ListadoIngresoLocal;
import ordanel.ednom.Fragments.ListadoInventarioCuadernillo;
import ordanel.ednom.Fragments.ListadoInventarioFicha;
import ordanel.ednom.Fragments.ReporteGeneral;
import ordanel.ednom.Fragments.Welcome;
import ordanel.ednom.Interfaces.MainI;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, MainI {

    ProgressDialog progressDialog;
    DocentesE docentesE;
    DocentesBL docentesBL;
    InstrumentoE instrumentoE;
    InstrumentoBL instrumentoBL;

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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if ( id == R.id.action_logout )
        {
            logOut();
        }

        return super.onOptionsItemSelected(item);
    }

    public void logOut() {
        Intent intent = new Intent( getApplicationContext(), Login.class );
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                msg = "sincronizando, espere unos segundos";
                view.setBackgroundColor(getResources().getColor(R.color.warning));
                textView.setVisibility(View.VISIBLE);
                textView.setTextColor(getResources().getColor(R.color.warning));
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