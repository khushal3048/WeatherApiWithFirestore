package com.example.weatherapiwithfirestore;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class dashboardFragment extends Fragment {

    TextView txt_name,weather_name,temp_min,the_temp,temp_max,humidity,predictability;
    ImageView weather_img;
    FirebaseFirestore db;
    FirebaseUser user;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    Controller con;
    RecyclerView recyclerView ;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        readFirestore();
        txt_name= view.findViewById(R.id.txt_dashname);
        weather_img = view.findViewById(R.id.img_weather);
        weather_name = view.findViewById(R.id.weather_name);
        temp_min = view.findViewById(R.id.temp_min);
        the_temp = view.findViewById(R.id.the_temp);
        temp_max = view.findViewById(R.id.temp_max);
        humidity = view.findViewById(R.id.humidity);
        predictability = view.findViewById(R.id.predictability);
        recyclerView = view.findViewById(R.id.recyclerview);

        //btn_logout = view.findViewById(R.id.btn_logout);

        /*btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                con = new Controller();
                con.navigatetofragment(R.id.loginFragment,getActivity(),null);
            }
        });*/

        Getdataservice service = RetroFitInstance.getRetrofitInstance().create(Getdataservice.class);


        Call<Weather> call = service.getWeather();
        System.out.println("Call : " + call);
        call.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {

                Weather real = response.body();
                ArrayList<ConsolidatedWeather> conArray = new ArrayList<>(real.getConsolidatedWeather());

                setupData(conArray);

                System.out.println("Response: " + conArray.get(0).getWeatherStateName());

            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                System.out.println("Error : "+t.getMessage());
            }
        });

    }

    public  void readFirestore(){

        Long lastSignIn = auth.getCurrentUser().getMetadata().getLastSignInTimestamp();
        
        DocumentReference docref =db.collection("users").document(user.getUid());
        docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot =task.getResult();
                    if (documentSnapshot.exists()){
                        Log.d("snapdata",documentSnapshot.getData().toString());
                        txt_name.setText("Welcome ..."+documentSnapshot.get("name"));

                    }
                }
            }
        });

    }

    public void setupData(ArrayList<ConsolidatedWeather> arrWeather){

        setupImage(imageString(arrWeather.get(0).getWeatherStateAbbr()),weather_img);
        weather_name.setText(arrWeather.get(0).getWeatherStateName().toString());
        temp_min.setText(arrWeather.get(0).getMinTemp().toString());
        the_temp.setText(arrWeather.get(0).getTheTemp().toString());
        temp_max.setText(arrWeather.get(0).getMaxTemp().toString());
        humidity.setText("Humidity: " + arrWeather.get(0).getHumidity().toString() + "%");
        predictability.setText("Predictability: " + arrWeather.get(0).getPredictability().toString() + "%");

        initView(arrWeather);

    }

    public String imageString(String abbr){

        return "https://www.metaweather.com/static/img/weather/png/" + abbr + ".png";

    }

    public void setupImage(String url, ImageView imgV){

        Glide.with(getActivity().getApplicationContext()).asBitmap().load(url).into(imgV);

    }


    public void initView(ArrayList<ConsolidatedWeather> wearray)
    {
        wearray.remove(0);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);

        recyclerView.setLayoutManager(layoutManager);
        Recyadapter adapter = new Recyadapter(wearray,getActivity().getApplicationContext());
        recyclerView.setAdapter(adapter);
    }
    public dashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = getArguments().getParcelable("user");
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


}
