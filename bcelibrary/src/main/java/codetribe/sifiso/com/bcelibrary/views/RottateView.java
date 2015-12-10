package codetribe.sifiso.com.bcelibrary.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by sifiso on 2015-10-30.
 */
public class RottateView extends TextView {
    public RottateView(Context context) {
        super(context);
    }

    public RottateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RottateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
    }
}
