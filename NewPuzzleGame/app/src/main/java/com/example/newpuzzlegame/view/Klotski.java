package com.example.newpuzzlegame.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newpuzzlegame.Db;
import com.example.newpuzzlegame.MainActivity;
import com.example.newpuzzlegame.Menu;
import com.example.newpuzzlegame.Play;
import com.example.newpuzzlegame.R;
import com.example.newpuzzlegame.UserName;
import com.example.newpuzzlegame.model.Block;
import com.example.newpuzzlegame.util.Dimension;
import com.example.newpuzzlegame.util.L;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.abs;


public class Klotski extends SurfaceView implements SurfaceHolder.Callback {

    private final String TAG = getClass().getSimpleName();
    private SurfaceHolder mSurfaceHolder;
    private DrawThread mDrawThread;
    private int[][] map = new int[5][4];
    private List<Block> mBlocks,bls;
    private int stepsno = 0;
    private Rect mRect;
    private int mCellWidth;
    private int mCellHeight;
    private AlertDialog alertDialog;
    private float mBlockSpacing;
    private Drawable mDrawable1x1;
    private Drawable mDrawable1x2;
    private Drawable mDrawable2x1;
    private Drawable mDrawable2x2;
    private Drawable mDrawable3x3;

    //get the score
    String f_id;
    public int onlineScore;
    public int thisScore;
    int thisTimeMode;
    int thisTimeSteps;
    int thisTimeSeconds;
    int thiTimeLevel;

    int firstx,firsty;
    float lastx,lasty;
    DatabaseReference myref,myrefnew;

    public Klotski(Context context) { this(context, null);
    }

    public Klotski(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Klotski(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        setZOrderOnTop(true);
        mSurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);

        SharedPreferences mSettings = getContext().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        f_id = mSettings.getString("f_id","");

        setWillNotDraw(false);

        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.Klotski);
        mBlockSpacing = ta.getDimension(R.styleable.Klotski_blockSpacing, Dimension.dp2px(getContext(), 3));
        mDrawable1x1 = ta.getDrawable(R.styleable.Klotski_blockDrawable1x1);
        mDrawable1x2 = ta.getDrawable(R.styleable.Klotski_blockDrawable1x2);
        mDrawable2x1 = ta.getDrawable(R.styleable.Klotski_blockDrawable2x1);
        mDrawable2x2 = ta.getDrawable(R.styleable.Klotski_blockDrawable2x2);
        mDrawable3x3 = ta.getDrawable(R.styleable.Klotski_blockDrawable3x3);
        ta.recycle();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        // sizes of the board
        int mHeight = getHeight();
        int mWidth = getWidth();

        MainActivity activity =  (MainActivity)getContext();
        activity.lay.setLayoutParams(getLayoutParams());

        Log.d("myz", "before adjust the height -> " + mHeight + " width -> " + mWidth);

        ViewGroup.LayoutParams params = getLayoutParams();

        if (mWidth / 4 <= mHeight / 5) {
            // need to reduce the height
            Log.d("myz", "change height");
            params.height = 5 * mWidth / 4;
            params.width = mWidth;
        } else {
            // Need to reduce the width
            Log.d("myz", "Change width");
            params.height = mHeight;
            params.width = 4 * mHeight / 5;
        }

        Log.d("myz", "after adjust the height -> " + params.height + " width -> " + params.width);
        setLayoutParams(params);



        Log.i(TAG, "adjusted height -> " + params.height + " width -> " + params.width);

        mCellWidth = (params.width - getPaddingLeft() - getPaddingRight()) / 4;
        mCellHeight = (params.height - getPaddingTop() - getPaddingBottom()) / 5;
        Log.i("myz", "mCellWidth -> " + mCellWidth + " mCellHeight -> " + mCellHeight);
        mRect = new Rect(0, 0, params.width - getPaddingLeft() - getPaddingRight(), params.height - getPaddingTop() - getPaddingBottom());

