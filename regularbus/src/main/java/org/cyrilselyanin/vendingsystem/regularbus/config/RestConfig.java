package org.cyrilselyanin.vendingsystem.regularbus.config;

import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.BusTrip;
import org.cyrilselyanin.vendingsystem.regularbus.domain.Seat;
import org.cyrilselyanin.vendingsystem.regularbus.domain.Ticket;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Component
public class RestConfig implements RepositoryRestConfigurer {
    @Override
    public void configureRepositoryRestConfiguration(
            RepositoryRestConfiguration config,
            CorsRegistry cors
    ) {
        config.exposeIdsFor(
//                Carrier.class,
//                Bus.class,
//                BusPointType_cl.class,
//                Fare.class,
//                BusPoint.class,
                BusTrip.class,
                Seat.class,
                Ticket.class
        );
    }
}