package com.boxever;

import com.boxever.model.FinalRouteTree;
import com.boxever.model.Route;

import javax.inject.Singleton;
import java.util.*;

@Singleton
class RouteService {

    static final List<Route> routes = Route.createRoutes();

    List<Route> solution;
    Boolean thereIsAnEnd;
    FinalRouteTree finalRouteTree;

    public FinalRouteTree init(String departure, String finalDestination) throws Exception {
        solution = new ArrayList<>();
        thereIsAnEnd = false;
        finalRouteTree = new FinalRouteTree();
        findShortRoute(departure, finalDestination);
        cleanPaths(this.solution, departure, finalDestination);
        return finalRouteTree;
    }

    public List<Route> findShortRoute(String departure, String finalDestination) throws Exception {
        for (Route route : routes) {

            if (route.getDepartureAirport().equals(departure) && route.getArrivalAirport().equals(finalDestination)) {
                thereIsAnEnd = true;
                solution.add(route);
            } else if (route.getDepartureAirport().equals(departure)) {
                solution.add(route);
                findShortRoute(route.getArrivalAirport(), finalDestination);
            }
        }

        //todo test if this works
        if (!thereIsAnEnd) {
            throw new Exception("One of the paths can't be calculated, tree doesn't have an end");
        }

        return solution;
    }

    //todo change name
    public void cleanPaths(List<Route> solution, String departure, String finalDestination) throws Exception {

        Map<String, List<Route>> resultMap = new HashMap<>();
        List<Route> temporaryResult = new ArrayList<>();


        //todo think carefuly if we need it
        if (!solution.stream().findFirst().get().getDepartureAirport().equals(departure)) {
            throw new Exception("paths don't start with the departure airport");
        }

        List<Route> visitedAirports = new ArrayList<>();

        int counter = 0;
        boolean isNewNode = false;

        for (Route route : solution) {
            if (counter++ > 0 && route.getDepartureAirport().equals(departure)) {
                visitedAirports.clear();
            }

            if (isNewNode && !route.getDepartureAirport().equals(departure)) {
                temporaryResult = findParentNodes(route.getDepartureAirport(), visitedAirports);
                isNewNode = false;
            }
            temporaryResult.add(route);

            if (route.getArrivalAirport().equals(finalDestination)) {
                resultMap.put(UUID.randomUUID().toString(), new ArrayList<>(temporaryResult));
                temporaryResult.clear();
                isNewNode = true;
            }

            visitedAirports.add(route);
        }

        resultMap.forEach((String key, List<Route> routeValue) -> calculateRoutesDuration(routeValue));
    }

    private List<Route> findParentNodes(String currentDeparture, List<Route> visitedAirports) throws Exception {
        List<Route> nodeWithPreviousAirports = new ArrayList<>();
        for (Route route : visitedAirports) {
            nodeWithPreviousAirports.add(route);
            if (route.getArrivalAirport().equals(currentDeparture)) {
                return nodeWithPreviousAirports;
            }
        }

        throw new Exception("one of the nodes parents is not the received departure!!!");
    }

    //todo names
    private void calculateRoutesDuration(List<Route> routes) {
        Integer currentValue = 0;

        for (Route route : routes) {
            currentValue += route.getDuration();
        }

        if (this.finalRouteTree.getDuration() == null || this.finalRouteTree.getDuration() > currentValue) {
            this.finalRouteTree.setDuration(currentValue);
            this.finalRouteTree.setRoutes(routes);
        }
    }
}
