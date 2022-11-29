package org.cyrilselyanin.vendingsystem.regularbus.controller;

import org.cyrilselyanin.vendingsystem.regularbus.domain.BusTrip;
import org.cyrilselyanin.vendingsystem.regularbus.domain.Ticket;
import org.cyrilselyanin.vendingsystem.regularbus.helper.BusTripModelAssembler;
import org.cyrilselyanin.vendingsystem.regularbus.helper.TicketModelAssembler;
import org.cyrilselyanin.vendingsystem.regularbus.service.TicketService;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RepositoryRestController
public class TicketController {
    private final TicketService ticketService;
    private final TicketModelAssembler ticketModelAssembler;
    private final BusTripModelAssembler busTripModelAssembler;

    public TicketController(
            TicketService ticketService,
            TicketModelAssembler ticketModelAssembler,
            BusTripModelAssembler busTripModelAssembler
    ) {
        this.ticketService = ticketService;
        this.ticketModelAssembler = ticketModelAssembler;
        this.busTripModelAssembler = busTripModelAssembler;
    }

    @GetMapping("/api/tickets/{id}")
    public ResponseEntity<EntityModel<Ticket>> getTicketById(@PathVariable Long id) {
        return ticketService.getTicket(id)
                    .map(ticket -> {
                        EntityModel<Ticket> ticketRepresentation = ticketModelAssembler
                                .toModel(ticket)
                                .add(
                                        linkTo(
                                                methodOn(TicketController.class)
                                                        .getAllTickets()
                                        ).withRel("tickets")
                                );
                        return ResponseEntity.ok(ticketRepresentation);
                    })
                    .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/api/tickets")
    public ResponseEntity<CollectionModel<EntityModel<Ticket>>> getAllTickets() {
        return ResponseEntity.ok(
                ticketModelAssembler.toCollectionModel(
                        ticketService.getAllTickets()
                )
        );
    }

    @GetMapping("/api/tickets/{id}/busTrip")
    public ResponseEntity<EntityModel<BusTrip>> getTicketBusTrip(
            @PathVariable(name = "id") Long ticketId
    ) {
        return ticketService.getTicketBusTrip(ticketId)
                .map(busTrip -> {
                    EntityModel<BusTrip> busTripRepresentation = busTripModelAssembler
                            .toModel(busTrip);
                    return ResponseEntity.ok(busTripRepresentation);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
