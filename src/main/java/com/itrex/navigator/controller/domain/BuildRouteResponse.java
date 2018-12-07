package com.itrex.navigator.controller.domain;

import com.itrex.navigator.model.Route;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BuildRouteResponse {

    private List<Route> routes;

}
