# 환경구성
FROM azul/zulu-openjdk:17-latest

# jar file 변수 설정
ARG JAR_FILE=build/libs/*.jar

# 빌드파일 복사
COPY ${JAR_FILE} app.jar

# jar 파일 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]