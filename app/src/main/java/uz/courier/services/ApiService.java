package uz.courier.services;

import okhttp3.RequestBody;
import retrofit2.http.Query;
import uz.courier.api.response.DataResponse;
import uz.courier.models.Confirmation;
import uz.courier.models.Document;
import uz.courier.api.response.models.DocumentType;
import uz.courier.models.Package;
import uz.courier.models.Transport;
import uz.courier.models.TransportType;
import uz.courier.models.Country;
import uz.courier.models.Region;
import uz.courier.models.User;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

    @FormUrlEncoded
    @POST("user/login")
    Call<DataResponse<User>> loginUser(@Field("login") String first, @Field("password") String last);

    @FormUrlEncoded
    @POST("user/register")
    Call<DataResponse<Confirmation>> createUser(
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("password") String password,
            @Field("password_confirm") String passwordConfirm,
            @Field("first_name") String firstName,
            @Field("last_name") String lastName,
            @Field("country_id") Integer country_id,
            @Field("region_id") Integer region_id
    );

    @GET("user/get-me")
    Call<DataResponse<User>> getMe();

    @FormUrlEncoded
    @POST("user/confirm-email")
    Call<DataResponse<User>> confirmUser(@Field("email") String email, @Field("code") String code);

    @FormUrlEncoded
    @POST("user/update")
    Call<DataResponse<User>> updateUser(
            @Field("first_name") String firstname,
            @Field("last_name") String lastname,
            @Field("email") String email,
            @Field("country_id") Integer counryId,
            @Field("region_id") Integer regionId
    );

    @Multipart
    @POST("user/upload-image")
    Call<DataResponse<User>> uploadImage(@Part MultipartBody.Part image);

    @GET("user/transports")
    Call<DataResponse<ArrayList<Transport>>> getTransports();

    @GET("user/documents")
    Call<DataResponse<ArrayList<Document>>> getDocuments();

    @GET("location/country")
    Call<DataResponse<ArrayList<Country>>> getCountries(@Query("per-page") Integer perPage);

    @GET("location/region")
    Call<DataResponse<ArrayList<Region>>> getRegions(@Query("filter[country_id]") Integer country_id, @Query("per-page") Integer perPage);

    @Multipart
    @POST("transport")
    Call<DataResponse<Transport>> createTransport(
        @Part("transport_type_id") int typeId,
        @Part("transport_mark_id") int markId,
        @Part("name") RequestBody name,
        @Part("number") RequestBody number,
        @Part("weight_min") RequestBody weightMin,
        @Part("weight_max") RequestBody weightMax,
        @Part MultipartBody.Part image
    );

    @FormUrlEncoded
    @POST("transport/{id}")
    Call<DataResponse<Transport>> updateTransport(
        @Path("id") int id,
        @Field("transport_type_id") int typeId,
        @Field("transport_mark_id") int markId,
        @Field("name") String name,
        @Field("number") String number,
        @Field("weight_min") int weightMin,
        @Field("weight_max") int weightMax,
        MultipartBody.Part image
    );

    @GET("transport/{id}")
    Call<DataResponse<Transport>> getTransport(@Path("id") int id);

    @GET("transport/types")
    Call<DataResponse<ArrayList<TransportType>>> getTransportTypes();

//    @GET("transport/marks")
//    Call<DataResponse<ArrayList<TransportMark>>> getTransportMarks();

    @DELETE("transport/{id}")
    Call<DataResponse<Transport>> deleteTransport(@Path("id") int id);

    @Multipart
    @POST("document")
    Call<DataResponse<Document>> createDocument(
            @Part("name") String name,
            @Part("type_id") int typeId,
            @Part MultipartBody.Part file
    );

    @GET("document/{id}")
    Call<DataResponse<Document>> getDocument(@Path("id") int id);

    @DELETE("document/{id}")
    Call<DataResponse<Document>> deleteDocument(@Path("id") int id);

    @DELETE("document/types")
    Call<DataResponse<DocumentType>> getDocumentTypes();

    @FormUrlEncoded
    @POST("package")
    Call<DataResponse<Package>> createPkg(
            @Field("from") String from,
            @Field("to") String to,
            @Field("transport_type_id") int transportTypeId,
            @Field("distance") int distance,
            @Field("payment_type") int paymentType,
            @Field("start_lat") float startLat,
            @Field("start_lon") float startLon,
            @Field("end_lat") float endLat,
            @Field("end_lon") float endLon,
            @Field("start_date") int startDate,
            @Field("description") String description ,
            @Field("weight") int weight
    );

    @FormUrlEncoded
    @GET("package/{id}")
    Call<DataResponse<Package>> getPkg(
            @Path("id") int id
    );

    @DELETE("package/{id}")
    Call<DataResponse<Package>> deletePkg(
            @Path("id") int id
    );


}
