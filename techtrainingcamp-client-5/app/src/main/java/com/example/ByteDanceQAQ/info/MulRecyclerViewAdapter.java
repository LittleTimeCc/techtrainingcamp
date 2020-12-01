package com.example.ByteDanceQAQ.info_implement;


/*
* Date：2020.12.1
* Author: LC & CP
*
* */

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test_information.R;

import java.util.List;

import static android.media.CamcorderProfile.get;

public class MulRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int NEW_NONE_TYPE = 0;//无图文模式
    private static final int NEW_MUL_TYPE = 4;//多图文模式
    private static final int NEW_OTHER_TYPE = 5;//多图文模式
    private static final int TYPE_2 = 2;//单图文模式
    private static final int TYPE_3 = 3;//单图文模式
    private static final int TYPE_1 = 1;//单图文模式
    private Context context;
    private List<NewsPhotoBean> list;
    private MyItemClickListener mItemClickListener;

    public MulRecyclerViewAdapter(Context context, List<NewsPhotoBean> list) {
        this.context = context;
        this.list = list;
    }

    //重写getItemViewType方法,通过此方法来判断应该加载是哪种类型布局
    @Override
    public int getItemViewType(int position) {
        int type = list.get(position).getType();
        switch (type) {
            case 0:
                return NEW_NONE_TYPE;
            case 4:
                return NEW_MUL_TYPE;
            case 2:
                return TYPE_2;
            case 3:
                return TYPE_3;
            case 1:
                return TYPE_1;
        }
        return NEW_OTHER_TYPE;
    }

    //根据不同的item类型来加载不同的viewholder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType) {
            case NEW_NONE_TYPE:
                return new NewsViewHolder(inflater.inflate(R.layout.recyclerview_item_type_00, parent, false), mItemClickListener);
            case NEW_MUL_TYPE:
                return new NewsPhotosViewHolder(inflater.inflate(R.layout.recyclerview_item_type_04, parent, false), mItemClickListener);
            case TYPE_2:
                return new NewsPhotoViewHolder(inflater.inflate(R.layout.recyclerview_item_type_02, parent, false), mItemClickListener);
            case TYPE_3:
                return new NewsPhotoViewHolder(inflater.inflate(R.layout.recyclerview_item_type_03, parent, false), mItemClickListener);
            case TYPE_1:
                return new NewsPhotoViewHolder(inflater.inflate(R.layout.recyclerview_item_type_01, parent, false), mItemClickListener);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        System.out.println(list.get(position).getTitle());
        //把对应位置的数据得到
        String title = list.get(position).getTitle();
        String time = list.get(position).getF_time();
        String author = list.get(position).getAuthor();
        List<String> ls = list.get(position).getList();//这里是json数据中的图片集合，也就是封面。

        //如果无图文
        if (holder instanceof NewsViewHolder) {
            ((NewsViewHolder) holder).tx_news_simple_photos_title.setText(title);
            ((NewsViewHolder) holder).tx_news_simple_photos_time.setText(time);
            ((NewsViewHolder) holder).tx_news_simple_photos_author.setText(author);
            ((NewsViewHolder) holder).tx_news_simple_photos_author.setText(author);
            return;
        }

        //如果单图文
        if (holder instanceof NewsPhotoViewHolder) {
            ((NewsPhotoViewHolder) holder).tx_news_simple_photos_title.setText(title);
            ((NewsPhotoViewHolder) holder).tx_news_simple_photos_time.setText(time);
            ((NewsPhotoViewHolder) holder).tx_news_simple_photos_author.setText(author);
            ((NewsPhotoViewHolder) holder).tx_news_simple_photos_author.setText(author);
            String s = ls.get(0);
            s = s.substring(0, s.lastIndexOf("."));
            s.toLowerCase();
            int imageId = context.getResources().getIdentifier(s, "drawable", "com.example.test_information");
            System.out.println(s);
            ((NewsPhotoViewHolder) holder).img_news_simple_photos_01.setImageResource(imageId);//单图文不用遍历直接将图片转换bitmap对象设置到ImageView上
            return;
        }
        //如果多图文
        if (holder instanceof NewsPhotosViewHolder) {
            ((NewsPhotosViewHolder) holder).tx_news_mul_photos_title.setText(title);
            ((NewsPhotosViewHolder) holder).tx_news_mul_photos_time.setText(time);
            ((NewsPhotosViewHolder) holder).tx_news_mul_photos_author.setText(author);
            int[] imageId = new int[ls.size()];
            int i = 0;
            for (String s : ls) {                                                                //截取图片名称以及找到对应drawable下图片
                s = s.substring(0, s.lastIndexOf("."));
                s.toLowerCase();
                imageId[i++] = context.getResources().getIdentifier(s, "drawable", "com.example.test_information");
            }
            i = 0;
            ((NewsPhotosViewHolder) holder).img_news_mul_photos_01.setImageResource(imageId[i++]);
            ((NewsPhotosViewHolder) holder).img_news_mul_photos_02.setImageResource(imageId[i++]);
            ((NewsPhotosViewHolder) holder).img_news_mul_photos_03.setImageResource(imageId[i++]);
            ((NewsPhotosViewHolder) holder).img_news_mul_photos_04.setImageResource(imageId[i++]);
            return;
        }
    }

    @Override
    public int getItemCount() {

        return list.size();
    }


    /**
     * NewsViewHolder为无图文模式
     */
    class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tx_news_simple_photos_title;//标题
        private TextView tx_news_simple_photos_time;//单图文模式的更新时间
        private TextView tx_news_simple_photos_author;//单图文模式的新闻作者
        private MyItemClickListener mListener;

        public NewsViewHolder(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView);
            this.mListener = myItemClickListener;
            itemView.setOnClickListener(this);
            tx_news_simple_photos_title = (TextView) itemView.findViewById(R.id.tx_news_simple_photos_title);//标题
            tx_news_simple_photos_time = (TextView) itemView.findViewById(R.id.tx_news_simple_photos_time);//单图文模式的更新时间
            tx_news_simple_photos_author = (TextView) itemView.findViewById(R.id.img_news_simple_photos_author);//单图文模式的新闻作者
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }

        }
    }

    /**
     * NewsPhotoViewHolder为单图文模式
     */
    class NewsPhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tx_news_simple_photos_title;//标题
        private ImageView img_news_simple_photos_01;//单图文模式的唯一一张图
        private TextView tx_news_simple_photos_time;//单图文模式的更新时间
        private TextView tx_news_simple_photos_author;//单图文模式的新闻作者
        private MyItemClickListener mListener;

        public NewsPhotoViewHolder(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView);
            this.mListener = myItemClickListener;
            itemView.setOnClickListener(this);
            tx_news_simple_photos_title = (TextView) itemView.findViewById(R.id.tx_news_simple_photos_title);//标题
            img_news_simple_photos_01 = (ImageView) itemView.findViewById(R.id.tx_news_simple_photos_01);//单图文模式的唯一一张图
            tx_news_simple_photos_time = (TextView) itemView.findViewById(R.id.tx_news_simple_photos_time);//单图文模式的更新时间
            tx_news_simple_photos_author = (TextView) itemView.findViewById(R.id.img_news_simple_photos_author);//单图文模式的新闻作者
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }

        }
    }


    /**
     * NewsPhotosViewHolder为多图模式
     */


    class NewsPhotosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tx_news_mul_photos_title;//标题
        private ImageView img_news_mul_photos_01;//多图文模式的第一张图
        private ImageView img_news_mul_photos_02;//多图文模式的第二张图
        private ImageView img_news_mul_photos_03;//多图文模式的第三张图
        private ImageView img_news_mul_photos_04;//多图文模式的第四张图
        private TextView tx_news_mul_photos_time;//多图文模式的更新时间
        private TextView tx_news_mul_photos_author;//多图文模式的新闻作者
        private MyItemClickListener mListener;

        public NewsPhotosViewHolder(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView);
            this.mListener = myItemClickListener;
            itemView.setOnClickListener(this);
            tx_news_mul_photos_title = (TextView) itemView.findViewById(R.id.tx_news_mul_photos_title);
            img_news_mul_photos_01 = (ImageView) itemView.findViewById(R.id.img_news_mul_photos_01);
            img_news_mul_photos_02 = (ImageView) itemView.findViewById(R.id.img_news_mul_photos_02);
            img_news_mul_photos_03 = (ImageView) itemView.findViewById(R.id.img_news_mul_photos_03);
            img_news_mul_photos_04 = (ImageView) itemView.findViewById(R.id.img_news_mul_photos_04);
            tx_news_mul_photos_time = (TextView) itemView.findViewById(R.id.tx_news_mul_photos_time);
            tx_news_mul_photos_author = (TextView) itemView.findViewById(R.id.tx_news_mul_photos_author);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }

        }
    }


    public interface MyItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setItemClickListener(MyItemClickListener myItemClickListener) {
        this.mItemClickListener = myItemClickListener;
    }


}
