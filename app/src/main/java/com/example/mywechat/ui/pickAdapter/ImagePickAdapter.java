package com.example.mywechat.ui.pickAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mywechat.R;

import java.util.ArrayList;
import java.util.List;

public class ImagePickAdapter extends RecyclerView.Adapter<ImagePickAdapter.SelectedPicViewHolder> {
    private int maxImgCount;
    private Context mContext;
    private List<Object> mData;
    private LayoutInflater mInflater;
    private OnRecyclerViewItemClickListener listener;
    private boolean isAdded;   //是否额外添加了最后一个图片
    private OnItemRemoveClick onItemRemoveClick;

    public boolean isAdded() {
        return isAdded;
    }

    public void setAdded(boolean added) {
        isAdded = added;
    }

    public void setOnItemRemoveClick(OnItemRemoveClick onItemRemoveClick) {
        this.onItemRemoveClick = onItemRemoveClick;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    public void setImages(List<Object> data) {
        mData = new ArrayList<>(data);
        if (getItemCount() < maxImgCount) {
            mData.add(new Object());
            isAdded = true;
        } else {
            isAdded = false;
        }
        notifyDataSetChanged();
    }
    public void addImg() {
        if (getItemCount() < maxImgCount) {
            mData.add(new Object());
            isAdded = true;
        } else {
            isAdded = false;
        }
    }

    public List<Object> getImages() {
        //由于图片未选满时，最后一张显示添加图片，因此这个方法返回真正的已选图片
        if (isAdded) return new ArrayList<>(mData.subList(0, mData.size() - 1));
        else return mData;
    }

    public ImagePickAdapter(Context mContext, List<Object> data, int maxImgCount) {
        this.mContext = mContext;
        this.maxImgCount = maxImgCount;
        this.mInflater = LayoutInflater.from(mContext);
        setImages(data);
    }

    @Override
    public SelectedPicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SelectedPicViewHolder(mInflater.inflate(R.layout.item_list_upload_image, parent, false));
    }

    @Override
    public void onBindViewHolder(SelectedPicViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class SelectedPicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RelativeLayout cancelImg;
        private int clickPosition;
        private ImageView iv_img;

        public SelectedPicViewHolder(View itemView) {
            super(itemView);
//            iv_img = (ImageView) itemView.findViewById(R.id.iv_img);
//            cancelImg = ((RelativeLayout) itemView.findViewById(R.id.item_upload_image_cancelImg));
        }

        public void bind(final int position) {
            //设置条目的点击事件
            itemView.setOnClickListener(this);
            //根据条目位置设置图片
//            ImageItem item = mData.get(position);
//            if (isAdded && position == getItemCount() - 1) {
//                iv_img.setScaleType(ImageView.ScaleType.FIT_XY);
//                cancelImg.setVisibility(View.INVISIBLE);
//                iv_img.setImageResource(R.mipmap.img_add);
//                clickPosition = UploadMoreImagesActivity.IMAGE_ITEM_ADD;
//            } else {
//                iv_img.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                cancelImg.setVisibility(View.VISIBLE);
//                ImagePicker.getInstance().getImageLoader().displayImage((Activity) mContext, item.path, iv_img, 0, 0);
//                clickPosition = position;
//            }
            cancelImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeItem(position);
                    onItemRemoveClick.onItemRemoveClick();
                }
            });
        }

        @Override
        public void onClick(View v) {
            if (listener != null) listener.onItemClick(v, clickPosition);
        }
    }

    /**
     * 删除某条item
     * @param position
     */
    public void removeItem(int position){
        mData.remove(position);
//        notifyItemRemoved(position);
    }

    public interface OnItemRemoveClick {
        public void onItemRemoveClick();
    }

}
