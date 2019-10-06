package tutorial.root.ui.activity.mainActivity;

import java.util.List;

import tutorial.root.pojo.response.Bus;
import tutorial.root.utils.BaseView;

public interface MainActivityContractor {

    interface View extends BaseView<MainActivityPresenter> {
        void initializeUI();
        void setListeners();

        void openOrCloseBusList();
        void hideLoading();
        void showRouteCard();

        void configureRecyclerView();
        void configureBusList (List<Bus> busList);

        void changeRoute(String busCode, String route);
        void drawWayLine(List<Bus> busList);

        void addMarkerForBus(float lang, float lat);
        void removeAllMarkers();
    }

    interface Presenter {
        void checkPeriodically();

        void prepareDataForUser(List<Bus> buses);
        void calculateSpecificBusRoute();
        void switchRoute(String busCode, String route);
        void calculateCurrentSpecificBusRoute();

        void changeSelectedBus(String busCode, String busRoute);
    }

}
