package com.boxever.model;

import lombok.Data;

import java.util.List;

@Data
public class FinalRouteTree {

    private Integer duration;

    private List<Route> routes;

    public FinalRouteTree() {
    }
}
