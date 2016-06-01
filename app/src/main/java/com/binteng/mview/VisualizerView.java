package com.binteng.mview;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.view.View;

/**
 * 自定义声音波浪
 *
 */
public class VisualizerView extends View {

	private byte[] mBytes;// 波形数组
	private Paint mPaint = new Paint();// 主画笔
	private int mSpectrumNum = 7;// 取样数

	// 波浪顶部动态圆系数
	RectF cricle2;

	// 波浪底部固定圆系数
	RectF cricle1;

	Path path1;// 底部梯形

	/**
	 * 所有 波浪的参数
	 */

	private int mViewWidth;// 宽
	private int mViewHeight;// 高

	private float mLevelLine = 0;// 水位线
	private float mWaveHeight = 80;// 波浪起伏幅度
	private float mWaveWidth = 200;// 波长
	private float mLeftSide;// 被隐藏的最左边的波形
	private float mMoveLen;// 水波平移的距离
	public static final float SPEED = 1.7f;// 水波平移速度
	private List<Point> mPointsList;// 波浪顶部的点阵
	private Path mWavePath;// 波浪的路径
	private boolean isMeasured = false;

	private Timer timer;
	private MyTimerTask mTask;

	private float progress = 100;// 进度
	private double mHeight;// 波浪幅度

	public void setProgress(float i) {
		this.progress = (100 - i) / 100f;
	}

	protected float getProgress() {
		return this.progress;
	}

	public double getmHeight() {
		return this.mHeight;
	}

	public void setmHeight(double i) {
		this.mHeight = i;
	}

	@SuppressLint("HandlerLeak")
	Handler updateHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 记录平移总位移
			mMoveLen += SPEED;
			// 根据View高度和传过来的进度计算水位线高度
			mLevelLine = getProgress() * mViewHeight;
			// 根据View宽度和传过来的振幅计算波形峰值
			mWaveHeight = (float) (mViewWidth / (getmHeight() * 10f));

