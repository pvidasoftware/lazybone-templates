FROM gcr.io/google_appengine/openjdk8
VOLUME /tmp
ADD puravida-creta-0.1.war app.jar
ENTRYPOINT [ "java", "-Djava.security.egd=file:/dev/./urandom", "-Dgrails.env=production", "-jar", "/app.jar"]