package ar.com.cristianduarte.screendimensionsgrid;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.DimenRes;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

/**
 * Created by cduarte on 3/11/16.
 */
public class GridDimView extends View {

    private Paint gridLinePaint;
    private Paint gridLinePaint2;

    private Paint gridImportantLinePaint;

    private Resources res;

    private DimensionPx bucketDimensionPx;
    private DimensionPx realDimensionPx;

    public GridDimView(Context context) {
        super(context);
        init();
    }
    public GridDimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public GridDimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    @TargetApi(21)
    public GridDimView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.d("CRAZY", "80dp withCalc : " + realDimensionPx.dimension(R.dimen.margin_5_times) + " withGetDime: " + bucketDimensionPx.dimension(R.dimen.margin_5_times));
        Log.d("CRAZY", "80dp withCalc : " + realDimensionPx.dimension(R.dimen.activity_horizontal_margin) + " withGetDime: " + bucketDimensionPx.dimension(R.dimen.activity_horizontal_margin));

        float margin = bucketDimensionPx.dimension(R.dimen.activity_horizontal_margin);
        int measuredHeight = getMeasuredHeight();
        for (float x = 0; x < getMeasuredWidth(); x=x+margin) {
            canvas.drawLine(x, 0, x, measuredHeight, gridLinePaint);
        }
        // remark the first one
        gridLinePaint.setStrokeWidth(res.getDimension(R.dimen.strong_stroke_w));
        canvas.drawLine(margin, 0, margin, measuredHeight, gridLinePaint);
        gridLinePaint.setStrokeWidth(1);
        for (float y = 0; y < measuredHeight; y=y+margin) {
            canvas.drawLine(0, y, getMeasuredWidth(), y, gridLinePaint2);
        }

        canvas.drawText("16dp", margin + 10, margin, gridLinePaint);

        int w = getMeasuredWidth();
        int textH = res.getDimensionPixelSize(R.dimen.dimensions_text_height);
        canvas.drawLine(0, measuredHeight / 2, getMeasuredWidth(), measuredHeight / 2, gridImportantLinePaint);
        canvas.drawText("" + w + "px | " + bucketDimensionPx.pxToDpInt(w) + "dp" + " (bucket)",
                margin, measuredHeight / 2 - textH, gridImportantLinePaint);
        canvas.drawText(
                "" + w + "px | " + realDimensionPx.pxToDpInt(w) + "dp | " + realDimensionPx.dpi() + "dpi | "
                        + realDimensionPx.pxToCm(w) + "cm"
                        + " (real)",
                margin, measuredHeight / 2 + textH, gridImportantLinePaint);

        canvas.save();
        canvas.rotate(90);

        canvas.drawText("" + measuredHeight + "px | " + realDimensionPx.pxToDp(measuredHeight) + " dp | "
                + realDimensionPx.pxToCm(measuredHeight) + "cm + "
                + realDimensionPx.pxToCm(realDimensionPx.dimension(R.dimen.statusbar_height))
                + "cm (" + realDimensionPx.pxToDp(realDimensionPx.dimension(R.dimen.statusbar_height))
                + "dp) "
                + " = " + (realDimensionPx.pxToCm(realDimensionPx.dimension(R.dimen.statusbar_height)) + realDimensionPx.pxToCm(measuredHeight))
                + "cm"
                , realDimensionPx.dimension(R.dimen.actionbar_height), -w + textH, gridImportantLinePaint);

        canvas.drawLine(0, measuredHeight / 2, getMeasuredWidth(), measuredHeight / 2, gridImportantLinePaint);

        canvas.restore();

    }

    private void init() {

        res = getContext().getResources();
        realDimensionPx = new RealDimensionPx(res);
        bucketDimensionPx = new BucketDimensionPx(res);

        gridLinePaint = new Paint();
        gridLinePaint.setColor(res.getColor(R.color.greenTranslucid));
        gridLinePaint.setTextSize(res.getDimensionPixelSize(R.dimen.dimensions_text_size));
        gridLinePaint2 = new Paint();
        gridLinePaint2.setColor(res.getColor(R.color.redTranslucid));

        gridImportantLinePaint = new Paint();
        gridImportantLinePaint.setColor(res.getColor(R.color.green_dark));
        gridImportantLinePaint.setStrokeWidth(res.getDimension(R.dimen.strong_stroke_w));
        gridImportantLinePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        gridImportantLinePaint.setTextSize(res.getDimensionPixelSize(R.dimen.dimensions_text_size));

    }

    interface DimensionPx {
        float dimension(@DimenRes int id);
        float dpToPx(float dp);
        float pxToDp(float px);
        int dpToPxInt(float dp);
        int pxToDpInt(float px);
        float dpi();
        float pxToCm(float px);
    }

    static class BucketDimensionPx implements  DimensionPx {
        private Resources res;
        private DisplayMetrics displayMetrics;
        BucketDimensionPx (Resources res) {
            this.res = res;
            this.displayMetrics = res.getDisplayMetrics();
        }
        public float dimension(@DimenRes int id) {
            return res.getDimension(id);
        }
        @Override
        public float dpToPx(float dp) {
            return dp * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        }
        @Override
        public float pxToDp(float px) {
            return px / (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        }
        @Override
        public int dpToPxInt(float dp) {
            return Math.round(dpToPx(dp));
        }
        @Override
        public int pxToDpInt(float px) {
            return Math.round(pxToDp(px));
        }
        @Override
        public float dpi() { return displayMetrics.xdpi; }
        @Override
        public float pxToCm(float px) {
            float dp = pxToDp(px);
            return (float) 2.54 * (px / (160 * (px/dp)));
            /*
                Given equation
                px = dp * (dpi / 160)
                so...
                px = dp * ((dots/inch) / 160)
                px / dp = ((dots/inch) / 160)
                160 * (px / dp) = dots/inch
                inch = dots / (160 * (px / dp))

                those dots are the same pixels
                inch  = px / (160 * (px / dp))
            * */
        }
    }

    static class RealDimensionPx implements  DimensionPx {
        private Resources res;
        private DisplayMetrics displayMetrics;
        RealDimensionPx (Resources res) {
            this.res = res;
            this.displayMetrics = res.getDisplayMetrics();
        }
        public float dimension(@DimenRes int id) {
            return dpToPx(getDimensionInDp(id) );
        }
        @Override
        public float dpToPx(float dp) {
            return dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT);
        }
        @Override
        public float pxToDp(float px) {
            return px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT);
        }
        private float getDimensionInDp(@DimenRes int id) {
            return res.getDimension(id) / res.getDisplayMetrics().density;
        }
        @Override
        public int dpToPxInt(float dp) {
            return Math.round(dpToPx(dp));
        }
        @Override
        public int pxToDpInt(float px) {
            return Math.round(pxToDp(px));
        }
        @Override
        public float dpi() { return displayMetrics.xdpi; }
        @Override
        public float pxToCm(float px) {
            float dp = pxToDp(px);
            return (float) 2.54 * (px / (160 * (px/dp)));
        }
    }

}