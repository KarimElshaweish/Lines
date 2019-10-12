package com.example.lines.Acticites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.chaos.view.PinView;
import com.example.lines.R;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity {

    EditText fullName,phoneNumbertxt,password;
    ScrollView signupView;
    RelativeLayout vertficationViwe,details;
    Button btnSignUp;
    SpinKitView spin_kit;
    private void __init__(){
        fullName=findViewById(R.id.fullName);
        phoneNumbertxt=findViewById(R.id.phoneNumber);
        password=findViewById(R.id.password);
        signupView=findViewById(R.id.signupView);
        vertficationViwe=findViewById(R.id.vertficationViwe);
        btnSignUp=findViewById(R.id.btnSignUp);
        spin_kit=findViewById(R.id.spin_kit);
        details=findViewById(R.id.details);
        pinViewSetup();
    }
    private void validation(){
        if(phoneNumbertxt.getText().toString().equals("")){
            phoneNumbertxt.setError("please enter your phone number");
        }else if(fullName.getText().toString().equals("")){
            fullName.setError("please enter your full name");
        }else if(password.getText().toString().equals("")){
            password.setError("please enter your password");
        }else if(password.getText().toString().toCharArray().length<4){
            password.setError("please enter password more than 4 text");
        }else{
            signupView.setVisibility(View.GONE);
            vertficationViwe.setVisibility(View.VISIBLE);
            details.setEnabled(false);
            details.setClickable(false);
            spin_kit.setVisibility(View.VISIBLE);
            startPhoneNumberVerification(phoneNumbertxt.getText().toString());
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       __init__();
       spin_kit.setVisibility(View.VISIBLE);
       FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
       details.setEnabled(false);
       if(user!=null){
           startActivity(new Intent(this,HomeMain.class));
       }else{
           spin_kit.setVisibility(View.GONE);
       }
    }

    public void finish(View view) {
        finish();
    }

    public void Signup(View view) {
        validation();
    }
    private void pinViewSetup(){
        final PinView pinView = findViewById(R.id.pinView);
        pinView.setTextColor(
                ResourcesCompat.getColor(getResources(), R.color.colorAccent,getTheme()));
        pinView.setTextColor(
                ResourcesCompat.getColorStateList(getResources(), R.color.colorPrimaryDark, getTheme()));
        pinView.setLineColor(
                ResourcesCompat.getColor(getResources(), R.color.line_colors, getTheme()));
        pinView.setLineColor(
                ResourcesCompat.getColorStateList(getResources(), R.color.line_colors, getTheme()));
        pinView.setItemCount(4);
        pinView.setItemHeight(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_size));
        pinView.setItemWidth(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_size));
        pinView.setItemRadius(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_radius));
        pinView.setItemSpacing(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_spacing));
        pinView.setLineWidth(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_line_width));
        pinView.setAnimationEnable(true);// start animation when adding text
        pinView.setCursorVisible(false);
        pinView.setCursorWidth(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_cursor_width));
        pinView.setItemBackgroundColor(Color.BLACK);
        pinView.setHideLineWhenFilled(false);

    }

    public void submit(View view) {

    }
    private Boolean mVerificationInProgress=false;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String mVerificationId;
    private void startPhoneNumberVerification(final String phoneNumber) {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d("Auth", "onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w("Auth", "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d("Auth", "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // ...
            }
        };
        // [START start_phone_auth]
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,// Phone number to verify
                30L,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);
        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;


    }
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Auth", "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            startActivity(new Intent(getBaseContext(),HomeMain.class));
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("Auth", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

}
