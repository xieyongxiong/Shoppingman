package com.codenofree.shoppingman.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codenofree.shoppingman.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xieyongxiong on 2018/1/15.
 */

public class BottomTabView extends FrameLayout {
    RecyclerView mRecyclerView;
    private Context mContext;
    private int[] mImages;
    private int[] mSelectedImages;
    private int[] mTitles;
    private int mSelectedColor;
    private OnItemClickListener onItemClickListener;
    private BottomTabAdapter mBotttomTabAdapter;
    private GridLayoutManager mGridLayoutManager;
    private List<Integer> filterSelected = new ArrayList<>();

    public BottomTabView(Context context) {
        super(context);
    }

    public BottomTabView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public BottomTabView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initView() {
        View view = View.inflate(mContext, R.layout.bottom_tab_view, this);
        mRecyclerView = view.findViewById(R.id.recyclerView);
    }

    public void setResource(int[] images, int[] selectedImage, int[] titles, int color, int colums) {
        mImages = images;
        mSelectedImages = selectedImage;
        mTitles = titles;
        mSelectedColor = color;
        mBotttomTabAdapter = new BottomTabAdapter();
        mGridLayoutManager = new GridLayoutManager(mContext, colums);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(mBotttomTabAdapter);
        for(int i=0;i<images.length;i++){
            filterSelected.add(i);
        }
    }

    public void setItemChecked(int position) {
        for (int i = 0; i < mImages.length; i++) {
            View view = mGridLayoutManager.findViewByPosition(i);
            ImageView bottomImage = view.findViewById(R.id.bottom_image);
            TextView bottomText = view.findViewById(R.id.bottom_text);
            if (i == position) {
                bottomImage.setImageResource(mSelectedImages[i]);
                bottomText.setTextColor(getResources().getColor(mSelectedColor));
            } else {
                bottomImage.setImageResource(mImages[i]);
            }
        }
    }

    public void setItemResource(int position, int imageId) {
        View view = mGridLayoutManager.findViewByPosition(position);
        ImageView bottomImage = view.findViewById(R.id.bottom_image);
        bottomImage.setImageResource(imageId);
    }

    public void setItemEnable(int position, int imageId) {
        View view = mGridLayoutManager.findViewByPosition(position);
        ImageView bottomImage = view.findViewById(R.id.bottom_image);
        bottomImage.setImageResource(imageId);
        view.setEnabled(false);
    }

    public void reset() {
        for (int i = 0; i < mImages.length; i++) {
            View view = mGridLayoutManager.findViewByPosition(i);
            view.setEnabled(true);
            ImageView bottomImage = view.findViewById(R.id.bottom_image);
            TextView bottomText = view.findViewById(R.id.bottom_text);
            bottomImage.setImageResource(mImages[i]);
        }
    }
    public List<Integer> getSelected(){
        return filterSelected;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int postion);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class BottomTabAdapter extends RecyclerView.Adapter<BottomTabAdapter.BottomTabHolder> {
        private LayoutInflater inflater;

        public BottomTabAdapter() {
            inflater = LayoutInflater.from(mContext);
        }

        @Override
        public BottomTabHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new BottomTabHolder(inflater.inflate(R.layout.bottom_tab_item, parent, false));
        }

        @Override
        public void onBindViewHolder(BottomTabHolder holder, int position) {
            holder.bottomImage.setImageResource(mImages[position]);
            holder.bottomText.setText(mContext.getResources().getString(mTitles[position]));
        }

        @Override
        public int getItemCount() {
            return mImages.length;
        }

        public class BottomTabHolder extends RecyclerView.ViewHolder implements OnClickListener {
            ImageView bottomImage;
            TextView bottomText;
            LinearLayout bottomLayout;

            public BottomTabHolder(View view) {
                super(view);
                bottomImage = view.findViewById(R.id.bottom_image);
                bottomText = view.findViewById(R.id.bottom_text);
                bottomLayout = view.findViewById(R.id.bottom_layout);
                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
//                onItemClickListener.onItemClick(itemView, getAdapterPosition());
                Log.i("thg","click");
                View view = mGridLayoutManager.findViewByPosition(getAdapterPosition());
                LinearLayout layout = view.findViewById(R.id.bottom_layout);
                if(filterSelected.contains(getAdapterPosition())){
                    filterSelected.remove(filterSelected.indexOf(getAdapterPosition()));
                    layout.setBackground(null);
                }else {
                    filterSelected.add(getAdapterPosition());
                    layout.setBackgroundResource(R.drawable.filter_background);
                }
            }
        }
    }
}
