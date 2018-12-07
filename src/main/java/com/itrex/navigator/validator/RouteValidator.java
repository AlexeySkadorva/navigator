package com.itrex.navigator.validator;

import com.itrex.navigator.exception.CityNotExistsException;
import com.itrex.navigator.exception.ValidationException;
import com.itrex.navigator.model.City;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

public class RouteValidator implements Validator {

    public void validateRoute(City departure, City destination, int distance) {
        if(departure.equals(destination)) {
            throw new ValidationException("Departure and destination is the same city");
        }

        if(distance < 0) {
            throw new ValidationException("Negative value of distance");
        }
    }

    public void validateCities(Graph<City, DefaultWeightedEdge> citiesGraph, City departure, City destination) {
        validateCity(citiesGraph, departure);
        validateCity(citiesGraph, destination);
    }

    private void validateCity(Graph<City, DefaultWeightedEdge> citiesGraph,City city) {
        if(!citiesGraph.containsVertex(city)) {
            throw new CityNotExistsException("City " + city.getName() + " not exists in route list");
        }
    }

}
