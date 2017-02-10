package com.v7ench.kiyo;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class PagerActivity extends AppCompatActivity {


    SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    FrameLayout frameLayout;
    Button mNextBtn;
    Button mSkipBtn, mFinishBtn;

    ImageView zero, one, two,three;
    ImageView[] indicators;

    int lastLeftValue = 0;

    CoordinatorLayout mCoordinator;


    static final String TAG = "PagerActivity";

    int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        }

        setContentView(R.layout.activity_pager);


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mNextBtn = (Button) findViewById(R.id.intro_btn_next);


        mSkipBtn = (Button) findViewById(R.id.intro_btn_skip);
        mFinishBtn = (Button) findViewById(R.id.intro_btn_finish);

        zero = (ImageView) findViewById(R.id.intro_indicator_0);
        one = (ImageView) findViewById(R.id.intro_indicator_1);
        two = (ImageView) findViewById(R.id.intro_indicator_2);
        three = (ImageView) findViewById(R.id.intro_indicator_3);

        mCoordinator = (CoordinatorLayout) findViewById(R.id.main_content);

frameLayout =(FrameLayout) findViewById(R.id.frameme);
        indicators = new ImageView[]{zero, one, two, three};

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setCurrentItem(page);
        updateIndicators(page);

        final int color1 = ContextCompat.getColor(this, R.color.color1);
        final int color2 = ContextCompat.getColor(this, R.color.color2);
        final int color3 = ContextCompat.getColor(this, R.color.color3);
        final int color4 = ContextCompat.getColor(this, R.color.color4);
        final int color11 = ContextCompat.getColor(this, R.color.color11);
        final int color22 = ContextCompat.getColor(this, R.color.color22);
        final int color33 = ContextCompat.getColor(this, R.color.color33);
        final int color44 = ContextCompat.getColor(this, R.color.color44);
        final int[] colorList = new int[]{color1, color2, color3, color4};

        final ArgbEvaluator evaluator = new ArgbEvaluator();

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                /*
                color update
                 */
                int colorUpdate = (Integer) evaluator.evaluate(positionOffset, colorList[position], colorList[position == 3 ? position : position + 1]);
                mViewPager.setBackgroundColor(colorUpdate);

            }

            @Override
            public void onPageSelected(int position) {

                page = position;

                updateIndicators(page);

                switch (position) {
                    case 0:
                        mViewPager.setBackgroundColor(color1);
                        frameLayout.setBackgroundColor(color11);
                        break;
                    case 1:
                        mViewPager.setBackgroundColor(color2);
                        frameLayout.setBackgroundColor(color22);

                        break;
                    case 2:
                        mViewPager.setBackgroundColor(color3);
                        frameLayout.setBackgroundColor(color33);

                        break;
                    case 3:
                        mViewPager.setBackgroundColor(color4);
                        frameLayout.setBackgroundColor(color44);

                        break;
                }


                mNextBtn.setVisibility(position == 3 ? View.GONE : View.VISIBLE);
                mFinishBtn.setVisibility(position == 3 ? View.VISIBLE : View.GONE);


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page += 1;
                mViewPager.setCurrentItem(page, true);
            }
        });

        mSkipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        mFinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //  update 1st time pref
                Utils.saveSharedSetting(PagerActivity.this, SignActivity.PREF_USER_FIRST_TIME, "false");

            }
        });

    }

    void updateIndicators(int position) {
        for (int i = 0; i < indicators.length; i++) {
            indicators[i].setBackgroundResource(
                    i == position ? R.drawable.indicator_selected : R.drawable.indicator_unselected
            );
        }
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        ImageView img;

        int[] bgs = new int[]{R.drawable.rev1, R.drawable.rev2, R.drawable.rev3, R.drawable.rev4};
        String[] sliden2 =new String[]{"Welcome to Alpha Safe","Result Accuracy Guaranteed","Specilized Lab Test","Join us to bring a CHANGE"};

        String[] sliden =new String[]{"Ensure your instruments are clean and get certified\n" +
                "by the industry experts. Introducing Alpha Safe Clinic.","Scan Test Strips and get 99% result accuracy.\n" +
                "Complete sterilization guaranteed.","Scan, test and send BI strip to get them\n" +
                "tested at the lab by the specialists.","Be a part of ALPHA SAFE Family and guarantee\n" +
                "complete peace of mind to your patients."};
        public PlaceholderFragment() {
        }


        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_pager, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label2);
            TextView textView2 = (TextView) rootView.findViewById(R.id.section_label1);
            textView2.setText(sliden2[getArguments().getInt(ARG_SECTION_NUMBER) - 1]);

            textView.setText(sliden[getArguments().getInt(ARG_SECTION_NUMBER) - 1]);

            img = (ImageView) rootView.findViewById(R.id.section_img);
            img.setBackgroundResource(bgs[getArguments().getInt(ARG_SECTION_NUMBER) - 1]);


            return rootView;
        }


    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return PlaceholderFragment.newInstance(position + 1);

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
                case 3:
                    return "SECTION 4";

            }
            return null;
        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }
}