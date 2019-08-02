package com.task.notificationapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.task.notificationapp.utils.NotificationUtil;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        ScreenSlidePageFragment.OnButtonClickedListener,
        ViewPager.OnPageChangeListener {

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    private static int mPageNum = 0;
    private boolean isBackToExitClickedTwice;
    private ArrayList<ScreenSlidePageFragment> mFragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();

        mPager = findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), mFragmentList);
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(this);

        Intent intent = getIntent();
        if (intent != null &&
                intent.hasExtra(NotificationUtil.NOTIFICATION_NUMBER)) {
            int number = intent.getIntExtra(NotificationUtil.NOTIFICATION_NUMBER, 1);
            if (mFragmentList.size() == 0) {
                for(int i = 0; i < number; i++) {
                    addFragment();
                }
            } else {
                mPager.setCurrentItem(number - 1);
            }
        } else {
            addFragment();
        }
    }

    @Override
    public void onButtonClicked(View view) {
        switch (view.getId()) {
            case R.id.button_notification:
                NotificationUtil.notifyUser(this, mPager.getCurrentItem() + 1);
                break;
            case R.id.button_decrease:
                removeFragment();
                break;
            case R.id.button_increase:
                addFragment();
                break;
        }
    }

    public void addFragment() {
        if (mFragmentList.size() >= 1) {
            mFragmentList.add(new ScreenSlidePageFragment(++mPageNum, true));
        } else {
            mFragmentList.add(new ScreenSlidePageFragment(++mPageNum, false));
        }
        mPagerAdapter.notifyDataSetChanged();
        mPager.setCurrentItem(mFragmentList.size() - 1);
    }

    public void removeFragment() {
        mFragmentList.remove(--mPageNum);
        mPagerAdapter.notifyDataSetChanged();
        if (mPageNum == 1) {
            mFragmentList.get(0).setRemoveBtnAvailable(false);
        }
        mPager.setCurrentItem(mFragmentList.size() - 1);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0 && mFragmentList.size() > 1) {
            mFragmentList.get(0).setRemoveBtnAvailable(true);
        } else if (position == 0 && mFragmentList.size() == 1) {
            mFragmentList.get(0).setRemoveBtnAvailable(false);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int number = intent.getIntExtra(NotificationUtil.NOTIFICATION_NUMBER, 1);
        mPager.setCurrentItem(number - 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPageNum = 0;
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            if (isBackToExitClickedTwice) {
                super.onBackPressed();
                return;
            }

            isBackToExitClickedTwice = true;
            Snackbar.make(mPager, "Please, click BACK again to exit", Snackbar.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isBackToExitClickedTwice = false;
                }
            }, 2000);
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        private ArrayList<ScreenSlidePageFragment> fragmentList;

        public ScreenSlidePagerAdapter(FragmentManager fm, ArrayList<ScreenSlidePageFragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
