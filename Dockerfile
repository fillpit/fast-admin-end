FROM openjdk:8u265-jre-slim
MAINTAINER kenfei <kenfei@aliyun.com>

ADD ./app/target/admin-exec.jar /usr/src/

EXPOSE 8090

CMD ["java", "-Xmx200m", "-jar", "/usr/src/admin-exec.jar"]
