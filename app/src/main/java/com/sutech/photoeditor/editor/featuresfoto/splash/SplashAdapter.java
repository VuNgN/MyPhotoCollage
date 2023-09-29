package com.sutech.photoeditor.editor.featuresfoto.splash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sutech.photoeditor.util.ImageUtil;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.sutech.photoeditor.R;
import com.sutech.photoeditor.editor.sticker.SplashSticker;
import com.sutech.photoeditor.util.AssetUtils;
import com.sutech.photoeditor.util.SystemUtil;

import java.util.ArrayList;
import java.util.List;

public class SplashAdapter extends RecyclerView.Adapter<SplashAdapter.ViewHolder> {
    private int borderWidth;
    private Context context;

    public int selectedSquareIndex;

    public SplashChangeListener splashChangeListener;

    public List<SplashItem> splashList = new ArrayList();

    interface SplashChangeListener {
        void onSelected(SplashSticker splashSticker);
    }

    SplashAdapter(Context context2, SplashChangeListener splashChangeListener2, boolean z) {
        this.context = context2;
        this.splashChangeListener = splashChangeListener2;
        this.borderWidth = SystemUtil.dpToPx(context2, 3);
        if (z) {
            this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "splash/mask1.png"), AssetUtils.loadBitmapFromAssets(context2, "splash/frame1.png")), R.drawable.splash01));
            this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "splash/mask2.png"), AssetUtils.loadBitmapFromAssets(context2, "splash/frame2.png")), R.drawable.splash02));
            this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "splash/mask3.png"), AssetUtils.loadBitmapFromAssets(context2, "splash/frame3.png")), R.drawable.splash03));
            this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "splash/mask4.png"), AssetUtils.loadBitmapFromAssets(context2, "splash/frame4.png")), R.drawable.splash04));
            this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "splash/mask5.png"), AssetUtils.loadBitmapFromAssets(context2, "splash/frame5.png")), R.drawable.splash05));
            this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "splash/mask6.png"), AssetUtils.loadBitmapFromAssets(context2, "splash/frame6.png")), R.drawable.splash06));
            this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "splash/mask7.png"), AssetUtils.loadBitmapFromAssets(context2, "splash/frame7.png")), R.drawable.splash07));
            this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "splash/mask8.png"), AssetUtils.loadBitmapFromAssets(context2, "splash/frame8.png")), R.drawable.splash08));
            this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "splash/mask9.png"), AssetUtils.loadBitmapFromAssets(context2, "splash/frame9.png")), R.drawable.splash09));
            this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "splash/mask11.png"), AssetUtils.loadBitmapFromAssets(context2, "splash/frame11.png")), R.drawable.splash10));
            this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "splash/mask12.png"), AssetUtils.loadBitmapFromAssets(context2, "splash/frame12.png")), R.drawable.splash11));
            this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "splash/mask14.png"), AssetUtils.loadBitmapFromAssets(context2, "splash/frame14.png")), R.drawable.splash12));
            this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "splash/mask17.png"), AssetUtils.loadBitmapFromAssets(context2, "splash/frame17.png")), R.drawable.splash13));
            this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "splash/mask18.png"), AssetUtils.loadBitmapFromAssets(context2, "splash/frame18.png")), R.drawable.splash14));
            return;
        }
        this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "blur/blur_1_mask.png"), AssetUtils.loadBitmapFromAssets(context2, "blur/blur_1_shadow.png")), R.drawable.blur_1));
        this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "blur/blur_2_mask.png"), AssetUtils.loadBitmapFromAssets(context2, "blur/blur_2_shadow.png")), R.drawable.blur_2));
        this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "blur/blur_3_mask.png"), AssetUtils.loadBitmapFromAssets(context2, "blur/blur_3_shadow.png")), R.drawable.blur_3));
        this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "blur/blur_4_mask.png"), AssetUtils.loadBitmapFromAssets(context2, "blur/blur_4_shadow.png")), R.drawable.blur_4));
        this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "blur/blur_5_mask.png"), AssetUtils.loadBitmapFromAssets(context2, "blur/blur_5_shadow.png")), R.drawable.blur_5));
        this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "blur/blur_7_mask.png"), AssetUtils.loadBitmapFromAssets(context2, "blur/blur_7_shadow.png")), R.drawable.blur_6));
        this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "blur/blur_8_mask.png"), AssetUtils.loadBitmapFromAssets(context2, "blur/blur_8_shadow.png")), R.drawable.blur_7));
        this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "blur/blur_9_mask.png"), AssetUtils.loadBitmapFromAssets(context2, "blur/blur_9_shadow.png")), R.drawable.blur_8));
        this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "blur/blur_10_mask.png"), AssetUtils.loadBitmapFromAssets(context2, "blur/blur_10_shadow.png")), R.drawable.blur_9));
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.splash_view, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ImageUtil.INSTANCE.setImage(viewHolder.splash,splashList.get(i).drawableId,0.1f);
        if (this.selectedSquareIndex == i) {
            viewHolder.splash.setBorderColor(this.context.getResources().getColor(R.color.colorAccent));
            viewHolder.splash.setBorderWidth(this.borderWidth);
            return;
        }
        viewHolder.splash.setBorderColor(0);
        viewHolder.splash.setBorderWidth(this.borderWidth);
    }

    public int getItemCount() {
        return this.splashList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RoundedImageView splash;

        public ViewHolder(View view) {
            super(view);
            this.splash = (RoundedImageView) view.findViewById(R.id.splash);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            int unused = SplashAdapter.this.selectedSquareIndex = getAdapterPosition();
            if (SplashAdapter.this.selectedSquareIndex < 0) {
                int unused2 = SplashAdapter.this.selectedSquareIndex = 0;
            }
            if (SplashAdapter.this.selectedSquareIndex >= SplashAdapter.this.splashList.size()) {
                int unused3 = SplashAdapter.this.selectedSquareIndex = SplashAdapter.this.splashList.size() - 1;
            }
            SplashAdapter.this.splashChangeListener.onSelected(((SplashItem) SplashAdapter.this.splashList.get(SplashAdapter.this.selectedSquareIndex)).splashSticker);
            SplashAdapter.this.notifyDataSetChanged();
        }
    }

    class SplashItem {
        int drawableId;
        SplashSticker splashSticker;

        SplashItem(SplashSticker splashSticker2, int i) {
            this.splashSticker = splashSticker2;
            this.drawableId = i;
        }
    }
}
