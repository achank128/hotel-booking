package com.turquoise.hotelbookrecomendation.Activities;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.turquoise.hotelbookrecomendation.Fragments.Home;
import com.turquoise.hotelbookrecomendation.Fragments.Recommendation;
import com.turquoise.hotelbookrecomendation.R;
import com.turquoise.hotelbookrecomendation.model.Booking;
import com.turquoise.hotelbookrecomendation.model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Serializable {

    private static Toolbar toolbar;
    private TextView txtUsername;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static Booking bookings = new Booking();
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        txtUsername = findViewById(R.id.username);
        if(getIntent().getExtras() != null){
            user = (User) getIntent().getExtras().get("user");
            txtUsername.setText(user.getUsername());
        }


        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("NKC");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }


    private void setupViewPager(final ViewPager viewPager) {
        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFrag(new Home(), "Home");
        viewPagerAdapter.addFrag(new Recommendation(), "Booking");
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    ((Recommendation) viewPagerAdapter.getItem(position)).updateList();
                } else {
                    ((Home) viewPagerAdapter.getItem(position)).updateList();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        viewPager.setAdapter(viewPagerAdapter);
    }


    public static void updatec(int n) {
        int cur = Integer.valueOf(((TextView) toolbar.findViewById(R.id.cartCount)).getText().toString());
        ((TextView) toolbar.findViewById(R.id.cartCount)).setText(String.valueOf(cur + n));
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public User getUser() {
        return user;
    }
}
