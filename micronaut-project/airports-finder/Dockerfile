FROM openjdk:11.0.10-jdk
COPY build/libs/airports-finder-*-all.jar airports-finder-0.1.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "airports-finder-0.1.jar"]

