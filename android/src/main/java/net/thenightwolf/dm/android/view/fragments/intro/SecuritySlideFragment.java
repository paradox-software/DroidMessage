package net.thenightwolf.dm.android.view.fragments.intro;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.github.paolorotolo.appintro.ISlidePolicy;
import net.thenightwolf.dm.android.R;
import net.thenightwolf.dm.android.view.fragments.dialog.PasswordSetDialog;

import static android.R.attr.fragment;
import static android.R.attr.setupActivity;

public class SecuritySlideFragment extends Fragment implements ISlidePolicy{


    private boolean passwordSet = false;

    private SharedPreferences sharedPreferences;

    @BindView(R.id.setPasswordButton)
    public Button setPasswordButton;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        Window window = getActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.darkGreen));

        View view = inflater.inflate(R.layout.security_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public boolean isPolicyRespected() {
        return passwordSet;
    }

    @OnClick(R.id.setPasswordButton)
    public void setPassword(){
        if(!passwordSet) {
            DialogFragment fragment = new PasswordSetDialog();
            fragment.setTargetFragment(this, 5001);
            fragment.show(getFragmentManager(), "password_dialog");
        } else {
            Toast.makeText(getActivity(), "Password already set!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUserIllegallyRequestedNextPage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Please set password")
                .setMessage("A password must be set before you can continue.")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                });
        builder.create().show();
    }

    @Override
    public void onActivityResult(int reqCode, int resCode, Intent intent) {
        if(reqCode == 5001){
            if(intent.getBooleanExtra("PASSWORD_SET", false)){
                setPasswordButton.setText("Password Set!");
                passwordSet = true;
            }
        }
    }

}
