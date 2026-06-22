package com.musicstream.app.config;

import com.musicstream.account.application.AccountRegistration;
import com.musicstream.account.application.AccountRegistrationService;
import com.musicstream.account.domain.repository.ListenerAccountRepository;
import com.musicstream.billing.acl.AccountGateway;
import com.musicstream.billing.application.SubscriptionManagement;
import com.musicstream.billing.application.SubscriptionService;
import com.musicstream.billing.application.TransactionAuthorization;
import com.musicstream.billing.application.TransactionAuthorizationAppService;
import com.musicstream.billing.domain.repository.SubscriberRepository;
import com.musicstream.billing.domain.service.AuthorizationRule;
import com.musicstream.billing.domain.service.TransactionAuthorizationService;
import com.musicstream.billing.domain.service.rules.CardMustBeActiveRule;
import com.musicstream.billing.domain.service.rules.DuplicatedTransactionRule;
import com.musicstream.billing.domain.service.rules.HighFrequencySmallIntervalRule;
import com.musicstream.library.acl.AccountDirectory;
import com.musicstream.library.application.MusicLibraryManagement;
import com.musicstream.library.application.MusicLibraryService;
import com.musicstream.library.domain.repository.MusicLibraryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.util.List;

/**
 * Composition root. The Bounded Contexts are framework-free plain Java, so this
 * is where their application services and domain services are constructed and
 * wired against the Spring-managed adapters (repositories, ACLs, clock).
 *
 * Adding a new anti-fraud rule means adding one bean to {@link
 * #authorizationRules}; nothing else changes — the policy is open for extension.
 */
@Configuration
class BeanConfiguration {

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }

    // ---- Anti-fraud rules (Open/Closed: register a new rule here) -----------

    @Bean
    List<AuthorizationRule> authorizationRules() {
        return List.of(
                new CardMustBeActiveRule(),
                new HighFrequencySmallIntervalRule(),
                new DuplicatedTransactionRule());
    }

    @Bean
    TransactionAuthorizationService transactionAuthorizationService(List<AuthorizationRule> rules) {
        return new TransactionAuthorizationService(rules);
    }

    // ---- Application services (use cases) -----------------------------------

    @Bean
    AccountRegistration accountRegistration(ListenerAccountRepository accounts) {
        return new AccountRegistrationService(accounts);
    }

    @Bean
    MusicLibraryManagement musicLibraryManagement(MusicLibraryRepository libraries,
                                                  AccountDirectory accountDirectory) {
        return new MusicLibraryService(libraries, accountDirectory);
    }

    @Bean
    SubscriptionManagement subscriptionManagement(SubscriberRepository subscribers,
                                                  AccountGateway accountGateway,
                                                  Clock clock) {
        return new SubscriptionService(subscribers, accountGateway, clock);
    }

    @Bean
    TransactionAuthorization transactionAuthorization(SubscriberRepository subscribers,
                                                      AccountGateway accountGateway,
                                                      TransactionAuthorizationService authorizationService,
                                                      Clock clock) {
        return new TransactionAuthorizationAppService(subscribers, accountGateway, authorizationService, clock);
    }
}
