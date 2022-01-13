#!/bin/sh

#    [ -f "${SERVER_SSL_KEY-STORE}" ] || \
#        keytool -dname ${KEY_DNAME} -noprompt -genkeypair -alias ${SERVER_SSL_KEY_ALIAS} -keyalg RSA -keysize 2048 -storetype PKCS12 \
#            -keystore ${SERVER_SSL_KEY_STORE} -storepass ${SERVER_SSL_KEY_STORE_PASSWORD} -validity ${KEY_VALIDITY} \
#    && chmod 0400 ${SERVER_SSL_KEY_STORE}  \

#echo "Importing authorized certificates from ${CERTS_FOLDER}"
#find ${CERTS_FOLDER} -iname "*.crt" | xargs -I{} keytool -import -trustcacerts -keystore cacerts -storepass changeit -noprompt -alias {} -file {}

java -jar /opt/tracer/tracer.war