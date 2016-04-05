package puravida.creta

import grails.plugin.springsecurity.userdetails.GrailsUser
import org.springframework.security.core.GrantedAuthority

/**
 * Created by jorge on 2/04/16.
 */
class GoogleUserDetails extends GrailsUser {

    GoogleUserDetails(
            String username, String password, boolean enabled,
            boolean accountNonExpired, boolean credentialsNonExpired,
            boolean accountNonLocked,
            Collection<GrantedAuthority> authorities,
            long id) {
        super(username, password, enabled, accountNonExpired,
                credentialsNonExpired, accountNonLocked, authorities, id)
    }

}
