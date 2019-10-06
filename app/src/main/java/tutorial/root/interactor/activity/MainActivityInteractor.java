package tutorial.root.interactor.activity;

import retrofit2.Callback;
import tutorial.root.api.ApiInitializer;
import tutorial.root.pojo.response.Response;

public class MainActivityInteractor {

    private ApiInitializer apiInitializer;

    public MainActivityInteractor() {apiInitializer = ApiInitializer.getInstance();}

    public void getAllData(Callback<Response> callback) {
        apiInitializer
                .createApiService()
                .getApiService()
                .getAllData()
                .enqueue(callback);
    }

}
