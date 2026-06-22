package com.musicstream.app.rest;

import com.musicstream.account.application.AccountRegistration;
import com.musicstream.account.domain.model.Email;
import com.musicstream.account.domain.model.ListenerAccount;
import com.musicstream.app.rest.dto.OperationRequest;
import com.musicstream.app.rest.dto.OperationResponse;
import com.musicstream.billing.application.SubscriptionManagement;
import com.musicstream.billing.application.TransactionAuthorization;
import com.musicstream.billing.domain.model.Plan;
import com.musicstream.billing.domain.service.AuthorizationResult;
import com.musicstream.billing.domain.service.RejectionReason;
import com.musicstream.library.application.MusicLibraryManagement;
import com.musicstream.library.domain.model.PlaylistId;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Routes a processed line to the right context's use case based on its
 * {@link OperationType}. The dispatcher only translates between the wire
 * envelope and the intention-revealing application interfaces — it holds no
 * business logic of its own.
 */
@Service
class OperationDispatcher {

    private final AccountRegistration accountRegistration;
    private final SubscriptionManagement subscriptionManagement;
    private final MusicLibraryManagement musicLibraryManagement;
    private final TransactionAuthorization transactionAuthorization;

    OperationDispatcher(AccountRegistration accountRegistration,
                        SubscriptionManagement subscriptionManagement,
                        MusicLibraryManagement musicLibraryManagement,
                        TransactionAuthorization transactionAuthorization) {
        this.accountRegistration = accountRegistration;
        this.subscriptionManagement = subscriptionManagement;
        this.musicLibraryManagement = musicLibraryManagement;
        this.transactionAuthorization = transactionAuthorization;
    }

    OperationResponse dispatch(OperationRequest request) {
        OperationType type = OperationType.fromWire(request.type());
        return switch (type) {
            case ACCOUNT -> createAccount(request);
            case CREDIT_CARD -> registerCreditCard(request);
            case SUBSCRIPTION -> subscribe(request);
            case FAVORITE -> favorite(request);
            case PLAYLIST -> createPlaylist(request);
            case ADD_TO_PLAYLIST -> addToPlaylist(request);
            case AUTHORIZE_TRANSACTION -> authorize(request);
        };
    }

    private OperationResponse createAccount(OperationRequest request) {
        ListenerAccount account = accountRegistration.register(request.name(), Email.of(request.email()));
        return OperationResponse.ok(OperationType.ACCOUNT.wire(),
                Map.of("listenerId", account.id().value(),
                        "email", account.email().value()));
    }

    private OperationResponse registerCreditCard(OperationRequest request) {
        boolean active = request.cardActive() == null || request.cardActive();
        subscriptionManagement.registerCreditCard(
                request.listenerId(), request.cardNumber(), request.cardHolder(),
                YearMonth.parse(request.cardExpiry()), active);
        return OperationResponse.ok(OperationType.CREDIT_CARD.wire(),
                Map.of("listenerId", request.listenerId(), "cardActive", active));
    }

    private OperationResponse subscribe(OperationRequest request) {
        subscriptionManagement.subscribe(request.listenerId(), Plan.valueOf(request.plan().toUpperCase()));
        return OperationResponse.ok(OperationType.SUBSCRIPTION.wire(),
                Map.of("listenerId", request.listenerId(), "plan", request.plan().toUpperCase()));
    }

    private OperationResponse favorite(OperationRequest request) {
        musicLibraryManagement.favoriteTrack(request.listenerId(), request.trackId());
        return OperationResponse.ok(OperationType.FAVORITE.wire(),
                Map.of("listenerId", request.listenerId(), "trackId", request.trackId()));
    }

    private OperationResponse createPlaylist(OperationRequest request) {
        PlaylistId playlistId = musicLibraryManagement.createPlaylist(request.listenerId(), request.playlistName());
        return OperationResponse.ok(OperationType.PLAYLIST.wire(),
                Map.of("listenerId", request.listenerId(),
                        "playlistId", playlistId.value(),
                        "playlistName", request.playlistName()));
    }

    private OperationResponse addToPlaylist(OperationRequest request) {
        musicLibraryManagement.addTrackToPlaylist(
                request.listenerId(), request.playlistId(), request.trackId());
        return OperationResponse.ok(OperationType.ADD_TO_PLAYLIST.wire(),
                Map.of("listenerId", request.listenerId(),
                        "playlistId", request.playlistId(),
                        "trackId", request.trackId()));
    }

    private OperationResponse authorize(OperationRequest request) {
        AuthorizationResult result = transactionAuthorization.authorize(
                request.listenerId(), request.amount(), request.merchant(), request.occurredAt());

        List<String> violations = result.violations().stream().map(RejectionReason::code).toList();
        Map<String, Object> data = new HashMap<>();
        data.put("listenerId", request.listenerId());
        data.put("violations", violations);
        result.transactionId().ifPresent(id -> data.put("transactionId", id.value()));

        String status = result.isApproved() ? "approved" : "rejected";
        return OperationResponse.of(OperationType.AUTHORIZE_TRANSACTION.wire(), status, data);
    }
}
