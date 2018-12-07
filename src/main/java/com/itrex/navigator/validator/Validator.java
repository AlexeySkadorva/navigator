package com.itrex.navigator.validator;

import com.itrex.navigator.model.City;
import com.itrex.navigator.model.RouteSegment;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

public interface Validator {

    void validateRouteSegment(RouteSegment routeSegment);

    void validateCities(Graph<City, DefaultWeightedEdge> citiesGraph, City departure, City destination);

}
