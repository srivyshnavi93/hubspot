FROM eclipse-temurin:8u362-b09-jre-focal

USER 0

RUN mkdir /hubspot
COPY ./target/hubspot-api-0.0.1-SNAPSHOT.war /hubspot/hubspot-api-0.0.1-SNAPSHOT.war
RUN apt-get update && apt-get install -y curl git wget nano vim

ENTRYPOINT ["java", "-jar", "/hubspot/hubspot-api-0.0.1-SNAPSHOT.war", "HubspotApplication"]