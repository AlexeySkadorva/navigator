package com.itrex.navigator.service.impl;

import com.itrex.navigator.exception.RouteNotExistsException;
import com.itrex.navigator.model.City;
import com.itrex.navigator.model.Route;
import com.itrex.navigator.repository.RouteRepository;
import com.itrex.navigator.service.RouteService;
import com.itrex.navigator.validator.RouteValidator;
import com.itrex.navigator.validator.Validator;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.KShortestSimplePaths;
import org.jgrapht.graph.AsUndirectedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultRouteService implements RouteService {

    private static final int COUNT_OF_FIRST_ROUTES = 100;

    private final Validator validator;
    private final RouteRepository routeRepository;


    public DefaultRouteService(RouteRepository routeRepository) {
        this.validator = new RouteValidator();
        this.routeRepository = routeRepository;
    }

    @Override
    public void save(City departure, City destination, int distance) {
        validator.validateRoute(departure, destination, distance);

        routeRepository.save(departure, destination, distance);
    }

    @Override
    public List<Route> getRoutes(City departure, City destination) {
        Graph<City, DefaultWeightedEdge> citiesGraph = routeRepository.getAll();

        validator.validateCities(citiesGraph, departure, destination);

        KShortestSimplePaths<City, DefaultWeightedEdge> paths = new KShortestSimplePaths(new AsUndirectedGraph(citiesGraph));
        List<GraphPath<City, DefaultWeightedEdge>> allPathsBetweenCities = paths.getPaths(departure, destination, COUNT_OF_FIRST_ROUTES);

        if(CollectionUtils.isEmpty(allPathsBetweenCities)) {
            throw new RouteNotExistsException("Not found route between " + departure.getName() + " and " + destination.getName() + " cities");
        }

        return allPathsBetweenCities.stream()
            .sorted(Comparator.comparing(GraphPath::getWeight))
            .map(path -> new Route(path.getVertexList(), (int) path.getWeight()))
            .collect(Collectors.toList());
    }

}