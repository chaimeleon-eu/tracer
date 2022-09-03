#!/bin/sh

#    [ -f "${SERVER_SSL_KEY-STORE}" ] || \
#        keytool -dname ${KEY_DNAME} -noprompt -genkeypair -alias ${SERVER_SSL_KEY_ALIAS} -keyalg RSA -keysize 2048 -storetype PKCS12 \
#            -keystore ${SERVER_SSL_KEY_STORE} -storepass ${SERVER_SSL_KEY_STORE_PASSWORD} -validity ${KEY_VALIDITY} \
#    && chmod 0400 ${SERVER_SSL_KEY_STORE}  \

#echo "Importing authorized certificates from ${CERTS_FOLDER}"
#find ${CERTS_FOLDER} -iname "*.crt" | xargs -I{} keytool -import -trustcacerts -keystore cacerts -storepass changeit -noprompt -alias {} -file {}

FILE=${BASE_DIR}/${CACHEDBNAME}.mv.db
if [ -f $FILE ]; then
    DATE=`date -u +"%Y-%m-%dT%H-%M-%SZ"`
    NEW_FILE=$FILE.$DATE.tar.gz
    echo "Cache DB $FILE exists, create backup $NEW_FILE"
    env GZIP=-9  tar -czvf $NEW_FILE -C ${BASE_DIR} $FILE
    ls -al ${BASE_DIR}
else
    echo  echo "Cache DB $FILE doesn't exists"
fi

java -jar /opt/tracer/tracer.war
