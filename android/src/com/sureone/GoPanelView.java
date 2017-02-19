/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sureone;

// Need the following import to get access to the app resources, since this
// class is in a sub-package.
import java.util.Locale;

import com.sureone.go.CLogicGo;
import com.sureone.go.GoOp;
import com.sureone.go.GoStep;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import android.content.res.AssetManager;
import android.app.Activity;
import android.content.Context;
import java.lang.reflect.Field;
import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.AbsoluteLayout;
import android.widget.AbsoluteLayout;
import android.widget.TextView;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;



/**
 * Example of how to write a custom subclass of View. LabelView
 * is used to draw simple text views. Note that it does not handle
 * styled text or right-to-left writing systems.
 *
 */
public class GoPanelView extends View {
    private Paint mTextPaint;


    class FocusWidget {
        Bitmap mBig=null;
        Bitmap mSmall=null;
        boolean visible=false;
    }
    class ChessBMP {
        Bitmap mBig=null;
        Bitmap mSmall=null;
        Bitmap mShadow=null;
        Bitmap mMu=null;
    }
    private android.graphics.Bitmap mMemBmp=null;
    private android.graphics.Bitmap mBigBoardBmp;
    private android.graphics.Bitmap mSmallBoardBmp;
    private FocusWidget mFocusWidget = null;

    private ChessBMP mBlackBmp = null;
    private ChessBMP mWhiteBmp = null;

    int BOARD_OFFSET_320 = 16;
    int BOARD_STEP_320 = 16;
    int mBoardSize=19;
    public Bitmap mChessMem=null;

    public int mScaleRate = 2;
    private boolean IsQiPanDrawed=false;
    private float mScare=1;
    private int mCanvasWidth;
    private int mCanvasHeight;
    public boolean mFullBoard = true;

    public int mTopX=BOARD_OFFSET_320;
    public int mTopY=BOARD_OFFSET_320;
    public int mStepW=BOARD_STEP_320;

    public int mBigViewX=0;
    public int mBigViewY=0;

    public int mFocusX=0;
    public int mFocusY=0;

    public boolean mDianMuMode=false;
    public boolean mDisableInput=true;
    public DianMuResult mDianMuResult = null;
    Context mParent = null;
    /**
     * Constructor.  This version is only needed if you will be instantiating
     * the object manually (not from a layout XML file).
     * @param context
     */
    public GoPanelView(Context context) {
        super(context);
        initBoardView();
    }

