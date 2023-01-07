FROM bellsoft/liberica-openjdk-alpine:17
COPY /target/cashregister-*.jar cashregister.jar
ENTRYPOINT ["java"]
CMD ["-jar", "cashregister.jar"]