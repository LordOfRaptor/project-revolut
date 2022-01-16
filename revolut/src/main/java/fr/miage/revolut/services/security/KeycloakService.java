package fr.miage.revolut.services.security;

import fr.miage.revolut.dto.UserRequest;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class KeycloakService {

    private static final Logger log = Logger.getLogger(KeycloakService.class.toString());

    @Autowired
    private Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${app.role}")
    private String role;

    @Value("${keycloak.resource}")
    private String resources;

    public Response create(UserRequest request)  {
        keycloak.tokenManager().getAccessToken();
        var password = preparePasswordRepresentation(request.getPassword());
        var user = prepareUserRepresentation(request, password);
        var response = keycloak
                .realm(realm)
                .users()
                .create(user);
        String userId = CreatedResponseUtil.getCreatedId(response);
        ClientRepresentation client = keycloak.realm(realm).clients().findByClientId(resources).get(0);
        RoleRepresentation clientRoleUser  = keycloak.realm(realm).clients().get(
                        client.getId())
                .roles().get(role).toRepresentation();
        keycloak.realm(realm)
                .users()
                .get(userId)
                .roles()
                .clientLevel(client.getId())
                .add(List.of(clientRoleUser));
        return response;
    }

    private CredentialRepresentation  preparePasswordRepresentation(String password) {
        var cR = new CredentialRepresentation();
        cR.setTemporary(false);
        cR.setType(CredentialRepresentation.PASSWORD);
        cR.setValue(password);
        return cR;
    }
    private UserRepresentation prepareUserRepresentation(UserRequest request, CredentialRepresentation cR )  {
        var newUser = new UserRepresentation();
        newUser.setEnabled(true);
        String id = UUID.randomUUID().toString();
        newUser.setId(id);
        newUser.setUsername(request.getUsername());
        newUser.setCredentials(List.of(cR));

        return newUser;
    }
}
