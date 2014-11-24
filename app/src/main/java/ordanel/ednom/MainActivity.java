package ordanel.ednom;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
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

import ordanel.ednom.Business.LocalBL;
import ordanel.ednom.Entity.DocentesE;
import ordanel.ednom.Fragments.AsistenciaAula;
import ordanel.ednom.Fragments.IngresoLocal;
import ordanel.ednom.Fragments.Welcome;
import ordanel.ednom.Interfaces.MainI;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, MainI {

    ProgressDialog progressDialog;

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
                fragment = new Welcome().newInstance( position + 1 );
                break;

            case 1:
                fragment = new IngresoLocal().newInstance( position + 1 );
                break;

            case 2:
                fragment = new AsistenciaAula().newInstance( position + 1 );
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
        if (id == R.id.action_settings) {
            return true;
        }
        else if ( id == R.id.action_logout )
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
    public void searchPerson( String conditional ) {

        this.showDialog( "Buscando Docente!" );
        DocentesE docentesE = new LocalBL( MainActivity.this ).searchPerson( conditional );

        this.showPerson( docentesE );

    }

    @Override
    public void showPerson( DocentesE docentesE ) {

        progressDialog.dismiss();

        TextView txtDni = (TextView) findViewById( R.id.txtDNI );
        TextView txtApePaterno = (TextView) findViewById( R.id.txtApePaterno );
        TextView txtApeMaterno = (TextView) findViewById( R.id.txtApeMaterno );
        TextView txtNombres = (TextView) findViewById( R.id.txtNombres );
        TextView txtLocalAplicacion = (TextView) findViewById( R.id.txtLocalAplicacion );
        TextView txtAula = (TextView) findViewById( R.id.txtAula );

        txtDni.setText( "" );
        txtApePaterno.setText( "" );
        txtApeMaterno.setText( "" );
        txtNombres.setText( "" );
        txtLocalAplicacion.setText( "" );
        txtAula.setText( "" );

        if ( docentesE.getStatus() == 0 )
        {
            txtDni.setText( docentesE.getNro_doc().toString() );
            txtApePaterno.setText( docentesE.getApe_pat().toString() );
            txtApeMaterno.setText( docentesE.getApe_mat().toString() );
            txtNombres.setText( docentesE.getNombres().toString() );

            txtAula.setText( docentesE.getAulaLocalE().getNro_aula().toString() );
        }
        else
        {

            String msg = "";

            switch ( docentesE.getStatus() )
            {
                case 1:
                    msg = getString( R.string.docente_not_found );
                    break;

                case 2:
                    msg = getString( R.string.docente_not_access );
                    break;

                case 3:
                    msg = getString( R.string.docente_not_register );
                    break;
            }

            Toast.makeText( MainActivity.this, msg, Toast.LENGTH_LONG ).show();
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