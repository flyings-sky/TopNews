package andfans.com.andfans_csdn;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import andfans.com.andfans_csdn.Fragments.ZhiHuFragment;
import andfans.com.andfans_csdn.Fragments.MineFragment;
import andfans.com.andfans_csdn.Fragments.SystemSettingFragment;
import andfans.com.andfans_csdn.Fragments.SoccerFragment;

public class MainActivity extends ActionBarActivity {
    //声明相关变量
    private NavigationView mNavigationView;
    private MenuItem mCurrentNavMenuItem;
    private Context mContext;
    private Toolbar mToolbar;
    private TextView mTextView;
    private DrawerLayout mDrawerLayout;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private SoccerFragment mSoccerFragment;
    private ZhiHuFragment mZhiHuFragment;
    private MineFragment mMineFragment;
    private SystemSettingFragment mSystemSettingFragment;
    long editTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentManager = getSupportFragmentManager();
        mContext = this;
        mTextView = (TextView) findViewById(R.id.id_main_toolbar_title);
        mToolbar = (Toolbar) findViewById(R.id.id_main_toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.id_main_dl);
        mNavigationView = (NavigationView) findViewById(R.id.id_main_nav_view);
        View headView = mNavigationView.getHeaderView(0);
        headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"点击了header",Toast.LENGTH_SHORT).show();
            }
        });
        mCurrentNavMenuItem = mNavigationView.getMenu().getItem(0);
        mCurrentNavMenuItem.setChecked(true);
        mFragmentTransaction = mFragmentManager.beginTransaction();
        if(mZhiHuFragment == null){
            mZhiHuFragment = new ZhiHuFragment();
            mFragmentTransaction.add(R.id.id_main_frame, mZhiHuFragment);
        }else {
            mFragmentTransaction.show(mZhiHuFragment);
        }
        mFragmentTransaction.commit();
//        ZhiHuFragment.setRefreshing(mZhiHuFragment.getmRefreshLayout(),true,true);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if(mCurrentNavMenuItem != item){
                    mCurrentNavMenuItem = item;
                }
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                switch (mCurrentNavMenuItem.getItemId()){
                    case R.id.id_menu_zhihu:
//                        Toast.makeText(mContext,"点击了知乎精选",Toast.LENGTH_SHORT).show();
                        mTextView.setText("知乎精选");
//                        mToolbar.getMenu().findItem(R.id.id_menu_news).setVisible(false);
//                        mToolbar.getMenu().findItem(R.id.id_menu_publish).setVisible(true);
//                        mToolbar.getMenu().findItem(R.id.id_menu_search).setVisible(true);
                        mFragmentTransaction = mFragmentManager.beginTransaction();
                        HideFragment(mZhiHuFragment);
                        if(mZhiHuFragment == null){
                            mZhiHuFragment = new ZhiHuFragment();
                            mFragmentTransaction.add(R.id.id_main_frame, mZhiHuFragment);
                        }else {
                            mFragmentTransaction.show(mZhiHuFragment);
                        }
                        break;
                    case R.id.id_menu_soccer:
//                        Toast.makeText(mContext,"点击了足坛资讯",Toast.LENGTH_SHORT).show();
                        mTextView.setText("足坛资讯");
//                        mToolbar.getMenu().findItem(R.id.id_menu_news).setVisible(false);
//                        mToolbar.getMenu().findItem(R.id.id_menu_publish).setVisible(false);
//                        mToolbar.getMenu().findItem(R.id.id_menu_search).setVisible(true);
                        mFragmentTransaction = mFragmentManager.beginTransaction();
                        HideFragment(mSoccerFragment);
                        if(mSoccerFragment == null){
                            mSoccerFragment = new SoccerFragment();
                            mFragmentTransaction.add(R.id.id_main_frame, mSoccerFragment);
                        }else {
                            mFragmentTransaction.show(mSoccerFragment);
                        }
                        break;
                    case R.id.id_menu_mine:
//                        Toast.makeText(mContext,"点击了我的",Toast.LENGTH_SHORT).show();
                        mTextView.setText("我的CSDN");
//                        mToolbar.getMenu().findItem(R.id.id_menu_news).setVisible(true);
//                        mToolbar.getMenu().findItem(R.id.id_menu_publish).setVisible(false);
//                        mToolbar.getMenu().findItem(R.id.id_menu_search).setVisible(false);
                        mFragmentTransaction = mFragmentManager.beginTransaction();
                        HideFragment(mMineFragment);
                        if(mMineFragment == null){
                            mMineFragment = new MineFragment();
                            mFragmentTransaction.add(R.id.id_main_frame,mMineFragment);
                        }else {
                            mFragmentTransaction.show(mMineFragment);
                        }
                        break;
                    case R.id.id_menu_system_settings:
//                        Toast.makeText(mContext,"点击了设置",Toast.LENGTH_SHORT).show();
                        mTextView.setText("系统设置");
//                        mToolbar.getMenu().findItem(R.id.id_menu_news).setVisible(false);
//                        mToolbar.getMenu().findItem(R.id.id_menu_publish).setVisible(false);
//                        mToolbar.getMenu().findItem(R.id.id_menu_search).setVisible(false);
                        mFragmentTransaction = mFragmentManager.beginTransaction();
                        HideFragment(mSystemSettingFragment);
                        if(mSystemSettingFragment == null){
                            mSystemSettingFragment = new SystemSettingFragment();
                            mFragmentTransaction.add(R.id.id_main_frame,mSystemSettingFragment);
                        }else {
                            mFragmentTransaction.show(mSystemSettingFragment);
                        }
                        break;
                }
                mFragmentTransaction.commit();
                return true;
            }
        });


        mToolbar.setTitle("");
        mTextView.setText("资讯串烧");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.navigation);
