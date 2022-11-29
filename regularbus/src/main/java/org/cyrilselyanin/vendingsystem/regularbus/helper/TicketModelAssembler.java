package org.cyrilselyanin.vendingsystem.regularbus.helper;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.cyrilselyanin.vendingsystem.regularbus.controller.TicketController;
import org.cyrilselyanin.vendingsystem.regularbus.domain.Ticket;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class TicketModelAssembler implements SimpleRepresentationModelAssembler<Ticket> {
    @Override
    public void addLinks(EntityModel<Ticket> resource) {
        if (resource.getContent() == null) return;
        Long ticketId = resource.getContent().getId();
        if (ticketId == null) return;
        resource.add(
                WebMvcLinkBuilder.linkTo(
                        methodOn(
                                TicketController.class
                        ).getTicketById(ticketId)
                ).withSelfRel()
        );
        resource.add(
                linkTo(
                        methodOn(
                                TicketController.class
                        ).getTicketBusTrip(ticketId)
                ).withRel("busTrip")
        );
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<Ticket>> resources) {
        resources.add(
                linkTo(
                        methodOn(
                                TicketController.class
                        ).getAllTickets()
                ).withSelfRel()
        );
    }
}