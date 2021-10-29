FROM alpine:3.14.2

ADD src /opt/tracer-src/src
ADD pom.xml /opt/tracer-src

RUN \    
    # Install those dependencies that will be removed afterwards
    apk --no-cache add --virtual build-dependencies \
        bash openjdk11 maven \
    && cd /opt/tracer-src \
    && mvn -U clean package \
    && mkdir -p /opt/tracer/ssl \
    && mv /opt/tracer-src/target/tracer.war /opt/tracer \
    # Install the runtime dependencies
    && apk del build-dependencies  \
    && rm -rf /opt/tracer-src /root/.m2 /var/cache/apk/* \
    && apk --no-cache add openjdk11-jre-headless

ENV SERVER_PORT 8443 
ENV SERVER_SSL_KEY_ALIAS '' 
ENV SERVER_SSL_KEY_STORE_PASSWORD ''
ENV SERVER_SSL_KEY_STORE /opt/tracer/ssl/cert.p12

ENV KEY_DNAME 'CN=localhost,OU=I3M,O=UPV,L=VALENCIA,S=VALENCIA,C=ES'
ENV KEY_VALIDITY 3650
    
EXPOSE 8443

ENTRYPOINT \
#    [ -f "${SERVER_SSL_KEY-STORE}" ] || \
#        keytool -dname ${KEY_DNAME} -noprompt -genkeypair -alias ${SERVER_SSL_KEY_ALIAS} -keyalg RSA -keysize 2048 -storetype PKCS12 \
#            -keystore ${SERVER_SSL_KEY_STORE} -storepass ${SERVER_SSL_KEY_STORE_PASSWORD} -validity ${KEY_VALIDITY} \
#    && chmod 0400 ${SERVER_SSL_KEY_STORE}  \
    java -jar -Dspring.profiles.active=prod /opt/tracer/tracer.war
    