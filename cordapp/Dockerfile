FROM openjdk:8u171-jdk-alpine

# Override default value with 'docker build --build-arg BUILDTIME_CORDA_VERSION=version'
# example: 'docker build --build-arg BUILDTIME_CORDA_VERSION=3.1-corda -t corda/node:3.1 .'
ARG BUILDTIME_CORDA_VERSION=3.1-corda
ARG BUILDTIME_JAVA_OPTIONS

ENV CORDA_VERSION=${BUILDTIME_CORDA_VERSION}
ENV JAVA_OPTIONS=${BUILDTIME_JAVA_OPTIONS}

# Set image labels
LABEL net.corda.version=${CORDA_VERSION} \
      maintainer="<devops@r3.com>" \
      vendor="R3"

RUN echo http://nl.alpinelinux.org/alpine/v3.8/main/ > /etc/apk/repositories; \
    echo http://mirror.yandex.ru/mirrors/alpine/v3.8/community >> /etc/apk/repositories


RUN apk add bash iputils && \
    rm -rf /var/cache/apk/* && \
    # Add user to run the app && \
    addgroup corda && \
    adduser -G corda -D -s /bin/bash corda && \
    # Create /opt/corda directory && \
    mkdir -p /opt/corda/src

#RUN apk add screen

# Copy corda jar
ADD  https://dl.bintray.com/r3/corda/net/corda/corda/${CORDA_VERSION}/corda-${CORDA_VERSION}.jar                       /opt/corda/corda.jar
RUN  chown -R corda:corda /opt/corda
#ADD --chown=corda:corda https://dl.bintray.com/r3/corda/net/corda/corda-webserver/${CORDA_VERSION}/corda-webserver-${CORDA_VERSION}.jar   /opt/corda/corda-webserver.jar


COPY ./run-corda.sh /run-corda.sh
COPY . /opt/corda/src
RUN cd /opt/corda/src && \
    sync && \
    chmod +x gradlew && \
   ./gradlew -Dhttp.proxyHost=webproxy.prd.lab-nxtit.priv -Dhttp.proxyPort=3128 -Dhttps.proxyHost=webproxy.prd.lab-nxtit.priv -Dhttps.proxyPort=3128 -x test deployNodeUAT && \
 #  ./gradlew  deployNodesUAT && \
   sync

RUN chmod +x /run-corda.sh && \
    sync
#COPY /opt/corda/src/build /opt/corda/app
#RUN rm -rf /opt/corda/src


RUN chmod -R u+x /opt/corda && \
    chgrp -R 0 /opt/corda && \
    chmod -R g=u /opt/corda /etc/passwd

# Working directory for Corda
WORKDIR /opt/corda
ENV HOME=/opt/corda

USER 10001
EXPOSE 10002
EXPOSE 10003
EXPOSE 10043
EXPOSE 10004

# Start it
CMD ["/run-corda.sh"]