        updateBlocks();
    }

    private int touchedId = -1;
    private float mDownX = 0;
    private float mDownY = 0;
    private float mLastX = 0;
    private float mLastY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                L.i(this, "ACTION_DOWN");
                mDownX = event.getX();
                mDownY = event.getY();

                touchedId = getTouchedBlock((int) mDownX, (int) mDownY);

                try {
                    Block s = mBlocks.get(touchedId);
                    firstx = Math.round((float) s.getRect().top / mCellHeight) * mCellHeight;
                    firsty = Math.round((float) s.getRect().left / mCellWidth) * mCellWidth;
                }catch (Exception e){
                    Log.d("myz", "err :"+e);
                }

                Log.d("myz", "Initial X"+mDownX+" Initial Y"+mDownY);
                break;

            case MotionEvent.ACTION_UP:
                L.i(this, "ACTION_UP");
                if (touchedId == -1) {
                    break;
                }

                Block b = mBlocks.get(touchedId);

                lastx = b.getX() ;
                lasty = b.getY() ;


                int newTop = Math.round((float) b.getRect().top / mCellHeight) * mCellHeight;
                int newLeft = Math.round((float) b.getRect().left / mCellWidth) * mCellWidth;
                b.getRect().offsetTo(newLeft, newTop);
                Log.d("myz", "firstx  :" + firsty+" lastx   :"+ newLeft);
                Log.d("myz", "firsty  :" + firstx+ " lasty  :"+newTop);

                int steps = 0;

                if(firsty != newLeft){
                    steps =  abs(newLeft - firsty) / mCellHeight;
                }else if(firstx != newTop){
                    steps =  abs(firstx - newTop) / mCellWidth;
                }

                MainActivity activity = (MainActivity) getContext();
                activity.setSteps(steps);

                mDownX = 0;
                mDownY = 0;
                Log.d("myz", "Touched id" + touchedId);

                //  Log.d("myz", "Touch ID  :" + touchedId);
                if(touchedId==1){
                    Block block = mBlocks.get(touchedId);
                  /*
                    Rect rect = new Rect(block.getRect().left,
                            block.getRect().top,
                            block.getRect().right,
                            block.getRect().bottom);
                    Log.d("myz", "rect.left :"+ rect.left + "   event.getX()  :"+event.getX()+"   mLastX:   "+mLastX);
                    Log.d("myz", "rect.top :"+ rect.top + "   event.getY()  :"+event.getY()+"   mLastY:   "+mLastY);
                    Log.d("myz", "mCellHeight :"+ mCellHeight);
                    Log.d("myz", "mCellWidth :"+ mCellWidth);
                    Log.d("myz", "newTop :"+ newTop);
                    Log.d("myz", "newLeft :"+ newLeft);
                    */
                  //win!!
                    if(block.getRect().top/mCellHeight==3 && block.getRect().left/mCellWidth==1){
                        Log.d("myz", "You Won");
                        MainActivity x = (MainActivity) getContext();
                        x.t.cancel();
                        //set indicator of this level to finished
                        getScore();
                    }

                }

                if(touchedId==4){
                    Block block = mBlocks.get(touchedId);
                    if(block.getRect().top/mCellWidth==3 && block.getRect().left/mCellHeight==1){
                        Log.d("myz", "You Won 4 block");
                    }

                }
                touchedId = -1;
                break;
            case MotionEvent.ACTION_MOVE:
                L.i(this, "ACTION_MOVE");
                //  Log.d("myz", "ACTION_MOVE");
                if (touchedId == -1) {
                    break;
                }
                Block block = mBlocks.get(touchedId);
                Rect rect = new Rect(block.getRect().left,
                        block.getRect().top,
                        block.getRect().right,
                        block.getRect().bottom);
                int newX = (int) (rect.left + event.getX() - mLastX);
                rect.offsetTo(newX, rect.top);
                if (canMove(rect, touchedId)) {
                    block.setRect(rect);
                    mBlocks.set(touchedId, block);
                }
                rect = new Rect(block.getRect().left,
                        block.getRect().top,
                        block.getRect().right,
                        block.getRect().bottom);
                int newY = (int) (rect.top + event.getY() - mLastY);
                rect.offsetTo(rect.left, newY);
                if (canMove(rect, touchedId)) {
                    block.setRect(rect);
                    mBlocks.set(touchedId, block);
                }
                break;
            default:
                break;
        }
        mLastX = event.getX();
        mLastY = event.getY();
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mDrawThread = new DrawThread(mSurfaceHolder);
        mDrawThread.startDrawing();
        mDrawThread.setBlocks(mBlocks);
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("myz", "surfaceChanged");
        updateBlocks();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mDrawThread.stopDrawing();
    }

    protected int getTouchedBlock(int x, int y) {
        for (int i = 1; i < mBlocks.size(); i++) {
            if (mBlocks.get(i).getRect().contains(x, y)) {
                return i;
            }
        }
        return -1;
    }

    protected boolean canMove(Rect rect, int ignore) {
        for (int i = 1; i < mBlocks.size(); i++) {
            if ((i != ignore && rect.intersect(mBlocks.get(i).getRect())) || !mRect.contains(rect)) {
                return false;
            }
        }
        return true;
    }

    public void setBlocks(List<Block> blocks) {
        mBlocks = blocks;
        if (mBlocks != null) {
            for (Block block : mBlocks) {
                if (block.getDrawable() == null) {
                    block.setDrawable(resolveDrawable(block.getType()));
                }
            }
        }
        updateBlocks();
    }

    protected void updateBlocks() {

        if (mBlocks != null) {
            for (int i = 0; i < mBlocks.size(); i++) {
                Block block = mBlocks.get(i);
                block.getRect().left = block.getX() * mCellWidth;
                block.getRect().top = block.getY() * mCellWidth;
                block.getRect().right = (block.getX() + block.getType().width()) * mCellWidth;
                block.getRect().bottom = (block.getY() + block.getType().height()) * mCellHeight;
                mBlocks.set(i, block);
            }
        }



        if (mDrawThread != null) {
            mDrawThread.setBlocks(mBlocks);
        }



    }

    protected Drawable resolveDrawable(Block.Type type) {
        if (type == Block.Type.RECT_1x1) {
            return mDrawable1x1;
        } else if (type == Block.Type.RECT_1x2) {
            return mDrawable1x2;
        } else if (type == Block.Type.RECT_2x1) {
            return mDrawable2x1;
        } else if (type == Block.Type.RECT_2x2) {
            return mDrawable2x2;
        }else if(type == Block.Type.RECT_3x3){
            return mDrawable3x3;
        }
        return null;
    }

    private class DrawThread extends Thread {

    private SurfaceHolder mSurfaceHolder;
        private boolean running;

        private List<Block> mBlocks;

        public DrawThread(SurfaceHolder mSurfaceHolder) {
            this.mSurfaceHolder = mSurfaceHolder;
        }

        public void startDrawing() {
            running = true;
            start();
        }

        public void stopDrawing() {
            running = false;
        }

        public void setBlocks(List<Block> blocks) {
            this.mBlocks = blocks;
        }

        @Override
        public void run() {
            Canvas canvas;
            while (running) {
                if (mBlocks == null) {
                    continue;
                }
                canvas = null;
                try {
                    canvas = mSurfaceHolder.lockCanvas(null);
                    synchronized (mSurfaceHolder) {
                        draw(canvas);
                    }
                } finally {
                    if (canvas != null) {
                        mSurfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }

        public void draw(Canvas canvas) {
            int spacing = (int) (mBlockSpacing / 2);
            if (canvas == null)
                return;
            Paint paint = new Paint();
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            canvas.drawPaint(paint);
            for (Block block : mBlocks) {
                Drawable drawable = block.getDrawable();
                drawable.setBounds(
                        block.getRect().left + spacing,
                        block.getRect().top + spacing,
                        block.getRect().right - spacing,
                        block.getRect().bottom - spacing);
                drawable.draw(canvas);
            }
        }
    }


    @SuppressLint("ResourceAsColor")
    public void openDialogtime2(){

        alertDialog = new AlertDialog.Builder(getContext()).create();
        // Set Custom Title
        TextView title = new TextView(getContext());
        // Title Properties
        title.setText(R.string.time);
        title.setPadding(20, 70, 20, 30);   // Set Position
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        title.setTextSize(20);
        alertDialog.setCustomTitle(title);

        // Set Message
        TextView msg = new TextView(getContext());
        // Message Properties
        msg.setText(R.string.sorry);
        msg.setGravity(Gravity.CENTER_HORIZONTAL);
        msg.setTextColor(Color.BLACK);
        alertDialog.setView(msg);
        // Set Button
        // you can more buttons
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,""+getResources().getString(R.string.finishcl), new DialogInterface.OnClickListener() {
            @SuppressLint("SetTextI18n")
            public void onClick(DialogInterface dialog, int which) {

                Intent username = new Intent(getContext(), Menu.class);
                ((Activity)getContext()).startActivity(username);
                ((Activity)getContext()).finish();

            }
        });
/*
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Perform Action on Button
            }
        });
*/
        new Dialog(getContext());
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        alertDialog.show();

        // Set Properties for OK Button
        final Button okBT = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        LinearLayout.LayoutParams neutralBtnLP = (LinearLayout.LayoutParams) okBT.getLayoutParams();
        neutralBtnLP.gravity = Gravity.FILL_HORIZONTAL;
        okBT.setPadding(50, 10, 10, 10);   // Set Position
        okBT.setTextColor(R.color.grey1);
        okBT.setLayoutParams(neutralBtnLP);

        final Button cancelBT = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams negBtnLP = (LinearLayout.LayoutParams) okBT.getLayoutParams();
        negBtnLP.gravity = Gravity.FILL_HORIZONTAL;
        cancelBT.setTextColor(R.color.grey1);
        cancelBT.setLayoutParams(negBtnLP);
    }

    @SuppressLint("ResourceAsColor")
    public void openDialogtime(){
        MainActivity activity =  (MainActivity)getContext();
        Intent play = new Intent(getContext(), Play.class);
        play.putExtra("mode",thisTimeMode);

        alertDialog = new AlertDialog.Builder(getContext()).create();
        // Set Custom Title
        TextView title = new TextView(getContext());
        // Title Properties
        title.setText(R.string.achieved);
        title.setPadding(20, 70, 20, 30);   // Set Position
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        title.setTextSize(20);
        alertDialog.setCustomTitle(title);
        if (thisTimeMode==1){
            // Set Message
            TextView msg = new TextView(getContext());
            // Message Properties
            msg.setText(getResources().getString(R.string.won_relax,thisTimeSteps));
            msg.setGravity(Gravity.CENTER_HORIZONTAL);
            msg.setTextColor(Color.BLACK);
            alertDialog.setView(msg);
        }
        else if (thisTimeMode== 2){
            // Set Message
            TextView msg = new TextView(getContext());
            // Message Properties
            if (thisScore>onlineScore){
                msg.setText(getResources().getString(R.string.best_score,thisTimeSteps,(59-thisTimeSeconds),thisScore));

            }
            else{
                msg.setText(getResources().getString(R.string.won_challenge,thisTimeSteps,(59-thisTimeSeconds),thisScore));
            }

            msg.setGravity(Gravity.CENTER_HORIZONTAL);
            msg.setTextColor(Color.BLACK);
            alertDialog.setView(msg);


            Log.d("joshua","this time score"+Integer.toString(onlineScore));

            Log.d("joshua","this time score"+Integer.toString(thisScore));
        }

        // Set Button
        // you can more buttons
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,""+getResources().getString(R.string.finishcl), new DialogInterface.OnClickListener() {
            @SuppressLint("SetTextI18n")
            public void onClick(DialogInterface dialog, int which) {
                Intent username = new Intent(getContext(), Menu.class);
                ((Activity)getContext()).startActivity(username);
                ((Activity)getContext()).finish();

            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"Next level", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getContext(),Play.class);
                intent.putExtra("mode", ((MainActivity) getContext()).mode);
                intent.putExtra("name", ((MainActivity) getContext()).name);
                getContext().startActivity(intent);
            }
        });



        new Dialog(getContext());
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        alertDialog.show();

        // Set Properties for OK Button
        final Button okBT = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        LinearLayout.LayoutParams neutralBtnLP = (LinearLayout.LayoutParams) okBT.getLayoutParams();
        neutralBtnLP.gravity = Gravity.FILL_HORIZONTAL;
        okBT.setPadding(50, 10, 10, 10);   // Set Position
        okBT.setTextColor(R.color.grey1);
        okBT.setLayoutParams(neutralBtnLP);

        final Button cancelBT = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams negBtnLP = (LinearLayout.LayoutParams) okBT.getLayoutParams();
        negBtnLP.gravity = Gravity.FILL_HORIZONTAL;
        cancelBT.setTextColor(R.color.grey1);
        cancelBT.setLayoutParams(negBtnLP);
        //set this level indicator
        Db database = new Db();
        database.setLevelIndicator(f_id,((MainActivity) getContext()).level+1,true);
    }


    public void getScore(){
        myref = FirebaseDatabase.getInstance().getReference().child("users").child(f_id).child("score");
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MainActivity activity =  (MainActivity)getContext();
                thisTimeMode = activity.mode;
                thisTimeSteps = activity.steps;
                thisTimeSeconds = activity.seconds;
                thiTimeLevel = activity.level;
                thisScore = thiTimeLevel*5000/((59-thisTimeSeconds)+thisTimeSteps);
                // String snap = dataSnapshot.getValue(String.class);
                Long scor = dataSnapshot.getValue(Long.class);


                assert scor != null;
                if(scor.equals("")){
                    onlineScore = 0;
                }else {
                    onlineScore= new Long(scor).intValue();
                }
                openDialogtime();


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("myz", "Error set score");

            }
        });
    }

    public void setScore(int thisScore){

        int s = thisScore;
        myrefnew = FirebaseDatabase.getInstance().getReference();
        myrefnew.child("users").child(f_id).child("score").setValue(s)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("joshua", "Success");

//                        Toast.makeText(getContext(), ""+getContext().getString(R.string.score), Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("joshua", "Failure");
//                        Toast.makeText(getContext(), ""+getContext().getString(R.string.scorenot), Toast.LENGTH_SHORT).show();

                    }
                });
    }
}
