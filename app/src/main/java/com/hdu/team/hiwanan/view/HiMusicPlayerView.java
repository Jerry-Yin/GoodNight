package com.hdu.team.hiwanan.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import co.mobiwise.library.OnPlayPauseToggleListener;
import co.mobiwise.library.PlayPauseDrawable;

/**
 * Created by JerryYin on 4/28/17.
 */

public class HiMusicPlayerView extends View implements OnPlayPauseToggleListener {
    private static Rect mRectText;
    private static Paint mPaintTime;
    private RectF rectF;
    private static Paint mPaintProgressEmpty;
    private static Paint mPaintProgressLoaded;
    private OnClickListener onClickListener;
    private static Paint mPaintButton;
    private static Region mButtonRegion;
    private static Paint mPaintCover;
    private Bitmap mBitmapCover;
    private BitmapShader mShader;
    private float mCoverScale;
    private int mHeight;
    private int mWidth;
    private float mCenterX;
    private float mCenterY;
    private int mRotateDegrees;
    private Handler mHandlerRotate;
    private final Runnable mRunnableRotate = new Runnable() {
        public void run() {
            if (HiMusicPlayerView.this.isRotating) {
                if (HiMusicPlayerView.this.currentProgress > HiMusicPlayerView.this.maxProgress) {
                    HiMusicPlayerView.this.currentProgress = 0;
                    HiMusicPlayerView.this.setProgress(HiMusicPlayerView.this.currentProgress);
                    HiMusicPlayerView.this.stop();
                }

                HiMusicPlayerView.this.updateCoverRotate();
                HiMusicPlayerView.this.mHandlerRotate.postDelayed(HiMusicPlayerView.this.mRunnableRotate, (long) HiMusicPlayerView.ROTATE_DELAY);
            }

        }
    };
    private Handler mHandlerProgress;
    private Runnable mRunnableProgress = new Runnable() {
        public void run() {
            if (HiMusicPlayerView.this.isRotating) {
                HiMusicPlayerView.this.currentProgress++;
                HiMusicPlayerView.this.mHandlerProgress.postDelayed(HiMusicPlayerView.this.mRunnableProgress, (long) HiMusicPlayerView.PROGRESS_SECOND_MS);
            }

        }
    };
    private boolean isRotating;
    private static int ROTATE_DELAY = 10;
    private static int PROGRESS_SECOND_MS = 1000;
    private static int VELOCITY = 1;
//    private int mCoverColor = -7829368;
    private int mCoverColor = 1;
    private float mButtonRadius = 120.0F;
//    private int mButtonColor = -12303292;
    private int mButtonColor = 1;
    private int mProgressEmptyColor = 553648127;
//    private int mProgressLoadedColor = -16744098;
    private int mProgressLoadedColor = 3;
    private int mTextSize = 40;
    private int mTextColor = -1;
    private int currentProgress = 0;
    private int maxProgress = 100;
    private boolean isAutoProgress = true;
    private boolean mProgressVisibility = true;
    private static final long PLAY_PAUSE_ANIMATION_DURATION = 200L;
    private PlayPauseDrawable mPlayPauseDrawable;
    private AnimatorSet mAnimatorSet;
    private boolean mFirstDraw = true;
    private Target target = new Target() {
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            HiMusicPlayerView.this.mBitmapCover = bitmap;
            HiMusicPlayerView.this.createShader();
            HiMusicPlayerView.this.postInvalidate();
        }

