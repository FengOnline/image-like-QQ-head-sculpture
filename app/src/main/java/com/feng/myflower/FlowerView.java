package com.feng.myflower;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


public class FlowerView extends View {

	Bitmap[] mFlowers = new Bitmap[3];
	MyFlower flowers [] = new MyFlower[35];
	Random r = new Random();
	Matrix m = new Matrix();
	Paint p = new Paint();

	int mW=480;
	int mH=100;
	Context context;

	public FlowerView(Context context) {
		super(context);
		this.context=context;
		initFlower();
	}

	public FlowerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context=context;
		initFlower();
	}

	public FlowerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		initFlower();
	}

	private void initFlower() {
		loadFlower();
		addRect();

		Timer myTimer = new Timer();
		TimerTask mTask = new TimerTask() {
			@Override
			public void run() {
				invalidateView();
			}
		};
		myTimer.schedule(mTask, 3000, 5);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mW = getMeasuredWidth();
		mH = getMeasuredHeight();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		Log.e("TAG", "----------------");
		for (int i = 0; i < flowers.length; i++) {
			MyFlower rect = flowers[i];
			if (rect.y<=mH) {
				rect.y += rect.g;
				rect.x+=rect.g-8;
				canvas.save();
				m.reset();
				m.setScale(rect.s, rect.s);
				canvas.setMatrix(m);
				p.setAlpha(rect.a);
				canvas.drawBitmap(mFlowers[rect.type], 2*rect.x, 2*rect.y, p);//图片太大 所以图片缩小一半 坐标放大一倍
				canvas.restore();
			}else {
				rect.init();
			}

			if (rect.x >= mW || rect.x < - 20) {
				rect.init();
			}
			flowers[i] = rect;
		}
	}

	public void loadFlower(){
		mFlowers[0] = ((BitmapDrawable)getContext().getResources().getDrawable(R.mipmap.flower1)).getBitmap();
		mFlowers[1] = ((BitmapDrawable)getContext().getResources().getDrawable(R.mipmap.flower2)).getBitmap();
		mFlowers[2] = ((BitmapDrawable)getContext().getResources().getDrawable(R.mipmap.flower3)).getBitmap();
	}

	public void recly(){
		for (int i = 0; i < mFlowers.length; i++) {
			if (mFlowers[i] != null && !mFlowers[i].isRecycled()) {
				mFlowers[i].recycle();
			}
		}
	}

	public void addRect(){
		for (int i = 0; i < flowers.length; i++) {
			flowers[i] = new MyFlower();
		}
	}

	public void invalidateView(){
		if (Looper.getMainLooper() == Looper.myLooper()) {
			invalidate();
		} else {
			postInvalidate();
		}
	}


	class MyFlower{
		int x;
		int y;
		float s;//尺寸
		int a;//透明度
		int g;//降落速度
		int type;//类型 3种

		public void init(){
			float aa = r.nextFloat();
			x = r.nextInt(mW);
			y = 0;
			if(aa >= 0.5) {
				s = 0.5f;
			}else if (aa <= 0.2) {
				s = 0.3f;
			}else{
				s = aa;
			}
			a = r.nextInt(155) + 100;
			g = r.nextInt(15)+5;                                                                                                                      ;
			type=r.nextInt(3);
		}

		public MyFlower(){
			super();
			init();
		}

	}

}