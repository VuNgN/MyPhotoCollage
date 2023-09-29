package com.sutech.photoeditor.editor.filterscolor;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.sutech.photoeditor.R;
import com.sutech.photoeditor.util.SystemUtil;

import java.util.List;

public class FilterViewAdapter extends RecyclerView.Adapter<FilterViewAdapter.ViewHolder> {
    private final List<Bitmap> bitmaps;
    private final int borderWidth;
    private final Context context;

    public List<com.sutech.photoeditor.editor.filterscolor.FilterUtils.FilterBean> filterEffects;

    public com.sutech.photoeditor.editor.filterscolor.FilterListener mFilterListener;

    public int selectedFilterIndex = 0;

    public FilterViewAdapter(List<Bitmap> list, FilterListener filterListener, Context context2, List<FilterUtils.FilterBean> list2) {
        this.mFilterListener = filterListener;
        this.bitmaps = list;
        this.context = context2;
        this.filterEffects = list2;
        this.borderWidth = SystemUtil.dpToPx(context2, 3);
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_filter_view, viewGroup, false));
    }

    public void reset() {
        this.selectedFilterIndex = 0;
        notifyDataSetChanged();
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.mTxtFilterName.setText(this.filterEffects.get(i).getName());
        viewHolder.mImageFilterView.setImageBitmap(this.bitmaps.get(i));
        viewHolder.mImageFilterView.setBorderColor(this.context.getResources().getColor(R.color.colorAccent));
        if (this.selectedFilterIndex == i) {
            viewHolder.mImageFilterView.setBorderColor(this.context.getResources().getColor(R.color.colorAccent));
            viewHolder.mImageFilterView.setBorderWidth(this.borderWidth);
            return;
        }
        viewHolder.mImageFilterView.setBorderColor(0);
        viewHolder.mImageFilterView.setBorderWidth(this.borderWidth);
    }

    public int getItemCount() {
        return this.bitmaps.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView mImageFilterView;
        TextView mTxtFilterName;
        ConstraintLayout wrapFilterItem;

        ViewHolder(View view) {
            super(view);
            this.mImageFilterView = view.findViewById(R.id.imgFilterView);
            this.mTxtFilterName = view.findViewById(R.id.txtFilterName);
            this.wrapFilterItem = view.findViewById(R.id.wrapFilterItem);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    selectedFilterIndex = getLayoutPosition();
                    mFilterListener.onFilterSelected(filterEffects.get(selectedFilterIndex).getConfig());
                    notifyDataSetChanged();
                }
            });
        }
    }
}
