package com.example.mywechat.ui.discover;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mywechat.R;

import java.util.ArrayList;
import java.util.List;

public class ImagePickAdapter extends RecyclerView.Adapter<ImagePickAdapter.ImagePickViewHolder> {
    private int maxImgCount;
    private Context mContext;
    private List<ImagePick> mData;
    private LayoutInflater mInflater;
    private OnRecyclerViewItemClickListener listener;
    private boolean isAdded;   //是否额外添加了最后一个图片

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    public boolean isAdded() {
        return isAdded;
    }

    public void setAdded(boolean added) {
        isAdded = added;
    }

    public void setImages(List<ImagePick> data) {
        mData = new ArrayList<>(data);
        if (getItemCount() < maxImgCount) {
            mData.add(new ImagePick(mContext));
            isAdded = true;
        } else {
            isAdded = false;
        }
        notifyDataSetChanged();
    }

    public void addImg() {
        if (getItemCount() < maxImgCount) {
            mData.add(new ImagePick(mContext));
            isAdded = true;
        } else {
            isAdded = false;
        }
    }

    public List<ImagePick> getImages() {
        //由于图片未选满时，最后一张显示添加图片，因此这个方法返回真正的已选图片
        if (isAdded) return new ArrayList<>(mData.subList(0, mData.size() - 1));
        else return mData;
    }

    public ImagePickAdapter(Context mContext, List<ImagePick> data, int maxImgCount) {
        this.mContext = mContext;
        this.maxImgCount = maxImgCount;
        this.mInflater = LayoutInflater.from(mContext);
        setImages(data);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public ImagePickViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImagePickViewHolder(mInflater.inflate(R.layout.item_list_upload_image, parent, false));
    }

    @Override
    public void onBindViewHolder(ImagePickViewHolder holder, int position) {
        holder.bind(position);
    }

    public class ImagePickViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private int clickPosition;
        private ImageView newImageView;

        public ImagePickViewHolder(View itemView) {
            super(itemView);
            newImageView = itemView.findViewById(R.id.newImageView);
        }

        public void bind(final int position) {
            //设置条目的点击事件
            itemView.setOnClickListener(this);
            //根据条目位置设置图片
            if (isAdded && position == getItemCount() - 1) {
                newImageView.setImageResource(R.drawable.icon_plus_rect);
                clickPosition = NewDiscoverActivity.IMAGE_ITEM_ADD;
            } else {
                newImageView.setImageBitmap(mData.get(position).getImage());
                clickPosition = position;
            }
        }

        @Override
        public void onClick(View v) {
            if (listener != null) listener.onItemClick(v, clickPosition);
        }
    }
}
