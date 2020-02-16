package uz.courier.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import uz.courier.R;

import java.util.Objects;

import uz.courier.util.SharedPrefs;
import uz.courier.view.LoginActivity;


public class LogoutDialog extends Dialog implements DialogInterface.OnClickListener {

    private Activity activity;

    private Button exit;
    private Button cancel;

    public LogoutDialog(Activity _activity) {
        super(_activity, R.style.fullscreen_dialog);
        this.activity = _activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logout_dialog);
        Objects.requireNonNull(getWindow()).setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        cancel = (Button) findViewById(R.id.logoutCancel);
        exit = (Button) findViewById(R.id.logoutExit);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefs sharedPrefs = new SharedPrefs(getContext());
                sharedPrefs.getSharedPreferences().edit().clear().commit();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                getContext().startActivity(intent);
                Objects.requireNonNull(getOwnerActivity()).finish();
            }
        });
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

    }
}
