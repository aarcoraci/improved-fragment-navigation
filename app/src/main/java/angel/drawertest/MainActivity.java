package angel.drawertest;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import angel.drawertest.fragments.MyAppFragment;
import angel.drawertest.infrastructure.AppSection;
import angel.drawertest.infrastructure.MyAppFragmentFactory;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentManager.OnBackStackChangedListener {

    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // let the fragment manager know that MainActivity will be listening to events
        getSupportFragmentManager().addOnBackStackChangedListener(this);

        /*
         AppBar setup
         */
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                    onBackPressed();
            }
        });
        /*
            app should start on the home section
            notice we won't be adding it to the back stack. Pressing back
            should exist the app
         */
        navigateToSection(AppSection.INBOX, false);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_sent) {
            navigateToSection(AppSection.SENT, true);
        } else if (id == R.id.nav_trash) {
            navigateToSection(AppSection.TRASH, true);
        } else if (id == R.id.nav_inbox) {
            navigateToSection(AppSection.INBOX, false);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Navigates to a fragment within the app
     *
     * @param section    where to go
     * @param addToStack should this transaction be added to the back stack ?
     */
    public void navigateToSection(AppSection section, boolean addToStack) {

        // obtain the fragment we want to navigate to
        MyAppFragment fragment = MyAppFragmentFactory.getFragment(section);

        // if this is a root fragment, clear everything up to the home and add this one
        if (fragment.getIsRootSection())
            getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_content, fragment, fragment.getFragmentTag());
        if (addToStack)
            fragmentTransaction.addToBackStack(fragment.getFragmentTag());
        fragmentTransaction.commit();
    }

    @Override
    public void onBackStackChanged() {
        MyAppFragment currentFragment = getCurrentFragment();
        if (currentFragment != null) {

            // since we are here, we can update the selected item on the sidebar
            setNavigationViewCheckedItem(currentFragment);

            if (currentFragment.getIsRootSection()) {
                showRootNavigation();
            } else {
                // this is not a "root" fragment. Use back arrow navigation on the app bar
                showBackArrowNavigation();
            }
        }
    }

    /**
     * Updates the sidebar according to a specific fragment
     * @param fragment fragment being shown
     */
    private void setNavigationViewCheckedItem(MyAppFragment fragment){
        if(fragment.getFragmentTag().equals(AppSection.INBOX.toString()))
            navigationView.setCheckedItem(R.id.nav_inbox);
        else if(fragment.getFragmentTag().equals(AppSection.SENT.toString()))
            navigationView.setCheckedItem(R.id.nav_sent);
        else if(fragment.getFragmentTag().equals(AppSection.TRASH.toString()))
            navigationView.setCheckedItem(R.id.nav_trash);
    }

    /**
     * Get the current visible MyAppFragment.
     *
     * @return current visible MyAppFragment. null if the stack is empty or fragment is not MyAppFragment
     */
    private MyAppFragment getCurrentFragment() {
        List<Fragment> fragmentStack = getSupportFragmentManager().getFragments();
        if (fragmentStack == null || fragmentStack.size() == 0)
            return null;

        for (Fragment current : fragmentStack) {
            if (current != null && current.isVisible() && (current instanceof MyAppFragment))
                return (MyAppFragment) current;
        }

        return null;
    }

    //region application bar

    /**
     * Display the hamburger icon on the application bar
     */
    private void showRootNavigation() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    /**
     * Display the back arrow on the application bar
     */
    private void showBackArrowNavigation() {
        // order of these calls IS important
        toggle.setDrawerIndicatorEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }
    //endregion
}
