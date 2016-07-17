package com.peppe130.rominstaller.activities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import mehdi.sakout.fancybuttons.FancyButton;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;
import com.mikepenz.iconics.IconicsDrawable;
import com.peppe130.rominstaller.ControlCenter;
import com.peppe130.rominstaller.R;
import com.peppe130.rominstaller.core.Utils;
import com.peppe130.rominstaller.core.CustomFileChooser;


@SuppressWarnings("ResultOfMethodCallIgnored, ConstantConditions")
public class DownloadActivity extends AppCompatActivity implements CustomFileChooser.FileCallback {

    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_layout);

        Utils.ACTIVITY = this;

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new Adapter());

    }

    public class ViewHolder extends AbstractDraggableItemViewHolder {

        FancyButton mButton;

        public ViewHolder(View itemView) {
            super(itemView);
            mButton = (FancyButton) itemView.findViewById(R.id.button);
            mButton.setBackgroundColor(Utils.FetchPrimaryColor());
        }

    }

    public class Item {
        public final long ID;
        public final String TEXT;

        public Item(long id, String text) {
            this.ID = id;
            this.TEXT = text;
        }
    }

    public class Adapter extends RecyclerView.Adapter<ViewHolder> {

        List<Item> mItems;

        public Adapter() {
            setHasStableIds(true);

            mItems = new ArrayList<>();
            for (int mInt = 0; mInt < ControlCenter.AVAILABLE_DOWNLOADS_NUMBER; mInt++) {
                mItems.add(new Item(mInt, ControlCenter.DownloadNameGetter(mInt)));
            }

        }

        @Override
        public long getItemId(int position) {
            return mItems.get(position).ID;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_layout, parent, false);
            return new ViewHolder(mView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final Item mItem = mItems.get(position);
            holder.mButton.setText(mItem.TEXT);
            holder.mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ControlCenter.DownloadActionGetter((int) mItem.ID);
                }
            });
            holder.mButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            holder.mButton.setBorderColor(ContextCompat.getColor(DownloadActivity.this, ButtonBorderColorChooser()));
                            break;
                        case MotionEvent.ACTION_UP:
                            holder.mButton.setBorderColor(0);
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            holder.mButton.setBorderColor(0);
                            break;
                    }
                    return false;
                }
            });
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

    }

    @Nullable
    public Integer ButtonBorderColorChooser() {

        Integer mTheme = null;

        try {
            mTheme = Utils.ACTIVITY.getPackageManager().getPackageInfo(Utils.ACTIVITY.getPackageName(), 0).applicationInfo.theme;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        switch (mTheme) {
            case R.style.AppTheme_Light:
                return R.color.colorPrimaryDark_Theme_Light;
            case R.style.AppTheme_Dark:
                return R.color.colorAccent_Theme_Dark;
            default:
                return null;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        Utils.ACTIVITY = this;

        File mROMFolder = new File(Environment.getExternalStorageDirectory().getPath() + "/" + getString(R.string.rom_folder));

        if(!mROMFolder.exists()) {
            mROMFolder.mkdirs();
        }

    }

    @Override
    public void onBackPressed() {
        if (!Utils.SHOULD_LOCK_UI) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        menu.findItem(R.id.clear_downloads).setEnabled(!Utils.SHOULD_LOCK_UI);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_download_menu, menu);
        IconicsDrawable mClearDownloadsIcon = new IconicsDrawable(DownloadActivity.this)
                .icon(ControlCenter.CLEAR_DOWNLOADS_ICON)
                .actionBar()
                .color(Color.WHITE)
                .sizeDp(30);
        MenuItem mClearDownloads = menu.findItem(R.id.clear_downloads);
        mClearDownloads.setIcon(mClearDownloadsIcon);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear_downloads:
                final File mFile = new File(Environment.getExternalStorageDirectory().getPath() + "/" + getString(R.string.rom_folder));
                if (mFile.exists() && mFile.isDirectory() && mFile.listFiles().length != 0) {
                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(DownloadActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(getString(R.string.clear_downloads_dialog_title))
                            .setContentText(getString(R.string.clear_downloads_dialog_message))
                            .setConfirmText(getString(R.string.positive_button))
                            .setCancelText(getString(R.string.what_is_inside_button))
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();
                                    Utils.DeleteFolderRecursively(mFile.toString());
                                    Utils.ToastShort(DownloadActivity.this, getString(R.string.cleared));
                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            new CustomFileChooser.Builder(DownloadActivity.this)
                                                    .initialPath(Environment.getExternalStorageDirectory().getPath() + "/" + getString(R.string.rom_folder))
                                                    .lockFolder(null, true, true)
                                                    .canceledOnTouchOutside(true)
                                                    .show();
                                        }
                                    }, 300);
                                }
                            });
                    sweetAlertDialog.setCanceledOnTouchOutside(true);
                    sweetAlertDialog.show();
                } else {
                    Utils.ToastLong(DownloadActivity.this, getString(R.string.clear_downloads_folder_is_empty));
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onFileSelection(@NonNull CustomFileChooser dialog, @NonNull File file) {
        // Do nothing
    }

}