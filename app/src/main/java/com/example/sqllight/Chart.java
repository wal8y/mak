package com.example.sqllight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

public class Chart extends View {
    private List<Expense> expenses;
    private Paint paint;
    private RectF rectF;
    private Random random;
    TextView tv;

    public Chart(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        random = new Random();
        rectF = new RectF();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int padding = 50;
        int size = Math.min(w, h) - (padding * 2);
        int left = (w - size) / 2;
        int top = (h - size) / 2;
        rectF.set(left, top, left + size, top + size);
    }

    public void setData(List<Expense> expenses) {
        this.expenses = expenses;
        invalidate();
    }

    private int generateRandomColor() {
        return Color.argb(255,
                random.nextInt(256),
                random.nextInt(256),
                random.nextInt(256));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (expenses == null || expenses.isEmpty()) {
            paint.setColor(Color.BLACK);
            paint.setTextSize(50);
            paint.setTextAlign(Paint.Align.CENTER);
            float x = getWidth() / 2f;
            float y = getHeight() / 2f;
            canvas.drawText("NO RECORDS FOUND", x, y, paint);
            return;
        }

        float totalAmount = 0;
        for (Expense expense : expenses) {
            totalAmount += expense.cash;
        }

        float startAngle = 0;
        for (Expense expense : expenses) {
            float sweepAngle = (expense.cash / totalAmount) * 360;
            paint.setColor(generateRandomColor());
            canvas.drawArc(rectF, startAngle, sweepAngle, true, paint);
            float textAngle = startAngle + (sweepAngle / 2);
            float textX = (float) (rectF.centerX() + (rectF.width() / 3) * Math.cos(Math.toRadians(textAngle)));
            float textY = (float) (rectF.centerY() + (rectF.height() / 3) * Math.sin(Math.toRadians(textAngle)));
            paint.setColor(Color.WHITE);
            paint.setTextSize(30);
            canvas.drawText(expense.type, textX, textY, paint);

            startAngle += sweepAngle;
        }
    }
}
