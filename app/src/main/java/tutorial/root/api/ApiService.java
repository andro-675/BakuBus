package tutorial.root.api;

import retrofit2.Call;
import retrofit2.http.GET;
import tutorial.root.pojo.response.Response;

public interface ApiService {

    @GET("az/ajax/apiNew1")
    Call<Response> getAllData();

}
