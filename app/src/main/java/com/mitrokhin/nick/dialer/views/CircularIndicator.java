package com.mitrokhin.nick.dialer.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.mitrokhin.nick.dialer.R;

/**
 * Created by kolyan on 3/5/17.
 */

public class CircularIndicator extends View {
    private int max = 100;
    private int min = 0;
    private int value = 50;
    private boolean isSetting = false;
    private int lineColor = Color.BLUE;
    private int backLineColor = Color.GRAY;
    private int lineWidth = 10;
    private int textColor = Color.BLACK;
    private int textSize = 100;
    private CircularIndicatorViewInfo viewInfo;
    private CircularIndicatorPainter painter;


    public CircularIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadAttributes(context, attrs);
        init();
    }

    private void loadAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context
                .getTheme()
                .obtainStyledAttributes(
                        attrs,
                        R.styleable.CircularIndicator,
                        0, 0);
        try {
            max = ta.getInteger(R.styleable.CircularIndicator_max, max);
            min = ta.getInteger(R.styleable.CircularIndicator_min, min);
            value = ta.getInteger(R.styleable.CircularIndicator_value, value);
            lineColor = ta.getColor(R.styleable.CircularIndicator_lineColor, lineColor);
            backLineColor = ta.getColor(R.styleable.CircularIndicator_backLineColor, backLineColor);
            lineWidth = ta.getInteger(R.styleable.CircularIndicator_lineWidth, lineWidth);
            textColor = ta.getColor(R.styleable.CircularIndicator_textColor, textColor);
            textSize = ta.getInteger(R.styleable.CircularIndicator_textSize, textSize);
        } finally {
            ta.recycle();
        }
        validateOptions();
    }

    private void init() {
        viewInfo = new CircularIndicatorViewInfo(this);
        painter = new CircularIndicatorPainter(this);
    }

    private void validateOptions() {
        int temp = max;
        max = Math.max(min, max);
        min = Math.min(min, temp);

        value = (value > max) ? max : Math.max(value, min);
    }

    public void setLineColor(int color) {
        if(color != lineColor) {
            lineColor = color;
            redraw();
        }
    }

    public int getLineColor() {
        return lineColor;
    }

    public int getBackLineColor() {
        return backLineColor;
    }

    public void setBackLineColor(int color) {
        if(color != backLineColor) {
            backLineColor = color;
            redraw();
        }
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int width) {
        if(width != lineWidth) {
            lineWidth = width;
            redraw();
        }
    }

    public void setTextColor(int color) {
        if(color != textColor) {
            textColor = color;
            redraw();
        }
    }

    public int getTextColor() {
        return textColor;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int size) {
        if(size != textSize) {
            textSize = size;
            redraw();
        }
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        if(this.max != max) {
            this.max = max;
            redraw();
        }
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        if(this.min != min) {
            this.min = min;
            redraw();
        }
    }

    public void beginUpdate() {
        isSetting = true;
    }

    public void endUpdate() {
        if(isSetting) {
            isSetting = false;
            redraw();
        }
    }

    public void setValue(int value) {
        if(this.value != value) {
            this.value = value;
            redraw();
        }
    }

    public int getValue() {
        return value;
    }

    private void redraw() {
        if(!isSetting) {
            validateOptions();
            invalidate();
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewInfo.calculate();
        setMeasuredDimension(viewInfo.getWidth(), viewInfo.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        painter.drawLine(canvas, viewInfo);
        painter.drawLabel(canvas, viewInfo);
    }
}


class CircularIndicatorViewInfo {
    private static final int MAX_ANGLE = 360;
    private CircularIndicator indicator;
    private Rect bounds;
    private Rect labelBounds;
    private int currentAngle;
    private int width;
    private int height;

    CircularIndicatorViewInfo(CircularIndicator indicator) {
        this.indicator = indicator;
        bounds = new Rect();
        labelBounds = new Rect();
    }

    int getCurrentAngle() {
        return currentAngle;
    }

    Rect getBounds() {
        return new Rect(bounds);
    }

    Rect getLabelBounds() {
        return new Rect(labelBounds);
    }

    int getWidth() {
        return width;
    }

    int getHeight() {
        return height;
    }

    private void calculateIndicatorInfo() {
        int widthWithoutPadding = indicator.getMeasuredWidth() - indicator.getPaddingLeft() - indicator.getPaddingRight(),
                heightWithoutPadding = indicator.getMeasuredHeight() - indicator.getPaddingTop() - indicator.getPaddingBottom();
        int workingSize = Math.min(widthWithoutPadding, heightWithoutPadding),
                lineWidthOffset = (int)Math.floor(indicator.getLineWidth() / (double)2) + 1;
        bounds.set(indicator.getPaddingLeft() + lineWidthOffset, indicator.getPaddingTop() + lineWidthOffset, workingSize - lineWidthOffset, workingSize - lineWidthOffset);
        width = workingSize + indicator.getPaddingLeft() + indicator.getPaddingRight();
        height = workingSize + indicator.getPaddingTop() + indicator.getPaddingBottom();
        double proportion = (indicator.getValue() - indicator.getMin()) / (double)(indicator.getMax() - indicator.getMin());
        currentAngle = (int)Math.ceil(proportion * CircularIndicatorViewInfo.MAX_ANGLE);
    }

    private void calculateLabelInfo() {
        int centerY = (int)Math.ceil(bounds.exactCenterY()),
                centerX = (int)Math.ceil(bounds.exactCenterX()),
                radius = (int)Math.ceil((bounds.width() - indicator.getLineWidth()) / (double)2),
                rectHalfSize = (int)Math.floor(Math.sqrt(Math.pow(radius, 2) * 2) / 2);
        labelBounds.set(centerX - rectHalfSize, centerY - rectHalfSize, centerX + rectHalfSize, centerY + rectHalfSize);
    }

    void calculate() {
        calculateIndicatorInfo();
        calculateLabelInfo();
    }
}


class CircularIndicatorPainter {
    private static final int START_ANGLE = 270;
    private Paint linePaint;
    private Paint backLinePaint;
    private Paint labelPaint;
    private CircularIndicator indicator;

    CircularIndicatorPainter(CircularIndicator indicator) {
        this.indicator = indicator;
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);
        backLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backLinePaint.setStyle(Paint.Style.STROKE);
        labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    void drawLine(Canvas canvas, CircularIndicatorViewInfo viewInfo) {
        linePaint.setColor(indicator.getLineColor());
        linePaint.setStrokeWidth(indicator.getLineWidth());
        backLinePaint.setStrokeWidth(linePaint.getStrokeWidth());
        backLinePaint.setColor(indicator.getBackLineColor());
        RectF drawBounds = new RectF(viewInfo.getBounds());
        canvas.drawArc(drawBounds, viewInfo.getCurrentAngle(), 360, false, backLinePaint);
        canvas.drawArc(drawBounds, CircularIndicatorPainter.START_ANGLE, viewInfo.getCurrentAngle(), false, linePaint);
    }

    void drawLabel(Canvas canvas, CircularIndicatorViewInfo viewInfo) {
        labelPaint.setColor(indicator.getTextColor());
        labelPaint.setTextSize(indicator.getTextSize());
        Rect labelBounds = viewInfo.getLabelBounds();
        String text = Integer.toString(indicator.getValue());
        int charCount = labelPaint.breakText(text, true, labelBounds.width(), null),
                startPos = (text.length() - charCount) / 2;
        Rect textBounds = new Rect();
        labelPaint.getTextBounds(text, startPos, charCount, textBounds);
        canvas.drawText(text, startPos, startPos + charCount,
                (int)Math.floor(labelBounds.exactCenterX() - textBounds.width() / (double)2),
                (int)Math.floor(labelBounds.exactCenterY() + (textBounds.height() - textBounds.height() * 0.3) / (double)2),
                labelPaint);

    }
}