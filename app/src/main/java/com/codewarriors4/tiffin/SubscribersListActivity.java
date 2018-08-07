package com.codewarriors4.tiffin;

import android.app.Activity;
import android.content.Intent;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.codewarriors4.tiffin.adapters.SubscribersListAdapter;
import com.codewarriors4.tiffin.models.SubscribersListModel;
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
    private RecyclerView recyclerView;
    private String listString;
    private boolean isLoaded = false;
    private SessionUtli sessionUtli;
    ArrayList<SubscribersListModel> lists;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscribers_list_layout);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.subscribers_Recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //tabLayout = findViewById(R.id.tab_layout);
       // viewPager = findViewById(R.id.view_pager);
        //setupViewPager(viewPager);
        //tabLayout.setupWithViewPager(viewPager);
        sessionUtli = SessionUtli.getSession(getSharedPreferences(Constants.SHAREDPREFERNCE, Activity.MODE_PRIVATE));
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        new SubscribersList().execute("");

    }

//    private void setupViewPager(ViewPager viewPager) {
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        SubscribersTab_Fragment subscribersTab_fragment = new SubscribersTab_Fragment();
//        SubscribersTab_Fragment subscribersTab_fragment2 = new SubscribersTab_Fragment();
//        SubscribersTab_Fragment subscribersTab_fragment3 = new SubscribersTab_Fragment();
//
//        adapter.addFragment(subscribersTab_fragment, "Monthly");
//        adapter.addFragment(subscribersTab_fragment2, "Daily");
//        adapter.addFragment(subscribersTab_fragment3, "All");
//        viewPager.setOffscreenPageLimit(2);
//        viewPager.setAdapter(adapter);
//    }



//    class ViewPagerAdapter extends FragmentPagerAdapter {
//        private final List<Fragment> mFragmentList = new ArrayList<>();
//        private final List<String> mFragmentTitleList = new ArrayList<>();
//
//        public ViewPagerAdapter(FragmentManager manager) {
//            super(manager);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return mFragmentList.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return mFragmentList.size();
//        }
//
//        public void addFragment(Fragment fragment, String title) {
//            Bundle bundle = new Bundle();
//            switch (title){
//                case "Monthly":
//                    bundle.putString(SubscribersTab_Fragment.KEY, Constants.GETSUBSCRIBERSMONTHLY );
//                    fragment.setArguments(bundle);
//                    break;
//                case "Daily":
//                    bundle.putString(SubscribersTab_Fragment.KEY, Constants.GETSUBSCRIBERSDAILY );
//                    fragment.setArguments(bundle);
//                    break;
//                case "All":
//                    bundle.putString(SubscribersTab_Fragment.KEY, Constants.GETSUBSCRIBERS );
//                    fragment.setArguments(bundle);
//                    break;
//            }
//            mFragmentList.add(fragment);
//            mFragmentTitleList.add(title);
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return mFragmentTitleList.get(position);
//        }
//    }

    private String getSubscribersList(String string){
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setEndPoint(Constants.BASE_URL + Constants.GETSUBSCRIBERS);
        requestPackage.setMethod("POST");
        requestPackage.setHeader("Authorization", "Bearer " +sessionUtli.getValue("access_token"));
        requestPackage.setHeader("Accept", "application/json; q=0.5");
        try {
            return HttpHelper.downloadFromFeed(requestPackage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private class SubscribersList extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                String homeMakerList = getSubscribersList(strings[0]);
                return homeMakerList;
            } catch (Exception e) {
                return e.getMessage();
            }


        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPreExecute() {


        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            initValues(aVoid);
        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initValues(String subscribersListStr)
    {
        try {

            JSONArray subscribersList =  new JSONArray(subscribersListStr);
            lists = new ArrayList<>();
            try {
                for(int i = 0; i < subscribersList.length() ; i++){

                    JSONObject subscriber = subscribersList.getJSONObject(i);
                    String firstName = subscriber.getString("UserFname");
                    String lastName = subscriber.getString("UserLname");
                    String email = subscriber.getString("email");
                    String street = subscriber.getString("UserStreet");
                    String phoneNumber = subscriber.getString("UserPhone");
                    String cost = subscriber.getString("SubCost");
                    String packageName = subscriber.getString("HMPName");
                    String packageDesc = subscriber.getString("HMPDesc");
                    String startDate = subscriber.getString("SubStartDate").split(" ")[0];
                    String endDate = subscriber.getString("SubEndDate").split(" ")[0];

                    lists.add(new SubscribersListModel(firstName + " " +lastName,
                            email, street, phoneNumber, cost, packageName, packageDesc, startDate, endDate));
                }

                SubscribersListAdapter subscribersListAdapter = new SubscribersListAdapter(this, lists);
                recyclerView.setAdapter(subscribersListAdapter);
                subscribersListAdapter.setOnItemClickListener(new SubscribersListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent intent = new Intent(SubscribersListActivity.this, SubscriptionFullView.class);
                        intent.putExtra("Example Item", lists.get(position));

                        startActivity(intent);
                    }
                });

            } catch (JSONException e) {


            }

            //homeMakerProgressList.setVisibility(View.GONE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



































}