    /**
     * Construct object, initializing with any attributes we understand from a
     * layout file. These attributes are defined in
     * SDK/assets/res/any/classes.xml.
     *
     * @see android.view.View#View(android.content.Context, android.util.AttributeSet)
     */
    public GoPanelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBoardView();
        setDrawingCacheEnabled(true);
    }

    private final void initBoardView() {
    }
    int mLineColor = Color.BLACK;
    public void setLineColor(int c) {
        mLineColor = c;
    }
    /*
        requestLayout();
        invalidate();
     */
    int mWidth=240;
    public void setWidth(int w) {
        mWidth=w;
    }


    public void freeMem() {
        if(mChessMem!=null) mChessMem.recycle();
        mChessMem=null;
    }
    int mCursorX=-1;
    int mCursorY=-1;

    /**
     * @see android.view.View#measure(int, int)
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = mWidth;
        setMeasuredDimension(w,w);
    }

    /**
     * Determines the width of this view
     * @param measureSpec A measureSpec packed into an int
     * @return The width of the view, honoring constraints from measureSpec
     */
    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {

        }

        return result;
    }

    /**
     * Determines the height of this view
     * @param measureSpec A measureSpec packed into an int
     * @return The height of the view, honoring constraints from measureSpec
     */
    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {

        }
        return result;
    }
    /**
     * Render the text
     *
     * @see android.view.View#onDraw(android.graphics.Canvas)
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint(canvas);
    }


    int mBlinkCursor=0;
    void updateCursor() {
        mBlinkCursor++;
        if(mBlinkCursor==4) mBlinkCursor=0;
        invalidateEx(false);
    }

    void ptol(float x,float y) {
        mCursorX=getLX(x,mTopX,mStepW);
        mCursorY=getLY(y,mTopY,mStepW);
    }
    void drawCursor(Canvas canvas) {
        if(mCursorX==-1) return;
        float px,py;
        int lx,ly;
        lx=mCursorX;
        ly=mCursorY;
        px = getPX(lx,mTopX,mStepW);
        py = getPY(ly,mTopY,mStepW);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        float w = mStepW/(float)1.5;
        w=(w/4)*(mBlinkCursor+1);
        float x1=px-w+1;
        float x2=px+w;
        float y1=py+(float)0.5;
        float y2=y1;
        canvas.drawLine(x1, y1, x2, y2, paint);
        x1=px+(float)0.5;
        x2=x1;
        y1=py-w+1;
        y2=py+w;
        canvas.drawLine(x1, y1, x2, y2, paint);
    }


    public void setBoardSize(int s) {
        mBoardSize=s;
    }


    android.graphics.Bitmap CreateMemBmp(int w,int h) {
        Bitmap memBmp = android.graphics.Bitmap.createBitmap(w, h, android.graphics.Bitmap.Config.ARGB_4444);
        return memBmp;
    }

    Bitmap drawChesses(Bitmap memBmp,int boardSize,int topX,int topY,int stepW,boolean bCnt) {
        Canvas canvasMem = new Canvas();
        canvasMem.setBitmap(memBmp);
        if(mChessListener.haveLogic()==false) return null;
        final Paint aap = new Paint(Paint.ANTI_ALIAS_FLAG);

        aap.setAntiAlias(true);
        aap.setFilterBitmap(true);
        int space=1;

        for (int x=0 ; x<boardSize ; x++) {
            for(int y=0 ; y<boardSize; y++) {
                float px,py;
                Bitmap bmp=null;
                Bitmap shadow=null;

                if(mChessListener.getSide(x, y)==1) {
                    bmp = black_stone;
                    shadow = bshadow;

                } else if(mChessListener.getSide(x, y)==2) {
                    bmp = white_stone;
                    shadow = wshadow;

                } else if(mDianMuMode==true) {
                    if(mDianMuResult.getSide(x, y)==1) {
                        bmp=mBlackBmp.mMu;
                    } else if(mDianMuResult.getSide(x, y)==2) {
                        bmp=mWhiteBmp.mMu;
                    }
                }
                if(bmp==null) continue;
                int lx=mChessListener.getLastStepX();
                int ly=mChessListener.getLastStepY();
                if(mFullBoard==true) {
                    px = getPX(x,topX,stepW);
                    py = getPY(18-y,topY,stepW);
                    Rect src = new Rect();
                    Rect dst = new Rect();
                    dst.top = (int)(py-(stepW)/2);
                    dst.left = (int)(px-(stepW)/2);
                    dst.right = (int)(px+stepW/2+1);
                    dst.bottom = (int)(py+stepW/2+1);

                    src.top=0;
                    src.left=0;
                    src.right=bmp.getWidth();
                    src.bottom=bmp.getHeight();

                    //draw the step NO.
                    canvasMem.drawBitmap(bmp, src, dst,aap);




                    

                    if(bCnt==true) {
                        Paint paint = new Paint();
                        paint.setColor(Color.WHITE);
                        if(mChessListener.getSide(x, y)==2) {
                            paint.setColor(Color.BLACK);
                        }
                        paint.setAntiAlias(true);
                        int stepNo = mChessListener.getStepNo(x,y);
                        String tag = new Integer(stepNo).toString();
                        paint.setTextAlign(Paint.Align.CENTER);
                        canvasMem.drawText(tag,px,py+5,paint);

                    }else{
						if(x==lx && y==ly) {
							Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
							paint.setColor(Color.RED);

							canvasMem.drawRect(px-3, py-3, px+4, py+4, paint);
						}
					}
                } else {

                    px = getPX(x,topX*mScaleRate,stepW*mScaleRate);
                    py = getPY(18-y,topY*mScaleRate,stepW*mScaleRate);
                    Rect src = new Rect();
                    Rect dst = new Rect();
                    dst.top = (int)(py-(stepW*mScaleRate)/2);
                    dst.left = (int)(px-(stepW*mScaleRate)/2);
                    dst.right = (int)(px+(stepW*mScaleRate)/2+1);
                    dst.bottom = (int)(py+(stepW*mScaleRate)/2+1);

                    src.top=0;
                    src.left=0;
                    src.right=bmp.getWidth();
                    src.bottom=bmp.getHeight();

                    //draw the step NO.
                    canvasMem.drawBitmap(bmp, src, dst,aap);

                    if(x==lx && y==ly) {
                        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                        paint.setColor(Color.RED);

                        canvasMem.drawRect(px-6, py-6, px+7, py+7, paint);
                    }




                }
            }
        }

        return memBmp;
    }


    void drawChessesDirect(Canvas canvasMem,int boardSize,int topX,int topY,int stepW,boolean bCnt) {

        if(mChessListener.haveLogic()==false) return;
        final Paint aap = new Paint();
        aap.setFilterBitmap(true);
        int space=1;

        for (int x=0 ; x<boardSize ; x++) {
            for(int y=0 ; y<boardSize; y++) {
                float px,py;
                Bitmap bmp=null;
                Bitmap shadow=null;

                if(mChessListener.getSide(x, y)==1) {
                    bmp = black_stone;
                    shadow = bshadow;

                } else if(mChessListener.getSide(x, y)==2) {
                    bmp = white_stone;
                    shadow = wshadow;

                } else if(mDianMuMode==true) {
                    if(mDianMuResult.getSide(x, y)==1) {
                        bmp=mBlackBmp.mMu;
                    } else if(mDianMuResult.getSide(x, y)==2) {
                        bmp=mWhiteBmp.mMu;
                    }
                }
                if(bmp==null) continue;
                int lx=mChessListener.getLastStepX();
                int ly=mChessListener.getLastStepY();
                if(mFullBoard==true) {
                    px = getPX(x,topX,stepW);
                    py = getPY(18-y,topY,stepW);
                    Rect src = new Rect();
                    Rect dst = new Rect();
                    dst.top = (int)(0.5f+py-(stepW)/2);
                    dst.left = (int)(1.5f+px-(stepW)/2);
                    dst.right = (int)(1.5f+px+stepW/2);
                    dst.bottom = (int)(0.5f+py+stepW/2);

                    float f6 = stepW/4;

                    dst.left = ((int)(dst.left - 3.0F * f6 / 4.0F));
                    dst.right = ((int)(dst.right + f6 / 4.0F));
                    dst.bottom = ((int)(f6 + dst.bottom));
                    canvasMem.drawBitmap(shadow, null, dst,aap);

                    dst.left = ((int)(dst.left + 3.0F * f6 / 4.0F));
                    dst.right = ((int)(dst.right - f6 / 4.0F));
                    dst.bottom = ((int)(dst.bottom - f6));

                    //draw the step NO.
                    canvasMem.drawBitmap(bmp, null, dst,aap);






                    if(bCnt==true) {
                        Paint paint = new Paint();
                        paint.setColor(Color.WHITE);
                        if(mChessListener.getSide(x, y)==2) {
                            paint.setColor(Color.BLACK);
                        }
                        paint.setAntiAlias(true);
                        int stepNo = mChessListener.getStepNo(x,y);
                        String tag = new Integer(stepNo).toString();
                        paint.setTextAlign(Paint.Align.CENTER);
                        canvasMem.drawText(tag,px,py+5,paint);

                    }else{
                        if(x==lx && y==ly) {
                            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                            paint.setColor(Color.RED);

                            canvasMem.drawRect(px-3, py-3, px+4, py+4, paint);
                        }
                    }
                } else {

                    px = getPX(x,topX*mScaleRate,stepW*mScaleRate);
                    py = getPY(18-y,topY*mScaleRate,stepW*mScaleRate);

                    Rect dst = new Rect();
                    dst.top = (int)(1.5f+py-(stepW*mScaleRate)/2 - this.mBigViewY);
                    dst.left = (int)(2.5f+px-(stepW*mScaleRate)/2-this.mBigViewX);
                    dst.right = (int)(1.5f+px+(stepW*mScaleRate)/2-this.mBigViewX);
                    dst.bottom = (int)(py+(stepW*mScaleRate)/2-this.mBigViewY-0.5);


                    float f3 = stepW * this.mScaleRate / 3.0F;
                    dst.left = ((int)(dst.left - 3.0F * f3 / 4.0F));
                    dst.right = ((int)(dst.right + f3 / 4.0F));
                    dst.bottom = ((int)(f3 + dst.bottom));
                    canvasMem.drawBitmap(shadow, null, dst, aap);
                    dst.left = ((int)(dst.left + 3.0F * f3 / 4.0F));
                    dst.right = ((int)(dst.right - f3 / 4.0F));
                    dst.bottom = ((int)(dst.bottom - f3));
                    canvasMem.drawBitmap(bmp, null, dst, aap);



                    if(x==lx && y==ly) {
                        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                        paint.setColor(Color.RED);

                        px-=this.mBigViewX;
                        py-=this.mBigViewY;

                        canvasMem.drawRect(px-6, py-6, px+7, py+7, paint);
                    }




                }
            }
        }
    }

	private Bitmap  black_stone = null;   // 黑子
	private Bitmap  white_stone = null;   // 白子

    private Bitmap  bshadow = null;   // 黑子
    private Bitmap  wshadow = null;   // 白子



	// 从Assets中读取图片到black_stone和white_stone
	private void getChessImageFromAssetsFile(int chessRadius)
	{
		boolean load_picture_flag=false;

        bshadow=BitmapFactory.decodeResource(getResources(),R.drawable.blackshadow);

        wshadow=BitmapFactory.decodeResource(getResources(),R.drawable.whiteshadow);
            black_stone = BitmapFactory.decodeResource( getResources(), R.drawable.blackstone52) ;
            white_stone = BitmapFactory.decodeResource( getResources(), R.drawable.whitestone52) ;
			load_picture_flag = true;
			return;
	}
	


    void paint(Canvas canvas) {
        mCanvasWidth=mWidth;
        mCanvasHeight=mWidth;
        //xHelper.log("goapp","cavas w="+mCanvasWidth+",h"+ mCanvasHeight);
        if(mCanvasWidth>mCanvasHeight) {
            int tmp =  mCanvasWidth;
            mCanvasWidth = mCanvasHeight;
            mCanvasHeight = tmp;
        }

        mStepW=mCanvasWidth/(mBoardSize+1);
        mTopX=mStepW;
        mTopY=mStepW;
        drawBoardMem(mBoardSize,mTopX,mTopY,mStepW);
        final Paint ap = new Paint(Paint.ANTI_ALIAS_FLAG);

        if(mBlackBmp==null) {
				
			int chessRadius;
            mBlackBmp = new ChessBMP();
            mWhiteBmp = new ChessBMP();
			chessRadius = mStepW/2;
			getChessImageFromAssetsFile(chessRadius);
			mBlackBmp.mSmall = black_stone;
			mWhiteBmp.mSmall = white_stone;

			
			//chessRadius = mStepW*mScaleRate/2;
			//getChessImageFromAssetsFile(chessRadius);
			mBlackBmp.mBig = black_stone;
			mWhiteBmp.mBig = white_stone;
	
			mBlackBmp.mMu=BitmapFactory.decodeResource( getResources(), R.drawable.bmu16) ;
			mWhiteBmp.mMu=BitmapFactory.decodeResource( getResources(), R.drawable.wmu16) ;					

        }


		



        if(mFocusWidget==null) {
            mFocusWidget = new FocusWidget();
            mFocusWidget.mSmall = BitmapFactory.decodeResource(getResources(), R.drawable.focus18) ;
        }
        if(mDianMuMode==true) {
            // mFullBoard=true;
        }
        Bitmap boardBmp=mBigBoardBmp;


       // Bitmap chessMem = mChessMem;
        if(mFullBoard==false) {
            boardBmp=mBigBoardBmp;
            //xHelper.log("xGoActivity","BigViewX="+mBigViewX+"BigViewY="+mBigViewY);
            //canvas.drawBitmap(boardBmp ,mBigViewX,mBigViewY,  null) ;

            Rect src = new Rect();
            Rect dst = new Rect();
            src.top = mBigViewY;
            src.left = mBigViewX;
            src.right = mBigViewX+mCanvasWidth;
            src.bottom = mBigViewY+mCanvasWidth;

            dst.top=0;
            dst.left=0;
            dst.right=mCanvasWidth;
            dst.bottom=mCanvasWidth;
            canvas.drawBitmap(boardBmp, src, dst, ap);
           // if(chessMem!=null)
           //     canvas.drawBitmap(chessMem, src, dst, ap);



            if(mChessListener.haveLogic()) {
                if(mDirty==true) {
                    freeMem();
                }

                drawChessesDirect(canvas,mBoardSize,mTopX,mTopY,mStepW,false);

            }


        } else {
            boardBmp=mSmallBoardBmp;

            canvas.drawBitmap(boardBmp ,0,0,  ap) ;



            if(mChessListener.haveLogic()) {
                if(mDirty==true) {
                    freeMem();
                }

                drawChessesDirect(canvas,mBoardSize,mTopX,mTopY,mStepW,false);

            }

        }

        if(bCursorMode==true) drawCursor(canvas);
        else drawTempChess(canvas);

    }

    boolean bCursorMode=false;
    public void setCursorMode(boolean b) {
        bCursorMode=b;
    }
    void showFocus() {
        if(mFocusWidget!=null){
            if(mFocusWidget.visible == true){
                mFocusWidget.visible = false;
            }else{
                mFocusWidget.visible = true;
            }
        }
        this.invalidateEx(true);
    }
    void hideFocus() {
        if(mFocusWidget!=null) mFocusWidget.visible = false;
        this.invalidateEx(true);
    }


    void scrollView(float dx,float dy) {
        mBigViewX-=dx;
        mBigViewY-=dy;
        if(mBigViewX<=0) mBigViewX=0;
        if(mBigViewY<=0) mBigViewY=0;
        if(mBigViewX+mCanvasWidth>=mCanvasWidth*mScaleRate)
            mBigViewX=mCanvasWidth*(mScaleRate-1);
        if(mBigViewY+mCanvasWidth>=mCanvasWidth*mScaleRate)
            mBigViewY=mCanvasWidth*(mScaleRate-1);
        this.invalidateEx(true);

    }

    boolean mDirty = false;
    void invalidateEx(boolean dirty) {
        mDirty=dirty;
        this.invalidate();
    }



    void putChess() {
        mDianMuResult.clean();
        int x,y;
        x=mFocusX;
        y=mFocusY;
        mChessListener.onPutChess(x,y);
        mCursorX=-1;
        mCursorY=-1;



    }


    ChessEventListener mChessListener=null;
    public void setChessListener(ChessEventListener listener) {
        mChessListener=listener;
    }

    void reCalFocus(float x,float y) {
        int lx,ly;
        if(mFullBoard==true) {
             lx = getLX(x,mTopX,mStepW);
             ly = getLY(y,mTopY,mStepW);
            mFocusX=lx;
            mFocusY=ly;
        } else {
             lx = getLX(x+mBigViewX,mTopX*mScaleRate,mStepW*mScaleRate);
             ly = getLY(y+mBigViewY,mTopY*mScaleRate,mStepW*mScaleRate);
            mFocusX=lx;
            mFocusY=ly;
        }
        xHelper.log("reCalFocus lx"+lx+"ly="+ly);
    }

    void switchBoardMode(boolean mode) {
        if(this.mFullBoard!=mode) {
            this.mFullBoard = mode;
            if(mode==false) {
                //xHelper.log("GoActivity","lx="+mFocusX+"ly="+mFocusY);

                float px_old = getPX(mFocusX,mTopX,mStepW);
                float py_old = getPY(mFocusY,mTopY,mStepW);

                float px = getPX(mFocusX,mTopX*mScaleRate,mStepW*mScaleRate);
                float py = getPY(mFocusY,mTopY*mScaleRate,mStepW*mScaleRate);

                float x=px-px_old;
                float y=py-py_old;
                if(x<=0) x=0;
                if(y<=0) y=0;
                mBigViewX=(int)x;
                mBigViewY=(int)y;
                // draw focus


            } else {
                // mFocusWidget.visible=false;
            }
            invalidateEx(true);
        }


    }

    Bitmap scaleBmp(Bitmap src,int rate) {
        if(rate!=1) {
            int bmpW = src.getWidth();
            int bmpH = src.getHeight();
            Matrix matrix=new Matrix();
            matrix.postScale(rate, rate);
            Bitmap resizeBmp=Bitmap.createBitmap(src, 0, 0, bmpW, bmpH, matrix, true);
            return resizeBmp;
        }
        return src;

    }

    void drawBoardMem(int size,int lx,int ly,float step) {
        if(xBoardBmp.IsQiPanDrawed==true && xBoardBmp.mBoardSize==mBoardSize) {
            mBigBoardBmp=xBoardBmp.mBigBoardBmp;
            mSmallBoardBmp=xBoardBmp.mSmallBoardBmp;
        } else {
            int w=mCanvasWidth;
            xBoardBmp.mSmallBoardBmp=Bitmap.createBitmap(w, w, android.graphics.Bitmap.Config.ARGB_4444);
            w*=mScaleRate;
            xBoardBmp.mBigBoardBmp=Bitmap.createBitmap(w, w, android.graphics.Bitmap.Config.ARGB_4444);
            mBigBoardBmp=xBoardBmp.mBigBoardBmp;
            mSmallBoardBmp=xBoardBmp.mSmallBoardBmp;
            drawQiPanEx(mSmallBoardBmp,mBoardSize,mTopX,mTopY,mStepW);
            drawQiPanEx(mBigBoardBmp,mBoardSize,mTopX*mScaleRate,mTopY*mScaleRate,mStepW*mScaleRate);
        }
    }

    Bitmap forceDraw(Context ctx,int size,int w) {
        int stepW=w/(size+1);
        int topX=stepW;
        int topY=stepW;
        Bitmap memBmp=Bitmap.createBitmap(w, w, android.graphics.Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas();
        canvas.setBitmap(memBmp);
        android.graphics.drawable.BitmapDrawable backg = (BitmapDrawable)getResources().getDrawable(R.drawable.backrepeat);
        Rect bounds = new Rect(0,0,w,w);
        backg.setBounds(bounds);
        backg.draw(canvas);
        drawQiPanEx(memBmp,size,topX,topY,stepW);
        if(mChessListener.haveLogic()==true) {
            drawChesses(memBmp,size,topX,topY,stepW,true);
        }
        return memBmp;
    }
    void drawQiPanEx(Bitmap memBmp,int size,int lx,int ly,float step) {

        xBoardBmp.IsQiPanDrawed = true;
        xBoardBmp.mBoardSize=size;
        Canvas canvas = new Canvas();
        canvas.setBitmap(memBmp);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(12);
        paint.setAntiAlias(true);

        Paint paintDot = new Paint();
        paintDot.setColor(Color.BLACK);

        int width = (int)(step*(size-1));
        int startX,startY,stopX,stopY;

        ly-=2;

        startX=lx;
        startY=ly;
        stopX=lx+width;
        stopY=ly;

        Bitmap h_outline= BitmapFactory.decodeResource(getResources(), R.drawable.houtline) ;
        Bitmap v_outline= BitmapFactory.decodeResource(getResources(), R.drawable.voutline) ;
        Bitmap h_bmp= BitmapFactory.decodeResource(getResources(), R.drawable.h_bl) ;
        Bitmap v_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.v_bl) ;
        Bitmap dot_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.dot) ;
        //draw horizon line
        for(int i=0 ; i<size; i++) {
            //if(i==0 || i==size-1)
            //	canvas.drawLine(startX, startY, stopX, stopY, paintDot);
            //else
            //canvas.drawLine(startX, startY, stopX, stopY, paint);
            int x1=startX;
            int x2=stopX;
            while(x1<x2) {
                if(i==0 || i==size-1)
                    canvas.drawBitmap(h_outline, x1, startY+1, null);
                else
                    canvas.drawBitmap(h_bmp, x1, startY+2, null);
                x1++;
            }
            String tag = new Integer(19-i).toString();
            paint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(tag,startX-4,startY+6,paint);
            paint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(tag,stopX+4,startY+6,paint);
            startY+=step;
            stopY+=step;

        }


        //draw ver line
        startX=lx-2;
        startY=ly+2;
        stopX=lx;
        stopY=ly+width+2;
        for(int i=0 ; i<size; i++) {
            //if(i==0 || i==size-1)
            //	canvas.drawLine(startX, startY, stopX, stopY, paintDot);
            //else
            //canvas.drawLine(startX, startY, stopX, stopY, paint);
            int y1=startY;
            int y2=stopY;
            if(i==0 || i==size-1)
                y2=stopY+2;
            if(i==size-1)
                startX++;
            while(y1<y2) {
                if(i==0 || i==size-1) {
                    canvas.drawBitmap(v_outline, startX+1, y1-1, null);
                } else
                    canvas.drawBitmap(v_bmp, startX+2, y1, null);
                y1++;
            }
            int ix = i;
            if(ix>7) ix++;
            String tag = new String(Character.toChars(65+ix));
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(tag,startX+2,startY-6,paint);
            canvas.drawText(tag,startX+2,stopY+16,paint);
            startX+=step;
            stopX+=step;

        }
        //draw dots

        final Paint ap = new Paint(Paint.ANTI_ALIAS_FLAG);
        if(mBoardSize==19) {
            startX=(int)(lx+3*step);
            startY=(int)(ly+3*step);
            float offset=(float)2;
            float offsety=(float)0;
            //canvas.drawRect(startX-offset, startY-offset, startX+offset+1,startY+offset+1, paintDot);
            canvas.drawBitmap(dot_bmp, startX-offset, startY-offsety, ap);

            startX=(int)(lx+3*step);
            startY=(int)(ly+9*step);

            //canvas.drawRect(startX-offset, startY-offset, startX+offset+1,startY+offset+1, paintDot);

            canvas.drawBitmap(dot_bmp, startX-offset, startY-offsety, ap);

            startX=(int)(lx+9*step);
            startY=(int)(ly+3*step);

            //canvas.drawRect(startX-offset, startY-offset, startX+offset+1,startY+offset+1, paintDot);
            canvas.drawBitmap(dot_bmp, startX-offset, startY-offsety, ap);
            startX=(int)(lx+9*step);
            startY=(int)(ly+9*step);

            //canvas.drawRect(startX-offset, startY-offset, startX+offset+1,startY+offset+1, paintDot);
            canvas.drawBitmap(dot_bmp, startX-offset, startY-offsety, ap);
            startX=(int)(lx+3*step);
            startY=(int)(ly+15*step);

            //canvas.drawRect(startX-offset, startY-offset, startX+offset+1,startY+offset+1, paintDot);
            canvas.drawBitmap(dot_bmp, startX-offset, startY-offsety, ap);
            startX=(int)(lx+9*step);
            startY=(int)(ly+15*step);

            //canvas.drawRect(startX-offset, startY-offset, startX+offset+1,startY+offset+1, paintDot);
            canvas.drawBitmap(dot_bmp, startX-offset, startY-offsety, ap);
            startX=(int)(lx+15*step);
            startY=(int)(ly+3*step);

            //canvas.drawRect(startX-offset, startY-offset, startX+offset+1,startY+offset+1, paintDot);
            canvas.drawBitmap(dot_bmp, startX-offset, startY-offsety, ap);
            startX=(int)(lx+15*step);
            startY=(int)(ly+9*step);

            //canvas.drawRect(startX-offset, startY-offset, startX+offset+1,startY+offset+1, paintDot);
            canvas.drawBitmap(dot_bmp, startX-offset, startY-offsety, ap);
            startX=(int)(lx+15*step);
            startY=(int)(ly+15*step);

            //canvas.drawRect(startX-offset, startY-offset, startX+offset+1,startY+offset+1, paintDot);
            canvas.drawBitmap(dot_bmp, startX-offset, startY-offsety, ap);
        }
    }

    int getLX(float x,int topx,int stepW) {
        x=x-topx;
        int index=(int)(x/stepW);
        int offset=(int)(x % stepW);
        if(offset>(stepW/2)) {
            index++;
        }
        if(index==mBoardSize) {
            index--;
        }
        if(index>mBoardSize) {
            return -1;
        }
        return index;
    }
    int getLY(float y,int topy,int stepW) {
        y=y-topy;
        int index=(int)(y/stepW);
        int offset=(int)(y % stepW);
        if(offset>(stepW/2)) {
            index++;
        }
        if(index==mBoardSize) {
            index--;
        }
        if(index>mBoardSize) {
            return -1;
        }
        return (index);
    }

    float getPX(int x,int topx,int stepw) {
        float px;
        px=(x)*stepw+topx;
        return px;
    }
    float getPY(int y,int topy,int stepw) {
        float py;
        py=(y)*stepw+topy;
        return py;
    }


    int mCurrentSide=1;
    void drawTempChess(Canvas paramCanvas)
    {
        Paint localPaint = new Paint();
        localPaint.setFilterBitmap(true);
        Bitmap localBitmap1;
        Bitmap localBitmap2;
        if (this.mCurrentSide == 1)
        {
            localBitmap1 = this.black_stone;
            localBitmap2 = this.bshadow;
        }else{

            localBitmap1 = this.mWhiteBmp.mSmall;
            localBitmap2 = this.wshadow;
        }

        if(mDianMuMode==true) return;
        if (this.mFocusWidget.visible == true){
            if (this.mFullBoard){
                float f1 = getPX(this.mFocusX, this.mTopX, this.mStepW);
                float f2 = getPY(this.mFocusY, this.mTopY, this.mStepW);
                new Rect();
                Rect localRect1 = new Rect();
                localRect1.top = ((int)(0.5F + (f2 - this.mStepW / 2)));
                localRect1.left = ((int)(0.5F + (1.0F + (f1 - this.mStepW / 2))));
                localRect1.right = ((int)(2.0F + (f1 + this.mStepW / 2) - 0.5F));
                localRect1.bottom = ((int)(1.0F + (f2 + this.mStepW / 2) - 0.5F));
                float f3 = this.mStepW / 3;
                localRect1.left = ((int)(localRect1.left - 3.0F * f3 / 4.0F));
                localRect1.right = ((int)(localRect1.right + f3 / 4.0F));
                localRect1.bottom = ((int)(f3 + localRect1.bottom));
                paramCanvas.drawBitmap(localBitmap2, null, localRect1, localPaint);
                localRect1.left = ((int)(localRect1.left + 3.0F * f3 / 4.0F));
                localRect1.right = ((int)(localRect1.right - f3 / 4.0F));
                localRect1.bottom = ((int)(localRect1.bottom - f3));
                paramCanvas.drawBitmap(localBitmap1, null, localRect1, localPaint);
            }else{
                float f4 = getPX(this.mFocusX, this.mTopX * this.mScaleRate, this.mStepW * this.mScaleRate);
                float f5 = getPY(this.mFocusY, this.mTopY * this.mScaleRate, this.mStepW * this.mScaleRate);
                new Rect();
                Rect localRect2 = new Rect();
                localRect2.top = ((int)(0.5F + (1.0F + (f5 - this.mStepW * this.mScaleRate / 2.0F) - this.mBigViewY)));
                localRect2.left = ((int)(0.5F + (2.0F + (f4 - this.mStepW * this.mScaleRate / 2.0F) - this.mBigViewX)));
                localRect2.right = ((int)(3.0F + (f4 + this.mStepW * this.mScaleRate / 2.0F) - 1.0F - this.mBigViewX - 0.5F));
                localRect2.bottom = ((int)(1.0F + (f5 + this.mStepW * this.mScaleRate / 2.0F) - 1.0F - this.mBigViewY - 0.5F));
                float f6 = this.mStepW * this.mScaleRate / 3.0F;
                localRect2.left = ((int)(localRect2.left - 3.0F * f6 / 4.0F));
                localRect2.right = ((int)(localRect2.right + f6 / 4.0F));
                localRect2.bottom = ((int)(f6 + localRect2.bottom));
                paramCanvas.drawBitmap(localBitmap2, null, localRect2, localPaint);
                localRect2.left = ((int)(localRect2.left + 3.0F * f6 / 4.0F));
                localRect2.right = ((int)(localRect2.right - f6 / 4.0F));
                localRect2.bottom = ((int)(localRect2.bottom - f6));
                paramCanvas.drawBitmap(localBitmap1, null, localRect2, localPaint);
            }
        }
    }
}
