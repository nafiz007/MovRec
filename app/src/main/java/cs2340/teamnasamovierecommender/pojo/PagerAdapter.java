package cs2340.teamnasamovierecommender.pojo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import cs2340.teamnasamovierecommender.fragment.NewUpcomingFragment;
import cs2340.teamnasamovierecommender.fragment.RatingsFragment;
import cs2340.teamnasamovierecommender.fragment.RecommendationsFragment;
import cs2340.teamnasamovierecommender.fragment.SearchFragment;

/**
 * @author Anas Tahir Khan
 * @author Anuragsharma Venukadasula
 * @author Sai Srivatsav Muppiri
 * @author Sayed Nafiz Imtiaz Ali
 * @version 3.0.2
 * @since 4/18/2016
 */
public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    /**
     * Constructor.
     *
     * @param fm FragmentManager:
     * @param NumOfTabs int: number of items.
     */
    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    /**
     * Return the Fragment associated with the specified position (tab).
     *
     * @param position int: position of the tab.
     * @return Fragment: fragment associated with the tab.
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new NewUpcomingFragment();
            case 1:
                return new RecommendationsFragment();
            case 2:
                return new RatingsFragment();
            case 3:
                return new SearchFragment();
            default:
                return null;
        }
    }

    /**
     * Return the number of views (tabs/fragments) available.
     *
     * @return int: number of tabs/fragments.
     */
    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}
