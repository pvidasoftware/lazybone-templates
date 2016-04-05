package puravida.creta

import com.google.api.services.storage.model.Bucket
import com.google.api.services.storage.model.StorageObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.StorageScopes
import org.springframework.context.annotation.Lazy
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.MutablePropertySources
import org.springframework.core.env.PropertiesPropertySource
import org.springframework.core.env.PropertySource;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collection;

/**
 * Created by jorge on 4/04/16.
 */
class StorageConfiguration {

    public static PropertySource<java.util.Properties>fromBucket( String environment = 'development', String file = 'configuration.properties'){
        Properties prp = new Properties()
        try {
            Storage storage = buildService()
            InputStream inputStream = storage.objects().get("puravida-creta", "$environment/$file").executeMediaAsInputStream()
            prp.load(inputStream)
        }catch(e){
            println "Error cargando properties from $environment/$file"
        }
        new PropertiesPropertySource('bucket',prp)
    }

    private static Storage buildService() throws IOException, GeneralSecurityException {
        HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = new JacksonFactory();
        GoogleCredential credential = GoogleCredential.getApplicationDefault(transport, jsonFactory);

        if (credential.createScopedRequired()) {
            Collection<String> allScopes = StorageScopes.all();
            credential = credential.createScoped(allScopes);
        }

        return new Storage.Builder(transport, jsonFactory, credential)
                //.setApplicationName("puravida-creta")
                .build();
    }

    private static Bucket getBucket(String name){
        Storage storage = buildService()
        Storage.Buckets.Get bucketRequest = storage.buckets().get(name);
        bucketRequest.setProjection("full");
        bucketRequest.execute()
    }

}
