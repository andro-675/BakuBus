package tutorial.root.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import tutorial.root.pojo.response.Bus;

public class Algorithims {

    public static List<Bus> sortBusesAccordingToCodes (List<Bus> allBusList)
    {
        Collections.sort(allBusList, new Comparator<Bus>() {

            @Override
            public int compare(Bus bus1, Bus bus2) {
                return extractInt(bus1.getAttributes().getDisplayRouteCode()) - extractInt(bus2.getAttributes().getDisplayRouteCode());
            }

            int extractInt(String s) {
                String num = s.replaceAll("\\D", "");
                return num.isEmpty() ? 0 : Integer.parseInt(num);
            }

        });

        return allBusList;
    }

    public static List<Bus> createBusListForUser (List<Bus> sortedAllBusList)
    {
        List<Bus> generatedBusListForUser = new ArrayList<>();
        List<String> destinationsForChecking = new ArrayList<>();

        for(Bus bus : sortedAllBusList) {

            String destination = bus.getAttributes().getRouteName();

            if (!destinationsForChecking.contains(destination)) {
                destinationsForChecking.add(destination);
                generatedBusListForUser.add(bus);
            }

        }

        return generatedBusListForUser;
    }

    public static List<Bus> filterForSpecificRoute (String busCode, String busRoute, List<Bus> sortedAllBusList)
    {
        List<Bus> filteredBusListForSpecificRoute = new ArrayList<>();

        for (Bus bus : sortedAllBusList) {
            if (
                    bus.getAttributes().getDisplayRouteCode().equals(busCode) &&
                    bus.getAttributes().getRouteName().equals(busRoute)
            ) {
                filteredBusListForSpecificRoute.add(bus);
            }
        }

        return filteredBusListForSpecificRoute;
    }

}
