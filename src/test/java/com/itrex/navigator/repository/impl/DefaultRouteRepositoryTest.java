package com.itrex.navigator.repository.impl;

import com.itrex.navigator.model.City;
import com.itrex.navigator.model.RouteSegment;
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
        RouteSegment routeSegment = new RouteSegment(departure, destination, distance);

        RouteSegment actualRouteSegment = routeRepository.save(routeSegment);

        Graph<City, DefaultWeightedEdge> citiesGraph = routeRepository.getAll();

        Set<DefaultWeightedEdge> allEdges = citiesGraph.getAllEdges(departure, destination);

        assertEquals(allEdges.size(), 1);
        assertEquals(routeSegment, actualRouteSegment);
    }

    @Test
    public void saveDuplicateRouteTest() {
        City departure = new City("A");
        City destination = new City("B");
        int distance = 10;
        RouteSegment routeSegment = new RouteSegment(departure, destination, distance);

        RouteSegment actualRouteSegment = routeRepository.save(routeSegment);
        routeRepository.save(routeSegment);

        Graph<City, DefaultWeightedEdge> citiesGraph = routeRepository.getAll();

        Set<DefaultWeightedEdge> allEdges = citiesGraph.getAllEdges(departure, destination);

        assertEquals(allEdges.size(), 1);
        assertEquals(routeSegment, actualRouteSegment);
    }

    @Test
    public void saveInverseRouteTest() {
        City departure = new City("A");
        City destination = new City("B");
        int distance = 5;
        RouteSegment routeSegment = new RouteSegment(departure, destination, distance);
        RouteSegment inverseRouteSegment = new RouteSegment(destination, departure, distance);

        RouteSegment actualRouteSegment = routeRepository.save(routeSegment);
        routeRepository.save(inverseRouteSegment);

        Graph<City, DefaultWeightedEdge> citiesGraph = routeRepository.getAll();

        Set<DefaultWeightedEdge> allEdges = citiesGraph.getAllEdges(departure, destination);

        assertEquals(allEdges.size(), 1);
        assertEquals(routeSegment, actualRouteSegment);
    }

    @Test
    public void getAllTest() {
        Graph<City, DefaultWeightedEdge> allRoutes = routeRepository.getAll();

        assertNotNull(allRoutes);
    }

}