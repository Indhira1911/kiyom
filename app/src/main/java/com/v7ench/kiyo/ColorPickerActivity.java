package com.v7ench.kiyo;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.v7ench.kiyo.data.ColorItem;
import com.v7ench.kiyo.data.ColorItems;
import com.v7ench.kiyo.global.AppController;
import com.v7ench.kiyo.global.UrlReq;
import com.v7ench.kiyo.utils.Cameras;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ColorPickerActivity extends AppCompatActivity implements CameraColorPickerPreview.OnColorSelectedListener, View.OnClickListener {
    protected static final String TAG = ColorPickerActivity.class.getSimpleName();
    protected static final String PICKED_COLOR_PROGRESS_PROPERTY_NAME = "pickedColorProgress";
    protected static final String SAVE_COMPLETED_PROGRESS_PROPERTY_NAME = "saveCompletedProgress";
    protected static final long DURATION_CONFIRM_SAVE_MESSAGE = 400;
    protected static final long DELAY_HIDE_CONFIRM_SAVE_MESSAGE = 1400;
    private static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return c;
    }
    protected Camera mCamera;

    protected boolean mIsPortrait;

    protected FrameLayout mPreviewContainer;

    protected CameraColorPickerPreview mCameraPreview;

    protected CameraAsyncTask mCameraAsyncTask;

    protected int mSelectedColor;

    protected int mLastPickedColor;

    protected View mPickedColorPreview;

    protected View mPickedColorPreviewAnimated;

    protected ObjectAnimator mPickedColorProgressAnimator;

    protected float mTranslationDeltaX;
    protected float mTranslationDeltaY;
    protected TextView mColorPreviewText;

    protected View mPointerRing;

    protected View mSaveCompletedIcon;

    protected View mSaveButton;

    protected float mSaveCompletedProgress;

    protected ObjectAnimator mSaveCompletedProgressAnimator;

    protected TextView mConfirmSaveMessage;

    protected Interpolator mConfirmSaveMessageInterpolator;

    protected Runnable mHideConfirmSaveMessage;

    protected boolean mIsFlashOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);
        Intent details=getIntent();
         String dqr=details.getStringExtra("dqr");
        initPickedColorProgressAnimator();
        initSaveCompletedProgressAnimator();
        initViews(dqr);
        initTranslationDeltas();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Setup the camera asynchronously.
        mCameraAsyncTask = new CameraAsyncTask();
        mCameraAsyncTask.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Cancel the Camera AsyncTask.
        mCameraAsyncTask.cancel(true);

        // Release the camera.
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }

        // Remove the camera preview
        if (mCameraPreview != null) {
            mPreviewContainer.removeView(mCameraPreview);
        }
    }

    @Override
    protected void onDestroy() {
        // Remove any pending mHideConfirmSaveMessage.
        mConfirmSaveMessage.removeCallbacks(mHideConfirmSaveMessage);

        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isFlashSupported()) {
            getMenuInflater().inflate(R.menu.menu_color_picker, menu);
            final MenuItem flashItem = menu.findItem(R.id.menu_color_picker_action_flash);
            int flashIcon = mIsFlashOn ? R.drawable.ic_flash_off : R.drawable.ic_flash_on;
            flashItem.setIcon(flashIcon);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int itemId = item.getItemId();
        boolean handled;
        switch (itemId) {
            case android.R.id.home:
                finish();
                handled = true;
                break;

            case R.id.menu_color_picker_action_flash:
                toggleFlash();
                handled = true;
                break;

            default:
                handled = super.onOptionsItemSelected(item);
        }

        return handled;
    }

    @Override
    public void onColorSelected(int color) {
        mSelectedColor = color;
        mPointerRing.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public void onClick(View v) {
        if (v == mCameraPreview) {
            mColorPreviewText.setVisibility(View.VISIBLE);
            animatePickedColor(mSelectedColor);
        } else if (v.getId() == R.id.activity_color_picker_save_button) {
            ColorItems.saveColorItem(this, new ColorItem(mLastPickedColor));
            //ColorItems.deleteColorItem(this, new ColorItem(mLastPickedColor));

        }
    }
    protected void initViews(final String dqr) {
        mIsPortrait = true;
        mPreviewContainer = (FrameLayout) findViewById(R.id.activity_color_picker_preview_container);
        mPickedColorPreview = findViewById(R.id.activity_color_picker_color_preview);
        mPickedColorPreviewAnimated = findViewById(R.id.activity_color_picker_animated_preview);
        mColorPreviewText = (TextView) findViewById(R.id.activity_color_picker_color_preview_text);
        mPointerRing = findViewById(R.id.activity_color_picker_pointer_ring);
        mSaveCompletedIcon = findViewById(R.id.activity_color_picker_save_completed);
        mSaveButton = findViewById(R.id.activity_color_picker_save_button);
        mSaveButton.setOnClickListener(this);
        mSaveCompletedIcon.setOnClickListener(this);
        mConfirmSaveMessage = (TextView) findViewById(R.id.activity_color_picker_confirm_save_message);
        mHideConfirmSaveMessage = new Runnable() {
            @Override
            public void run() {
                mConfirmSaveMessage.animate().translationY(-mConfirmSaveMessage.getMeasuredHeight()).setDuration(DURATION_CONFIRM_SAVE_MESSAGE)                     .start();
            }
        };
        positionConfirmSaveMessage();
        mConfirmSaveMessageInterpolator = new DecelerateInterpolator();
       mLastPickedColor = ColorItems.getLastPickedColor(this);
        applyPreviewColor(mLastPickedColor);
        mSaveCompletedIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String postcol=mColorPreviewText.getText().toString();
                if (postcol==Integer.toString(mLastPickedColor)) {
                    Toast.makeText(getApplicationContext(),"Tap on screen to get color",Toast.LENGTH_SHORT).show();
                }
                else {
                    mSaveCompletedIcon.setVisibility(View.GONE);
                    pcolor(dqr, postcol);
                }
                        }
        });
    }

