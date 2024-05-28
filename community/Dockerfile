FROM openjdk:17-alpine
ARG JAR_FILE=/build/libs/community-0.0.1-SNAPSHOT.jar
ARG DB_URL
ARG S3_ACCESS_KEY
ARG S3_SECRET_KEY
ENV DB_URL=${DB_URL}
ENV S3_ACCESS_KEY=${S3_ACCESS_KEY}
ENV S3_SECRET_KEY=${S3_SECRET_KEY}
COPY ${JAR_FILE} /app.jar
ENTRYPOINT ["java","-Duser.timezone=Asia/Seoul","-jar","/app.jar"]