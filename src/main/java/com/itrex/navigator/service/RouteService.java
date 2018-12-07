package com.itrex.navigator.service;

import com.itrex.navigator.model.City;
import com.itrex.navigator.model.Route;
import com.itrex.navigator.model.RouteSegment;

import java.util.List;

public interface RouteService {

    RouteSegment save(RouteSegment routeSegment);

    List<Route> getRoutes(City departure, City destination);

}
