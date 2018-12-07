package com.itrex.navigator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteSegment {

    private City departure;

    private City destination;

    private Integer distance;

}
