package puravida.creta


import com.google.gcloud.datastore.DatastoreOptions
import com.google.gcloud.datastore.Query
import com.google.gcloud.datastore.Entity
import com.google.gcloud.datastore.Key
import com.google.gcloud.datastore.KeyFactory
import com.google.gcloud.datastore.QueryResults
import com.google.gcloud.datastore.ReadOption
import org.springframework.security.core.authority.SimpleGrantedAuthority

import javax.annotation.PostConstruct

/**
 * Created by jorge on 4/04/16.
 */
class Datastore {

    com.google.gcloud.datastore.Datastore datastore
    KeyFactory keyFactoryUsers

    @PostConstruct
    void init(){
        try {
            DatastoreOptions options = DatastoreOptions.builder().build();
            datastore = options.service();
            keyFactoryUsers = datastore.newKeyFactory().kind("Users");
        }catch(Exception e){
            e.printStackTrace()
        }
    }

    boolean isUsersEmpty(){
        Query<Entity> query = Query.entityQueryBuilder()
                .kind("Users")
                .limit(1)
                .build();
        QueryResults<Entity> results = datastore.run(query);
        !results.hasNext()
    }

    void addUser( GoogleUserDetails userDetails){
        Key key = keyFactoryUsers.newKey(userDetails.username)
        Entity entity = Entity.builder(key).
                set("username",userDetails.username).
                set("password",userDetails.password).
                set("enabled",userDetails.enabled).
                set("accountNonExpired",userDetails.accountNonExpired).
                set("credentialsNonExpired",userDetails.credentialsNonExpired).
                set("accountNonLocked",userDetails.accountNonLocked).
                set("role", userDetails.authorities.join(',')).
                build()
        datastore.put(entity)
    }

    GoogleUserDetails findUser(username){
        Key key = keyFactoryUsers.newKey(username);
        Entity entity = datastore.get(key)
        if( !entity ){
            return null
        }
        List<SimpleGrantedAuthority>roles = []
        entity.String("role").split(',').each{
            roles.add(new SimpleGrantedAuthority(it))
        }
        GoogleUserDetails user = new GoogleUserDetails(
                entity.getString("username"),
                entity.getString("password"),
                entity.getBoolean("enabled"),
                entity.getBoolean("accountNonExpired"),
                entity.getBoolean("credentialsNonExpired"),
                entity.getBoolean("accountNonLocked"),
                roles,
                entity.getLong("id"))
        user
    }

}
