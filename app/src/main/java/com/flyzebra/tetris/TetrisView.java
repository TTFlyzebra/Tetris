package com.flyzebra.tetris;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

@SuppressLint("UseSparseArrays")
public class TetrisView extends View implements OnTouchListener {	
	public static final String TAG = "com.flyzebra";
	private Context context;
	// View测量获得的长和宽
	private int width;
//	private int height;
	// 方块画笔//边框的画笔
	private Paint block_paint1;
	private Paint block_paint2;
	private Paint text_paint1;
	private Paint text_paint2;
	private Paint text_paint3;
	private Paint back_paint1;
	private Paint back_paint2;
	private Paint button_paint1;
	private Paint button_paint2;
	// 方块大小
	private int block_size;
	private int offset_x, offset_y;
	// 定义方块数组
	private int block_array[][] = new int[20][10];
	private Rect view_rect = new Rect();
	
	private TetriShape cret_tetrishape;
	private TetriShape next_tetrishape;	
	private boolean gamepause = false;
	private boolean gamestop = true;
	private boolean gamemusic = true;
	// 按钮
	private float radius1, radius2, radius3;
	private float up_x;
	private float up_y;
	private float down_x;
	private float down_y;
	private float left_x;
	private float left_y;
	private float right_x;
	private float right_y;
	private float exit_x;
	private float exit_y;
	private float start_x;
	private float start_y;
	private float pause_x;
	private float pause_y;
	private float music_x;
	private float music_y;
	private float rotate_x;
	private float rotate_y;
//	private ResetGameTask resetTask;
	
	private boolean isDownLeft = false, isDownRight = false, isDownUp = false,
			isDownDown = false, isDownExit = false, isDownStart = false,
			isDownPasue = false, isDownMusic = false, isDownRotate = false;

	// 声音
	SoundPool sp;
	HashMap<Integer, Integer> hm; 
	int sound_1=0;
	int sound_2=0;
	int sound_3=0;

	private int cret_score = 0;
	private int base_score = 100;
	private int speed_score = 5000;
	private int speed = 1;

	private String ME_SHARE = "Tetri";
	private String H_SCORE = "h_score";
	private int high_score = 0;
	private String H_TIME = "h_time";
	private String high_time = "00:00:00";
	private int count_time = 0;
	
	//线程控制变量
	private boolean TC_CountTimeThread = true;
	private boolean TC_StartGameThread = true;
	private boolean TC_ResetGameThread = true;
	
	private Bitmap bt_bitmap01,bt_bitmap02,bk_bitmap03;
	private Rect bt_rect01,bt_rect02,bk_rect03;
	private Rect up_rect;
	private Rect down_rect;
	private Rect left_rect;
	private Rect right_rect;
	private Rect rotate_rect;
	private Rect exit_rect;
	private Rect start_rect;
	private Rect pause_rect;
	private Rect music_rect;

