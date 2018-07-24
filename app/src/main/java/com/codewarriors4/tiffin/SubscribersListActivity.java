package com.codewarriors4.tiffin;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.codewarriors4.tiffin.utils.Constants;
import com.codewarriors4.tiffin.utils.HttpHelper;
import com.codewarriors4.tiffin.utils.RequestPackage;
import com.codewarriors4.tiffin.utils.SessionUtli;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SubscribersListActivity extends AppCompatActivity
{
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscribers_list_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        SubscribersTab_Fragment subscribersTab_fragment = new SubscribersTab_Fragment();
        SubscribersTab_Fragment subscribersTab_fragment2 = new SubscribersTab_Fragment();
        SubscribersTab_Fragment subscribersTab_fragment3 = new SubscribersTab_Fragment();

        adapter.addFragment(subscribersTab_fragment, "Monthly");
        adapter.addFragment(subscribersTab_fragment2, "Daily");
        adapter.addFragment(subscribersTab_fragment3, "All");
        viewPager.setOffscreenPageLimit(2);
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
            Bundle bundle = new Bundle();
            switch (title){
                case "Monthly":
                    bundle.putString(SubscribersTab_Fragment.KEY, Constants.GETSUBSCRIBERSMONTHLY );
                    fragment.setArguments(bundle);
                    break;
                case "Daily":
                    bundle.putString(SubscribersTab_Fragment.KEY, Constants.GETSUBSCRIBERSDAILY );
                    fragment.setArguments(bundle);
                    break;
                case "All":
                    bundle.putString(SubscribersTab_Fragment.KEY, Constants.GETSUBSCRIBERS );
                    fragment.setArguments(bundle);
                    break;
            }
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }




































}
