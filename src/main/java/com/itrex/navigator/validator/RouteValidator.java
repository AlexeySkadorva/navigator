package com.itrex.navigator.validator;

import com.itrex.navigator.exception.CityNotExistsException;
import com.itrex.navigator.exception.ValidationException;
import com.itrex.navigator.model.City;
import com.itrex.navigator.model.RouteSegment;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

public class RouteValidator implements Validator {

    private static final int MINIMAL_DISTANCE_VALUE = 0;


    public void validateRouteSegment(RouteSegment routeSegment) {
        if (routeSegment.getDeparture().equals(routeSegment.getDestination())) {
            throw new ValidationException("Departure and destination is the same city");
        }

        if (routeSegment.getDistance() <= MINIMAL_DISTANCE_VALUE) {
            throw new ValidationException("Distance must be greater than 0");
        }
    }

    public void validateCities(Graph<City, DefaultWeightedEdge> citiesGraph, City departure, City destination) {
        validateCity(citiesGraph, departure);
        validateCity(citiesGraph, destination);
    }

    private void validateCity(Graph<City, DefaultWeightedEdge> citiesGraph, City city) {
        if (!citiesGraph.containsVertex(city)) {
            throw new CityNotExistsException("City " + city.getName() + " not exists in route list");
        }
    }

}