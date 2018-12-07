package com.itrex.navigator.repository;

import com.itrex.navigator.model.City;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

public interface RouteRepository {

    void save(City departure, City destination, int distance);

    Graph<City, DefaultWeightedEdge> getAll();

}
