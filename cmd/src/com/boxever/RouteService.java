package com.boxever;

import java.util.*;

public class RouteService {

    static final List<Route> routes = Route.createRoutes();

    //uncleanedTree will contain all the pairs calculated by the recursive function

    List<Route> uncleanedTree = new ArrayList<>();
    Boolean thereIsAnEnd = false;
    FinalRouteTree finalRouteTree = new FinalRouteTree();

    /**
     * It will get all the pairs (Route) starting with the received departure airport and ending in the destination
     *
     * @param departure        Node to begin at
     * @param finalDestination node to finish at
     * @throws Exception if one of the chains can't be calculated, there is no path from departure to the finalDestination
     */
    public void findAllRoutes(String departure, String finalDestination) throws Exception {
        for (Route route : routes) {

            //exit condition, when the finalDestination is reached
            if (route.getDepartureAirport().equals(departure) && route.getArrivalAirport().equals(finalDestination)) {
                thereIsAnEnd = true;
                uncleanedTree.add(route);
                //We keep calling this function with the next departure airport (next child node)
            } else if (route.getDepartureAirport().equals(departure)) {
                uncleanedTree.add(route);
                findAllRoutes(route.getArrivalAirport(), finalDestination);
            }
        }

        //if we finish and no ending was found, we throw an exception
        if (!thereIsAnEnd) {
            throw new Exception("One of the paths can't be calculated, tree doesn't have an end");
        }
    }

    /**
     * It will get in a map the different valid chains from the uncleaned tree
     *
     * @param uncleanedTree    with all the possible pairs of nodes
     * @param departure        departure city
     * @param finalDestination finalDestination
     * @throws Exception if the uncleanedTree is invalid (start is not departure)
     */
    public void cleanPaths(List<Route> uncleanedTree, String departure, String finalDestination) throws Exception {
        Map<String, List<Route>> resultMap = new HashMap<>();
        List<Route> temporaryResult = new ArrayList<>();

        //we check that the tree starts with the departure received
        if (!uncleanedTree.stream().findFirst().get().getDepartureAirport().equals(departure)) {
            throw new Exception("paths don't start with the departure airport");
        }

        List<Route> visitedAirports = new ArrayList<>();
        int counter = 0;
        boolean isNewNode = false;

        for (Route route : uncleanedTree) {
            //everytime we get the departure city again, we clean the visitedAirports as we are calculating a new possible path
            if (counter++ > 0 && route.getDepartureAirport().equals(departure)) {
                visitedAirports.clear();
            }

            //if the new node doesn't start with the departure city, it means that is a new node, we will fill it with its parents again
            if (isNewNode && !route.getDepartureAirport().equals(departure)) {
                temporaryResult = findParentNodes(route.getDepartureAirport(), visitedAirports);
                isNewNode = false;
            }
            temporaryResult.add(route);

            //if we reach the final destination, we will store it in the map and declare isNewNode true so the process can start again
            if (route.getArrivalAirport().equals(finalDestination)) {
                resultMap.put(UUID.randomUUID().toString(), new ArrayList<>(temporaryResult));
                temporaryResult.clear();
                isNewNode = true;
            }

            visitedAirports.add(route);
        }

        //for each element of the map, we calculate the duration of the trip and replace the finalRouteTree with the fastest solution
        resultMap.forEach((String key, List<Route> routeValue) -> calculateRoutesDuration(routeValue));
    }

    /**
     * It gets a pair and the visited nodes so we can "climb back" to the parent node, this will be necessary when a node separates into differents childs
     *
     * @param currentDeparture
     * @param visitedAirports
     * @return It returns all the parents up to the departure
     * @throws Exception is thrown if the parent node is not the received departure
     */
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

    /**
     * It receives all the routes of a trip with n stopovers and it calculates the total duration, if the total duration is
     * shorter than the one in the finalSolution, it will replace it.
     *
     * @param routes all the routes of the trip.
     */
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
