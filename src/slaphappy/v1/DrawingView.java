package slaphappy.v1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
//import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

@SuppressLint("ViewConstructor")
public class DrawingView extends View {
	private Paint paint = new Paint();
	private SparseArray<PointF> mActivePointers = new SparseArray<PointF>();
	DisplayMetrics metrics = this.getResources().getDisplayMetrics();
	private float x = (float) (metrics.widthPixels / 2);
	private float y = (float) (metrics.heightPixels / 2);
	private float radius = (float) (Math.max(metrics.heightPixels, metrics.widthPixels) / 12);
	private int color = getColor();
	private int backgroundColor = -1;
	private SharedPreferences prefs;
	private int theme = -1;
	private int effect = 0;
	private int sound = -1;
	private Boolean touched = false;
	float multiplier = 1.1f;
	float newRadius = 40f;
	
    private Context mContext;
    float a = 0f;
    float b = 0f;
    private int aVelocity = 30;
    private int bVelocity = 20;
    private Handler h;
    private final int FRAME_RATE = 10;
    private Bitmap ball;
    private Bitmap cat;
    
    SoundPool cartoonPool;
    SoundPool waterPool;
    SoundPool pianoPool;
    SoundPool harpPool;
    SoundPool scifiPool;
    SoundPool catPool;
    int cartoonSounds[] = new int[8];
    int waterSounds[] = new int[6];
    int pianoSounds[] = new int[9];
    int harpSounds[] = new int[6];
    int scifiSounds[] = new int[10];
    int catSounds[] = new int[4];
	
