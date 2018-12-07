package com.itrex.navigator.controller;

import com.itrex.navigator.domain.RouteRequest;
import com.itrex.navigator.domain.RouteResponse;
import com.itrex.navigator.domain.RouteSegmentRequest;
import com.itrex.navigator.domain.RouteSegmentResponse;
import com.itrex.navigator.model.City;
import com.itrex.navigator.model.Route;
import com.itrex.navigator.model.RouteSegment;
import com.itrex.navigator.service.RouteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/route")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @PostMapping
    public RouteSegmentResponse save(@RequestBody RouteSegmentRequest routeSegemntRequest) {
        RouteSegment routeSegment = new RouteSegment(routeSegemntRequest.getDeparture(),
            routeSegemntRequest.getDestination(), routeSegemntRequest.getDistance());

        RouteSegment savedRouteSegment = routeService.save(routeSegment);

        return new RouteSegmentResponse(savedRouteSegment);
    }

    @GetMapping
    public RouteResponse getRoutes(RouteRequest routeRequest) {
        City departure = new City(routeRequest.getDeparture());
        City destination = new City(routeRequest.getDestination());

        List<Route> routes = routeService.getRoutes(departure, destination);

        return new RouteResponse(routes);
    }

}
