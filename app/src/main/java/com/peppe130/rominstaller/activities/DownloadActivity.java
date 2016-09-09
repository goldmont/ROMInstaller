/*

    Copyright © 2016, Giuseppe Montuoro.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

*/

package com.peppe130.rominstaller.activities;

import android.support.annotation.NonNull;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.peppe130.rominstaller.R;
import com.peppe130.rominstaller.core.Utils;
import com.peppe130.rominstaller.ControlCenter;
import com.peppe130.bouncingdialogs.BouncingDialog;
import com.peppe130.rominstaller.core.CustomFileChooser;
import mehdi.sakout.fancybuttons.FancyButton;
import com.mikepenz.iconics.IconicsDrawable;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;

//
public class DownloadActivity extends AppCompatActivity implements CustomFileChooser.FileCallback {

    RecyclerView mRecyclerView;

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Utils.InitializeActivity(this);

        setTheme(Utils.FetchTheme(this) == 0 ? R.style.AppTheme_Light : R.style.AppTheme_Dark);

        setContentView(R.layout.activity_download_layout);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.setAdapter(new Adapter());

        if (ControlCenter.AVAILABLE_DOWNLOADS_NUMBER == 0) {

            TextView mTextView = (TextView) findViewById(R.id.textView);

            mTextView.setVisibility(View.VISIBLE);

        }

    }

    @SuppressWarnings("ConstantConditions")
    public class ViewHolder extends AbstractDraggableItemViewHolder {

        FancyButton mButton;

        public ViewHolder(View itemView) {

            super(itemView);

            mButton = (FancyButton) itemView.findViewById(R.id.button);

            mButton.setBackgroundColor(ContextCompat.getColor(Utils.ACTIVITY, Utils.FetchPrimaryColor()));

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
        @SuppressWarnings("ConstantConditions")
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            final Item mItem = mItems.get(position);

            holder.mButton.setText(mItem.TEXT);

            holder.mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (Utils.isConnected()) {

                        if (Utils.isWifiAvailable()) {

                            ControlCenter.DownloadActionGetter((int) mItem.ID);

                        } else {

                            new BouncingDialog(DownloadActivity.this, BouncingDialog.WARNING_TYPE)
                                    .title(getString(R.string.no_wifi_network_dialog_title))
                                    .content(getString(R.string.no_wifi_network_dialog_message))
                                    .positiveText(getString(R.string.positive_button))
                                    .onPositive(new BouncingDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(BouncingDialog bouncingDialog) {

                                            ControlCenter.DownloadActionGetter((int) mItem.ID);

                                        }
                                    })
                                    .negativeText(getString(R.string.negative_button))
                                    .autoDismiss(true)
                                    .show();

                        }

                    } else {

                        new BouncingDialog(DownloadActivity.this, BouncingDialog.ERROR_TYPE)
                                .title(getString(R.string.no_network_connection_dialog_title))
                                .content(getString(R.string.no_network_connection_dialog_message))
                                .positiveText(getString(R.string.ok_button))
                                .show();

                    }

                }
            });

            holder.mButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    switch (motionEvent.getAction()) {

                        case MotionEvent.ACTION_DOWN:

                            holder.mButton.setBorderColor(ContextCompat.getColor(DownloadActivity.this, Utils.FetchButtonBorderColor()));

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

    @Override
    @SuppressWarnings("ResultOfMethodCallIgnored")
    protected void onResume() {

        super.onResume();

        Utils.ACTIVITY = this;

        File mROMFolder = new File(Environment.getExternalStorageDirectory().getPath() + "/" + Utils.ROM_DOWNLOAD_FOLDER);

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

                final File mFile = new File(Environment.getExternalStorageDirectory().getPath() + "/" + Utils.ROM_DOWNLOAD_FOLDER);

                if (mFile.exists() && mFile.isDirectory() && mFile.listFiles().length != 0) {

                    BouncingDialog bouncingDialog = new BouncingDialog(DownloadActivity.this, BouncingDialog.WARNING_TYPE)
                            .title(getString(R.string.clear_downloads_dialog_title))
                            .content(getString(R.string.clear_downloads_dialog_message))
                            .positiveText(getString(R.string.positive_button))
                            .negativeText(getString(R.string.what_is_inside_button))
                            .onPositive(new BouncingDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(BouncingDialog bouncingDialog1) {

                                    bouncingDialog1.dismiss();

                                    Utils.DeleteFolderRecursively(mFile.toString());

                                    Utils.ToastShort(DownloadActivity.this, getString(R.string.cleared));

                                }
                            })
                            .onNegative(new BouncingDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(BouncingDialog bouncingDialog1) {

                                    bouncingDialog1.dismiss();

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            new CustomFileChooser.Builder(DownloadActivity.this)
                                                    .initialPath(Environment.getExternalStorageDirectory().getPath() + "/" + Utils.ROM_DOWNLOAD_FOLDER)
                                                    .lockFolder(null, true, true)
                                                    .canceledOnTouchOutside(true)
                                                    .show();

                                        }
                                    }, 300);

                                }
                            });
                    bouncingDialog.setCanceledOnTouchOutside(true);
                    bouncingDialog.show();

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
