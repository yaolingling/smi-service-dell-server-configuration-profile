FROM openjdk:8-jre
VOLUME /tmp
ADD build/libs/dell-server-configuration-profile*.jar app.jar
COPY pkg/scripts/* /scripts/
COPY pkg/scripts/* /opt/dell/smi/scripts/
COPY application.yml /application.yml
EXPOSE 46018
RUN bash -c 'touch /app.jar'
RUN chmod +x /opt/dell/smi/scripts/nfs_tool.sh
RUN apt-get update -qq && apt-get install -y nfs-common
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]