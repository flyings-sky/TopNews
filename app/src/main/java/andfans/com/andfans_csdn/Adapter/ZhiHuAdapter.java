package andfans.com.andfans_csdn.Adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;

import java.util.ArrayList;

import andfans.com.andfans_csdn.Activity.ZhiHuDescribeActivity;
import andfans.com.andfans_csdn.Bean.ZhiHu.ZhiHuItem;
import andfans.com.andfans_csdn.R;

/**
 *
 * Created by 兆鹏 on 2017/2/14.
 */
public class ZhiHuAdapter extends RecyclerView.Adapter<ZhiHuAdapter.ZhiHuViewHolder>{
    private ArrayList<ZhiHuItem> datas = new ArrayList<>();
    private int layout;
    private Context mContext;

    public ZhiHuAdapter(Context context,ArrayList<ZhiHuItem> datas,int layout){
        this.datas = datas;
        this.layout = layout;
        this.mContext = context;

    }

    @Override
    public ZhiHuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ZhiHuViewHolder holder = new ZhiHuViewHolder(LayoutInflater.from(parent.getContext()).inflate(layout,parent,false));
        return holder;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ZhiHuAdapter.ZhiHuViewHolder holder, final int position) {
        final ZhiHuItem zhihuDailyItem = datas.get(holder.getAdapterPosition());
        holder.zhiHuText.setText(datas.get(position).getTitle());
        final String [] im = datas.get(position).getImages();
        Glide.with(mContext).load(im[0]).
                override(800, 800).centerCrop()
                .into(holder.getZhiHuImage());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ZhiHuDescribeActivity.class);
                intent.putExtra("id", zhihuDailyItem.getId());
                intent.putExtra("title", zhihuDailyItem.getTitle());
                intent.putExtra("image",im[0]);
                mContext.startActivity(intent);
            }
        });
    }

    static class ZhiHuViewHolder extends RecyclerView.ViewHolder{
        private ImageView zhiHuImage;
        private TextView zhiHuText;

        public ZhiHuViewHolder(View itemView) {
            super(itemView);
            zhiHuImage = (ImageView) itemView.findViewById(R.id.id_zhihu_item_im);
            zhiHuText = (TextView) itemView.findViewById(R.id.id_zhihu_item_tv);
        }

        public ImageView getZhiHuImage() {
            return zhiHuImage;
        }

        public void setZhiHuImage(ImageView zhiHuImage) {
            this.zhiHuImage = zhiHuImage;
        }

        public TextView getZhiHuText() {
            return zhiHuText;
        }

        public void setZhiHuText(TextView zhiHuText) {
            this.zhiHuText = zhiHuText;
        }
    }

    public void addItems(int position,ArrayList<ZhiHuItem> list) {
        datas.addAll(position,list);
        notifyDataSetChanged();

    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas.size();
    }

}


