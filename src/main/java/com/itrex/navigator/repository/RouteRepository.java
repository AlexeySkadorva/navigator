package com.itrex.navigator.repository;

import com.itrex.navigator.model.City;
import com.itrex.navigator.model.RouteSegment;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

public interface RouteRepository {

    RouteSegment save(RouteSegment routeSegment);

    Graph<City, DefaultWeightedEdge> getAll();

}