			mLeftSide += SPEED;
			// 波形平移
			for (int i = 0; i < mPointsList.size(); i++) {
				mPointsList.get(i).setX(mPointsList.get(i).getX() + SPEED);
				switch (i % 4) {
					case 0:
					case 2:
						mPointsList.get(i).setY(mLevelLine);
						break;
					case 1:
						mPointsList.get(i).setY(mLevelLine + mWaveHeight);
						break;
					case 3:
						mPointsList.get(i).setY(mLevelLine - mWaveHeight);
						break;
				}
			}
			if (mMoveLen >= mWaveWidth) {
				// 波形平移超过一个完整波形后复位
				mMoveLen = 0;
				resetPoints();
			}

		}

	};

	/**
	 * 所有点的x坐标都还原到初始状态，也就是一个周期前的状态
	 */
	private void resetPoints() {
		mLeftSide = -mWaveWidth;
		for (int i = 0; i < mPointsList.size(); i++) {
			mPointsList.get(i).setX(i * mWaveWidth / 4 - mWaveWidth);
		}
	}

	public VisualizerView(Context context) {
		super(context);
		init();
	}

	private void init() {
		mBytes = null;

		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.parseColor("#E54B4C"));

		mPointsList = new ArrayList<Point>();
		timer = new Timer();
		mWavePath = new Path();
	}

	/**
	 * 根据声波数组更新波浪的数据
	 *
	 * @param fft
	 */
	public void updateVisualizer(byte[] fft) {

		byte[] model = new byte[fft.length / 2 + 1];

		model[0] = (byte) Math.abs(fft[0]);
		for (int i = 2, j = 1; j < mSpectrumNum;) {
			model[j] = (byte) Math.hypot(fft[i], fft[i + 1]);
			i += 2;
			j++;
		}
		mBytes = model;
		invalidate();// 重绘

	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		super.onWindowFocusChanged(hasWindowFocus);
		// 开始波动
		start();
	}

	private void start() {
		if (mTask != null) {
			mTask.cancel();
			mTask = null;
		}
		mTask = new MyTimerTask(updateHandler);
		timer.schedule(mTask, 0, 10);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (!isMeasured) {
			isMeasured = true;

			mViewWidth = getMeasuredWidth();
			mViewHeight = getMeasuredWidth();

			// 根据View宽度计算波形峰值
			mWaveHeight = mViewWidth / 10f;

			// 水位线从最底下开始上升
			mLevelLine = mViewHeight;

			// 波长等于四倍View宽度也就是View中只能看到四分之一个波形，这样可以使起伏更明显
			mWaveWidth = mViewWidth / 4;
			// 左边隐藏的距离预留一个波形
			mLeftSide = -mWaveWidth;
			// 这里计算在可见的View宽度中能容纳几个波形，注意n上取整
			int n = (int) Math.round(mViewWidth / mWaveWidth + 0.5);
			// n个波形需要4n+1个点，但是我们要预留一个波形在左边隐藏区域，所以需要4n+5个点
			for (int i = 0; i < (4 * n + 5); i++) {
				// 从P0开始初始化到P4n+4，总共4n+5个点
				float x = i * mWaveWidth / 4 - mWaveWidth;
				float y = 0;
				switch (i % 4) {
					case 0:
					case 2:
						// 零点位于水位线上
						y = mLevelLine + mWaveHeight;
						break;
					case 1:
						// 往下波动的控制点
						y = mLevelLine + 2 * mWaveHeight;
						break;
					case 3:
						// 往上波动的控制点
						y = mLevelLine;
						break;
				}
				mPointsList.add(new Point(x, y));
			}
		}
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawColor(Color.WHITE);
		if (mBytes == null) {
			return;
		}

		int padding = 100;
		final int baseX = (getWidth() - 2 * padding) / mSpectrumNum;
		final int height = getHeight();
		int r = baseX / 2;

		// 绘制频谱
		for (int i = 0; i < mSpectrumNum; i++) {
			if (mBytes[i] < 0) {
				mBytes[i] = 127;
			}

			int centerx = baseX * i + baseX / 2 + padding;

			int left = baseX * i + padding;
			int right = left + 2 * r;
			int top = height - mBytes[i] - 2 * r;

			cricle1 = new RectF(left, height - 2 * r, right, height);// 底部圆

			int r1 = (int) (r - (mBytes[i] / 8) * 1.2);
			cricle2 = new RectF(centerx - r1, top, centerx + r1, top + 2// 顶部圆
					* r1);

			canvas.drawOval(cricle1, mPaint);// 画圆
			canvas.drawOval(cricle2, mPaint);// 画圆

			path1 = new Path(); // 梯
			path1.moveTo(left, height - r);
			path1.lineTo(centerx - r1, top + r1);
			path1.lineTo(centerx + r1, top + r1);
			path1.lineTo(right, height - r);
			path1.lineTo(left, height - r);

			canvas.drawPath(path1, mPaint);// 画梯形
		}


		// 画底部的波浪
		mWavePath.reset();
		int i = 0;
		mWavePath.moveTo(mPointsList.get(0).getX(), mPointsList.get(0).getY());
		for (; i < mPointsList.size() - 2; i = i + 2) {
			mWavePath.quadTo(mPointsList.get(i + 1).getX(),
					mPointsList.get(i + 1).getY(), mPointsList.get(i + 2)
							.getX(), mPointsList.get(i + 2).getY());
		}
		mWavePath.lineTo(mPointsList.get(i).getX(), height);
		mWavePath.lineTo(mLeftSide, height);
		mWavePath.close();
		// mPaint的Style是FILL，会填充整个Path区域
		canvas.drawPath(mWavePath, mPaint);
	}


	class MyTimerTask extends TimerTask {
		Handler handler;

		public MyTimerTask(Handler handler) {
			this.handler = handler;
		}

		@Override
		public void run() {
			handler.sendMessage(handler.obtainMessage());
		}

	}

	/**
	 * 坐标点的实体类
	 *
	 * @author Administrator
	 *
	 */

	class Point {
		private float x;
		private float y;

		public float getX() {
			return x;
		}

		public void setX(float x) {
			this.x = x;
		}

		public float getY() {
			return y;
		}

		public void setY(float y) {
			this.y = y;
		}

		public Point(float x, float y) {
			this.x = x;
			this.y = y;
		}

	}

}