public  void pcolor(final String dqr, final String postcol)
{
    StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlReq.POSTCOLUP, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jObj =new JSONObject(response);
                boolean error = jObj.getBoolean("error");
                if (!error) {
                    JSONObject user = jObj.getJSONObject("user");
                    String content = user.getString("content");
                    String pack = user.getString("pack");
                    String sterlizer = user .getString("sterlizer");
                    String sload = user .getString("sload");
                    String sdate = user .getString("sdate");
                    String dqrres = user .getString("dqr");
                    String type = user .getString("type");
                     Intent intent = new Intent(ColorPickerActivity.this, TstScanResult.class);
                    intent.putExtra("content",content);
                    intent.putExtra("pack",pack);
                    intent.putExtra("dqr",dqrres);
                    intent.putExtra("sterlizer",sterlizer);
                    intent.putExtra("sload",sload);
                    intent.putExtra("sdate",sdate);
                    intent.putExtra("dqres",dqrres);
                    intent.putExtra("type",type);
                    intent.putExtra("postcol", postcol);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Try again later",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ColorPickerActivity.this, MainActivity.class);
                    startActivity(intent);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                mSaveCompletedIcon.setVisibility(View.VISIBLE);
            }

        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            mSaveCompletedIcon.setVisibility(View.VISIBLE);
        }
    }){
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String,String> params= new HashMap<String, String>();
            params.put("dqr",dqr);
            params.put("postcol",postcol);
            return params;
        }
    };
    AppController.getInstance().addToRequestQueue(stringRequest);
}
    protected void positionConfirmSaveMessage() {
        ViewTreeObserver vto = mConfirmSaveMessage.getViewTreeObserver();
        if (vto.isAlive()) {
            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    ViewTreeObserver vto = mConfirmSaveMessage.getViewTreeObserver();
                    vto.removeOnPreDrawListener(this);
                    mConfirmSaveMessage.setTranslationY(-mConfirmSaveMessage.getMeasuredHeight());
                    return true;
                }
            });
        }
    }


    @SuppressLint("NewApi")
    protected void initTranslationDeltas() {
        ViewTreeObserver vto = mPointerRing.getViewTreeObserver();
        if (vto.isAlive()) {
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ViewTreeObserver vto = mPointerRing.getViewTreeObserver();
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
                        vto.removeGlobalOnLayoutListener(this);
                    } else {
                        vto.removeOnGlobalLayoutListener(this);
                    }

                    final Rect pointerRingRect = new Rect();
                    final Rect colorPreviewAnimatedRect = new Rect();
                    mPointerRing.getGlobalVisibleRect(pointerRingRect);
                    mPickedColorPreviewAnimated.getGlobalVisibleRect(colorPreviewAnimatedRect);

                    mTranslationDeltaX = pointerRingRect.left - colorPreviewAnimatedRect.left;
                    mTranslationDeltaY = pointerRingRect.top - colorPreviewAnimatedRect.top;
                }
            });
        }
    }
    protected void initPickedColorProgressAnimator() {
        mPickedColorProgressAnimator = ObjectAnimator.ofFloat(this, PICKED_COLOR_PROGRESS_PROPERTY_NAME, 1f, 0f);
        mPickedColorProgressAnimator.setDuration(400);
        mPickedColorProgressAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mPickedColorPreviewAnimated.setVisibility(View.VISIBLE);
                mPickedColorPreviewAnimated.getBackground().setColorFilter(mSelectedColor, PorterDuff.Mode.SRC_ATOP);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ColorItems.saveLastPickedColor(ColorPickerActivity.this, mLastPickedColor);
                applyPreviewColor(mLastPickedColor);
                mPickedColorPreviewAnimated.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mPickedColorPreviewAnimated.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    protected boolean isFlashSupported() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    protected void toggleFlash() {
        if (mCamera != null) {
            final Camera.Parameters parameters = mCamera.getParameters();
            final String flashParameter = mIsFlashOn ? Camera.Parameters.FLASH_MODE_OFF : Camera.Parameters.FLASH_MODE_TORCH;
            parameters.setFlashMode(flashParameter);
            mCamera.stopPreview();
            mCamera.setParameters(parameters);
            mCamera.startPreview();
            mIsFlashOn = !mIsFlashOn;
            invalidateOptionsMenu();
        }
    }


    protected void initSaveCompletedProgressAnimator() {
        mSaveCompletedProgressAnimator = ObjectAnimator.ofFloat(this, SAVE_COMPLETED_PROGRESS_PROPERTY_NAME, 1f, 0f);
    }


    protected void applyPreviewColor(int previewColor) {
        setSaveCompleted(false);
        mPickedColorPreview.getBackground().setColorFilter(previewColor, PorterDuff.Mode.SRC_ATOP);
        mColorPreviewText.setText(ColorItem.makeHexString(previewColor));
    }


    protected void animatePickedColor(int pickedColor) {
        mLastPickedColor = pickedColor;
        if (mPickedColorProgressAnimator.isRunning()) {
            mPickedColorProgressAnimator.cancel();
        }
        mPickedColorProgressAnimator.start();
    }


    protected void setSaveCompleted(boolean isSaveCompleted) {
        mSaveButton.setEnabled(!isSaveCompleted);
        mSaveCompletedProgressAnimator.cancel();
        mSaveCompletedProgressAnimator.setFloatValues(mSaveCompletedProgress, isSaveCompleted ? 0f : 1f);
        mSaveCompletedProgressAnimator.start();

        if (isSaveCompleted) {
            mConfirmSaveMessage.setVisibility(View.VISIBLE);
            mConfirmSaveMessage.animate().translationY(0).setDuration(DURATION_CONFIRM_SAVE_MESSAGE).setInterpolator(mConfirmSaveMessageInterpolator).start();
            mConfirmSaveMessage.removeCallbacks(mHideConfirmSaveMessage);
            mConfirmSaveMessage.postDelayed(mHideConfirmSaveMessage, DELAY_HIDE_CONFIRM_SAVE_MESSAGE);
        }
    }



    private class CameraAsyncTask extends AsyncTask<Void, Void, Camera> {

        FrameLayout.LayoutParams mPreviewParams;


        @Override
        protected Camera doInBackground(Void... params) {
            Camera camera = getCameraInstance();
            if (camera == null) {
                ColorPickerActivity.this.finish();
            } else {
                   Camera.Parameters cameraParameters = camera.getParameters();
                Camera.Size bestSize = Cameras.getBestPreviewSize(
                        cameraParameters.getSupportedPreviewSizes(), mPreviewContainer.getWidth(), mPreviewContainer.getHeight(), mIsPortrait);
                cameraParameters.setPreviewSize(bestSize.width, bestSize.height);
                camera.setParameters(cameraParameters);
                Cameras.setCameraDisplayOrientation(ColorPickerActivity.this, camera);
                int[] adaptedDimension = Cameras.getProportionalDimension(
                        bestSize , mPreviewContainer.getWidth(),mPreviewContainer.getHeight(), mIsPortrait);
                mPreviewParams = new FrameLayout.LayoutParams(adaptedDimension[0], adaptedDimension[1]);
                mPreviewParams.gravity = Gravity.CENTER;
            }
            return camera;
        }

        @Override
        protected void onPostExecute(Camera camera) {
            super.onPostExecute(camera);

            if (!isCancelled()) {
                mCamera = camera;
                if (mCamera == null) {
                    ColorPickerActivity.this.finish();
                } else {
                    //set up camera preview
                    mCameraPreview = new CameraColorPickerPreview(ColorPickerActivity.this, mCamera);
                    mCameraPreview.setOnColorSelectedListener(ColorPickerActivity.this);
                    mCameraPreview.setOnClickListener(ColorPickerActivity.this);

                    //add camera preview
                    mPreviewContainer.addView(mCameraPreview, 0, mPreviewParams);
                }
            }
        }

        @Override
        protected void onCancelled(Camera camera) {
            super.onCancelled(camera);
            if (camera != null) {
                camera.release();
            }
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(ColorPickerActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
