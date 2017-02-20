package andfans.com.andfans_csdn.Bean.ZhiHu;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 *
 * Created by 兆鹏 on 2017/2/12.
 */
public class ZhiHuDaily {
    @SerializedName("date")
    private String date;
    @SerializedName("stories")
    private ArrayList<ZhiHuItem> stories;
    @SerializedName("top_stories")
    private ArrayList<ZhiHuItem> top_stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<ZhiHuItem> getStories() {
        return stories;
    }

    public void setStories(ArrayList<ZhiHuItem> stories) {
        this.stories = stories;
    }

    public ArrayList<ZhiHuItem> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(ArrayList<ZhiHuItem> top_stories) {
        this.top_stories = top_stories;
    }
}
