package com.itrex.navigator.controller.domain;

import com.itrex.navigator.model.City;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteRequest {

    private City departure;

    private City destination;

    private Integer distance;

}
