package com.musicstream.app.rest;

import com.musicstream.app.rest.dto.OperationRequest;
import com.musicstream.app.rest.dto.OperationResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Single REST entry point. Each request is one processed "line"; the controller
 * hands it to the {@link OperationDispatcher}, which decides the operation from
 * its {@code type}. A batch endpoint processes several lines in order.
 */
@RestController
@RequestMapping("/operations")
class OperationsController {

    private final OperationDispatcher dispatcher;

    OperationsController(OperationDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @PostMapping
    OperationResponse handle(@RequestBody OperationRequest request) {
        return dispatcher.dispatch(request);
    }

    @PostMapping("/batch")
    List<OperationResponse> handleBatch(@RequestBody List<OperationRequest> requests) {
        return requests.stream().map(dispatcher::dispatch).toList();
    }
}
