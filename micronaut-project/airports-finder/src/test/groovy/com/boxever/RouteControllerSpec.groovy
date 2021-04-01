package com.boxever

import com.boxever.model.FinalRouteTree
import com.boxever.model.Route
import io.micronaut.http.client.annotation.Client
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import spock.lang.AutoCleanup
import spock.lang.Specification
import spock.lang.Shared

import javax.inject.Inject

@MicronautTest
class RouteControllerSpec extends Specification {

    @Shared
    @Inject
    EmbeddedServer embeddedServer

    @Shared
    @AutoCleanup
    @Inject
    @Client("/")
    RxHttpClient client

    void "calculating the route does not throw any exceptions and response status is 200 OK"() {
        when:
        HttpResponse response = client.toBlocking().exchange("/route/fastest?departure=DUB&finalDestination=SYD")

        then:
        response.status == HttpStatus.OK

        and:
        noExceptionThrown()
    }

    void "returned json contains the fastest route, arrival and departure airports correspond to the ones sent"() {
        when:
        HttpResponse response = client.toBlocking().exchange("/route/fastest?departure=$departure&finalDestination=$finalDestination", FinalRouteTree)

        then:
        response.status == HttpStatus.OK
        FinalRouteTree responseBody = response.body.get()
        responseBody.duration == validatedDuration

        and: 'response routes correspond to the good route'
        List<Route> routes = responseBody.routes
        routes.size() == stopovers
        routes.first().departureAirport == departure
        routes.last().arrivalAirport == finalDestination

        where:
        departure | finalDestination | validatedDuration | stopovers
        'DUB'     | 'SYD'            | 21                | 3
        'LAS'     | 'SYD'            | 14                | 1
        'LAX'     | 'SYD'            | 13                | 1
        'BOS'     | 'SYD'            | 17                | 2
    }

    void "flight from DUB to SYD has the correct stops"() {
        when:
        HttpResponse response = client.toBlocking().exchange("/route/fastest?departure=$departure&finalDestination=$finalDestination", FinalRouteTree)

        then:
        response.status == HttpStatus.OK
        FinalRouteTree responseBody = response.body.get()
        responseBody.duration == validatedDuration

        and: 'response routes correspond to the good route DUB -LHR -BKK - SYD'
        List<Route> routes = responseBody.routes
        routes.size() == stopovers

        routes.eachWithIndex { Route route, int i ->
            switch (i) {
                case 0:
                    assert route.departureAirport == 'DUB'
                    assert route.arrivalAirport == 'LHR'
                    assert route.duration == 1
                    break
                case 1:
                    assert route.departureAirport == 'LHR'
                    assert route.arrivalAirport == 'BKK'
                    assert route.duration == 9
                    break
                case 2:
                    assert route.departureAirport == 'BKK'
                    assert route.arrivalAirport == 'SYD'
                    assert route.duration == 11
                    break
            }
        }

        where:
        departure | finalDestination | validatedDuration | stopovers
        'DUB'     | 'SYD'            | 21                | 3
    }
}