        public void onBitmapFailed(Drawable errorDrawable) {
        }

        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };
    Drawable.Callback callback = new Drawable.Callback() {
        public void invalidateDrawable(Drawable who) {
            HiMusicPlayerView.this.postInvalidate();
        }

        public void scheduleDrawable(Drawable who, Runnable what, long when) {
        }

        public void unscheduleDrawable(Drawable who, Runnable what) {
        }
    };

    public HiMusicPlayerView(Context context) {
        super(context);
        this.init(context, (AttributeSet) null);
    }

    public HiMusicPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs);
    }

    public HiMusicPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs);
    }

    @TargetApi(21)
    public HiMusicPlayerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.setWillNotDraw(false);
        this.mPlayPauseDrawable = new PlayPauseDrawable(context);
        this.mPlayPauseDrawable.setCallback(this.callback);
        this.mPlayPauseDrawable.setToggleListener(this);
        TypedArray a = context.obtainStyledAttributes(attrs, co.mobiwise.library.R.styleable.playerview);
        Drawable mDrawableCover = a.getDrawable(co.mobiwise.library.R.styleable.playerview_cover);
        if (mDrawableCover != null) {
            this.mBitmapCover = this.drawableToBitmap(mDrawableCover);
        }

        this.mButtonColor = a.getColor(co.mobiwise.library.R.styleable.playerview_buttonColor, this.mButtonColor);
        this.mProgressEmptyColor = a.getColor(co.mobiwise.library.R.styleable.playerview_progressEmptyColor, this.mProgressEmptyColor);
        this.mProgressLoadedColor = a.getColor(co.mobiwise.library.R.styleable.playerview_progressLoadedColor, this.mProgressLoadedColor);
        this.mTextColor = a.getColor(co.mobiwise.library.R.styleable.playerview_textColor, this.mTextColor);
        this.mTextSize = a.getDimensionPixelSize(co.mobiwise.library.R.styleable.playerview_textSize, this.mTextSize);
        a.recycle();
        this.mRotateDegrees = 0;
        this.mHandlerRotate = new Handler();
        this.mHandlerProgress = new Handler();
        mPaintButton = new Paint();
        mPaintButton.setAntiAlias(true);
        mPaintButton.setStyle(Paint.Style.FILL);
        mPaintButton.setColor(this.mButtonColor);
        mPaintProgressEmpty = new Paint();
        mPaintProgressEmpty.setAntiAlias(true);
        mPaintProgressEmpty.setColor(this.mProgressEmptyColor);
        mPaintProgressEmpty.setStyle(Paint.Style.STROKE);
        mPaintProgressEmpty.setStrokeWidth(12.0F);
        mPaintProgressLoaded = new Paint();
        mPaintProgressEmpty.setAntiAlias(true);
        mPaintProgressLoaded.setColor(this.mProgressLoadedColor);
        mPaintProgressLoaded.setStyle(Paint.Style.STROKE);
        mPaintProgressLoaded.setStrokeWidth(12.0F);
        mPaintTime = new Paint();
        mPaintTime.setColor(this.mTextColor);
        mPaintTime.setAntiAlias(true);
        mPaintTime.setTextSize((float) this.mTextSize);
        this.rectF = new RectF();
        mRectText = new Rect();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.mWidth = MeasureSpec.getSize(widthMeasureSpec);
        this.mHeight = MeasureSpec.getSize(heightMeasureSpec);
        int minSide = Math.min(this.mWidth, this.mHeight);
        this.mWidth = minSide;
        this.mHeight = minSide;
        this.setMeasuredDimension(this.mWidth, this.mHeight);
        this.mCenterX = (float) this.mWidth / 2.0F;
        this.mCenterY = (float) this.mHeight / 2.0F;
        this.rectF.set(20.0F, 20.0F, (float) this.mWidth - 20.0F, (float) this.mHeight - 20.0F);
        this.mButtonRadius = (float) this.mWidth / 8.0F;
        this.mPlayPauseDrawable.resize(1.2F * this.mButtonRadius / 5.0F, 3.0F * this.mButtonRadius / 5.0F + 10.0F, this.mButtonRadius / 5.0F);
        this.mPlayPauseDrawable.setBounds(0, 0, this.mWidth, this.mHeight);
        mButtonRegion = new Region((int) (this.mCenterX - this.mButtonRadius), (int) (this.mCenterY - this.mButtonRadius), (int) (this.mCenterX + this.mButtonRadius), (int) (this.mCenterY + this.mButtonRadius));
        this.createShader();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mShader != null) {
            float radius = this.mCenterX <= this.mCenterY ? this.mCenterX - 75.0F : this.mCenterY - 75.0F;
            canvas.rotate((float) this.mRotateDegrees, this.mCenterX, this.mCenterY);
            canvas.drawCircle(this.mCenterX, this.mCenterY, radius, mPaintCover);
            canvas.rotate((float) (-this.mRotateDegrees), this.mCenterX, this.mCenterY);
            canvas.drawCircle(this.mCenterX, this.mCenterY, this.mButtonRadius, mPaintButton);
            if (this.mProgressVisibility) {
                canvas.drawArc(this.rectF, 145.0F, 250.0F, false, mPaintProgressEmpty);
                canvas.drawArc(this.rectF, 145.0F, (float) this.calculatePastProgressDegree(), false, mPaintProgressLoaded);
                String leftTime = this.secondsToTime(this.calculateLeftSeconds());
                mPaintTime.getTextBounds(leftTime, 0, leftTime.length(), mRectText);
                canvas.drawText(leftTime, (float) ((double) this.mCenterX * Math.cos(Math.toRadians(35.0D))) + (float) this.mWidth / 2.0F - (float) mRectText.width() / 1.5F, (float) ((double) this.mCenterX * Math.sin(Math.toRadians(35.0D))) + (float) this.mHeight / 2.0F + (float) mRectText.height() + 15.0F, mPaintTime);
                String passedTime = this.secondsToTime(this.calculatePassedSeconds());
                mPaintTime.getTextBounds(passedTime, 0, passedTime.length(), mRectText);
                canvas.drawText(passedTime, (float) ((double) this.mCenterX * -Math.cos(Math.toRadians(35.0D))) + (float) this.mWidth / 2.0F - (float) mRectText.width() / 3.0F, (float) ((double) this.mCenterX * Math.sin(Math.toRadians(35.0D))) + (float) this.mHeight / 2.0F + (float) mRectText.height() + 15.0F, mPaintTime);
            }

            if (this.mFirstDraw) {
                this.toggle();
                this.mFirstDraw = false;
            }

            this.mPlayPauseDrawable.draw(canvas);
        }
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else {
            int width = drawable.getIntrinsicWidth();
            width = width > 0 ? width : 1;
            int height = drawable.getIntrinsicHeight();
            height = height > 0 ? height : 1;
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        }
    }

    private void createShader() {
        if (this.mWidth != 0) {
            if (this.mBitmapCover == null) {
                this.mBitmapCover = Bitmap.createBitmap(this.mWidth, this.mHeight, Bitmap.Config.ARGB_8888);
                this.mBitmapCover.eraseColor(this.mCoverColor);
            }

            this.mCoverScale = (float) this.mWidth / (float) this.mBitmapCover.getWidth();
            this.mBitmapCover = Bitmap.createScaledBitmap(this.mBitmapCover, (int) ((float) this.mBitmapCover.getWidth() * this.mCoverScale), (int) ((float) this.mBitmapCover.getHeight() * this.mCoverScale), true);
            this.mShader = new BitmapShader(this.mBitmapCover, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            mPaintCover = new Paint();
            mPaintCover.setAntiAlias(true);
            mPaintCover.setShader(this.mShader);
        }
    }

    public void updateCoverRotate() {
        this.mRotateDegrees += VELOCITY;
        this.mRotateDegrees %= 360;
        this.postInvalidate();
    }

    public boolean isRotating() {
        return this.isRotating;
    }

    public void start() {
        this.isRotating = true;
        this.mPlayPauseDrawable.setPlaying(this.isRotating);
        this.mHandlerRotate.removeCallbacksAndMessages((Object) null);
        this.mHandlerRotate.postDelayed(this.mRunnableRotate, (long) ROTATE_DELAY);
        if (this.isAutoProgress) {
            this.mHandlerProgress.removeCallbacksAndMessages((Object) null);
            this.mHandlerProgress.postDelayed(this.mRunnableProgress, (long) PROGRESS_SECOND_MS);
        }

        this.postInvalidate();
    }

    public void stop() {
        this.isRotating = false;
        this.mPlayPauseDrawable.setPlaying(this.isRotating);
        this.postInvalidate();
    }

    public void setVelocity(int velocity) {
        if (velocity > 0) {
            VELOCITY = velocity;
        }

    }

    public void setCoverDrawable(int coverDrawable) {
        Drawable drawable = this.getContext().getResources().getDrawable(coverDrawable);
        this.mBitmapCover = this.drawableToBitmap(drawable);
        this.createShader();
        this.postInvalidate();
    }

    public void setCoverDrawable(Drawable drawable) {
        this.mBitmapCover = this.drawableToBitmap(drawable);
        this.createShader();
        this.postInvalidate();
    }

    public void setCoverURL(String imageUrl) {
        Picasso.with(this.getContext()).load(imageUrl).into(this.target);
    }

    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case 0:
                return true;
            case 1:
                if (mButtonRegion.contains((int) x, (int) y) && this.onClickListener != null) {
                    this.onClickListener.onClick(this);
                }
            default:
                return super.onTouchEvent(event);
        }
    }

    public void setOnClickListener(OnClickListener l) {
        this.onClickListener = l;
    }

    private Bitmap getResizedBitmap(Bitmap bm, float newHeight, float newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = newWidth / (float) width;
        float scaleHeight = newHeight / (float) height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    public void setButtonColor(int color) {
        this.mButtonColor = color;
        mPaintButton.setColor(this.mButtonColor);
        this.postInvalidate();
    }

    public void setProgressEmptyColor(int color) {
        this.mProgressEmptyColor = color;
        mPaintProgressEmpty.setColor(this.mProgressEmptyColor);
        this.postInvalidate();
    }

    public void setProgressLoadedColor(int color) {
        this.mProgressLoadedColor = color;
        mPaintProgressLoaded.setColor(this.mProgressLoadedColor);
        this.postInvalidate();
    }

    public void setMax(int maxProgress) {
        this.maxProgress = maxProgress;
        this.postInvalidate();
    }

    public void setProgress(int currentProgress) {
        if (0 <= currentProgress && currentProgress <= this.maxProgress) {
            this.currentProgress = currentProgress;
            this.postInvalidate();
        }

    }

    public int getProgress() {
        return this.currentProgress;
    }

    private int calculateLeftSeconds() {
        return this.maxProgress - this.currentProgress;
    }

    private int calculatePassedSeconds() {
        return this.currentProgress;
    }

    private String secondsToTime(int seconds) {
        String time = "";
        String minutesText = String.valueOf(seconds / 60);
        if (minutesText.length() == 1) {
            minutesText = "0" + minutesText;
        }

        String secondsText = String.valueOf(seconds % 60);
        if (secondsText.length() == 1) {
            secondsText = "0" + secondsText;
        }

        time = minutesText + ":" + secondsText;
        return time;
    }

    private int calculatePastProgressDegree() {
        return 250 * this.currentProgress / this.maxProgress;
    }

    public void setAutoProgress(boolean isAutoProgress) {
        this.isAutoProgress = isAutoProgress;
    }

    public void setTimeColor(int color) {
        this.mTextColor = color;
        mPaintTime.setColor(this.mTextColor);
        this.postInvalidate();
    }

    public void setProgressVisibility(boolean mProgressVisibility) {
        this.mProgressVisibility = mProgressVisibility;
        this.postInvalidate();
    }


    public void toggle() {
        if (this.mAnimatorSet != null) {
            this.mAnimatorSet.cancel();
        }

        this.mAnimatorSet = new AnimatorSet();
        Animator pausePlayAnim = this.mPlayPauseDrawable.getPausePlayAnimator();
        this.mAnimatorSet.setInterpolator(new DecelerateInterpolator());
        this.mAnimatorSet.setDuration(200L);
        this.mAnimatorSet.playTogether(new Animator[]{pausePlayAnim});
        this.mAnimatorSet.start();
    }

    @Override
    public void onToggled() {
        this.onToggled();
    }
}
