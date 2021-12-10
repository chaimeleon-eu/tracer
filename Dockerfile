FROM alpine:3.14.2

ADD src /opt/tracer-src/src
ADD pom.xml /opt/tracer-src
ADD entrypoint.sh /opt

RUN \    
    # Install those dependencies that will be removed afterwards
    apk --no-cache add --virtual build-dependencies \
        bash openjdk11 maven \
    && chmod +x /opt/entrypoint.sh \
    && cd /opt/tracer-src \
    && mvn -U clean package \
    && mkdir -p /opt/tracer/ssl/truststore \
    && mkdir -p /opt/tracer/ssl/authorized \
    && mv /opt/tracer-src/target/tracer.war /opt/tracer \
    # Install the runtime dependencies
    && apk del build-dependencies  \
    && rm -rf /opt/tracer-src /root/.m2 /var/cache/apk/* \
    && apk --no-cache add openjdk11-jre-headless

ENV SERVER_PORT 8443 
ENV SERVER_SSL_KEY_ALIAS '' 
ENV SERVER_SSL_KEY_STORE_PASSWORD ''
ENV SERVER_SSL_KEY_STORE /opt/tracer/ssl/truststore/cert.p12
ENV CERTS_FOLDER /opt/tracer/ssl/authorized

ENV KEY_DNAME 'CN=localhost,OU=I3M,O=UPV,L=VALENCIA,S=VALENCIA,C=ES'
ENV KEY_VALIDITY 3650
    
EXPOSE 8443

ENTRYPOINT /opt/entrypoint.sh

    
    