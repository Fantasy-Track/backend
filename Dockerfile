FROM fantasytrack_proto AS proto

FROM maven:openjdk AS dev
WORKDIR /fantasytrack

COPY pom.xml .
COPY mobile/pom.xml mobile/pom.xml
COPY jobs/pom.xml jobs/pom.xml
COPY draft/pom.xml draft/pom.xml
COPY shared/pom.xml shared/pom.xml
RUN ["/usr/local/bin/mvn-entrypoint.sh", "mvn", "verify", "clean", "--fail-never"]

COPY mobile mobile
COPY jobs jobs
COPY draft draft
COPY shared shared

COPY --from=proto /fantasytrack/proto proto/

ENV PROTO_DIR=/fantasytrack/proto
RUN mvn protobuf:compile protobuf:compile-custom -pl shared
RUN mvn install -DskipTests=true
RUN mkdir build

FROM dev AS mobile
RUN mvn assembly:single -pl mobile
RUN mv mobile/bin/mobile.jar build
RUN rm -rf mobile draft jobs shared pom.xml
CMD [ "java", "-jar", "build/mobile.jar" ]

FROM dev AS draft
RUN mvn assembly:single -pl draft
RUN mv draft/bin/draft.jar build
RUN rm -rf mobile draft jobs shared pom.xml
CMD [ "java", "-jar", "build/draft.jar" ]

FROM dev AS jobs
RUN mvn assembly:single -pl jobs
RUN mv jobs/bin/jobs.jar build
RUN rm -rf mobile draft jobs shared pom.xml
CMD [ "java", "-jar", "build/jobs.jar" ]
