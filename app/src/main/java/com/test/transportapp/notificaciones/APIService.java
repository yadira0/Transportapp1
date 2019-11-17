package com.test.transportapp.notificaciones;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({
            "content-Type:application/json",
            "Authorization:key=AAAA-4x5KCo:APA91bGeliOF3ucG85KKIUbWftOTjlpBo_oJ0eqIXw1pwWF2w8h1pTqLQbU1yK3fgxEjiIC2sHGY2a0Bo-cwgKNBpzOf3tiXJ35j4mvKamtXerXlh5hZom7sB0_hx50MwSyMVa1ncguy"
    })

    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);

}
