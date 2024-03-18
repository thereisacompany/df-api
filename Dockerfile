FROM eclipse-temurin:8-jdk
COPY ./config /config
ENV TZ=Asia/Taipei
CMD ["./gradlew", "clean", "bootJar"]
ARG num
ENV JAR_PATH ./build/libs/DFsystem-${num}.jar
COPY $JAR_PATH /DFsystem.jar
EXPOSE 13000
CMD ["java", "-Xms512m", "-Xmx1g" ,"-XX:MinHeapFreeRatio=40","-XX:MaxHeapFreeRatio=70", "-jar", "./DFsystem.jar"]