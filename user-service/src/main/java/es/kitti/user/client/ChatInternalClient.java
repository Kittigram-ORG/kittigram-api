package es.kitti.user.client;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "chat-service")
@Path("/chats/internal")
public interface ChatInternalClient {

    @DELETE
    @Path("/users/{userId}")
    Uni<Response> anonymizeUser(
            @PathParam("userId") Long userId,
            @HeaderParam("X-Internal-Token") String internalToken
    );
}