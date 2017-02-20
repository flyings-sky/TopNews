package andfans.com.andfans_csdn.Activity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import andfans.com.andfans_csdn.API.ApiManage;
import andfans.com.andfans_csdn.Bean.ZhiHu.ZhiHuDailyContent;
import andfans.com.andfans_csdn.R;
import andfans.com.andfans_csdn.Utils.WebUtil;
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
                        if(zhiHuDailyContent.getBody() == null){
                            mWebView.loadUrl(zhiHuDailyContent.getmShareUrl());
                        }else {
                            String data = WebUtil.buildHtmlWithCss(zhiHuDailyContent.getBody(), zhiHuDailyContent.getCss());
                            mWebView.loadDataWithBaseURL(WebUtil.BASE_URL, data, WebUtil.MIME_TYPE, WebUtil.ENCODING, WebUtil.FAIL_URL);                        }

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        //settings.setUseWideViewPort(true);造成文字太小
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAppCachePath(getCacheDir().getAbsolutePath() + "/webViewCache");
        settings.setAppCacheEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //主要通知客户端app加载当前网页的 title，Favicon，progress,javascript dialog等事件，通知客户端处理这些相应的事件。
        mWebView.setWebChromeClient(new WebChromeClient());
//        //能够和js交互
//        mWebView.getSettings().setJavaScriptEnabled(true);
//        //缩放,设置为不能缩放可以防止页面上出现放大和缩小的图标
//        mWebView.getSettings().setBuiltInZoomControls(false);
//        //缓存
//        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//        //开启DOM storage API功能
//        mWebView.getSettings().setDomStorageEnabled(true);
//        //开启application Cache功能
//        mWebView.getSettings().setAppCacheEnabled(false);
//
//        mWebView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
////                presenter.openUrl(view, url);
//                return true;
//            }
//
//        });
    }

    private String convertZhihuContent(String preResult) {

        preResult = preResult.replace("<div class=\"img-place-holder\">", "");
        preResult = preResult.replace("<div class=\"headline\">", "");

        // 在api中，css的地址是以一个数组的形式给出，这里需要设置
        // in fact,in api,css addresses are given as an array
        // api中还有js的部分，这里不再解析js
        // javascript is included,but here I don't use it
        // 不再选择加载网络css，而是加载本地assets文件夹中的css
        // use the css file from local assets folder,not from network
        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/zhihu_daily.css\" type=\"text/css\">";


        // 根据主题的不同确定不同的加载内容
        // load content judging by different theme
        String theme = "<body className=\"\" onload=\"onLoaded()\">";
        if ((mContext.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)
                == Configuration.UI_MODE_NIGHT_YES){
            theme = "<body className=\"\" onload=\"onLoaded()\" class=\"night\">";
        }

        return new StringBuilder()
                .append("<!DOCTYPE html>\n")
                .append("<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n")
                .append("<head>\n")
                .append("\t<meta charset=\"utf-8\" />")
                .append(css)
                .append("\n</head>\n")
                .append(theme)
                .append(preResult)
                .append("</body></html>").toString();
    }
}
