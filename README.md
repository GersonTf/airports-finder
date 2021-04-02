# airports-finder
This project is a java program that loads a list of airports and is able to calculate the shortest path between an initial and final node if there is any. 

The root of the repository contains two projects with the same code, there are multiple ways to run this project. 

# Command line program
Inside the cmd folder, you can find a simple java 11 project that will print the solution after executing the main class. java 11 is required for this to work, the parameters with the arrival and departure airports can be changed in the main function.

An example of the response for DUB and SYD would be:

DUB -- LHR ( 1 )

LHR -- BKK ( 9 )

BKK -- SYD ( 11 )

time: 21

# Micronaut project
Inside the micronaut folder, there is a complete project that will create an api rest with an endpoint that will accept two query parameters:

    GET localhost:8080/route/fastest?departure=123&finalDestination=SYD

Using this project is the recommended approach as it is the one containing the tests (even though both projects execute the same code). 

Being a complex project, it will require having java and gradle installed but, this can be skipped thanks to a Dockerfile added to the project, so, to run the API you just need docker and run this two commands:

    docker build -t "awesome-airport" .
    docker run -p 8080:8080 awesome-airport

## Tests
The micronaut project uses spock for the tests and they are written with groovy (micronaut accepts both java and groovy) but the tests are quite readable. These tests can be found in the file test.groovy.com.boxever.RouteControllerSpec

# Solution design
The solution summary can be found in
## Summary
<details>
  <summary>this drawing</summary>
  
![alt text](https://i.ibb.co/Ht45SW2/summary.jpg)
</details>

Basically, the list of airports is considered to be a tree of pairs where each pair contains the departure node and the arrival node. 

## 1 Get pairs
The first step (RouteService.findAllRoutes) will get all the ordered pairs from the parent to the final destination node. As it can be seen in the drawing, the solution could be just a pair (direct flight), linear (there is only one chain) or to have multiple chains (different stopovers). The first function will return all the pairs with the duration of each flight, at this point we don't know which one is the fastest trip.

If one of the chains can't be formed (there is no valid path to the final destination) an exception will be thrown.

## 2 get chains
Once we have all the possible pairs of nodes, we have to separate them into different chains so we can calculate the fastest one (RouteService.cleanPaths). This will separate each chain in a map of routes. For example, for the airports LAX and SYD, we will obtain a map with two elements, containing this two chains:

LAX-LAS -> LAS-SYD (duration 16)

LAX-SYD (duration 13)

## 3 sort by duration
At this point, we just have all the different routes that we can take so RouteService.calculateRoutesDuration simply will find the fastest entry of the map (the shortest duration) and that will be our final solution.


# Load airports

As it wasn't especified how to load the airports, a class Route was created as a pojo that contains the departure airport, the arrival and the duration. The method Route.createRoutes() loads all the received airports.

