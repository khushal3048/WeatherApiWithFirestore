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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class loginFragment extends Fragment implements View.OnClickListener{

    EditText edt_email,edt_pad;
    Button btn_log;
    TextView txt_reg;
    private FirebaseAuth auth;
    FirebaseUser user;
    Controller navcontrol;
    ImageView imageView;

    public loginFragment() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edt_email = view.findViewById(R.id.edt_email);
        edt_pad = view.findViewById(R.id.edt_pass);
        btn_log = view.findViewById(R.id.btn_log);
        txt_reg = view.findViewById(R.id.txt_lrge);
        imageView = view.findViewById(R.id.iv);


        btn_log.setOnClickListener(this);
        txt_reg.setOnClickListener(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btn_log){
            if (TextUtils.isEmpty(edt_email.getText().toString())){
                edt_email.setError("Email can not be blank!");
                edt_email.requestFocus();
            }else if (TextUtils.isEmpty(edt_pad.getText().toString())){
                edt_pad.setError("Password can not be blank!");
                edt_pad.requestFocus();
            }else {
                if (edt_pad.getText().toString().length() < 6) {
                    edt_pad.setError("Password should have to be atleast 6");
                    edt_pad.requestFocus();
                } else {
                    String email = edt_email.getText().toString();
                    String pass = edt_pad.getText().toString();
                    loginUser(email,pass);
                }
            }
        }else if (id == R.id.txt_lrge){

            NavController navController = Navigation.findNavController(getActivity(),R.id.host_frag);
            navController.navigate(R.id.registerFragment);
        }
    }

    public void loginUser(String email,String password){
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    user = auth.getCurrentUser();
                    Toast.makeText(getActivity().getApplicationContext(),"Login successful",Toast.LENGTH_SHORT).show();


                    updateUI(user);
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public  void updateUI(FirebaseUser user){

        navcontrol = new Controller();
        Bundle b = new Bundle();
        b.putParcelable("user", user);

        navcontrol.navigatetofragment(R.id.dashboardFragment,getActivity(),b);
    }

    @Override
    public void onStart() {
        super.onStart();

        user =auth.getCurrentUser();
        if (user != null){
            updateUI(user);
            Toast.makeText(getActivity().getApplicationContext(),"user already login",Toast.LENGTH_SHORT).show();
        }
    }
}
