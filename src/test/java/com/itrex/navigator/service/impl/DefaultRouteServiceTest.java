package com.itrex.navigator.service.impl;

import com.itrex.navigator.exception.CityNotExistsException;
import com.itrex.navigator.exception.RouteNotExistsException;
import com.itrex.navigator.exception.ValidationException;
import com.itrex.navigator.model.City;
import com.itrex.navigator.model.Route;
import com.itrex.navigator.repository.RouteRepository;
import com.itrex.navigator.service.RouteService;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@RunWith(MockitoJUnitRunner.class)
public class DefaultRouteServiceTest {

    private RouteService routeService;

    @Mock private RouteRepository routeRepository;

    @Before
    public void setUp() {
        routeService = new DefaultRouteService(routeRepository);
    }

    @Test
    public void saveTest() {
        City departure = new City("A");
        City destination = new City("B");
        int distance = 10;

        doNothing().when(routeRepository).save(departure, destination, distance);

        routeService.save(departure, destination, distance);
    }

    @Test(expected = ValidationException.class)
    public void saveWithSameCitiesTest() {
        City departure = new City("A");
        City destination = new City("A");
        int distance = 10;

        routeService.save(departure, destination, distance);
    }

    @Test(expected = ValidationException.class)
    public void saveWithNegativeDistanceTest() {
        City departure = new City("A");
        City destination = new City("B");
        int distance = -10;

        routeService.save(departure, destination, distance);
    }

    @Test
    public void getRoutesBetweenTwoCitiesTest() {
        SimpleDirectedWeightedGraph<City, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        City departure = new City("A");
        City destination = new City("B");
        int distance = 10;

        addRoute(graph, departure, destination, distance);

        List<Route> expectedRoutes = Collections.singletonList(new Route(Arrays.asList(departure, destination), distance));

        given(routeRepository.getAll()).willReturn(graph);

        List<Route> actualRoutes = routeService.getRoutes(departure, destination);

        assertEquals(expectedRoutes, actualRoutes);
    }

    @Test
    public void getRoutesBetweenTwoCitiesInInverseOrderTest() {
        SimpleDirectedWeightedGraph<City, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        City departure = new City("A");
        City destination = new City("B");
        int distance = 5;

        addRoute(graph, destination, departure, distance);

        List<Route> expectedRoutes = Collections.singletonList(new Route(Arrays.asList(departure, destination), distance));

        given(routeRepository.getAll()).willReturn(graph);

        List<Route> actualRoutes = routeService.getRoutes(departure, destination);

        assertEquals(expectedRoutes, actualRoutes);
    }

    @Test
    public void getRoutesBetweenNotConnectedCitiesTest() {
        SimpleDirectedWeightedGraph<City, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        City departure = new City("A");
        City cityB = new City("B");
        City cityC = new City("C");
        City destination = new City("D");

        int distance = 10;

        addRoute(graph, departure, cityB, distance);
        addRoute(graph, cityB, cityC, distance);
        addRoute(graph, cityC, destination, distance);

        List<Route> expectedRoutes = Collections.singletonList(new Route(Arrays.asList(departure, cityB, cityC, destination), distance * 3));

        given(routeRepository.getAll()).willReturn(graph);

        List<Route> actualRoutes = routeService.getRoutes(departure, destination);

        assertEquals(expectedRoutes, actualRoutes);
    }

    @Test
    public void getRoutesWithTwoPathsTest() {
        SimpleDirectedWeightedGraph<City, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        City departure = new City("A");
        City cityB = new City("B");
        City cityC = new City("C");
        City destination = new City("D");

        int distance = 10;

        addRoute(graph, departure, cityB, distance);
        addRoute(graph, cityB, destination, distance);
        addRoute(graph, departure, cityC, distance);
        addRoute(graph, cityC, destination, distance);

        List<Route> expectedRoutes = new ArrayList<>();
        expectedRoutes.add(new Route(Arrays.asList(departure, cityB, destination), distance * 2));
        expectedRoutes.add(new Route(Arrays.asList(departure, cityC, destination), distance * 2));

        given(routeRepository.getAll()).willReturn(graph);

        List<Route> actualRoutes = routeService.getRoutes(departure, destination);

        assertEquals(expectedRoutes, actualRoutes);
    }

    @Test(expected = RouteNotExistsException.class)
    public void getRoutesWithNotExistsRouteTest() {
        SimpleDirectedWeightedGraph<City, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        City departure = new City("A");
        City destination = new City("D");
        graph.addVertex(departure);
        graph.addVertex(destination);

        given(routeRepository.getAll()).willReturn(graph);

        routeService.getRoutes(departure, destination);
    }

    @Test(expected = CityNotExistsException.class)
    public void getRoutesWithNotExistsDepartureCityTest() {
        SimpleDirectedWeightedGraph<City, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        City departure = new City("A");
        City destination = new City("D");
        graph.addVertex(destination);

        given(routeRepository.getAll()).willReturn(graph);

        routeService.getRoutes(departure, destination);
    }

    @Test(expected = CityNotExistsException.class)
    public void getRoutesWithNotExistsDestinationCityTest() {
        SimpleDirectedWeightedGraph<City, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        City departure = new City("A");
        City destination = new City("B");
        graph.addVertex(departure);

        given(routeRepository.getAll()).willReturn(graph);

        routeService.getRoutes(departure, destination);
    }

    private void addRoute(SimpleDirectedWeightedGraph<City, DefaultWeightedEdge> graph, City departure,
                          City destination, int distance) {
        if(!graph.containsVertex(departure)) {
            graph.addVertex(departure);
        }

        if(!graph.containsVertex(destination)) {
            graph.addVertex(destination);
        }

        graph.setEdgeWeight(graph.addEdge(departure, destination), distance);
    }

}