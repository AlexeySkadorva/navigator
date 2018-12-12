package com.itrex.navigator.service.impl;

import com.itrex.navigator.exception.RouteNotExistsException;
import com.itrex.navigator.graph.path.algorithm.AllPathsAlgorithm;
import com.itrex.navigator.model.City;
import com.itrex.navigator.model.Route;
import com.itrex.navigator.model.RouteSegment;
import com.itrex.navigator.repository.RouteRepository;
import com.itrex.navigator.service.RouteService;
import com.itrex.navigator.validator.RouteValidator;
import com.itrex.navigator.validator.Validator;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultRouteService implements RouteService {

    private final Validator validator;
    private final AllPathsAlgorithm<City, DefaultWeightedEdge> allPathsAlgorithm;

    private final RouteRepository routeRepository;


    public DefaultRouteService(RouteRepository routeRepository) {
        this.validator = new RouteValidator();
        allPathsAlgorithm = new AllPathsAlgorithm<>();

        this.routeRepository = routeRepository;
    }

    @Override
    public RouteSegment save(RouteSegment routeSegment) {
        validator.validateRouteSegment(routeSegment);

        return routeRepository.save(routeSegment);
    }

    @Override
    public List<Route> getRoutes(City departure, City destination) {
        Graph<City, DefaultWeightedEdge> citiesGraph = routeRepository.getAll();

        validator.validateCities(citiesGraph, departure, destination);

        List<GraphPath<City, DefaultWeightedEdge>> allPaths = allPathsAlgorithm.getPaths(citiesGraph, departure, destination);

        if (CollectionUtils.isEmpty(allPaths)) {
            throw new RouteNotExistsException("Route not exists between " + departure.getName() + " and " + destination.getName() + " cities");
        }

        return allPaths.stream()
            .sorted(Comparator.comparing(GraphPath::getWeight))
            .map(path -> new Route(path.getVertexList(), (int) path.getWeight()))
            .collect(Collectors.toList());
    }

}
