package com.sutech.photoeditor.editor.sticker.event;

import android.view.MotionEvent;

import com.sutech.photoeditor.editor.sticker.StickerView;

public interface StickerIconEvent {
    void onActionDown(StickerView stickerView, MotionEvent motionEvent);

    void onActionMove(StickerView stickerView, MotionEvent motionEvent);

    void onActionUp(StickerView stickerView, MotionEvent motionEvent);
}
