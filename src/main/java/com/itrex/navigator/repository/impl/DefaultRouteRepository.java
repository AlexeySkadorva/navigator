package com.itrex.navigator.repository.impl;

import com.itrex.navigator.model.City;
import com.itrex.navigator.model.RouteSegment;
import com.itrex.navigator.repository.RouteRepository;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public class DefaultRouteRepository implements RouteRepository {

    private Graph<City, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);


    @Override
    public synchronized RouteSegment save(RouteSegment routeSegment) {
        City departure = routeSegment.getDeparture();
        City destination = routeSegment.getDestination();

        addCity(departure);
        addCity(destination);

        DefaultWeightedEdge edge = getExistedOrCreatedEdge(departure, destination);

        graph.setEdgeWeight(edge, routeSegment.getDistance());

        return routeSegment;
    }

    @Override
    public Graph<City, DefaultWeightedEdge> getAll() {
        return graph;
    }

    private void addCity(City city) {
        if (!graph.containsVertex(city)) {
            graph.addVertex(city);
        }
    }

    private DefaultWeightedEdge getExistedOrCreatedEdge(City departure, City destination) {
        DefaultWeightedEdge edge = graph.getEdge(departure, destination);

        if (Objects.isNull(edge)) {
            edge = graph.getEdge(destination, departure);
            if (Objects.isNull(edge)) {
                edge = graph.addEdge(departure, destination);
            }
        }

        return edge;
    }

}
