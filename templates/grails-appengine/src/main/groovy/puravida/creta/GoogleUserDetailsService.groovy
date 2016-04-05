package puravida.creta

import com.google.gcloud.datastore.Entity
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.userdetails.GrailsUserDetailsService
import grails.plugin.springsecurity.userdetails.NoStackUsernameNotFoundException
import grails.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowire
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException

import javax.annotation.PostConstruct

/**
 * Created by jorge on 2/04/16.
 */
class GoogleUserDetailsService implements GrailsUserDetailsService{

    static final List NO_ROLES = [new SimpleGrantedAuthority(SpringSecurityUtils.NO_ROLE)]

    @Value('${admin}')
    String admin

    @Value('${password}')
    String password

    @Autowired
    SpringSecurityService springSecurityService

    @Autowired
    Datastore datastore

    UserDetails loadUserByUsername(String username, boolean loadRoles)
            throws UsernameNotFoundException {
        return loadUserByUsername(username)
    }

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if( username.equals(admin)){
            String pwd = springSecurityService.passwordEncoder ?
                    springSecurityService.encodePassword(password) :
                    password
            GoogleUserDetails user = new GoogleUserDetails(
                    username, pwd, true, true, true, true,
                    [ new SimpleGrantedAuthority('ROLE_ADMIN'),new SimpleGrantedAuthority('ROLE_USER')  ],
                    0)
            return user
        }

        GoogleUserDetails user = datastore.findUser(username)
        if (user == null) {
            throw new NoStackUsernameNotFoundException()
        }
        String pwd = springSecurityService.passwordEncoder ?
                springSecurityService.encodePassword(user.password) :
                user.password
        user.pwd = pwd
        return user
    }

}
