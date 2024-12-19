FROM amazoncorretto:8-alpine-jdk

EXPOSE 8080

COPY target/rest-api-example.jar .

ENTRYPOINT [  \
    "java", \
    "-Xmx2G", \
    "-jar", "rest-api-example.jar" \
]
