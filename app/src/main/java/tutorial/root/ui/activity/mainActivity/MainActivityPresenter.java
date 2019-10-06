package tutorial.root.ui.activity.mainActivity;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import tutorial.root.interactor.activity.MainActivityInteractor;
import tutorial.root.pojo.response.Bus;
import tutorial.root.pojo.response.Response;
import tutorial.root.utils.Algorithims;

class MainActivityPresenter implements MainActivityContractor.Presenter {

    private final String TAG = MainActivityPresenter.class.getSimpleName();

    private MainActivityContractor.View view;
    private MainActivityInteractor interactor;

    private List<Bus> allRawBusList;
    private List<Bus> allSortedBusList;

    private List<Bus> sortedBusListForUser;
    private List<Bus> filteredBusListForSpecificRoute;

    private String selectedBusCode;
    private String selectedBusRoute;

    MainActivityPresenter(MainActivityContractor.View view) {
        this.view = view;
        this.view.setPresenter(this);
        interactor = new MainActivityInteractor();
        checkPeriodically();
    }

    //For keeping the app up to date
    @Override
    public void checkPeriodically() {
        Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                interactor.getAllData(new AllDataCallback());
                handler.postDelayed(this, 15000);
            }
        };

        handler.post(runnable);
    }

    //the main process after receiving data from API
    @Override
    public void prepareDataForUser(List<Bus> allRawBusList) {
        allSortedBusList = Algorithims.sortBusesAccordingToCodes(allRawBusList);
        sortedBusListForUser = Algorithims.createBusListForUser(allSortedBusList);

        if (selectedBusCode != null && selectedBusRoute != null)
        {
            filteredBusListForSpecificRoute = Algorithims.filterForSpecificRoute(
                    selectedBusCode,
                    selectedBusRoute,
                    allSortedBusList
            );
        }
    }

    //updating selected bus parameters
    @Override
    public void changeSelectedBus(String busCode, String busRoute) {
        this.selectedBusCode = busCode;
        this.selectedBusRoute = busRoute;
    }

    //for drawing polyline according to coordinates of the bus selected
    @Override
    public void calculateSpecificBusRoute() {

        if (filteredBusListForSpecificRoute != null) {
            view.removeAllMarkers();
            for (Bus bus : filteredBusListForSpecificRoute) {
                view.addMarkerForBus(
                        Float.parseFloat(bus.getAttributes().getLongitude().replace(",", ".")),
                        Float.parseFloat(bus.getAttributes().getLatitude().replace(",", "."))
                );
            }
            view.drawWayLine(filteredBusListForSpecificRoute);
        }
    }

    //for drawing polyline according to current bus list data
    @Override
    public void calculateCurrentSpecificBusRoute() {
        filteredBusListForSpecificRoute = Algorithims.filterForSpecificRoute(selectedBusCode, selectedBusRoute, allSortedBusList);
        calculateSpecificBusRoute();
    }

    //switching route
    @Override
    public void switchRoute(String busCode, String route) {

        for (int i = 0; i < sortedBusListForUser.size(); i++) {

            Bus bus = sortedBusListForUser.get(i);

            if (bus.getAttributes().getDisplayRouteCode().equals(busCode) && !bus.getAttributes().getRouteName().equals(route)) {
                view.changeRoute(busCode, bus.getAttributes().getRouteName());
            }

        }

    }

    class AllDataCallback implements Callback<Response> {

        @Override
        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {

            Log.e(TAG, "onResponse" + " " + response.code());

            if (response.isSuccessful() && response.code() == 200) {

                if (response.body() != null && response.body().getBusList().size() != 0) {
                    allRawBusList = response.body().getBusList();
                    new AsyncBusListUpdating().execute(allRawBusList);
                }

            }
        }

        @Override
        public void onFailure(Call<Response> call, Throwable t) {
            Log.e(TAG, "onFailure:" + " " + t.getMessage());
        }

    }

    class AsyncBusListUpdating extends AsyncTask<List<Bus>, Void, Void> {

        @Override
        protected Void doInBackground(List<Bus>... buses) {
            prepareDataForUser(buses[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            view.configureBusList(sortedBusListForUser);
            calculateSpecificBusRoute();
        }
    }

}
