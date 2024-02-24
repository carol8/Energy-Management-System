FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /ds2023/monitoringms

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY checkstyle.xml .
RUN ./mvnw dependency:resolve
RUN ./mvnw dependency:resolve-plugins
COPY src src
RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp_userms
ARG DEPENDENCY=/ds2023/monitoringms/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","com.carol8.monitoring_microservice.MonitoringMicroserviceApplication"]