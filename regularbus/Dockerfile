FROM bellsoft/liberica-openjdk-alpine:17
COPY /target/regularbus-*.jar regularbus.jar
ENTRYPOINT ["java"]
CMD ["-jar", "regularbus.jar"]