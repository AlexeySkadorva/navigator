package com.itrex.navigator.controller.domain;

import com.itrex.navigator.model.RouteSegment;

public class RouteSegmentResponse extends RouteSegment {

    public RouteSegmentResponse(RouteSegment routeSegment) {
        super(routeSegment.getDeparture(), routeSegment.getDestination(), routeSegment.getDistance());
    }

}
