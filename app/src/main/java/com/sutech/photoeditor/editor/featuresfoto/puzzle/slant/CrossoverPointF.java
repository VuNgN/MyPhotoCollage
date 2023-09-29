package com.sutech.photoeditor.editor.featuresfoto.puzzle.slant;

import android.graphics.PointF;

class CrossoverPointF extends PointF {
    com.sutech.photoeditor.editor.featuresfoto.puzzle.slant.SlantLine horizontal;
    com.sutech.photoeditor.editor.featuresfoto.puzzle.slant.SlantLine vertical;

    CrossoverPointF() {
    }

    CrossoverPointF(float f, float f2) {
        this.x = f;
        this.y = f2;
    }

    CrossoverPointF(com.sutech.photoeditor.editor.featuresfoto.puzzle.slant.SlantLine slantLine, com.sutech.photoeditor.editor.featuresfoto.puzzle.slant.SlantLine slantLine2) {
        this.horizontal = slantLine;
        this.vertical = slantLine2;
    }


    public void update() {
        if (this.horizontal != null && this.vertical != null) {
            com.sutech.photoeditor.editor.featuresfoto.puzzle.slant.SlantUtils.intersectionOfLines(this, this.horizontal, this.vertical);
        }
    }
}
