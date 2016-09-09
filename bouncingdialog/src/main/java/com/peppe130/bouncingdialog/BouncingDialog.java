package com.peppe130.bouncingdialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import com.pnikosis.materialishprogress.ProgressWheel;


@SuppressWarnings("unused")
public class BouncingDialog extends Dialog implements View.OnClickListener {

    private View mDialogView;
    private AnimationSet mModalInAnim;
    private AnimationSet mModalOutAnim;
    private Animation mOverlayOutAnim;
    private Animation mErrorInAnim;
    private AnimationSet mErrorXInAnim;
    private AnimationSet mSuccessLayoutAnimSet;
    private Animation mSuccessBowAnim;
    private TextView mTitleTextView;
    private TextView mContentTextView;
    private String mTitleText;
    private String mContentText;
    private boolean mShowNegative;
    private boolean mShowContent;
    private String mNegativeText;
    private String mPositiveText;
    private int mDialogType;
    private FrameLayout mErrorFrame;
    private FrameLayout mSuccessFrame;
    private FrameLayout mProgressFrame;
    private SuccessDialog mSuccessTick;
    private ImageView mErrorX;
    private View mSuccessLeftMask;
    private View mSuccessRightMask;
    private Drawable mCustomImgDrawable;
    private ImageView mCustomImage;
    private Button mPositiveButton;
    private Button mNegativeButton;
    private ProgressControl mProgressControl;
    private FrameLayout mWarningFrame;
    private SingleButtonCallback mNegativeClickListener;
    private SingleButtonCallback mPositiveClickListener;
    private boolean mCloseFromCancel;
    private Boolean mAutodismiss;

    public static final int NORMAL_TYPE = 0;
    public static final int ERROR_TYPE = 1;
    public static final int SUCCESS_TYPE = 2;
    public static final int WARNING_TYPE = 3;
    public static final int CUSTOM_IMAGE_TYPE = 4;
    public static final int PROGRESS_TYPE = 5;

    public interface SingleButtonCallback {
        void onClick(BouncingDialog bouncingDialog);
    }

    public BouncingDialog(Context context) {
        this(context, NORMAL_TYPE);
    }

