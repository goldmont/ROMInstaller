package com.peppe130.rominstaller.core;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.peppe130.rominstaller.R;
import com.stericson.RootShell.exceptions.RootDeniedException;
import com.stericson.RootShell.execution.Command;
import com.stericson.RootTools.RootTools;
import cn.pedant.SweetAlert.SweetAlertDialog;


@SuppressLint("ValidFragment")
public class InstallPopupDialog extends DialogFragment {

    File mFile;

    public InstallPopupDialog(String mString) {
        mFile = new File(mString);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(getString(R.string.start_install_dialog_title))
                .setContentText(getString(R.string.start_install_dialog_message))
                .setConfirmText(getString(R.string.ok_button))
                .setCancelText(getString(R.string.later_button))
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        getActivity().finish();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        if (mFile.exists()) {
                            try {
                                Command install = new Command(0, "echo " + "install " + mFile.toString() + " > /cache/recovery/openrecoveryscript");
                                RootTools.getShell(true).add(install);
                            } catch (TimeoutException | RootDeniedException | IOException e) {
                                e.printStackTrace();
                            }
                        }

                        try {
                            Command reboot = new Command(0, "reboot recovery");
                            RootTools.getShell(true).add(reboot);
                        } catch (TimeoutException | RootDeniedException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                sweetAlertDialog.show();

        return sweetAlertDialog;
    }
}