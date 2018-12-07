package com.itrex.navigator.controller.domain;

import com.itrex.navigator.model.Route;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteResponse {

    private List<Route> routes;

}
