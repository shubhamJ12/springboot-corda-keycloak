package com.corda.backend.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.corda.backend.config.KeycloakConfiguration;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/api")
public class KeycloakResource {

    @Autowired
    KeycloakConfiguration keycloakConfiguration;

   Keycloak kc = null;

  @PostConstruct
    public void initKeycloak(){
        kc = Keycloak.getInstance(keycloakConfiguration.getServerUrl(), keycloakConfiguration.getRealmName(),
            keycloakConfiguration.getUsername(), keycloakConfiguration.getPassword(), keycloakConfiguration.getClientId());


   }

    @GetMapping("/get-all-realm-roles")
    @Timed
    public List<RoleRepresentation> getAllRealmRoles() {
        return kc.realm(keycloakConfiguration.getRealmName()).roles().list();
    }

    @GetMapping("/get-users-of-role")
    @Timed
    public Set<UserRepresentation> getRoleUserMembers(@RequestParam(value = "role") String role) {
        return kc.realm(keycloakConfiguration.getRealmName()).roles().get(role).getRoleUserMembers();
    }

    @GetMapping("/assign-realm-role")
    @Timed
    public String assignRealmRole(@RequestParam(value = "userId") String userId,@RequestParam(value = "roles") String[] roles) {
        //String userId = kc.realm(keycloakConfiguration.getRealmName()).users().search(username, 0, 1).get(0).getId();
        List<RoleRepresentation> userRoles = kc.realm(keycloakConfiguration.getRealmName()).users().get(userId).roles().realmLevel().listAll();
        kc.realm(keycloakConfiguration.getRealmName()).users().get(userId).roles().realmLevel().remove(userRoles);
        for(String role: roles){
            RoleRepresentation realmRoleRepresentation = kc.realm(keycloakConfiguration.getRealmName()).roles().get(role).toRepresentation();
            kc.realm(keycloakConfiguration.getRealmName()).users().get(userId).roles().realmLevel().add(Arrays.asList(realmRoleRepresentation));
        }

        return "Realm Role assigned";
    }
}
