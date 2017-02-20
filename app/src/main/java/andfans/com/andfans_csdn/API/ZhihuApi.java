package andfans.com.andfans_csdn.API;


import andfans.com.andfans_csdn.Bean.ZhiHu.ZhiHuDaily;
import andfans.com.andfans_csdn.Bean.ZhiHu.ZhiHuDailyContent;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 *
 * Created by 兆鹏 on 2017/2/10.
 */
public interface ZhihuApi {
    //获取知乎最新的消息
    @GET("/api/4/news/latest")
    Observable<ZhiHuDaily> getZhiHuNews();
    @GET("/api/4/news/{id}")
    Observable<ZhiHuDailyContent> getZhiHuNewsContent(@Path("id")String id);
    @GET("/api/4/news/before/{date}")
    Observable<ZhiHuDaily> getLastNews(@Path("date")String date);

}