    public BouncingDialog(Context context, int dialogType) {
        super(context, R.style.alert_dialog);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        mProgressControl = new ProgressControl(context);
        mDialogType = dialogType;
        mErrorInAnim = AnimationLoader.loadAnimation(getContext(), R.anim.error_frame_in);
        mErrorXInAnim = (AnimationSet) AnimationLoader.loadAnimation(getContext(), R.anim.error_x_in);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
            List<Animation> childAnims = mErrorXInAnim.getAnimations();
            int idx = 0;
            for (;idx < childAnims.size();idx++) {
                if (childAnims.get(idx) instanceof AlphaAnimation) {
                    break;
                }
            }
            if (idx < childAnims.size()) {
                childAnims.remove(idx);
            }
        }
        mSuccessBowAnim = AnimationLoader.loadAnimation(getContext(), R.anim.success_bow_roate);
        mSuccessLayoutAnimSet = (AnimationSet) AnimationLoader.loadAnimation(getContext(), R.anim.success_mask_layout);
        mModalInAnim = (AnimationSet) AnimationLoader.loadAnimation(getContext(), R.anim.modal_in);
        mModalOutAnim = (AnimationSet) AnimationLoader.loadAnimation(getContext(), R.anim.modal_out);
        mModalOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mDialogView.setVisibility(View.GONE);
                mDialogView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mCloseFromCancel) {
                            BouncingDialog.super.cancel();
                        } else {
                            BouncingDialog.super.dismiss();
                        }
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mOverlayOutAnim = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                WindowManager.LayoutParams wlp = getWindow().getAttributes();
                wlp.alpha = 1 - interpolatedTime;
                getWindow().setAttributes(wlp);
            }
        };
        mOverlayOutAnim.setDuration(120);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_dialog);

        mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
        mTitleTextView = (TextView)findViewById(R.id.title_text);
        mContentTextView = (TextView)findViewById(R.id.content_text);
        mErrorFrame = (FrameLayout)findViewById(R.id.error_frame);
        mErrorX = (ImageView)mErrorFrame.findViewById(R.id.error_x);
        mSuccessFrame = (FrameLayout)findViewById(R.id.success_frame);
        mProgressFrame = (FrameLayout)findViewById(R.id.progress_dialog);
        mSuccessTick = (SuccessDialog)mSuccessFrame.findViewById(R.id.success_tick);
        mSuccessLeftMask = mSuccessFrame.findViewById(R.id.mask_left);
        mSuccessRightMask = mSuccessFrame.findViewById(R.id.mask_right);
        mCustomImage = (ImageView)findViewById(R.id.custom_image);
        mWarningFrame = (FrameLayout)findViewById(R.id.warning_frame);
        mPositiveButton = (Button)findViewById(R.id.confirm_button);
        mNegativeButton = (Button)findViewById(R.id.cancel_button);
        mProgressControl.setProgressWheel((ProgressWheel)findViewById(R.id.progressWheel));
        mPositiveButton.setOnClickListener(this);
        mNegativeButton.setOnClickListener(this);

        title(mTitleText);
        content(mContentText);
        positiveText(mPositiveText);
        negativeText(mNegativeText);
        changeDialogType(mDialogType, true);

    }

    private void restore () {
        mCustomImage.setVisibility(View.GONE);
        mErrorFrame.setVisibility(View.GONE);
        mSuccessFrame.setVisibility(View.GONE);
        mWarningFrame.setVisibility(View.GONE);
        mProgressFrame.setVisibility(View.GONE);
        mPositiveButton.setVisibility(View.VISIBLE);

        mPositiveButton.setBackgroundResource(R.drawable.blue_button_background);
        mErrorFrame.clearAnimation();
        mErrorX.clearAnimation();
        mSuccessTick.clearAnimation();
        mSuccessLeftMask.clearAnimation();
        mSuccessRightMask.clearAnimation();
    }

    private void playAnimation () {
        if (mDialogType == ERROR_TYPE) {
            mErrorFrame.startAnimation(mErrorInAnim);
            mErrorX.startAnimation(mErrorXInAnim);
        } else if (mDialogType == SUCCESS_TYPE) {
            mSuccessTick.startTickAnim();
            mSuccessRightMask.startAnimation(mSuccessBowAnim);
        }
    }

    private void changeDialogType(int dialogType, boolean fromCreate) {
        mDialogType = dialogType;
        // call after created views
        if (mDialogView != null) {
            if (!fromCreate) {
                restore();
            }
            switch (mDialogType) {
                case ERROR_TYPE:
                    mErrorFrame.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS_TYPE:
                    mSuccessFrame.setVisibility(View.VISIBLE);
                    mSuccessLeftMask.startAnimation(mSuccessLayoutAnimSet.getAnimations().get(0));
                    mSuccessRightMask.startAnimation(mSuccessLayoutAnimSet.getAnimations().get(1));
                    break;
                case WARNING_TYPE:
                    mPositiveButton.setBackgroundResource(R.drawable.red_button_background);
                    mWarningFrame.setVisibility(View.VISIBLE);
                    break;
                case CUSTOM_IMAGE_TYPE:
                    setCustomImage(mCustomImgDrawable);
                    break;
                case PROGRESS_TYPE:
                    mProgressFrame.setVisibility(View.VISIBLE);
                    mPositiveButton.setVisibility(View.GONE);
                    break;
            }
            if (!fromCreate) {
                playAnimation();
            }
        }
    }

    public void changeDialogType(int alertType) {
        changeDialogType(alertType, false);
    }

    public int getDialogType() {
        return mDialogType;
    }

    public BouncingDialog title(String text) {
        mTitleText = text;
        if (mTitleTextView != null && mTitleText != null) {
            mTitleTextView.setText(mTitleText);
        }
        return this;
    }

    public String getTitle() {
        return mTitleText;
    }

    public BouncingDialog setCustomImage(Drawable drawable) {
        mCustomImgDrawable = drawable;
        if (mCustomImage != null && mCustomImgDrawable != null) {
            mCustomImage.setVisibility(View.VISIBLE);
            mCustomImage.setImageDrawable(mCustomImgDrawable);
        }
        return this;
    }

    public BouncingDialog setCustomImage(int resourceId) {
        return setCustomImage(ContextCompat.getDrawable(getContext(), resourceId));
    }

    public BouncingDialog content (String text) {
        mContentText = text;
        if (mContentTextView != null && mContentText != null) {
            showContentText(true);
            mContentTextView.setText(mContentText);
        }
        return this;
    }

    public String getContent() {
        return mContentText;
    }

    public BouncingDialog showNegativeButton (boolean isShow) {
        mShowNegative = isShow;
        if (mNegativeButton != null) {
            mNegativeButton.setVisibility(mShowNegative ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    public boolean isShowNegativeButton() {
        return mShowNegative;
    }

    public BouncingDialog showContentText (boolean isShow) {
        mShowContent = isShow;
        if (mContentTextView != null) {
            mContentTextView.setVisibility(mShowContent ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    public boolean isShowContentText() {
        return mShowContent;
    }

    public BouncingDialog positiveText (String text) {
        mPositiveText = text;
        if (mPositiveButton != null && mPositiveText != null) {
            mPositiveButton.setText(mPositiveText);
        }
        return this;
    }

    public String getPositiveText() {
        return mPositiveText;
    }

    public BouncingDialog negativeText (String text) {
        mNegativeText = text;
        if (mNegativeButton != null && mNegativeText != null) {
            showNegativeButton(true);
            mNegativeButton.setText(mNegativeText);
        }
        return this;
    }

    public String getNegativeText() {
        return mNegativeText;
    }

    public BouncingDialog autoDismiss(Boolean autodismiss) {
        mAutodismiss = autodismiss;
        return this;
    }

    public BouncingDialog onNegative (SingleButtonCallback listener) {
        mNegativeClickListener = listener;
        return this;
    }

    public BouncingDialog onPositive (SingleButtonCallback listener) {
        mPositiveClickListener = listener;
        return this;
    }

    protected void onStart() {
        mDialogView.startAnimation(mModalInAnim);
        playAnimation();
    }

    @Override
    public void cancel() {
        dismissWithAnimation(true);
    }

    public void dismissWithAnimation() {
        dismissWithAnimation(false);
    }

    private void dismissWithAnimation(boolean fromCancel) {
        mCloseFromCancel = fromCancel;
        mPositiveButton.startAnimation(mOverlayOutAnim);
        mDialogView.startAnimation(mModalOutAnim);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancel_button) {
            if (mNegativeClickListener != null) {
                mNegativeClickListener.onClick(BouncingDialog.this);
                if (mAutodismiss != null && mAutodismiss) {
                    dismissWithAnimation();
                }
            } else {
                dismissWithAnimation();
            }
        } else if (v.getId() == R.id.confirm_button) {
            if (mPositiveClickListener != null) {
                mPositiveClickListener.onClick(BouncingDialog.this);
                if (mAutodismiss != null && mAutodismiss) {
                    dismissWithAnimation();
                }
            } else {
                dismissWithAnimation();
            }
        }
    }

    public ProgressControl progressControl() {
        return mProgressControl;
    }

}