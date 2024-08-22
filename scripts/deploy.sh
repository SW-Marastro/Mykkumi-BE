#!/bin/bash

# Build JAR 파일 위치 확인 및 파일명 추출
BUILD_JAR=$(ls /home/ec2-user/action/build/libs/*.jar)
JAR_NAME=$(basename $BUILD_JAR)
DEPLOY_PATH=/home/ec2-user/action/
DEPLOY_LOG=/home/ec2-user/action/deploy.log
DEPLOY_ERR_LOG=/home/ec2-user/action/deploy_err.log

echo "> Build 파일명: $JAR_NAME" >> $DEPLOY_LOG

# Build JAR 파일을 Deploy 위치로 복사
echo "> Build 파일 복사" >> $DEPLOY_LOG
cp $BUILD_JAR $DEPLOY_PATH

# 현재 실행 중인 애플리케이션이 있으면 종료
echo "> 현재 실행 중인 애플리케이션 pid 확인" >> $DEPLOY_LOG
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]; then
    echo "> 현재 구동 중인 애플리케이션이 없습니다." >> $DEPLOY_LOG
else
    echo "> 현재 구동 중인 애플리케이션 종료: kill -9 $CURRENT_PID" >> $DEPLOY_LOG
    kill -9 $CURRENT_PID
    sleep 5
fi

# 새 JAR 파일로 애플리케이션 실행
DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
echo "> DEPLOY_JAR 배포: $DEPLOY_JAR" >> $DEPLOY_LOG
nohup java -jar -Dspring.profiles.active=dev $DEPLOY_JAR >> $DEPLOY_LOG 2>> $DEPLOY_ERR_LOG &

# 배포 완료 메시지 로그
echo "> 배포 완료!" >> $DEPLOY_LOG
