package org.ciscoadiz.notification.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.logging.Log;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.reactive.ReactiveMailer;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.ciscoadiz.notification.event.UserRegisteredEvent;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class UserRegisteredConsumer {

    @Inject
    ReactiveMailer mailer;

    @Inject
    ObjectMapper objectMapper;

    @Incoming("user-registered")
    public Uni<Void> onUserRegistered(String message) {
        try {
            UserRegisteredEvent event = objectMapper.readValue(message, UserRegisteredEvent.class);
            Log.infof("Sending activation email to %s", event.email());

            String activationUrl = "http://localhost:8080/api/users/activate?token=" + event.activationToken();

            return mailer.send(
                    Mail.withHtml(
                            event.email(),
                            "Activa tu cuenta en Kittigram 🐱",
                            """
                            <h1>¡Bienvenido a Kittigram, %s!</h1>
                            <p>Para activar tu cuenta haz clic en el siguiente enlace:</p>
                            <a href="%s">Activar mi cuenta</a>
                            <p>El enlace expirará en 24 horas.</p>
                            """.formatted(event.name(), activationUrl)
                    )
            );
        } catch (Exception e) {
            Log.errorf("Error processing user-registered event: %s", e.getMessage());
            return Uni.createFrom().voidItem();
        }
    }
}