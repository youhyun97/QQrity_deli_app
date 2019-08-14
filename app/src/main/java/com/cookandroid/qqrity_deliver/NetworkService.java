package com.cookandroid.qqrity_deliver;
import org.json.JSONObject;

import java.util.List;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface NetworkService {
    @FormUrlEncoded
    @POST("/qq_api/deliveryman/")
    Call<Deliveryman> register_deliveryman(@Field("delman_id") String delman_id,
                                           @Field("delman_pwd") String delman_pwd,
                                           @Field("delman_name") String delman_name,
                                           @Field("delman_company") String delman_company,
                                           @Field("delman_device") String delman_device,
                                           @Field("delman_phone") String delman_phone,
                                           @Field("delman_public") String delman_public);

    @PATCH("/qq_api/deliveryman/{pk}/")
    Call<Deliveryman> patch_deliveryman(@Path("pk") int pk, @Body Deliveryman deliveryman);

    @DELETE("/qq_api/deliveryman/{pk}/")
    Call<Deliveryman> delete_deliveryman(@Path("pk") int pk);

    @GET("/qq_api/deliveryman/")
    Call<JSONObject> get_deliveryman();

    @GET("/qq_api/deliveryman/{pk}/")
    Call<Deliveryman> get_pk_deliveryman(@Path("pk") String pk);
}
