package org.seguin.drinks_r_us.Models;

import org.seguin.drinks_r_us.Server.ServiceServeur;
import org.seguin.drinks_r_us.Server.ServiceServeurMock;

import java.util.concurrent.TimeUnit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;

/**
 * Created by Gabriel on 15/09/2017.
 */

public class RetroFitUtils {

    public static ServiceServeur get(){
        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl("http://5a5.di.college-em.info:7026/")
                .baseUrl("http://10.0.2.2:7026/")
                //.baseUrl("http://localhost:7026/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ServiceServeur service = retrofit.create(ServiceServeur.class);
        return service;
    }

    public static ServiceServeur getMock(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.deguet.org/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NetworkBehavior networkBehavior = NetworkBehavior.create();
        networkBehavior.setDelay(1000, TimeUnit.MILLISECONDS);
        networkBehavior.setVariancePercent(90);

        MockRetrofit mock = new MockRetrofit.Builder(retrofit)
                .networkBehavior(networkBehavior)
                .build();
        BehaviorDelegate<ServiceServeur> delegate =
                mock.create(ServiceServeur.class);
        ServiceServeur service = retrofit.create(ServiceServeur.class);

        return new ServiceServeurMock(delegate);
    }
}
