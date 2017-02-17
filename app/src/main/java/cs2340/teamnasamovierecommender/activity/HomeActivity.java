package cs2340.teamnasamovierecommender.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import cs2340.teamnasamovierecommender.R;
import cs2340.teamnasamovierecommender.pojo.PagerAdapter;

/**
 * <b>!!! Currently a work in progress... !!!</b>
 * <p/>
 * This class handles the home screen of the app, the screen that the user sees after a successful
 * login or registration.
 *
 * @author Anas Tahir Khan
 * @author Anuragsharma Venukadasula
 * @author Sai Srivatsav Muppiri
 * @author Sayed Nafiz Imtiaz Ali
 * @version 3.0.2
 * @since 4/18/2016
 */
public class HomeActivity extends AppCompatActivity {

    static ViewPager viewPager;
    SharedPreferences sharedPreferences;

    /**
     * Perform initialization of all fragments and loaders.
     *
     * @param savedInstanceState Bundle: If the activity is being re-initialized after previously
     *                           being shut down then this Bundle contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle).
     *                           <b>Note: Otherwise it is null.</b>
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        Toast.makeText(getApplicationContext(), "Successfully logged in.", Toast.LENGTH_SHORT).show();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        final String username = sharedPreferences.getString("Username", "userDefault");

        Log.d("username", username);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        if (toolbar != null) {
            toolbar.setTitle("New & Upcoming");
        } else {
            Log.d("toolbar", "toolbar is null");
        }
        setSupportActionBar(toolbar);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        if (tabLayout != null) {
            tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_local_movies_selector));
            tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_thumb_up_selector));
            tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_star_selector));
            tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_search_selector));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

            viewPager = (ViewPager) findViewById(R.id.view_pager);
            final PagerAdapter adapter = new PagerAdapter
                    (getSupportFragmentManager(), tabLayout.getTabCount());
            if (viewPager != null) {
                viewPager.setAdapter(adapter);
                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    /**
                     * Called when a tab that is already selected is chosen again by the user.
                     *
                     * @param tab TabLayout.Tab: The tab that was reselected.
                     */
                    @Override
                    public void onTabReselected(final TabLayout.Tab tab) {
                    }

                    /**
                     * Called when a tab enters the selected state.
                     *
                     * @param tab TabLayout.Tab: The tab that was selected.
                     */
                    @Override
                    public void onTabSelected(final TabLayout.Tab tab) {
                        switch (tab.getPosition()) {
                            case 0:
                                viewPager.setCurrentItem(0);
                                if (toolbar != null) {
                                    toolbar.setTitle("New & Upcoming");
                                } else {
                                    Log.d("toolbar", "toolbar title could not be changed");
                                }
                                break;
                            case 1:
                                viewPager.setCurrentItem(1);
                                if (toolbar != null) {
                                    toolbar.setTitle("Recommendations");
                                } else {
                                    Log.d("toolbar", "toolbar title could not be changed");
                                }
                                break;
                            case 2:
                                viewPager.setCurrentItem(2);
                                if (toolbar != null) {
                                    toolbar.setTitle("Ratings");
                                } else {
                                    Log.d("toolbar", "toolbar title could not be changed");
                                }
                                break;
                            case 3:
                                viewPager.setCurrentItem(3);
                                if (toolbar != null) {
                                    toolbar.setTitle("Search");
                                } else {
                                    Log.d("toolbar", "toolbar title could not be changed");
                                }
                                break;
                            default:
                                viewPager.setCurrentItem(tab.getPosition());
                                if (toolbar != null) {
                                    toolbar.setTitle("Tab " + String.valueOf(tab.getPosition()));
                                } else {
                                    Log.d("toolbar", "toolbar title could not be changed");
                                }
                                break;
                        }
                        viewPager.setCurrentItem(tab.getPosition());
                    }

                    /**
                     * Called when a tab exits the selected state.
                     *
                     * @param tab TabLayout.Tab: The tab that was unselected.
                     */
                    @Override
                    public void onTabUnselected(final TabLayout.Tab tab) {
                    }
                });
            } else {
                Log.d("viewPager", "viewPager is null");
            }
        } else {
            Log.d("tabLayout", "tabLayout is null");
        }
    }

    /**
     * This hook is called whenever an item in the options menu is selected.
     *
     * @param item MenuItem: The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to proceed, true to consume it
     * here.
     */
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();
        if (id == R.id.action_logout) {
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        }

        if (id == R.id.action_account) {
            startActivity(new Intent(this, AccountActivity.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }
        return id == R.id.action_account || super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
    }

}
