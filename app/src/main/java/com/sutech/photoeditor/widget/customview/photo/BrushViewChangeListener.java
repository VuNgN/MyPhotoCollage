package com.sutech.photoeditor.widget.customview.photo;

interface BrushViewChangeListener {
    void onStartDrawing();

    void onStopDrawing();

    void onViewAdd(com.sutech.photoeditor.widget.customview.photo.BrushDrawingView brushDrawingView);

    void onViewRemoved(BrushDrawingView brushDrawingView);
}
