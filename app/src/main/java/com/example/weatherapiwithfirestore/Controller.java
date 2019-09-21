package com.example.weatherapiwithfirestore;

import android.app.Activity;
import android.os.Bundle;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class Controller {
    public void navigatetofragment(int fragId, Activity act, Bundle bundle){
        NavController navController = Navigation.findNavController(act,R.id.host_frag);
        navController.navigate(fragId,bundle);
    }
}
