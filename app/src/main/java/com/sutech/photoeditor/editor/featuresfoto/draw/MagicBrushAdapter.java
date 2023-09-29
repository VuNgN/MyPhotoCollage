package com.sutech.photoeditor.editor.featuresfoto.draw;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sutech.photoeditor.R;
import com.sutech.photoeditor.util.SystemUtil;
import com.sutech.photoeditor.widget.customview.photo.DrawBitmapModel;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MagicBrushAdapter extends RecyclerView.Adapter<MagicBrushAdapter.ViewHolder> {
    public static List<DrawBitmapModel> drawBitmapModels = new ArrayList();
    private boolean addNoneColor;
    private int borderSize = 0;

    public BrushMagicListener brushMagicListener;
    private final Context context;

    public int selectedColorIndex;

    public MagicBrushAdapter(Context context2, BrushMagicListener brushMagicListener2) {
        this.context = context2;
        this.borderSize = SystemUtil.dpToPx(context2, 2);
        this.brushMagicListener = brushMagicListener2;
        drawBitmapModels = lstDrawBitmapModel(context2);
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.magic_brush_item, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.magicBrush.setImageResource(drawBitmapModels.get(i).getMainIcon());
        if (this.selectedColorIndex == i) {
            viewHolder.magicBrush.setBorderWidth(this.borderSize);
        } else {
            viewHolder.magicBrush.setBorderWidth(0);
        }
    }

    public void setSelectedColorIndex(int i) {
        this.selectedColorIndex = i;
    }

    public int getItemCount() {
        return drawBitmapModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView magicBrush;

        ViewHolder(View view) {
            super(view);
            this.magicBrush = view.findViewById(R.id.magicBrush);
            this.magicBrush.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    int unused = MagicBrushAdapter.this.selectedColorIndex = ViewHolder.this.getLayoutPosition();
                    MagicBrushAdapter.this.brushMagicListener.onMagicChanged(MagicBrushAdapter.drawBitmapModels.get(MagicBrushAdapter.this.selectedColorIndex));
                    MagicBrushAdapter.this.notifyDataSetChanged();
                }
            });
        }
    }

    public static List<DrawBitmapModel> lstDrawBitmapModel(Context context2) {
        if (drawBitmapModels != null && !drawBitmapModels.isEmpty()) {
            return drawBitmapModels;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(Integer.valueOf(R.drawable.b4));
        arrayList.add(Integer.valueOf(R.drawable.b5));
        arrayList.add(Integer.valueOf(R.drawable.b6));
        arrayList.add(Integer.valueOf(R.drawable.b7));
        arrayList.add(Integer.valueOf(R.drawable.b8));
        arrayList.add(Integer.valueOf(R.drawable.b9));
        arrayList.add(Integer.valueOf(R.drawable.b10));
        arrayList.add(Integer.valueOf(R.drawable.b11));
        arrayList.add(Integer.valueOf(R.drawable.b12));
        arrayList.add(Integer.valueOf(R.drawable.b13));
        arrayList.add(Integer.valueOf(R.drawable.b14));
        arrayList.add(Integer.valueOf(R.drawable.b15));
        drawBitmapModels.add(new DrawBitmapModel(R.drawable.butterfly, arrayList, true, context2));
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(Integer.valueOf(R.drawable.magic21));
        arrayList2.add(Integer.valueOf(R.drawable.magic22));
        arrayList2.add(Integer.valueOf(R.drawable.magic23));
        arrayList2.add(Integer.valueOf(R.drawable.magic24));
        drawBitmapModels.add(new DrawBitmapModel(R.drawable.heart_1, arrayList2, true, context2));
        ArrayList arrayList3 = new ArrayList();
        arrayList3.add(Integer.valueOf(R.drawable.flower1));
        arrayList3.add(Integer.valueOf(R.drawable.flower1));
        drawBitmapModels.add(new DrawBitmapModel(R.drawable.ic_flower_1, arrayList3, true, context2));
        ArrayList arrayList4 = new ArrayList();
        arrayList4.add(Integer.valueOf(R.drawable.s1));
        arrayList4.add(Integer.valueOf(R.drawable.s1));
        drawBitmapModels.add(new DrawBitmapModel(R.drawable.s1_icon, arrayList4, true, context2));
        ArrayList arrayList5 = new ArrayList();
        arrayList5.add(Integer.valueOf(R.drawable.b1));
        arrayList5.add(Integer.valueOf(R.drawable.b2));
        arrayList5.add(Integer.valueOf(R.drawable.b3));
        drawBitmapModels.add(new DrawBitmapModel(R.drawable.b1_icon, arrayList5, true, context2));
        ArrayList arrayList6 = new ArrayList();
        arrayList6.add(Integer.valueOf(R.drawable.flower3));
        arrayList6.add(Integer.valueOf(R.drawable.flower7));
        arrayList6.add(Integer.valueOf(R.drawable.flower5));
        arrayList6.add(Integer.valueOf(R.drawable.flower6));
        drawBitmapModels.add(new DrawBitmapModel(R.drawable.ic_flower_2, arrayList6, true, context2));
        ArrayList arrayList7 = new ArrayList();
        arrayList7.add(Integer.valueOf(R.drawable.m1));
        arrayList7.add(Integer.valueOf(R.drawable.m2));
        arrayList7.add(Integer.valueOf(R.drawable.m3));
        arrayList7.add(Integer.valueOf(R.drawable.m4));
        drawBitmapModels.add(new DrawBitmapModel(R.drawable.ic_bb2, arrayList7, true, context2));
        ArrayList arrayList8 = new ArrayList();
        arrayList8.add(Integer.valueOf(R.drawable.ss1));
        arrayList8.add(Integer.valueOf(R.drawable.ss2));
        arrayList8.add(Integer.valueOf(R.drawable.ss3));
        arrayList8.add(Integer.valueOf(R.drawable.ss4));
        drawBitmapModels.add(new DrawBitmapModel(R.drawable.ic_flower_3, arrayList8, true, context2));
        ArrayList arrayList9 = new ArrayList();
        arrayList9.add(Integer.valueOf(R.drawable.s17));
        arrayList9.add(Integer.valueOf(R.drawable.s17));
        drawBitmapModels.add(new DrawBitmapModel(R.drawable.smile_icon1, arrayList9, true, context2));
        ArrayList arrayList10 = new ArrayList();
        arrayList10.add(Integer.valueOf(R.drawable.s21));
        arrayList10.add(Integer.valueOf(R.drawable.s21));
        drawBitmapModels.add(new DrawBitmapModel(R.drawable.smile_icon2, arrayList10, true, context2));
        return drawBitmapModels;
    }
}
