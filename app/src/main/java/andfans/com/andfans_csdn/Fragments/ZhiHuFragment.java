package andfans.com.andfans_csdn.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;

import andfans.com.andfans_csdn.API.ApiManage;
import andfans.com.andfans_csdn.Adapter.ZhiHuAdapter;
import andfans.com.andfans_csdn.Bean.ZhiHu.ZhiHuDaily;
import andfans.com.andfans_csdn.R;
import andfans.com.andfans_csdn.Utils.DividerItemDecoration;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/*
 * Created by 兆鹏 on 2017/2/8.
 */
public class ZhiHuFragment extends Fragment {
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFloatingActionButton;
    private Context mContext;
    private ZhiHuAdapter mAdapter;
    private Calendar now,current;//now 用来指示日期选择器当前的日期，current用来指示当前显示数据的日期

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zhihu,container,false);
        mContext = getContext();
        initView(view);
        addListener();
        getData();
        return view;
    }

    public SwipeRefreshLayout getmRefreshLayout() {
        return mRefreshLayout;
    }

    private void getData() {
        ApiManage.getInstence().getZhihuApiService()
                .getZhiHuNews()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ZhiHuDaily>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showError(e.getMessage());
                    }

                    @Override
                    public void onNext(ZhiHuDaily zhiHuDaily) {
                        mAdapter = new ZhiHuAdapter(mContext,zhiHuDaily.getStories(),R.layout.zhihu_item);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                });
    }

    private void addListener() {
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 开始刷新，设置当前为刷新状态
                mRefreshLayout.setRefreshing(true);

                // 这里是主线程
                if(judgeCurrentAndNow(current,now)){
                    Toast.makeText(mContext,"没有新消息" ,Toast.LENGTH_SHORT).show();
                    mRefreshLayout.setRefreshing(false);
                }else {
                    Toast.makeText(mContext,"时间不同" ,Toast.LENGTH_SHORT).show();
                    // 一些比较耗时的操作，比如联网获取数据，需要放到子线程去执行
                    current.set(now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH));
                    getRefreshingData(now);
                    mRefreshLayout.setRefreshing(false);
                }
//                因为Calender的月份从0开始，所以真正的月份应该+1
//                Toast.makeText(mContext
//                        ,now.get(Calendar.YEAR)+"  "
//                                +(now.get(Calendar.MONTH)+1)+"  "+
//                                now.get(Calendar.DAY_OF_MONTH)
//                        ,Toast.LENGTH_SHORT).show();
            }
        });

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"点击了浮动按钮",Toast.LENGTH_SHORT).show();
                final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        now.set(Calendar.YEAR , year);
                        now.set(Calendar.MONTH , monthOfYear);
                        now.set(Calendar.DAY_OF_MONTH , dayOfMonth);
                        setRefreshing(mRefreshLayout,true,true);
                    }
                },now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.setMaxDate(Calendar.getInstance());
                Calendar minDate = Calendar.getInstance();
                // 2013.5.20是知乎日报api首次上线
                minDate.set(2013, 5, 20);
                datePickerDialog.setMinDate(minDate);
                //设置触摸时设别是否震动
                datePickerDialog.vibrate(false);
                datePickerDialog.show(getActivity().getFragmentManager(), "DatePickerDialog");
            }
        });

            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int totalItemCount = recyclerView.getAdapter().getItemCount();
                    int lastVisibleItemPosition = lm.findLastVisibleItemPosition();
                    int visibleItemCount = recyclerView.getChildCount();

                    if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition == totalItemCount - 1 && visibleItemCount > 0) {
                        current = getLastDate(current);
                        Toast.makeText(mContext
                                , current.get(Calendar.YEAR) + "  "
                                        + (current.get(Calendar.MONTH) + 1) + "  " +
                                        current.get(Calendar.DAY_OF_MONTH)
                                , Toast.LENGTH_SHORT).show();
                        getLoadingData(current);
                    }
                }
            });
    }

    private void getRefreshingData(Calendar current){
        ApiManage.getInstence().getZhihuApiService()
                .getLastNews(toStringDate(current))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ZhiHuDaily>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showError(e.getMessage());
                    }

                    @Override
                    public void onNext(ZhiHuDaily zhiHuDaily) {
                        mAdapter.addItems(0,zhiHuDaily.getStories());

                    }
                });
    }

    private void getLoadingData(Calendar current) {
        ApiManage.getInstence().getZhihuApiService()
                .getLastNews(toStringDate(current))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ZhiHuDaily>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showError(e.getMessage());
                    }

                    @Override
                    public void onNext(ZhiHuDaily zhiHuDaily) {
                        mAdapter.addItems(mAdapter.getItemCount(),zhiHuDaily.getStories());
                    }
                });
    }

    public boolean judgeCurrentAndNow(Calendar currentShow,Calendar now){
        return currentShow.get(Calendar.YEAR) == now.get(Calendar.YEAR) &&
                currentShow.get(Calendar.MONTH) == now.get(Calendar.MONTH) &&
                currentShow.get(Calendar.DAY_OF_MONTH) == now.get(Calendar.DAY_OF_MONTH);
    }


    //一进来自动刷新调用
    public static void setRefreshing(SwipeRefreshLayout refreshLayout,boolean refreshing, boolean notify){
        Class<? extends SwipeRefreshLayout> refreshLayoutClass = refreshLayout.getClass();
        if (refreshLayoutClass != null) {

            try {
                Method setRefreshing = refreshLayoutClass.getDeclaredMethod("setRefreshing", boolean.class, boolean.class);
                setRefreshing.setAccessible(true);
                setRefreshing.invoke(refreshLayout, refreshing, notify);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    //获取前一天的日期
    public Calendar getLastDate(Calendar current){
        current.set(Calendar.DATE,current.get(Calendar.DATE)-1);
        return current;
    }

    public String toStringDate(Calendar now){
        String year = now.get(Calendar.YEAR) + "";
        String month;
        if(0 <= now.get(Calendar.MONTH) && now.get(Calendar.MONTH) <= 8){
            int m = now.get(Calendar.MONTH)+1;
            month = "0"+m;
        }else {
            month = now.get(Calendar.MONTH)+1+"";
        }
        String day;
        if(1 <= now.get(Calendar.DAY_OF_MONTH)&&now.get(Calendar.DAY_OF_MONTH) < 10){
            day = "0"+now.get(Calendar.DAY_OF_MONTH);
        }else {
            day = now.get(Calendar.DAY_OF_MONTH)+"";
        }

        return year + month + day;
    }

    private void initView(View view) {
        now = Calendar.getInstance();
        current = Calendar.getInstance();
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.id_zhihu_srl);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.id_zhihu_recycle);
        mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.id_zhihu_fab);
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        //设置布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        //设置adapter
        mRecyclerView.setAdapter(mAdapter);
        //设置Item增加、移除动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.HORIZONTAL_LIST));
    }

    public void showError(String error) {

        if (mRecyclerView != null) {
            Snackbar.make(mRecyclerView, "请检查网络", Snackbar.LENGTH_SHORT).setAction("重试", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getData();
                }
            }).show();

        }
    }

}
