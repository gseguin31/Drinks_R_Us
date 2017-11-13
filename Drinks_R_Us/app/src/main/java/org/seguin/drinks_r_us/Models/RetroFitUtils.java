package org.seguin.drinks_r_us.Models;

import org.seguin.drinks_r_us.Server.ServiceServeur;
import org.seguin.drinks_r_us.Server.ServiceServeurMock;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Gabriel on 15/09/2017.
 */

public class RetroFitUtils {

    public static ServiceServeur get(){
        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl("http://5a5.di.college-em.info:7026/")
                //.baseUrl("http://10.0.2.2:7026/")
                //.baseUrl("https://localhost:7026/")
                //.baseUrl("https://localhost:8443/")
                .client(getClient())
                .baseUrl("https://10.0.2.2:8443/")
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

    public static OkHttpClient getClient(){
        try {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {}

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {}

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };
            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            // configure the builder to accept all SSL certificates
            builder = builder.sslSocketFactory(sslSocketFactory);
            // configure the builder to accept all hostnames includint localhost
            builder = builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            // Adds logging capability to see http exchanges on Android Monitor
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder = builder.addInterceptor(interceptor);
            return builder.build();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
