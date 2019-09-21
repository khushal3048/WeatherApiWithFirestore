package com.example.weatherapiwithfirestore;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class registerFragment extends Fragment {

    EditText edt_rname,edt_remail,edt_rpass,edt_rcpass;
    Button btn_reg;
    private FirebaseAuth auth;
    ImageView imageView;


    public registerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edt_remail = view.findViewById(R.id.edt_remail);
        edt_rpass = view.findViewById(R.id.edt_rpass);
        edt_rname = view.findViewById(R.id.edt_rname);
        edt_rcpass = view.findViewById(R.id.edt_rcpass);
        imageView = view.findViewById(R.id.iv);

        btn_reg = view.findViewById(R.id.btn_reg);
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isCheckEmptyField()){
                    if (edt_rpass.getText().toString().length()<6){
                        edt_rpass.setError(("Invalid password! Password should be at least 6 char!"));
                        edt_rpass.requestFocus();

                    }else{
                        if (!edt_rpass.getText().toString().equals(edt_rcpass.getText().toString())){

                            edt_rcpass.setError("Password is not match!");
                            edt_rcpass.requestFocus();

                        }else {
                            String name = edt_rname.getText().toString();
                            String email =edt_remail.getText().toString();
                            String pass = edt_rpass.getText().toString();
                            createUser(name,email,pass);
                        }
                    }
                }
            }
        });
    }

    private void createUser(final String name, final String email, String pass) {

        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    FirebaseUser user = auth.getCurrentUser();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    Map<String,Object> usermap = new HashMap<>();
                    usermap.put("name",name);
                    usermap.put("Email",email);



                    db.collection("users").document(user.getUid()).set(usermap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity().getApplicationContext(),"Registration Successfully done!!!",Toast.LENGTH_SHORT).show();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Log.d("from firebase",e.getMessage());
                        }
                    });

                }else {
                    Log.d("from Register",task.getException().getMessage());
                    Toast.makeText(getActivity().getApplicationContext(),"Registration failed!!!",Toast.LENGTH_LONG).show();
                }
            }
        });
        FirebaseAuth.getInstance().signOut();
        NavController navController = Navigation.findNavController(getActivity(),R.id.host_frag);
        navController.navigate(R.id.loginFragment);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    public boolean isCheckEmptyField(){
        if (TextUtils.isEmpty(edt_rname.getText().toString())){

            edt_rname.setError("Name cannot be blank!");
            edt_rname.requestFocus();
            return true;
        }
        else if (TextUtils.isEmpty(edt_remail.getText().toString())){

            edt_remail.setError("Email cannot be blank!");
            edt_remail.requestFocus();
            return true;
        }
        else if (TextUtils.isEmpty(edt_rpass.getText().toString())){

            edt_rpass.setError("Password cannot be blank!");
            edt_rpass.requestFocus();
            return true;
        }else if (TextUtils.isEmpty(edt_rcpass.getText().toString())){

            edt_rcpass.setError("Confirm Password cannot be blank!");
            edt_rcpass.requestFocus();
            return true;
        }
        return false;
    }

    public boolean isCheckEmptyField(EditText edt_txt){
        if(TextUtils.isEmpty(edt_txt.getText().toString())){

            edt_txt.setError("This filed can not be blank!");
            edt_txt.requestFocus();
            return true;

        }

        return false;
    }

}
