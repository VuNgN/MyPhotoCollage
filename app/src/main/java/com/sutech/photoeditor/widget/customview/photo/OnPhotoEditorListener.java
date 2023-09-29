package com.sutech.photoeditor.widget.customview.photo;

import android.view.View;

public interface OnPhotoEditorListener {
    void onAddViewListener(com.sutech.photoeditor.widget.customview.photo.ViewType viewType, int i);

    void onEditTextChangeListener(View view, String str, int i);

    @Deprecated
    void onRemoveViewListener(int i);

    void onRemoveViewListener(com.sutech.photoeditor.widget.customview.photo.ViewType viewType, int i);

    void onStartViewChangeListener(com.sutech.photoeditor.widget.customview.photo.ViewType viewType);

    void onStopViewChangeListener(ViewType viewType);
}
