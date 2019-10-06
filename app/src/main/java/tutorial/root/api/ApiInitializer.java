package tutorial.root.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiInitializer {

    private static ApiInitializer instance = null;
    private final String BASE_URL = "https://www.bakubus.az/";

    private static Retrofit retrofit = null;

    private ApiService apiService = null;

    private ApiInitializer() {
        buildRetrofit();
    }

    private void buildRetrofit() {
        if (ApiInitializer.retrofit == null) {
            ApiInitializer.retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
    }

    public static ApiInitializer getInstance() {
        if (instance == null) {
            instance = new ApiInitializer();
        }
        return instance;
    }

    public ApiInitializer createApiService() {
        apiService = retrofit.create(ApiService.class);
        return this;
    }

    public ApiService getApiService() {
        return apiService;
    }

}