//        mToolbar.setOnMenuItemClickListener(onMenuItemClickListener);
        // Navigation Icon 要設定在 setSupoortActionBar后才有作用
        // 否則會出現 back bottom
        // 使用ActionBarDrawerToggle，配合DrawerLayout和ActionBar,以实现推荐的抽屉功能。
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    //点击返回按时执行的操作，如果侧滑栏打开，则先将其关闭
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            if((System.currentTimeMillis()- editTime)>2000){
                Toast.makeText(MainActivity.this, "再点一次，退出", Toast.LENGTH_SHORT).show();
                editTime = System.currentTimeMillis();
            }else{
                super.onBackPressed();
            }
        }
    }

    private void HideFragment(Fragment fragment){
        if(fragment != mSoccerFragment && mSoccerFragment != null){
            mFragmentTransaction.hide(mSoccerFragment);
        }

        if(fragment != mZhiHuFragment && mZhiHuFragment != null){
            mFragmentTransaction.hide(mZhiHuFragment);
        }

        if(fragment != mMineFragment&&mMineFragment != null){
            mFragmentTransaction.hide(mMineFragment);
        }

        if(fragment != mSystemSettingFragment&&mSystemSettingFragment != null){
            mFragmentTransaction.hide(mSystemSettingFragment);
        }

    }


//    private Toolbar.OnMenuItemClickListener onMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
//        @Override
//        public boolean onMenuItemClick(MenuItem item) {
//            String msg = "";
//            switch (item.getItemId()) {
//                case android.R.id.home:
//                    if(mDrawerLayout.isDrawerOpen(Gravity.LEFT))
//                        mDrawerLayout.closeDrawer(Gravity.LEFT);
//                    else
//                        mDrawerLayout.openDrawer(Gravity.LEFT);
//                    break;
//                case R.id.id_menu_publish:
//                    msg += "Click publish";
//                    break;
//                case R.id.id_menu_search:
//                    msg += "Click search";
//                    break;
//                case R.id.id_menu_news:
//                    msg += "Click news";
//                    break;
//            }
//
//            if(!msg.equals("")) {
//                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
//            }
//            return true;
//        }
//    };

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.menu_main,menu);
//        menu.findItem(R.id.id_menu_news).setVisible(false);
//        return true;
//    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //阻止activity保存fragment的状态
        //super.onSaveInstanceState(outState);
    }

}
