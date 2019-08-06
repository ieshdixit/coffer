package elope.crazyones.com.coffer.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.util.SharedPreferencesUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import elope.crazyones.com.coffer.R;
import elope.crazyones.com.coffer.model.User;
import elope.crazyones.com.coffer.serverapi.ApiClient;
import elope.crazyones.com.coffer.serverapi.interfaces.UserInfoAPI;
import elope.crazyones.com.coffer.serverapi.response.BaseResponse;
import elope.crazyones.com.coffer.serverapi.response.CreateNewUserResponse;
import elope.crazyones.com.coffer.util.SharedPreferenceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends MainActivity {

    private static final String TAG = "SignUpActivity";
    private static final int RC_SIGN_IN = 9001;

    @BindView(R.id.bt_google_signin)
    Button btSignUpGoogle;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("915031800101-9epqpdglkdsoahaidddcblqtr0ujghcs.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Initialize Firebase Auth
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        temporaryIntent();
        loginUserWithElope(currentUser);
    }

    //Google Sign-In Authentication
    @OnClick(R.id.bt_google_signin)
    public void signInWithGoogleClick() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "", e);
                Toast.makeText(this, "Google sign-in failed", Toast.LENGTH_SHORT).show();
                // [START_EXCLUDE]
                loginUserWithElope(null);
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        showProgressDialog("Verifying credentials...");
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            loginUserWithElope(user);
                            temporaryIntent();
                        } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                           // Snackbar.make(findViewById(R.id.bt_facebook_login), "An account already exists with the same email address using Facebook. Try login with Facebook.", Snackbar.LENGTH_LONG).show();
                            loginUserWithElope(null);
                        }else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //Snackbar.make(findViewById(R.id.bt_facebook_login), "Authentication Failed. "+task.getException(), Snackbar.LENGTH_LONG).show();
                            loginUserWithElope(null);
                        }
                        hideProgressDialog();
                    }
                });
    }

    private void loginUserWithElope(FirebaseUser firebaseUser){
        if(firebaseUser == null)return;
        showProgressDialog("Signing in to Elope...");
        ApiClient.getClientWithoutAuth(this).create(UserInfoAPI.class).createNewUser(new User(firebaseUser))
                .enqueue(new Callback<BaseResponse<CreateNewUserResponse>>() {
                    @Override
                    public void onResponse(Call<BaseResponse<CreateNewUserResponse>> call, Response<BaseResponse<CreateNewUserResponse>> response) {
                        hideProgressDialog();
                        if (response.isSuccessful()){
                            SharedPreferenceUtils.setUserEmail(((CreateNewUserResponse)response.body().getData()).getEmail());
                            SharedPreferenceUtils.setUserId(((CreateNewUserResponse)response.body().getData()).get_id());
                            SharedPreferenceUtils.setUserToken(((CreateNewUserResponse)response.body().getData()).getToken());
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
//                            Snackbar.make(findViewById(R.id.bt_facebook_login),
//                                    "Error while signing in to elope server", Snackbar.LENGTH_LONG).show();
                            Log.e(TAG,response.errorBody().toString());
                        }
                    }
                    @Override
                    public void onFailure(Call<BaseResponse<CreateNewUserResponse>> call, Throwable t) {
                        hideProgressDialog();
//                        Snackbar.make(findViewById(R.id.bt_facebook_login),
//                                "Something went wrong! Try again.", Snackbar.LENGTH_LONG).show();
                        Log.e(TAG, "Couldn't get any response from Elope server. "+t.getMessage().toString());
                    }
                });

    }

    private void temporaryIntent(){
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
    }

}
