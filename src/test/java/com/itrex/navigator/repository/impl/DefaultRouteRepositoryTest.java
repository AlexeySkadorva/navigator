package com.itrex.navigator.repository.impl;

import com.itrex.navigator.model.City;
import com.itrex.navigator.repository.RouteRepository;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DefaultRouteRepositoryTest {

    private RouteRepository routeRepository;

    @Before
    public void setUp() {
        routeRepository = new DefaultRouteRepository();
    }

    @Test
    public void saveTest() {
        City departure = new City("A");
        City destination = new City("B");
        int distance = 10;

        routeRepository.save(departure, destination, distance);

        Graph<City, DefaultWeightedEdge> citiesGraph = routeRepository.getAll();

        Set<DefaultWeightedEdge> allEdges = citiesGraph.getAllEdges(departure, destination);

        assertEquals(allEdges.size(), 1);
    }

    @Test
    public void saveDuplicateRouteTest() {
        City departure = new City("A");
        City destination = new City("B");
        int distance = 10;

        routeRepository.save(departure, destination, distance);
        routeRepository.save(departure, destination, distance);

        Graph<City, DefaultWeightedEdge> citiesGraph = routeRepository.getAll();

        Set<DefaultWeightedEdge> allEdges = citiesGraph.getAllEdges(departure, destination);

        assertEquals(allEdges.size(), 1);
    }

    @Test
    public void saveInverseRouteTest() {
        City departure = new City("A");
        City destination = new City("B");
        int distance = 5;

        routeRepository.save(departure, destination, distance);
        routeRepository.save(destination, departure, distance);

        Graph<City, DefaultWeightedEdge> citiesGraph = routeRepository.getAll();

        Set<DefaultWeightedEdge> allEdges = citiesGraph.getAllEdges(departure, destination);

        assertEquals(allEdges.size(), 1);
    }

    @Test
    public void getAll() {
        Graph<City, DefaultWeightedEdge> allRoutes = routeRepository.getAll();

        assertNotNull(allRoutes);
    }

}