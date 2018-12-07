package com.itrex.navigator.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Route {

    private List<City> cities;

    private int distance;

}
