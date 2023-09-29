package com.sutech.photoeditor.editor.sticker.event;

import android.view.MotionEvent;

import com.sutech.photoeditor.editor.sticker.StickerView;

public class AlignHorizontallyEvent implements StickerIconEvent {
    public void onActionDown(StickerView stickerView, MotionEvent motionEvent) {
    }

    public void onActionMove(StickerView stickerView, MotionEvent motionEvent) {
    }

    public void onActionUp(StickerView stickerView, MotionEvent motionEvent) {
        stickerView.alignHorizontally();
    }
}
