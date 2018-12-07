package com.itrex.navigator.validator;

import com.itrex.navigator.model.City;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

public interface Validator {

    void validateRoute(City departure, City destination, int distance);

    void validateCities(Graph<City, DefaultWeightedEdge> citiesGraph, City departure, City destination);

}
