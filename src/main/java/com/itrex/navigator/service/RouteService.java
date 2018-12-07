package com.itrex.navigator.service;

import com.itrex.navigator.model.Route;
import com.itrex.navigator.model.City;

import java.util.List;

public interface RouteService {

    void save(City departure, City destination, int distance);

    List<Route> getRoutes(City departure, City destination);

}
