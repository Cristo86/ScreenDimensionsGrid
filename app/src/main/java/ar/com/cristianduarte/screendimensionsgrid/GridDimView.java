package ar.com.cristianduarte.screendimensionsgrid;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
        /*
        canvas.drawLine(realDimensionPx.dimension(R.dimen.margin_5_times), 100,
                        realDimensionPx.dimension(R.dimen.margin_5_times), 1000, gridLinePaint);
        canvas.drawLine(bucketDimensionPx.dimension(R.dimen.margin_5_times), 100,
                bucketDimensionPx.dimension(R.dimen.margin_5_times), 1000, gridLinePaint2);

        canvas.drawLine(realDimensionPx.dimension(R.dimen.activity_horizontal_margin), 100,
                realDimensionPx.dimension(R.dimen.activity_horizontal_margin), 600, gridLinePaint);
        canvas.drawLine(bucketDimensionPx.dimension(R.dimen.activity_horizontal_margin), 400,
                bucketDimensionPx.dimension(R.dimen.activity_horizontal_margin), 1000, gridLinePaint2);*/

        Log.d("CRAZY", "80dp withCalc : " + realDimensionPx.dimension(R.dimen.margin_5_times) + " withGetDime: " + bucketDimensionPx.dimension(R.dimen.margin_5_times));
        Log.d("CRAZY", "80dp withCalc : " + realDimensionPx.dimension(R.dimen.activity_horizontal_margin) + " withGetDime: " + bucketDimensionPx.dimension(R.dimen.activity_horizontal_margin));

        float margin = bucketDimensionPx.dimension(R.dimen.activity_horizontal_margin);
        int measuredHeight = getMeasuredHeight();
        for (float x = 0; x < getMeasuredWidth(); x=x+margin) {
            canvas.drawLine(x, 0, x, measuredHeight, gridLinePaint);
        }
    }

    private void init() {
        res = getContext().getResources();
        realDimensionPx = new RealDimensionPx(res);
        bucketDimensionPx = new BucketDimensionPx(res);

        gridLinePaint = new Paint();
        gridLinePaint.setColor(Color.GREEN);
        gridLinePaint2 = new Paint();
        gridLinePaint2.setColor(Color.RED);
    }


    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    public float getDimensionInDp(@DimenRes int id) {
        return res.getDimension(id) / res.getDisplayMetrics().density;
    }

    interface DimensionPx {
        float dimension(@DimenRes int id);
        float dpToPx(float dp);
        float pxToDp(float px);
        int dpToPxInt(float dp);
        int pxToDpInt(float px);
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
            return dp * (displayMetrics.density / DisplayMetrics.DENSITY_DEFAULT);
        }
        @Override
        public float pxToDp(float px) {
            return px / (displayMetrics.density / DisplayMetrics.DENSITY_DEFAULT);
        }
        @Override
        public int dpToPxInt(float dp) {
            return Math.round(dpToPx(dp));
        }
        @Override
        public int pxToDpInt(float px) {
            return Math.round(pxToDp(px));
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
    }

}