package com.dap.club.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dap.club.R;
import com.dap.club.data.Home;
import com.dap.club.util.DapLog;
import com.dap.club.view.DapDraweeView;

import java.util.List;

/**
 * Created by dap on 16/1/4.
 */
public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static enum ITEM_TYPE {
        ITEM_TYPE_NORMAL
    }
    private ClickListener itemListener;
    private ClickListener praiseListener;
    private List<Home> mData;

    public List<Home> getmData() {
        return mData;
    }

    public void setmData(List<Home> mData) {
        this.mData = mData;
        this.notifyDataSetChanged();
    }

    private final LayoutInflater mLayoutInflater;
    private final Context mContext;

    public HomeAdapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (viewType == ITEM_TYPE.ITEM_TYPE_NORMAL.ordinal()) {
//            return new NormalViewHolder(mLayoutInflater.inflate(R.layout.home_item_view, parent, false));
//        }else {
//            return null;
//        }

        return new NormalViewHolder(mLayoutInflater.inflate(R.layout.home_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof NormalViewHolder){
            //数据设置

            ((NormalViewHolder)holder).bindData(mData.get(position));
            ((NormalViewHolder)holder).root.findViewById(R.id.frs_list_item_top_linear_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != itemListener) {
                        itemListener.onClick(position, v);
                    }
                }
            });
            ((NormalViewHolder)holder).root.findViewById(R.id.frs_praise_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null!=praiseListener){
                        praiseListener.onClick(position, v);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (null==mData||mData.size()<=0){
        return 0;}else {
            return mData.size();
        }
    }

    public static class NormalViewHolder extends RecyclerView.ViewHolder{
        public TextView detail;
        public TextView title;
        public TextView time;
        public DapDraweeView img;
        public View root;
        public NormalViewHolder(View root) {
            super(root);
            this.root=root;
            title= (TextView) root.findViewById(R.id.frs_lv_title);
            detail= (TextView) root.findViewById(R.id.frs_lv_detail);
//            time= (TextView) root.findViewById(R.id.frs_lv_reply_time);
            img= (DapDraweeView) root.findViewById(R.id.abstract_img_layout);
        }

        public void bindData(Home home){
            title.setText(home.getTitle());
            detail.setText(home.getDetail());
            img.setUri(Uri.parse(home.getUrl()));
            DapLog.e(home.getUrl());
        }
    }

    public interface ClickListener{
         void onClick(int data,View v);
    }

    public void setItemClick(ClickListener listener){
        this.itemListener=listener;
    }
    public void setPraiseClick(ClickListener listener){
        this.praiseListener=listener;
    }
}
