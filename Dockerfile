FROM fantasytrack_proto AS proto

FROM maven:openjdk AS dev
WORKDIR /fantasytrack
COPY mobile mobile
COPY jobs jobs
COPY draft draft
COPY shared shared
COPY pom.xml .
COPY --from=proto /fantasytrack/proto proto/

RUN mvn protobuf:compile protobuf:compile-custom -pl shared
RUN mvn install -DskipTests=true

FROM dev AS mobile
RUN mvn assembly:single -pl mobile
CMD [ "java", "-jar", "mobile/bin/mobile.jar" ]

FROM dev AS draft
RUN mvn assembly:single -pl draft
CMD [ "java", "-jar", "draft/bin/draft.jar" ]

FROM dev AS jobs
RUN mvn assembly:single -pl jobs
CMD [ "java", "-jar", "jobs/bin/jobs.jar" ]