	public DrawingView(Context context, AttributeSet attrs, SharedPreferences prefs) {
		super(context, attrs);
		requestLayout();
		mContext = context;
		
	    paint.setAntiAlias(true);
	    paint.setStrokeWidth(100f);
	    paint.setColor(Color.BLACK);
	    paint.setStyle(Paint.Style.FILL);
	    paint.setStrokeJoin(Paint.Join.ROUND);
	    this.prefs = prefs;
	    setSoundEffectsEnabled(true);

	    h = new Handler();
	    ball = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ball);
	    cat = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.cat);
	    
	    scifiPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
	    pianoPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
	    cartoonPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
	    harpPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
	    waterPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
	    catPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
	    
	    scifiSounds[0] = scifiPool.load(context, R.raw.scifi1, 1);
	    scifiSounds[1] = scifiPool.load(context, R.raw.scifi2, 1);
	    scifiSounds[2] = scifiPool.load(context, R.raw.scifi3, 1);
	    scifiSounds[3] = scifiPool.load(context, R.raw.scifi4, 1);
	    scifiSounds[4] = scifiPool.load(context, R.raw.scifi5, 1);
	    scifiSounds[5] = scifiPool.load(context, R.raw.scifi6, 1);
	    scifiSounds[6] = scifiPool.load(context, R.raw.scifi7, 1);
	    scifiSounds[7] = scifiPool.load(context, R.raw.scifi8, 1);
	    scifiSounds[8] = scifiPool.load(context, R.raw.scifi9, 1);
	    scifiSounds[9] = scifiPool.load(context, R.raw.scifi10, 1);
	    
	    cartoonSounds[0] = cartoonPool.load(context, R.raw.cartoon1, 1);
	    cartoonSounds[1] = cartoonPool.load(context, R.raw.cartoon2, 1);
	    cartoonSounds[2] = cartoonPool.load(context, R.raw.cartoon3, 1);
	    cartoonSounds[3] = cartoonPool.load(context, R.raw.cartoon4, 1);
	    cartoonSounds[4] = cartoonPool.load(context, R.raw.cartoon5, 1);
	    cartoonSounds[5] = cartoonPool.load(context, R.raw.cartoon6, 1);
	    cartoonSounds[6] = cartoonPool.load(context, R.raw.cartoon7, 1);
	    
	    harpSounds[0] = harpPool.load(context, R.raw.harp1, 1);
	    harpSounds[1] = harpPool.load(context, R.raw.harp2, 1);
	    harpSounds[2] = harpPool.load(context, R.raw.harp3, 1);
	    harpSounds[3] = harpPool.load(context, R.raw.harp4, 1);
	    harpSounds[4] = harpPool.load(context, R.raw.harp5, 1);
	    harpSounds[5] = harpPool.load(context, R.raw.harp6, 1);
	    
	    pianoSounds[0] = pianoPool.load(context, R.raw.piano1, 1);
	    pianoSounds[1] = pianoPool.load(context, R.raw.piano2, 1);
	    pianoSounds[2] = pianoPool.load(context, R.raw.piano3, 1);
	    pianoSounds[3] = pianoPool.load(context, R.raw.piano4, 1);
	    pianoSounds[4] = pianoPool.load(context, R.raw.piano5, 1);
	    pianoSounds[5] = pianoPool.load(context, R.raw.piano6, 1);
	    pianoSounds[6] = pianoPool.load(context, R.raw.piano7, 1);
	    pianoSounds[7] = pianoPool.load(context, R.raw.piano8, 1);
	    pianoSounds[8] = pianoPool.load(context, R.raw.piano9, 1);
	    
	    waterSounds[0] = waterPool.load(context, R.raw.water1, 1);
	    waterSounds[1] = waterPool.load(context, R.raw.water2, 1);
	    waterSounds[2] = waterPool.load(context, R.raw.water3, 1);
	    waterSounds[3] = waterPool.load(context, R.raw.water4, 1);
	    waterSounds[4] = waterPool.load(context, R.raw.water5, 1);
	    waterSounds[5] = waterPool.load(context, R.raw.water6, 1);
	    
	    catSounds[0] = catPool.load(context, R.raw.meow1, 1);
	    catSounds[1] = catPool.load(context, R.raw.meow2, 1);
	    catSounds[2] = catPool.load(context, R.raw.meow4, 1);
	    catSounds[3] = catPool.load(context, R.raw.meow5, 1);
	}
	    
    private Runnable r = new Runnable() {

    	@Override
        public void run() {
    		invalidate();
    	}
    };
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		theme = Integer.parseInt(prefs.getString("theme", "-1"));
		sound = Integer.parseInt(prefs.getString("sound", "-1"));
		// Theme set to white background with black dots
		if (theme == 0) {
			canvas.drawColor(Color.WHITE);
			paint.setColor(Color.BLACK);
			
			for (int size = mActivePointers.size(), i = 0; i < size; i++) {
				PointF point = mActivePointers.valueAt(i);
    	
				if (point != null){
					x = point.x;
					y = point.y;
					canvas.drawCircle(x, y, radius, paint);
					playSoundEffect(0);
				}
			}
		
			canvas.drawCircle(x, y, radius, paint);
			h.postDelayed(r, FRAME_RATE/10);
		}
		else if (theme == 1) {
			canvas.drawColor(Color.BLACK);
			
			if (touched == true) {
				if (x > (this.getWidth() - cat.getWidth())) {
					System.out.println("width");
					System.out.println(this.getWidth());
					System.out.println(x);
					x -= cat.getWidth();
					System.out.println(x);
					
					if (x < 0) {
						x = 0f;
					}
					System.out.println(x);
				}	

				if (y > (this.getHeight() - cat.getHeight())) { 
					System.out.println("height");
					System.out.println(this.getHeight());
					System.out.println(y);
					y -= cat.getHeight();
					System.out.println(y);
					
					if (y < 0) {
						y = 0f;
					}
					System.out.println(y);
				}
				a = x;
				b = y;
			}

			if (a < 0 && b < 0) {
				a = this.getWidth()/2;
				b = this.getHeight()/2;
			} 
			else {
				a += aVelocity;
				b += bVelocity;

				if ((a > (this.getWidth() - cat.getWidth())) || (a < 0)) {
					aVelocity = aVelocity * -1;
				}	

				if ((b > (this.getHeight() - cat.getHeight())) || (b < 0)) {
					bVelocity = bVelocity*-1;
				}
			}

			canvas.drawBitmap(cat, a, b, null);
			touched = false;

			h.postDelayed(r, FRAME_RATE);
		}
		// No theme set, so use other user preferences
		else {
			backgroundColor = Integer.parseInt(prefs.getString("bk_color", "-1"));
			canvas.drawColor(backgroundColor);
			
			effect = Integer.parseInt(prefs.getString("veffect", "0"));
			
			// multi-touch colored circles
			if (effect == 0) {
				if (touched == true) {
					color = getColor();
					while (color == backgroundColor) {
						color = getColor();
					}
					
					paint.setColor(color);
				}
				
				for (int size = mActivePointers.size(), i = 0; i < size; i++) {
					PointF point = mActivePointers.valueAt(i);
	    	
					if (point != null){
						x = point.x;
						y = point.y;
						canvas.drawCircle(x, y, radius, paint);
						playSoundEffect(0);
					}
				}
			
				canvas.drawCircle(x, y, radius, paint);
				touched = false;
				h.postDelayed(r, FRAME_RATE/10);
			}
			// single-touch color burst
			else if (effect == 1) {
				if (touched == true) {
					newRadius = 40f;
					color = getColor();
					while (color == backgroundColor) {
						color = getColor();
					}
						
					paint.setColor(color);
				}
				else {
					if (newRadius <= (radius * 12)){
						newRadius *= multiplier;
					}
					else {
						newRadius = 40f;
						paint.setColor(backgroundColor);
					}
				}

				canvas.drawCircle(x, y, newRadius, paint);
				touched = false;
				
				h.postDelayed(r, FRAME_RATE/10);
			}
			// single-touch bouncing ball
			else if ((effect == 2) || (theme == 1)) {
				if (touched == true){
					if (x > (this.getWidth() - ball.getWidth())) {
						x -= ball.getWidth();
					}	

					if (y > (this.getHeight() - ball.getHeight())) { 
						y -= ball.getHeight();
					}
					a = x;
					b = y;
				}

				if (a < 0 && b < 0) {
					a = this.getWidth()/2;
					b = this.getHeight()/2;
				} 
				else {
					a += aVelocity;
					b += bVelocity;

					if ((a > (this.getWidth() - ball.getWidth())) || (a < 0)) {
						aVelocity = aVelocity * -1;
					}	

					if ((b > (this.getHeight() - ball.getHeight())) || (b < 0)) {
						bVelocity = bVelocity*-1;
					}
				}

				canvas.drawBitmap(ball, a, b, null);
				touched = false;

				h.postDelayed(r, FRAME_RATE);
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
	    // Single touch
		if ((((effect == 1) || (effect == 2)) & (theme == -1)) ||
		      (theme == 1)) {
			x = event.getX();
	        y = event.getY();

	        switch (event.getAction()) {
	        	case MotionEvent.ACTION_DOWN:
	        		if (theme == 1) {
	        			catPool.play(catSounds[(int)Math.floor(Math.random() * 4)], 100f, 100f, 1, 0, 1.0f);
	        		}
	        		else {
	        			if (sound != -1) {
	        				if (sound == 0) {
	        					cartoonPool.play(cartoonSounds[(int)Math.floor(Math.random() * 8)], 100f, 100f, 1, 0, 1.0f);
	        				}
	        				if (sound == 1) {
	        					pianoPool.play(pianoSounds[(int)Math.floor(Math.random() * 9)], 100f, 100f, 1, 0, 1.0f);
	        				}
	        				if (sound == 2) {
	        					waterPool.play(waterSounds[(int)Math.floor(Math.random() * 6)], 100f, 100f, 1, 0, 1.0f);
	        				}
	        				if (sound == 3) {
	        					harpPool.play(harpSounds[(int)Math.floor(Math.random() * 6)], 100f, 100f, 1, 0, 1.0f);
	        				}
	        				if (sound == 4) {
	        					scifiPool.play(scifiSounds[(int)Math.floor(Math.random() * 10)], 100f, 100f, 1, 0, 1.0f);
	        				}
	        			}
	        		}
	        		return true;
	        	case MotionEvent.ACTION_MOVE:
	        		break;
	        	case MotionEvent.ACTION_UP:
	        		// nothing to do
	        		break;
	        	default:
	        		return false;
	        }
	    }
	    // Multi-touch
		else if ((effect == 0) || (theme == 0)) {
			// get pointer index from the event object
	    	int pointerIndex = event.getActionIndex();

	    	// get pointer ID
	    	int pointerId = event.getPointerId(pointerIndex);

	    	// get masked (not specific to a pointer) action
	    	int maskedAction = event.getActionMasked();

	    	switch (maskedAction) {

	    		case MotionEvent.ACTION_DOWN:
	    			if (theme == 0) {
    					pianoPool.play(pianoSounds[(int)Math.floor(Math.random() * 9)], 100f, 100f, 1, 0, 1.0f);
    				}
	    			else {
	    				if (sound != -1) {
	    					if (sound == 0) {
	    						cartoonPool.play(cartoonSounds[(int)Math.floor(Math.random() * 8)], 100f, 100f, 1, 0, 1.0f);
	    					}
	    					if (sound == 1) {
	    						pianoPool.play(pianoSounds[(int)Math.floor(Math.random() * 9)], 100f, 100f, 1, 0, 1.0f);
	    					}
	    					if (sound == 2) {
	    						waterPool.play(waterSounds[(int)Math.floor(Math.random() * 6)], 100f, 100f, 1, 0, 1.0f);
	    					}
	    					if (sound == 3) {
	    						harpPool.play(harpSounds[(int)Math.floor(Math.random() * 6)], 100f, 100f, 1, 0, 1.0f);
	    					}
	    					if (sound == 4) {
	    						scifiPool.play(scifiSounds[(int)Math.floor(Math.random() * 10)], 100f, 100f, 1, 0, 1.0f);
	    					}
	    				}
	    			}
	    		case MotionEvent.ACTION_POINTER_DOWN: {
	    			// We have a new pointer. Lets add it to the list of pointers

	    			PointF f = new PointF();
	    			f.x = event.getX(pointerIndex);
	    			f.y = event.getY(pointerIndex);
	    			mActivePointers.put(pointerId, f);
	    			break;
	    		}
	    		case MotionEvent.ACTION_MOVE: { // a pointer was moved
	    			for (int size = event.getPointerCount(), i = 0; i < size; i++) {
	    				PointF point = mActivePointers.get(event.getPointerId(i));
	    					if (point != null) {
	    						point.x = event.getX(i);
	    						point.y = event.getY(i);
	    					}
	    			}
	    			break;
	    		}
	    		case MotionEvent.ACTION_UP:
	    		case MotionEvent.ACTION_POINTER_UP:
	    		case MotionEvent.ACTION_CANCEL: {
	    			mActivePointers.remove(pointerId);
	    			break;
	    		}
	    	}
	    }

	    // Schedules a repaint.
		touched = true;
	    invalidate();
	   	return true;
	}
	
	public int getColor(){
		return Color.rgb((int)Math.floor(Math.random() * 257), 
                         (int)Math.floor(Math.random() * 257), 
                         (int)Math.floor(Math.random() * 257));
	}
} 
