/*

    Copyright © 2016, Giuseppe Montuoro and Aidan Follestad.

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

package com.peppe130.rominstaller.core;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import com.google.common.io.Files;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.peppe130.rominstaller.R;
import com.peppe130.rominstaller.ControlCenter;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;


public class CustomFileChooser extends DialogFragment implements MaterialDialog.ListCallback {

    private final static String DEFAULT_TAG = "[MD_FILE_SELECTOR]";

    MaterialDialog mDialog;
    private File parentFolder;
    private File[] parentContents;
    private boolean canGoUp = true;
    private FileCallback mCallback;

    public interface FileCallback {

        void onFileSelection(@NonNull CustomFileChooser dialog, @NonNull File file);

    }

    public CustomFileChooser() {

    }

    String[] getContentsArray() {

        if (parentContents == null) {

            return new String[] {"..."};

        }

        if (getLockExplorer() && parentFolder.getAbsolutePath().equals(getPathToBeLocked())) {

            canGoUp = false;

            String[] results = new String[parentContents.length];

            for (int i = 0; i < parentContents.length; i++)

                results[i] = parentContents[i].getName();

            return results;

        } else {

            String[] results = new String[parentContents.length + (canGoUp ? 1 : 0)];

            results[0] = "...";

            for (int i = 0; i < parentContents.length; i++)

                results[canGoUp ? i + 1 : i] = parentContents[i].getName();

            return results;

        }

    }

    File[] listFiles(String mimeType) {

        File[] contents = parentFolder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {

                return getShowHiddenFiles() || !file.isHidden();

            }
        });

        List<File> results = new ArrayList<>();

        if (contents != null) {

            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

            for (File fi : contents) {

                if (fi.isDirectory()) {

                    results.add(fi);

                } else {

                    if (fileIsMimeType(fi, mimeType, mimeTypeMap)) {

                        results.add(fi);

                    }

                }

            }

            Collections.sort(results, new FileSorter());

            return results.toArray(new File[results.size()]);

        }

        return null;

    }

    boolean fileIsMimeType(File file, String mimeType, MimeTypeMap mimeTypeMap) {

        if (mimeType == null || mimeType.equals("*/*")) {

            return true;

        } else {

            String filename = file.toURI().toString();

            int dotPos = filename.lastIndexOf('.');

            if (dotPos == -1) {

                return false;

            }

            String fileExtension = filename.substring(dotPos + 1);

            String fileType = mimeTypeMap.getMimeTypeFromExtension(fileExtension);

            if (fileType == null) {

                return false;

            }

            if (fileType.equals(mimeType)) {

                return true;

            }

            int mimeTypeDelimiter = mimeType.lastIndexOf('/');

            if (mimeTypeDelimiter == -1) {

                return false;

            }

            String mimeTypeMainType = mimeType.substring(0, mimeTypeDelimiter);

            String mimeTypeSubtype = mimeType.substring(mimeTypeDelimiter + 1);

            if (!mimeTypeSubtype.equals("*")) {

                return false;

            }

            int fileTypeDelimiter = fileType.lastIndexOf('/');

            if (fileTypeDelimiter == -1) {

                return false;

            }

            String fileTypeMainType = fileType.substring(0, fileTypeDelimiter);

            if (fileTypeMainType.equals(mimeTypeMainType)) {

                return true;

            }

        }

        return false;

    }

    @NonNull
    @Override
    @SuppressWarnings("ConstantConditions")
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if (ControlCenter.TRIAL_MODE && !getLockExplorer()) {

            getFragmentManager().beginTransaction()
                    .add(new ChooseSampleFile(), "choose_sample_file")
                    .commitAllowingStateLoss();

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            return new MaterialDialog.Builder(getActivity())
                    .title(R.string.md_error_label)
                    .content(R.string.md_storage_perm_error)
                    .positiveText(android.R.string.ok)
                    .build();

        }

        if (getArguments() == null || !getArguments().containsKey("builder")) {

            throw new IllegalStateException("You must create a FileChooserDialog using the Builder.");

        } else if (!getArguments().containsKey("current_path")) {

            getArguments().putString("current_path", getInitialPath());

        }

        parentFolder = new File(getArguments().getString("current_path"));

        parentContents = listFiles(getBuilder().mMimeType);

        mDialog = new MaterialDialog.Builder(getActivity())
                .title(parentFolder.getAbsolutePath())
                .items((CharSequence[]) getContentsArray())
                .itemsCallback(this)
                .positiveText(getLockExplorer() ? getString(R.string.clear_downloads_button) : getString(R.string.rom_download_button))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        if (getLockExplorer()) {

                            dialog.dismiss();

                            Utils.DeleteFolderRecursively(getPathToBeLocked());

                            Utils.ToastShort(getActivity(), getString(R.string.cleared));

                        } else {

                            if (Utils.isConnected()) {

                                if (Utils.isWifiAvailable()) {

                                    new MaterialDialog.Builder(getActivity())
                                            .content(getString(R.string.download_rom_from_file_chooser_dialog_content))
                                            .positiveText(getString(R.string.positive_button))
                                            .negativeText(getString(R.string.negative_button))
                                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                                    mDialog.dismiss();

                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {

                                                            ControlCenter.DownloadROM();

                                                        }
                                                    }, 300);

                                                }
                                            })
                                            .show();

                                } else {

                                    new MaterialDialog.Builder(getActivity())
                                            .title(getString(R.string.no_wifi_network_dialog_title))
                                            .content(getString(R.string.no_wifi_network_dialog_message))
                                            .positiveText(getString(R.string.positive_button))
                                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                                    mDialog.dismiss();

                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {

                                                            ControlCenter.DownloadROM();

                                                        }
                                                    }, 300);

                                                }
                                            })
                                            .negativeText(getString(R.string.negative_button))
                                            .show();

                                }

                            } else {

                                new MaterialDialog.Builder(getActivity())
                                        .title(getString(R.string.no_network_connection_dialog_title))
                                        .content(getString(R.string.no_network_connection_dialog_message))
                                        .positiveText(getString(R.string.ok_button))
                                        .show();

                            }

                        }

                    }
                })
                .autoDismiss(false)
                .canceledOnTouchOutside(getCanceledOnTouchOutside())
                .build();

        return mDialog;

    }

    @Override
    public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence s) {

        if (canGoUp && i == 0) {

            parentFolder = parentFolder.getParentFile();

            if (parentFolder.getAbsolutePath().equals("/storage/emulated")) {

                parentFolder = parentFolder.getParentFile();

            }

            if (!getLockExplorer()) {

                canGoUp = parentFolder.getParent() != null;

            } else if (getLockExplorer() && parentFolder.getAbsolutePath().equals(getPathToBeLocked())) {

                canGoUp = false;

            }

        } else {

            if (getLockExplorer() && parentFolder.getAbsolutePath().equals(getPathToBeLocked())) {

                parentFolder = parentContents[i];

            } else {

                parentFolder = parentContents[canGoUp ? i - 1 : i];

            }

            canGoUp = true;

            if (parentFolder.getAbsolutePath().equals("/storage/emulated")) {

                parentFolder = Environment.getExternalStorageDirectory();

            }

        }

        if (parentFolder.isFile()) {

            if (getLockExplorer()) {

                String mContent = (String.format(getString(R.string.clear_single_download_dialog_message), parentFolder.getName()));

                new MaterialDialog.Builder(getActivity())
                        .content(mContent)
                        .positiveText(getString(R.string.positive_button))
                        .negativeText(getString(R.string.negative_button))
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            @SuppressWarnings("ResultOfMethodCallIgnored")
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                File mFile = parentFolder.getAbsoluteFile();

                                mFile.delete();

                            }
                        })
                        .dismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {

                                parentFolder = parentFolder.getAbsoluteFile().getParentFile();

                                parentContents = listFiles(getBuilder().mMimeType);

                                MaterialDialog dialog = (MaterialDialog) getDialog();

                                dialog.setTitle(parentFolder.toString());

                                getArguments().putString("current_path", parentFolder.toString());

                                dialog.setItems((CharSequence[]) getContentsArray());

                            }
                        })
                        .show();

            } else if (Files.getFileExtension(parentFolder.getName()).equalsIgnoreCase("zip")) {

                if (CheckZipPath(parentFolder)) {

                    mCallback.onFileSelection(this, parentFolder);

                    dismiss();

                } else {

                    Utils.ToastShort(getActivity(), getString(R.string.not_valid_zip_file_path));

                }

            } else {

                Utils.ToastShort(getActivity(), getString(R.string.not_valid_zip_file));

            }

        } else {

            parentContents = listFiles(getBuilder().mMimeType);

            MaterialDialog dialog = (MaterialDialog) getDialog();

            dialog.setTitle(parentFolder.getAbsolutePath());

            getArguments().putString("current_path", parentFolder.getAbsolutePath());

            dialog.setItems((CharSequence[]) getContentsArray());

        }

    }

    @NonNull
    private static Boolean CheckZipPath(File file) {

        @SuppressLint("SdCardPath")
        String[] mPaths = new String[] {"/storage/emulated", "/mnt/sdcard", "/sdcard"};

        for (String mPath : mPaths) {

            if (file.getAbsolutePath().contains(mPath)) {

                return true;

            }

        }

        return false;

    }

    @Override
    @SuppressWarnings("deprecation")
    public void onAttach(Activity activity) {

        super.onAttach(activity);

        mCallback = (FileCallback) activity;

    }

    public void show(FragmentActivity context) {

        final String tag = getBuilder().mTag;

        Fragment frag = context.getSupportFragmentManager().findFragmentByTag(tag);

        if (frag != null) {

            ((DialogFragment) frag).dismiss();

            context.getSupportFragmentManager().beginTransaction()
                    .remove(frag).commit();
        }

        show(context.getSupportFragmentManager(), tag);

    }

    @SuppressWarnings("unused")
    public static class Builder implements Serializable {

        @NonNull
        final transient AppCompatActivity mContext;
        String mInitialPath;
        String mMimeType;
        String mTag;
        String mPathToBeLocked;
        Boolean mLockExplorer = false;
        Boolean mCanceledOnTouchOutside = true;
        Boolean mShowHiddenFiles = false;

        public <ActivityType extends AppCompatActivity & FileCallback> Builder(@NonNull AppCompatActivity context) {
            mContext = context;
            mInitialPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            mMimeType = null;
            mLockExplorer = false;
            mCanceledOnTouchOutside = true;
            mShowHiddenFiles = false;
        }

        @NonNull
        public Builder initialPath(@Nullable String initialPath) {

            if (initialPath == null) {

                initialPath = File.separator;

            }

            mInitialPath = initialPath;

            return this;

        }

        @NonNull
        public Builder mimeType(@Nullable String type) {

            mMimeType = type;

            return this;

        }

        @NonNull
        public Builder lockFolder(@Nullable String pathToBeLocked, Boolean isLocked, Boolean equalsToInitialPath) {

            if (equalsToInitialPath) {

                mPathToBeLocked = mInitialPath;

            } else {

                mPathToBeLocked = pathToBeLocked;

            }

            mLockExplorer = isLocked;

            return this;

        }

        @NonNull
        public Builder canceledOnTouchOutside(boolean canceledOnTouchOutside) {

            this.mCanceledOnTouchOutside = canceledOnTouchOutside;

            return this;

        }

        @NonNull
        public Builder showHiddenFiles(boolean showHiddenFiles) {

            this.mShowHiddenFiles = showHiddenFiles;

            return this;

        }

        @NonNull
        public Builder tag(@Nullable String tag) {

            if (tag == null) {

                tag = DEFAULT_TAG;

            }

            mTag = tag;

            return this;

        }

        @NonNull
        CustomFileChooser build() {

            CustomFileChooser dialog = new CustomFileChooser();

            Bundle args = new Bundle();

            args.putSerializable("builder", this);

            dialog.setArguments(args);

            return dialog;

        }

        @NonNull
        public CustomFileChooser show() {

            CustomFileChooser dialog = build();

            dialog.show(mContext);

            return dialog;

        }

    }

    @NonNull
    public String getInitialPath() {

        return getBuilder().mInitialPath;

    }

    @NonNull
    public String getPathToBeLocked() {

        return getBuilder().mPathToBeLocked;

    }

    @NonNull
    public Boolean getLockExplorer() {

        return getBuilder().mLockExplorer;

    }

    @NonNull
    public Boolean getCanceledOnTouchOutside() {

        return getBuilder().mCanceledOnTouchOutside;

    }

    @NonNull
    public Boolean getShowHiddenFiles() {

        return getBuilder().mShowHiddenFiles;

    }

    @NonNull
    @SuppressWarnings("ConstantConditions")
    private Builder getBuilder() {

        return (Builder) getArguments().getSerializable("builder");

    }

    private static class FileSorter implements Comparator<File> {
        @Override
        public int compare(File lhs, File rhs) {

            if (lhs.isDirectory() && !rhs.isDirectory()) {

                return -1;

            } else if (!lhs.isDirectory() && rhs.isDirectory()) {

                return 1;

            } else {

                return lhs.getName().compareTo(rhs.getName());

            }

        }

    }

    public static class ChooseSampleFile extends DialogFragment {

        public ChooseSampleFile() {

        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            setCancelable(false);

            String mContent = (String.format(Utils.ACTIVITY.getString(R.string.choose_sample_file), Utils.ROM_DOWNLOAD_FOLDER + "."));

            return new MaterialDialog.Builder(getActivity())
                    .content(mContent)
                    .positiveText(getString(R.string.ok_button))
                    .canceledOnTouchOutside(false)
                    .show();

        }

    }

}