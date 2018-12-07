package com.itrex.navigator.controller;

import com.itrex.navigator.controller.domain.BuildRouteRequest;
import com.itrex.navigator.controller.domain.BuildRouteResponse;
import com.itrex.navigator.controller.domain.RouteRequest;
import com.itrex.navigator.model.City;
import com.itrex.navigator.model.Route;
import com.itrex.navigator.service.RouteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/route")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @PostMapping
    public void save(@RequestBody RouteRequest routeRequest) {
        routeService.save(routeRequest.getDeparture(), routeRequest.getDestination(), routeRequest.getDistance());
    }

    @GetMapping
    public BuildRouteResponse get(BuildRouteRequest buildRouteRequest) {
        City departure = new City(buildRouteRequest.getDeparture());
        City destination = new City(buildRouteRequest.getDestination());

        List<Route> routes = routeService.getRoutes(departure, destination);

        return new BuildRouteResponse(routes);
    }

}
