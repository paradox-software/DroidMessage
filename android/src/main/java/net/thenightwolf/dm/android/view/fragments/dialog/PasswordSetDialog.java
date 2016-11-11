package net.thenightwolf.dm.android.view.fragments.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import net.thenightwolf.dm.android.DMApplication;
import net.thenightwolf.dm.android.R;
import org.jasypt.util.password.StrongPasswordEncryptor;

import static net.thenightwolf.dm.android.R.string.set;

public class PasswordSetDialog extends DialogFragment {

    public void sendResult(boolean result){
        Intent intent = new Intent();
        intent.putExtra("PASSWORD_SET", result);
        getTargetFragment().onActivityResult(getTargetRequestCode(), 5001, intent);
    }

    private SharedPreferences preferences;

    @BindView(R.id.set_password)
    public EditText setPassword;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        preferences = DMApplication.getAppContext().getSharedPreferences("password", Context.MODE_PRIVATE);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.password_set_dialog, null);

        ButterKnife.bind(this, view);

        builder.setView(view)
                .setPositiveButton(set, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!setPassword.getText().toString().equals("")){
                            preferences.edit().putString(getString(R.string.passwordID), setPassword.getText().toString()).apply();
                            sendResult(true);
                        } else {
                            Toast.makeText(getActivity(), "Must enter password", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        sendResult(false);
                    }
                });
        return builder.create();
    }

}
