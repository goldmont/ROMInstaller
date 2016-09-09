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

package com.peppe130.rominstaller.core;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.peppe130.rominstaller.R;
import com.peppe130.bouncingdialogs.BouncingDialog;
import com.stericson.RootTools.RootTools;
import com.stericson.RootShell.execution.Command;
import com.stericson.RootShell.exceptions.RootDeniedException;


public class InstallDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        BouncingDialog bouncingDialog = new BouncingDialog(getActivity(), BouncingDialog.SUCCESS_TYPE)
                .title(getString(R.string.start_install_dialog_title))
                .content(getString(R.string.start_install_dialog_message))
                .positiveText(getString(R.string.ok_button))
                .negativeText(getString(R.string.later_button))
                .onPositive(new BouncingDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(BouncingDialog bouncingDialog1) {

                        bouncingDialog1.dismiss();

                        if (Utils.GetZipFile().exists()) {

                            try {

                                String mCommand = "echo install " + Utils.GetZipFile().getAbsolutePath() + " > " + "/cache/recovery/openrecoveryscript" + " && " + "reboot recovery";

                                Command mExecCommand = new Command(0, mCommand);

                                RootTools.getShell(true).add(mExecCommand);

                            } catch (TimeoutException | RootDeniedException | IOException e) {

                                e.printStackTrace();

                            }

                        }

                    }

                })
                .onNegative(new BouncingDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(BouncingDialog bouncingDialog1) {

                        bouncingDialog1.dismiss();

                        getActivity().finish();

                    }
                });
                bouncingDialog.show();

        return bouncingDialog;

    }

}