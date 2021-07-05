FROM openlmis/service-base:5

COPY build/libs/*.jar /service.jar
COPY build/consul /consul
