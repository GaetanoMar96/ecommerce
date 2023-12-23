FROM maven as build
WORKDIR /app
COPY . .
RUN mvn install

FROM openjdk:17-jdk-alpine
# Install bash and net-tools (for netstat)
RUN apk add --no-cache bash net-tools
ADD target/ecommerce-0.0.1-SNAPSHOT.jar ecommerce-0.0.1-SNAPSHOT.jar

EXPOSE 8080
CMD ["java", "-jar", "/ecommerce-0.0.1-SNAPSHOT.jar"]


