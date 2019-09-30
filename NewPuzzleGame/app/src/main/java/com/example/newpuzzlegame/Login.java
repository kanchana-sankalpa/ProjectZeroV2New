package com.example.newpuzzlegame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

//uimport org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


@SuppressLint("Registered")
public class Login extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private GoogleSignInClient mGoogleSignInClient;

    SignInButton signInButton;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private static final int REQ_CODE = 9001;
    private static final String TAG = "GoogleActivity";

    String user_pic_url;
    String name;
    String email;
    String google_id;
    DatabaseReference myRef;
    FirebaseUser fuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        //Google sign in
        signInButton = findViewById(R.id.signin_google);
        signInButton.setOnClickListener(this);


// Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        db = FirebaseFirestore.getInstance();

// ...
        mDatabase = FirebaseDatabase.getInstance().getReference();
         myRef = mDatabase.getRef();

        mAuth = FirebaseAuth.getInstance();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkConnection()) {
                    signIn();
                }
            }
        });
    }


    @Override
    public void onClick(View v) {

    }

    public boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if ((networkInfo != null) && (networkInfo.isConnected())) {

            return true;
        } else {
            Toast.makeText(this, ""+getString(R.string.internet1), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void handleResult(GoogleSignInResult result) {
        Log.d("myz", "result :"+result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            name = account != null ? account.getDisplayName() : null;
             email = account != null ? account.getEmail() : null;
            Uri user_pic = account != null ? account.getPhotoUrl() : null;
            if(user_pic != null){
                user_pic_url = user_pic.toString();
            }
             google_id = account.getId();
            //   google_token = account.getIdToken();

            String  platform = "google";

            Log.d("myz", "user pic :"+user_pic_url);
            Log.d("myz", "name :"+name);
            Log.d("myz", "email :"+email);



            //    gSignStore();

       //     checknewuser();

       //     checkUser();

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d("myz", "currentUser :"+currentUser);

        String uname;

        SharedPreferences mSettings = this.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        uname = mSettings.getString("uname","");
        Log.d("myz", "uname :"+uname);

        if(currentUser != null){
            if(uname.equals("")) {
                Intent username = new Intent(Login.this, UserName.class);
                startActivity(username);
                finish();
            }else{
                Intent username = new Intent(Login.this, Afterlogin.class);
                username.putExtra("name", uname);
                startActivity(username);
                finish();
            }
        }
        // updateUI(currentUser);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("myz", "signInWithCredential:success");
                                mAuth.getUid();
                            fuser = mAuth.getCurrentUser();
                            Log.d("myz", "currentFirebaseUser "+ fuser.getUid());
                            //   updateUI(user);
                            writeNewUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("myz", "signInWithCredential:failure", task.getException());
                            //   Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //   updateUI(null);
                        }

                        // ...
                    }
                });
    }


    // [START signin]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQ_CODE);
    }




    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == REQ_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                assert account != null;
                firebaseAuthWithGoogle(account);

                //..
                handleResult(result);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Log.d("myz", "error : "+e);
                // ...
            }
        }
    }

    public void checkUser() {
        // [START add_ada_lovelace]
        // Create a new user with a first and last name



        // Add a new document with a generated ID

        DocumentReference docRef = db.collection("users").document(email);

        Log.d("myz", "doc    :" + docRef);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        updateData();
                        Intent username = new Intent(Login.this, UserName.class);
                        startActivity(username);
                        finish();

                    } else {
                        addData();
                        Log.d("myz", "No record    :");
                    }
                } else {
                    Log.d("myz", "Failed task");
                }
            }
        });


    }
public void addData() {

    Map<String, Object> user = new HashMap<>();
    user.put("name", name);
    user.put("email", email);
    user.put("user_pic", user_pic_url);

    db.collection("users").document(email)
            .set(user)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("myz", "Success");
                    Intent username = new Intent(Login.this, UserName.class);
                    startActivity(username);
                    finish();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error adding document", e);
                    Log.d("myz", "Failed");
                }
            });
}

        public void updateData(){
            Map<String, Object> user = new HashMap<>();
            user.put("name", name);
            user.put("email", email);
            user.put("user_pic", user_pic_url);

            db.collection("users").document(email)
                    .update(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("myz", "DocumentSnapshot successfully updated!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("myz", "Error updating document", e);
                        }
                    });
        }

    public void getData(){
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }


    @IgnoreExtraProperties
    public class User {

        public String username;
        public String email;

        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public User(String username, String email) {
            this.username = username;
            this.email = email;
        }

    }
    private void writeNewUser() {
      //  User user = new User(name, email);

        Map<String,Object> user = new HashMap<String,Object>();
        user.put("name", name);
        user.put("email", email);
        user.put("pic", user_pic_url);
        user.put("user_name", "");


        String f_id = fuser.getUid();



        myRef.child("users").child(f_id).updateChildren(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("myz", "Success");


                        SharedPreferences sharedPref = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("f_id", f_id);
                        editor.apply();

                        Intent username = new Intent(Login.this, UserName.class);
                        startActivity(username);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("myz", "Failed");
                    }
                });



    }

   public void checknewuser(){
       DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(google_id);
       databaseReference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               if(dataSnapshot.exists()) {
                   Log.d("myz", "User already have");
                   writeNewUser();
               }else{
                   Log.d("myz", "User not exist");
                   writeNewUser();
               }
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {
               Log.d("myz", "Failed");
           }
       }
           );

}
}

