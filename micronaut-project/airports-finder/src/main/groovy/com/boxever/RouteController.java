package com.boxever;

import com.boxever.model.FinalRouteTree;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import static io.micronaut.http.MediaType.APPLICATION_JSON;

@Controller("/route")
class RouteController {

    @Inject
    RouteService routeService;

    @Get(uri = "/fastest", produces = APPLICATION_JSON)
    FinalRouteTree findShortestRoute(@QueryValue @NotNull String departure, @QueryValue @NotNull String finalDestination) throws Exception {
        routeService.findShortRoute(departure, finalDestination);
        routeService.cleanPaths(routeService.solution, departure, finalDestination);
        return routeService.finalRouteTree;
    }
}