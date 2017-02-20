package andfans.com.andfans_csdn.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import andfans.com.andfans_csdn.API.ApiManage;
import andfans.com.andfans_csdn.Bean.ZhiHu.ZhiHuDailyContent;
import andfans.com.andfans_csdn.R;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ZhiHuDescribeActivity extends AppCompatActivity {
    private ImageView mImageView;
    private WebView mWebView;
    private Toolbar mToolBar;
    private String mUrl,id,title,image;
    private CollapsingToolbarLayout mToolbarLayout;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhihudcontent);
        mContext = this;
        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");
        image = getIntent().getStringExtra("image");
        initView();
        ApiManage.getInstence().getZhihuApiService().getZhiHuNewsContent(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ZhiHuDailyContent>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ZhiHuDailyContent zhiHuDailyContent) {
                        mWebView.loadUrl(zhiHuDailyContent.getmShareUrl());
                        Glide.with(mContext)
                                .load(zhiHuDailyContent.getImage())
                                .asBitmap()
                                .centerCrop()
                                .into(mImageView);
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    private void initView() {
        mWebView = (WebView) findViewById(R.id.id_zhihu_content_web_view);
        mToolBar = (Toolbar) findViewById(R.id.id_zhihu_content_toolbar);
        mImageView = (ImageView) findViewById(R.id.id_zhihu_content_image_view);
        mToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.id_zhihu_content_toolbar_layout);
        mToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        mToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        mToolBar.setTitle(title);
        setSupportActionBar(mToolBar);
        mToolBar.setNavigationIcon(R.drawable.leftrow);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        mToolBar.setTitleTextColor(getResources().getColor(R.color.white));
        //能够和js交互
        mWebView.getSettings().setJavaScriptEnabled(true);
        //缩放,设置为不能缩放可以防止页面上出现放大和缩小的图标
        mWebView.getSettings().setBuiltInZoomControls(false);
        //缓存
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //开启DOM storage API功能
        mWebView.getSettings().setDomStorageEnabled(true);
        //开启application Cache功能
        mWebView.getSettings().setAppCacheEnabled(false);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                presenter.openUrl(view, url);
                return true;
            }

        });
    }
}