	@SuppressLint("ClickableViewAccessibility")
	public TetrisView(Context context) {
		super(context);
		this.context = context;

		block_paint1 = new Paint();
		block_paint1.setAntiAlias(true);
		block_paint1.setStyle(Paint.Style.FILL);
		block_paint1.setColor(Color.parseColor("#222222"));		

		block_paint2 = new Paint();
		block_paint2.setAntiAlias(true);
		block_paint2.setStyle(Paint.Style.FILL);
		block_paint2.setColor(Color.parseColor("#00FF00"));

		text_paint1 = new Paint();
		text_paint1.setColor(Color.parseColor("#FFFFFF"));
		text_paint1.setAntiAlias(true);
		text_paint1.setStyle(Paint.Style.FILL);
		text_paint1.setTextAlign(Paint.Align.CENTER);

		text_paint2 = new Paint();
		text_paint2.setColor(Color.parseColor("#BFFF00"));
		text_paint2.setAntiAlias(true);
		text_paint2.setStyle(Paint.Style.FILL);
		text_paint2.setTextAlign(Paint.Align.CENTER);

		text_paint3 = new Paint();
		text_paint3.setColor(Color.parseColor("#FFFF00"));
		text_paint3.setAntiAlias(true);
		text_paint3.setStyle(Paint.Style.FILL);
		text_paint3.setTextAlign(Paint.Align.CENTER);

		back_paint1 = new Paint();
		back_paint1.setAntiAlias(true);
		back_paint1.setStrokeWidth(4);
		back_paint1.setStyle(Paint.Style.STROKE);
		back_paint1.setColor(Color.parseColor("#FFFF00"));
		
		back_paint2 = new Paint();
		back_paint2.setAntiAlias(true);
		back_paint2.setStyle(Paint.Style.FILL);
		back_paint2.setColor(Color.parseColor("#000000"));
		
		button_paint1 = new Paint();
		button_paint1.setAntiAlias(true);
		button_paint1.setStyle(Paint.Style.FILL);
		button_paint1.setColor(Color.parseColor("#FFFF00"));
		
		button_paint2 = new Paint();
		button_paint2.setAntiAlias(true);
		button_paint2.setStyle(Paint.Style.FILL);
		button_paint2.setColor(Color.parseColor("#BBBB00"));

		bt_bitmap01=BitmapFactory.decodeResource(getResources(), R.drawable.button01);
		bt_bitmap02=BitmapFactory.decodeResource(getResources(), R.drawable.button02);
		bk_bitmap03=BitmapFactory.decodeResource(getResources(), R.drawable.bk_view);
		bt_rect01=new Rect(0, 0, bt_bitmap01.getWidth(), bt_bitmap01.getHeight());
		bt_rect02=new Rect(0, 0, bt_bitmap02.getWidth(), bt_bitmap02.getHeight());
		bk_rect03=new Rect(0, 0, bk_bitmap03.getWidth(), bk_bitmap03.getHeight());
		
		TC_StartGameThread = false;
		TC_CountTimeThread = false;
		TC_ResetGameThread = false;
		gamepause = false;

		setOnTouchListener(this);
		
		// 声音
		initSoundPool();
		sound_2 = playSound(2, 0);

		// 初始化最高分数
		SharedPreferences m_share = context.getSharedPreferences(ME_SHARE, Activity.MODE_PRIVATE);
		high_score = m_share.getInt(H_SCORE, 0);
		high_time = m_share.getString(H_TIME, "00:00:00");
		cret_score = 0;
		count_time = 0;		
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 绘制多余的边框
//		canvas.drawRect(0, 0, offset_x, height, back_paint1);
//		canvas.drawRect(width - offset_x, 0, width, height, back_paint1);
//		canvas.drawRect(0, 0, width, offset_y, back_paint1);
//		canvas.drawRect(0, offset_y + block_size * 20, width, height, back_paint1);
		//背景图片
		canvas.drawBitmap(bk_bitmap03, bk_rect03, view_rect, back_paint1);
		canvas.drawRect(view_rect,back_paint1);
		//游戏显示屏区背景
		canvas.drawRect(offset_x-5,offset_y-5,width - offset_x+5,offset_y+block_size * 20+5,back_paint1);
		canvas.drawRect(offset_x,offset_y,width - offset_x,offset_y+block_size * 20,back_paint2);
		// 绘制右边的显示组件
		int x = offset_x + block_size * 11;
		int y = offset_y; 
		// 右边框
		int l_y = x+block_size*4;
		canvas.drawRect(x, y, l_y, y+block_size*4, block_paint1);
//		canvas.drawRect(x, y+block_size*5, l_y, y+block_size*8f, block_paint1);
//		canvas.drawRect(x, y+block_size*9, l_y, y+block_size*11f, block_paint1);
//		canvas.drawRect(x, y+block_size*12, l_y, y+block_size*14f, block_paint1);
//		canvas.drawRect(x, y+block_size*15, l_y, y+block_size*17f, block_paint1);
//		canvas.drawRect(x, y+block_size*18, l_y, y+block_size*19.5f, block_paint1);
		if (next_tetrishape != null) {
			drawNextBlock(x,y,canvas);	
		}
		x = x+block_size*2;
		// 最高得分
		canvas.drawText("最高记录", x, y + block_size * 6, text_paint1);
		canvas.drawText(high_score + "分", x, y + block_size * 7, text_paint2);
		canvas.drawText(high_time, x, y + block_size * 8, text_paint2);
		// 本局得分
		canvas.drawText("本局得分", x, y + block_size * 10, text_paint1);
		canvas.drawText(cret_score + "分", x, y + block_size * 11, text_paint2);
		// 本局耗时
		canvas.drawText("本局耗时", x, y + block_size * 13, text_paint1);
		String time = getStrCurrentTime();
		canvas.drawText(time, x, y + block_size * 14, text_paint2);
		// 当前下落速度
		canvas.drawText("速度等级", x, y + block_size * 16, text_paint1);
		canvas.drawText(speed + "级", x, y + block_size * 17, text_paint2);
		if(gamemusic){
			canvas.drawText("音乐开", x, y + block_size * 19, text_paint2);
		}else{
			canvas.drawText("音乐关", x, y + block_size * 19, text_paint2);
		}
		
		// 按钮界面
		// up
		if (isDownUp) {
//			canvas.drawCircle(up_x, up_y, radius1, button_paint1);			
			canvas.drawBitmap(bt_bitmap01, bt_rect01, up_rect, button_paint1);
		} else {
//			canvas.drawCircle(up_x, up_y, radius1, button_paint2);
			canvas.drawBitmap(bt_bitmap02, bt_rect02, up_rect, button_paint2);
		}
		// down
		if (isDownDown) {
//			canvas.drawCircle(down_x, down_y, radius1, button_paint1);
			canvas.drawBitmap(bt_bitmap01, bt_rect01, down_rect, button_paint1);
		} else {
//			canvas.drawCircle(down_x, down_y, radius1, button_paint2);
			canvas.drawBitmap(bt_bitmap02, bt_rect02, down_rect, button_paint2);
		}
		// left
		if (isDownLeft) {
//			canvas.drawCircle(left_x, left_y, radius1, button_paint1);
			canvas.drawBitmap(bt_bitmap01, bt_rect01, left_rect, button_paint1);
		} else {
//			canvas.drawCircle(left_x, left_y, radius1, button_paint2);
			canvas.drawBitmap(bt_bitmap02, bt_rect02, left_rect, button_paint2);
		}
		// right
		if (isDownRight) {
//			canvas.drawCircle(right_x, right_y, radius1, button_paint1);
			canvas.drawBitmap(bt_bitmap01, bt_rect01, right_rect, button_paint1);
		} else {
//			canvas.drawCircle(right_x, right_y, radius1, button_paint2);
			canvas.drawBitmap(bt_bitmap02, bt_rect02, right_rect, button_paint2);
		}
				
		// exit
		if (isDownExit) {
//			canvas.drawCircle(exit_x, exit_y, radius2, button_paint1);
			canvas.drawBitmap(bt_bitmap01, bt_rect01, exit_rect, button_paint1);
		} else {
//			canvas.drawCircle(exit_x, exit_y, radius2, button_paint2);
			canvas.drawBitmap(bt_bitmap02, bt_rect02, exit_rect, button_paint2);
		}
		canvas.drawText("退出", exit_x, exit_y + block_size, text_paint3);
		// start
		if (isDownStart) {
//			canvas.drawCircle(start_x, start_y, radius2, button_paint1);
			canvas.drawBitmap(bt_bitmap01, bt_rect01, start_rect, button_paint1);
		} else {
//			canvas.drawCircle(start_x, start_y, radius2, button_paint2);
			canvas.drawBitmap(bt_bitmap02, bt_rect02, start_rect, button_paint2);
		}
		canvas.drawText("开始", start_x, exit_y + block_size,	text_paint3);
		// pause
		if (isDownPasue) {
//			canvas.drawCircle(pause_x, pause_y, radius2, button_paint1);
			canvas.drawBitmap(bt_bitmap01, bt_rect01, pause_rect, button_paint1);
		} else {
//			canvas.drawCircle(pause_x, pause_y, radius2, button_paint2);
			canvas.drawBitmap(bt_bitmap02, bt_rect02, pause_rect, button_paint2);
		}
		canvas.drawText("暂停", pause_x, exit_y + block_size,	text_paint3);
		// music
		if (isDownMusic) {
//			canvas.drawCircle(music_x, music_y, radius2, button_paint1);
			canvas.drawBitmap(bt_bitmap01, bt_rect01, music_rect, button_paint1);
		} else {
//			canvas.drawCircle(music_x, music_y, radius2, button_paint2);
			canvas.drawBitmap(bt_bitmap02, bt_rect02, music_rect, button_paint2);
		}
		canvas.drawText("音乐", music_x, exit_y + block_size,	text_paint3);
		
		// rotate
		if (isDownRotate) {
//			canvas.drawCircle(rotate_x, rotate_y, radius3, button_paint1);
			canvas.drawBitmap(bt_bitmap01, bt_rect01, rotate_rect, button_paint1);
		} else {
//			canvas.drawCircle(rotate_x, rotate_y, radius3, button_paint2);
			canvas.drawBitmap(bt_bitmap02, bt_rect02, rotate_rect, button_paint2);
		}
		// 当前方块
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 20; j++) {
				if (block_array[j][i] == 1) {
					drawblock(i, j, canvas, block_paint2);
				} else {
					drawblock(i, j, canvas, block_paint1);
				}
			}
		}
	}

	private void drawblock(float w, float h, Canvas canvas, Paint paint) {
		float left = offset_x + w * block_size + 1;
		float top = offset_y + h * block_size + 1;
		float right = left + block_size - 2;
		float bottom = top + block_size - 2;
		canvas.drawRect(left, top, right, bottom, paint);
	}

	// 向左移动
	private void moveLeft() {
		move(cret_tetrishape.getLeft() - 1, cret_tetrishape.getTop(),
				cret_tetrishape.getRight() - 1, cret_tetrishape.getBottom());
	}

	// 向右移动
	private void moveRight() {
		move(cret_tetrishape.getLeft() + 1, cret_tetrishape.getTop(),
				cret_tetrishape.getRight() + 1, cret_tetrishape.getBottom());
	}

	// 向下移动
	private synchronized void moveDown() {
		if(cret_tetrishape==null||gamepause) return;
		boolean flag = move(cret_tetrishape.getLeft(), cret_tetrishape.getTop() + 1,
				cret_tetrishape.getRight(), cret_tetrishape.getBottom() + 1);
		if (!flag) {
			// 判断游戏是否结束
			for (int i = 0; i < 10; i++) {
				if (block_array[0][i] == 1) {
					// 游戏结束
					gamestop = true;					
					break;
				}
			}
			if (!gamestop) {
				isDownDown = false;
				cret_tetrishape = next_tetrishape;
				next_tetrishape = new TetriShape();
				clearFullRows();
			} else {
				if(sound_2!=0){
					sp.stop(sound_2);
				}
				sound_2 = playSound(2,0);
				if(TC_ResetGameThread==false){
					TC_ResetGameThread=true;
					new ResetGameThread();
				}
//				if(resetTask==null){
//					resetTask = new ResetGameTask();
//					resetTask.execute();
//				}else{
//					resetTask.cancel(true);
//					resetTask=null;
//					resetTask = new ResetGameTask();
//					resetTask.execute();
//				}
			}
		}
	}

	private void clearFullRows() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				int num = 0;
				int new_block_array[][] = new int[20][10];
				int new_h = 19;
				for (int j = 19; j >= 0; j--) {
					boolean is_full = true;
					for (int i = 0; i < 10; i++) {
						if ((block_array[j][i]) == 0) {
							is_full = false;
							break;
						}
					}
					if (is_full) {
						num++;
						for (int i = 0; i < 10; i++) {
							block_array[j][i] = 0;
						}
					} else {
						for (int i = 0; i < 10; i++) {
							new_block_array[new_h][i] = block_array[j][i];
						}
						new_h--;
					}
				}
				if (num > 0) {
					if(sound_1!=0){
						sp.stop(sound_1);						
					}
					sound_1 = playSound(1, 0);
					cret_score = cret_score + num * num * base_score;
					// 更新最高记录
					if (cret_score > high_score) {
						high_score=cret_score;
						high_time=getStrCurrentTime();
						SharedPreferences m_shares = context.getSharedPreferences(ME_SHARE,Activity.MODE_PRIVATE);
						SharedPreferences.Editor editor = m_shares.edit();
						editor.putInt(H_SCORE, high_score);
						editor.putString(H_TIME,high_time );
						editor.commit();
					}
					speed = cret_score / speed_score + 1;
					if (speed > 9) {
						speed = 9;
					}
					postInvalidate();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					block_array = new_block_array.clone();
					postInvalidate();
				} else {
					if(sound_3!=0){
						sp.stop(sound_3);						
					}
					sound_3 = playSound(3, 0);
				}
			}			
		}).start();
	}
	
	
	private String getStrCurrentTime() {
		StringBuffer s_time = new StringBuffer();
		int hh = count_time/3600;
		int mm = (count_time/60)%60;
		int ss = count_time%60;
		if(hh<10){
			s_time.append("0");
		}
		s_time.append(hh+":");
		if(mm<10){
			s_time.append("0");
		}
		s_time.append(mm+":");
		if(ss<10){
			s_time.append("0");
		}
		s_time.append(ss);
		return s_time.toString();
	}

	// 判断能不能移动
	private synchronized boolean move(int left, int top, int right, int bottom) {
		// 擦除原来图像所在的数据
		eraseBlockArrayFormSharp(cret_tetrishape);
		boolean can_move = true;
		int shape[][] = cret_tetrishape.getShape();
		for (int i = left, w = 0; i <= right; i++, w++) {
			for (int j = top, h = 0; j <= bottom; j++, h++) {
				if (shape[h][w] == 1) {
					// 越界检测
					if (i < 0 || i >= 10 || j >= 20) {
						upBlockArrayFromSharp(cret_tetrishape);
						can_move = false;
						return can_move;
					}
					// 碰撞检测
					if (i >= 0 && j >= 0 && i < 10 && j < 20) {
						if (block_array[j][i] == 1) {
							upBlockArrayFromSharp(cret_tetrishape);
							can_move = false;
							return can_move;
						}
					}
				}
			}
		}
		cret_tetrishape.setPosition(left, top, right, bottom);
		upBlockArrayFromSharp(cret_tetrishape);
		postInvalidate();
		return can_move;
	}

	private void eraseBlockArrayFormSharp(TetriShape tetri) {
		int shape[][] = tetri.getShape();
		for (int i = tetri.getLeft(), w = 0; i <= tetri.getRight(); i++, w++) {
			for (int j = tetri.getTop(), h = 0; j <= tetri.getBottom(); j++, h++) {
				if (shape[h][w] == 1 && i >= 0 && j >= 0 && i < 10 && j < 20) {
					block_array[j][i] = 0;
				}
			}
		}
	}

	private void upBlockArrayFromSharp(TetriShape tetri) {
		int shape[][] = tetri.getShape();
		for (int i = tetri.getLeft(), w = 0; i <= tetri.getRight(); i++, w++) {
			for (int j = tetri.getTop(), h = 0; j <= tetri.getBottom(); j++, h++) {
				if (shape[h][w] == 1 && i >= 0 && j >= 0 && i < 10 && j < 20) {
					block_array[j][i] = 1;
				}
			}
		}
	}

	// 快速向下移动
	private void down() {		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (isDownDown) {
					moveDown();
					if(sound_3!=0){
						sp.stop(sound_3);						
					}
					sound_3 = playSound(3, 0);
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	// 退出
	private void exit() {	
		if(sound_3!=0){
			sp.stop(sound_3);						
		}
		sound_3 = playSound(3, 0);
		TC_CountTimeThread = false;
		TC_StartGameThread = false;
		TC_ResetGameThread = false;
		gamepause = false;
		gamestop = true;
		cret_tetrishape = null;
		next_tetrishape = null;
		sp.stop(sound_1);
		sp.stop(sound_2);
//		sp.stop(sound_3);
		Activity activity = (Activity) context;
		activity.finish();
	}

	// 从新开始
	private void start() {	
//		if(resetTask!=null){
//			resetTask.cancel(true);
//		}
		TC_ResetGameThread=false;
		if(sound_2!=0){
			sp.stop(sound_2);
		}
		if(sound_3!=0){
			sp.stop(sound_3);						
		}
		sound_3 = playSound(3, 0);
		gamepause = false;	
		cret_tetrishape=null;
		next_tetrishape=null;
		clearBlockArray();		
		postInvalidate();
		new Thread(new Runnable() {
			@Override
			public void run() {
				if(TC_StartGameThread){
					gamestop = true;
				}
				while (TC_StartGameThread) {
					try {
						Thread.sleep(500 - 500 * speed / 10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				TC_StartGameThread = true;				
				new StartGameThread();
			}
		}).start();
	}

	// 暂停
	private void pause() {
		if(sound_3!=0){
			sp.stop(sound_3);						
		}
		sound_3 = playSound(3, 0);
		if(cret_tetrishape!=null){
			gamepause = !gamepause;
		}
	}

	// 音乐
	private void music() {
		if(sound_3!=0){
			sp.stop(sound_3);						
		}
		sound_3 = playSound(3, 0);
		gamemusic=!gamemusic;
	}

	// 旋转
	private boolean rotate() {	
		if(sound_3!=0){
			sp.stop(sound_3);						
		}
		sound_3 = playSound(3, 0);
		eraseBlockArrayFormSharp(cret_tetrishape);
		boolean can_rotate = true;
		int rot_shape[][] = new int[4][4];
		int left = cret_tetrishape.getLeft();
		int top = cret_tetrishape.getTop();
		int right = cret_tetrishape.getRight();
		int bottom = cret_tetrishape.getBottom();
		rot_shape = cret_tetrishape.getRotShape();
		for (int i = left, w = 0; i <= right; i++, w++) {
			for (int j = top, h = 0; j <= bottom; j++, h++) {
				if (rot_shape[h][w] == 1) {
					// 越界检测
					if (i < 0 || i >= 10 || j >= 20) {
						upBlockArrayFromSharp(cret_tetrishape);
						can_rotate = false;
						return can_rotate;
					}
					// 碰撞检测
					if (i >= 0 && j >= 0 && i < 10 && j < 20) {
						if (block_array[j][i] == 1) {
							upBlockArrayFromSharp(cret_tetrishape);
							can_rotate = false;
							return can_rotate;
						}
					}
				}
			}
		}
		cret_tetrishape.setShape(rot_shape);
		upBlockArrayFromSharp(cret_tetrishape);
		postInvalidate();
		return can_rotate;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		if (width > 0 && height > 0) {
			this.width = width;
//			this.height = height;
			if (width / 17 < height / 27) {
				block_size = width / 17;
				offset_x = block_size / 2;
				offset_y = (height - block_size * 26) / 2;
			} else {
				block_size = height / 27;
				offset_x = (width - block_size * 16) / 2;
				offset_y = block_size / 2;
			}
			view_rect.left = 0;
			view_rect.top = 0;
			view_rect.right = width;
			view_rect.bottom = height;
			// 设置字体大小
			float textSize = block_size * 0.7f;
			text_paint1.setTextSize(textSize);			
			text_paint2.setTextSize(textSize);
			text_paint3.setTextSize(block_size * 0.5f);

			// 初始化按钮位置
			radius1 = block_size * 1.2f;
			up_x = offset_x + block_size * 4f;
			up_y = offset_y + block_size * 22;
			down_x = up_x;
			down_y = offset_y + block_size * 25;
			left_x = offset_x + block_size * 1.5f;
			left_y = offset_y + block_size * 23.5f;
			right_x = offset_x + block_size * 6.5f;
			right_y = left_y;
			
			radius2 = block_size * 0.5f;
			exit_x  = offset_x + block_size * 9;			
			start_x = offset_x + block_size * 11;
			pause_x = offset_x + block_size * 13;
			music_x = offset_x + block_size * 15;
			exit_y = start_y = pause_y = music_y = offset_y + block_size * 21f;
			
			rotate_x = offset_x + block_size * 13.5f;
			radius3 = block_size * 1.5f;
			rotate_y = offset_y + block_size * 24f;
			up_rect = new Rect((int) (up_x - radius1), (int) (up_y - radius1),
					(int) (up_x + radius1), (int) (up_y + radius1));
			down_rect = new Rect((int) (down_x - radius1), (int) (down_y - radius1),
					(int) (down_x + radius1), (int) (down_y + radius1));
			left_rect = new Rect((int) (left_x - radius1), (int) (left_y - radius1),
					(int) (left_x + radius1), (int) (left_y + radius1));
			right_rect = new Rect((int) (right_x - radius1), (int) (right_y - radius1),
					(int) (right_x + radius1), (int) (right_y + radius1));
			exit_rect = new Rect((int) (exit_x - radius2), (int) (exit_y - radius2),
					(int) (exit_x + radius2), (int) (exit_y + radius2));
			start_rect = new Rect((int) (start_x - radius2), (int) (start_y - radius2),
					(int) (start_x + radius2), (int) (start_y + radius2));
			pause_rect = new Rect((int) (pause_x - radius2), (int) (pause_y - radius2),
					(int) (pause_x + radius2), (int) (pause_y + radius2));
			music_rect = new Rect((int) (music_x - radius2), (int) (music_y - radius2),
					(int) (music_x + radius2), (int) (music_y + radius2));
			rotate_rect = new Rect((int) (rotate_x - radius3), (int) (rotate_y - radius3),
					(int) (rotate_x + radius3), (int) (rotate_y + radius3));
			setMeasuredDimension(width, height);
		}
	}
	
	

	class ResetGameThread extends Thread implements Runnable {
		public ResetGameThread() {
			gamepause = false;
			gamestop = true;
			cret_tetrishape=null;
			next_tetrishape=null;
			TC_CountTimeThread = false;
			TC_StartGameThread = false;			
			this.start();
		}

		@Override
		public void run() {						
			for (int j = 19; j >= 0; j--) {
				for (int i = 9; i >= 0; i--) {
					if(!TC_ResetGameThread) return;
					block_array[j][i] = 1;
					postInvalidate();
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			for (int j = 0; j <20; j++) {
				if(!TC_ResetGameThread) return;
				for (int i = 0; i < 10; i++) {
					block_array[j][i] = 0;
					postInvalidate();
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			clearBlockArray();
			postInvalidate();
			TC_ResetGameThread = false;
		}
	}

	class StartGameThread extends Thread implements Runnable {
		public StartGameThread() {			
			cret_tetrishape = new TetriShape();
			next_tetrishape = new TetriShape();
			// 读取最高分数
			SharedPreferences m_share = context.getSharedPreferences(ME_SHARE,Activity.MODE_PRIVATE);
			high_score = m_share.getInt(H_SCORE, 0);
			high_time = m_share.getString(H_TIME, "00:00:00");
			cret_score = 0;	
			speed=1;
			this.start();			
		}

		@Override
		public void run() {			
			while(TC_CountTimeThread){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}			
			count_time = 0;
			TC_CountTimeThread = true;
			gamestop = false;
			new CountTimeThread();
			postInvalidate();
			while (TC_StartGameThread&&!gamestop) {
				if (!gamepause) {
					moveDown();
				}
				try {
					Thread.sleep(500 - 500 * speed / 10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			TC_StartGameThread = false;
		}
	}

	class CountTimeThread extends Thread implements Runnable {
		public CountTimeThread() {
			this.start();
		}

		@Override
		public void run() {
			while (TC_CountTimeThread&&!gamestop) {
				long maxMemory = Runtime.getRuntime().maxMemory()/1024; 
				long totalMemory = Runtime.getRuntime().totalMemory()/1024; 
				long freeMemory = Runtime.getRuntime().freeMemory()/1024; 
				Log.v(TAG, "MemoryInfo->" +"maxMemory:"+maxMemory+"totalMemory:" + totalMemory+"freeMemory:"+freeMemory);	
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(!gamepause){
					count_time++;
				}
				postInvalidate();
			}			
			TC_CountTimeThread = false;
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			checkDownButton(event.getX(0), event.getY(0));
			break;
		case MotionEvent.ACTION_UP:
			if(isDownExit){
				isDownExit=false;
				exit();
			}else if(isDownStart){
				isDownStart=false;
				start();
			}else if(isDownPasue){
				isDownPasue = false;
				pause();
			}else if(isDownMusic){
				isDownMusic= false;
				music();
			}
			isDownLeft = isDownRight = isDownUp = isDownDown = isDownRotate = false;
			postInvalidate();
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			checkDownButton(event.getX(1), event.getY(1));
			break;
		case MotionEvent.ACTION_POINTER_UP:
			float x = event.getX(1);
			float y = event.getY(1);
			if (x > (left_x - radius1 * 1.5) && x < (left_x + radius1 * 1.5)
					&& y > (left_y - radius1 * 1.5)
					&& y < (left_y + radius1 * 1.5)) {
				isDownLeft = false;
			} else if (x > (right_x - radius1 * 1.5)
					&& x < (right_x + radius1 * 1.5)
					&& y > (right_y - radius1 * 1.5)
					&& y < (right_y + radius1) * 1.5) {
				isDownRight = false;
			}
			if(isDownExit){
				isDownExit=false;
				exit();
			}else if(isDownStart){
				isDownStart=false;
				start();
			}else if(isDownPasue){
				isDownPasue = false;
				pause();
			}else if(isDownMusic){
				isDownMusic= false;
				music();
			}
			isDownUp = isDownDown = isDownRotate = false;
			postInvalidate();
			break;
		}
		return true;
	}

	private void checkDownButton(float x, float y) {
		// exit
		if (x > (exit_x - block_size) && x < (exit_x + block_size)
				&& y > (exit_y - block_size) && y < (exit_y + block_size)) {
			isDownExit = true;				
		}
		// start
		if (x > (start_x - block_size) && x < (start_x + block_size)
				&& y > (start_y - block_size) && y < (start_y + block_size)) {
			isDownStart = true;			
		}
		// pause
		if (x > (pause_x - block_size) && x < (pause_x + block_size)
				&& y > (pause_y - block_size) && y < (pause_y + block_size)) {
			isDownPasue = true;			
		}
		// music
		if (x > (music_x - block_size) && x < (music_x + block_size)
				&& y > (music_y - block_size) && y < (music_y + block_size)) {
			isDownMusic = true;
		}
		if(cret_tetrishape==null) {
			postInvalidate();	
			return;
		}
		// left
		if (x > (left_x - radius1) && x < (left_x + radius1)
				&& y > (left_y - radius1) && y < (left_y + radius1)) {
			isDownLeft = true;
			postInvalidate();
			left();
		}
		// right
		if (x > (right_x - radius1) && x < (right_x + radius1)
				&& y > (right_y - radius1) && y < (right_y + radius1)) {
			isDownRight = true;
			postInvalidate();
			right();
		}
		// up
		if (x > (up_x - radius1) && x < (up_x + radius1)
				&& y > (up_y - radius1) && y < (up_y + radius1)) {
			isDownUp = true;
			postInvalidate();
		}
		// down
		if (x > (down_x - radius1) && x < (down_x + radius1)
				&& y > (down_y - radius1) && y < (down_y + radius1)) {
			isDownDown = true;
			postInvalidate();
			down();
		}		
		// rotate
		if (x > (rotate_x - radius3) && x < (rotate_x + radius3)
				&& y > (rotate_y - radius3) && y < (rotate_y + radius3)) {
			isDownRotate = true;
			postInvalidate();			
			rotate();
		}
	}

	private void right() {		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (isDownRight) {
					moveRight();
					if(sound_3!=0){
						sp.stop(sound_3);						
					}
					sound_3 = playSound(3, 0);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	private void left() {		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (isDownLeft) {
					moveLeft();
					if(sound_3!=0){
						sp.stop(sound_3);						
					}
					sound_3 = playSound(3, 0);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

	}

	private void clearBlockArray() {
		for (int j = 0; j < 20; j++) {
			for (int i = 0; i < 10; i++) {
				block_array[j][i] = 0;
			}
		}
	}

	public void initSoundPool() {
		sp = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
		hm = new HashMap<Integer, Integer>(); 
		hm.put(1, sp.load(context, R.raw.once_01, 1));
		hm.put(2, sp.load(context, R.raw.loop_02, 1));
		hm.put(3, sp.load(context, R.raw.once_03, 1));
	}

	public int playSound(int sound, int loop) { 
		if(!gamemusic) return 0;
		AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		float streamVolumeCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
		float streamVolumeMax = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume = streamVolumeCurrent / streamVolumeMax;
		return sp.play(hm.get(sound), volume, volume, 1, loop, 1.0f);
	}

//	private int getTextWidth(Paint paint, String str) {
//		int iRet = 0;
//		if (str != null && str.length() > 0) {
//			int len = str.length();
//			float[] widths = new float[len];
//			paint.getTextWidths(str, widths);
//			for (int j = 0; j < len; j++) {
//				iRet += (int) Math.ceil(widths[j]);
//			}
//		}
//		return iRet;
//	}
	
	private void drawNextBlock(int x, int y, Canvas canvas) {
		int shape[][] = next_tetrishape.getShape();
		switch (next_tetrishape.getShape_cret()) {
		//正方形
		case 0:						
		case 7:
		case 21:
		case 14:
			for (int i = 1; i < 3; i++) {
				for (int j = 1; j < 3; j++) {
					float left = x + i * block_size + 1;
					float top = y + j * block_size + 1;
					float right = left + block_size - 2;
					float bottom = top + block_size - 2;						
					if (shape[j][i] == 1) {						
						canvas.drawRect(left, top, right, bottom, block_paint2);
					}
				}
			}
			break;				
		//(第一列空)
		case 1:
		case 4:
		case 5:
		case 6:
		case 9:
		case 10:
		case 15:
		case 18:
		case 19:
		case 23:
		case 24:		
			for (int i = 1; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					float left = x + i * block_size-0.5f*block_size + 1;
					float top = y + j * block_size + 1;
					float right = left + block_size - 2;
					float bottom = top + block_size - 2;						
					if (shape[j][i] == 1) {						
						canvas.drawRect(left, top, right, bottom, block_paint2);
					}
				}
			}
			break;
		//(第一行空)
		case 2:
		case 3:
		case 8:
		case 11:
		case 12:
		case 16:
		case 17:
		case 22:
		case 25:
		case 26:
		case 27:
			for (int i = 0; i < 4; i++) {
				for (int j = 1; j < 4; j++) {
					float left = x + i * block_size + 1;
					float top = y + j * block_size-0.5f*block_size + 1;
					float right = left + block_size - 2;
					float bottom = top + block_size - 2;						
					if (shape[j][i] == 1) {						
						canvas.drawRect(left, top, right, bottom, block_paint2);
					}
				}
			}				
			break;
		// (前两列空,第一行空)
		case 13:
			for (int i = 2; i < 4; i++) {
				for (int j = 1; j < 4; j++) {
					float left = x + (i-1) * block_size + 1;
					float top = y + j * block_size-0.5f*block_size + 1;
					float right = left + block_size - 2;
					float bottom = top + block_size - 2;						
					if (shape[j][i] == 1) {						
						canvas.drawRect(left, top, right, bottom, block_paint2);
					}
				}
			}
			break;		
		//(前两行空,第一列空)
		case 20:
			for (int i = 1; i < 4; i++) {
				for (int j = 2; j < 4; j++) {
					float left = x + i * block_size-0.5f*block_size + 1;
					float top = y + (j-1) * block_size + 1;
					float right = left + block_size - 2;
					float bottom = top + block_size - 2;						
					if (shape[j][i] == 1) {						
						canvas.drawRect(left, top, right, bottom, block_paint2);
					}
				}
			}
			break;
		}		
	}
	
//	public class ResetGameTask extends AsyncTask<Void, Void, Void> {
//		@Override
//		protected Void doInBackground(Void... params) {
//			for (int j = 19; j >= 0; j--) {
//				for (int i = 9; i >= 0; i--) {
//					block_array[j][i] = 1;
//					postInvalidate();
//					try {
//						Thread.sleep(30);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//			for (int j = 0; j < 20; j++) {
//				for (int i = 0; i < 10; i++) {
//					block_array[j][i] = 0;
//					postInvalidate();
//					try {
//						Thread.sleep(30);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//			return null;
//		}
//	}

}
