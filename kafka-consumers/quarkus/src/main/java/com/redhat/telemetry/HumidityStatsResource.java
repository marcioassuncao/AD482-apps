package com.redhat.telemetry;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;


@Path("/humidity")
public class HumidityStatsResource {

    // TODO: Implement the endpoint to GET humidity stats
    @Inject
    HumidityConditionsConsumer consumer;

    @GET
    public HumidityStats getHumidityStats() {
        return consumer.stats;
    }
}
