package kirill_orloff.tdtest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Kirill Orloff on 25.07.17.
 */

interface UserGetInterface {
    @GET("/users.json")
    Call<List<UserModel>> getData();
}