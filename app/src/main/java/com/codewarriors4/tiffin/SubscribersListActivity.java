package com.codewarriors4.tiffin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SubscribersListActivity extends FragmentActivity
{
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscribers_list_layout);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Bundle bundle = new Bundle();
        bundle.putString(SubscribersTab_Fragment.KEY, "Monthly");
        Bundle bundle2 = new Bundle();
        bundle.putString(SubscribersTab_Fragment.KEY, "Daily");
        Bundle bundle3 = new Bundle();
        bundle.putString(SubscribersTab_Fragment.KEY, "All");

        SubscribersTab_Fragment subscribersTab_fragment = new SubscribersTab_Fragment();
        subscribersTab_fragment.setArguments(bundle);

        SubscribersTab_Fragment subscribersTab_fragment2 = new SubscribersTab_Fragment();
        subscribersTab_fragment.setArguments(bundle2);

        SubscribersTab_Fragment subscribersTab_fragment3 = new SubscribersTab_Fragment();
        subscribersTab_fragment.setArguments(bundle3);

        adapter.addFragment(subscribersTab_fragment, "Monthly");
        adapter.addFragment(subscribersTab_fragment2, "Daily");
        adapter.addFragment(subscribersTab_fragment3, "All");

        viewPager.setAdapter(adapter);
    }



    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
