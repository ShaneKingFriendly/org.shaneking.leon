#!/usr/bin/env bash

export sk_leon__sonar_dir=$(readlink -f "$(dirname "${BASH_SOURCE[0]}")")
if [ ${SK_EXP__SONAR__TOKEN} ]; then
  cd ${sk_leon__sonar_dir}
  mvn sonar:sonar -Dsonar.host.url=http://sonar.shaneking.org -Dsonar.login=${SK_EXP__SONAR__TOKEN}
  sk_leon_modules=("org.shaneking.leon.persistence" "org.shaneking.leon.rr" "org.shaneking.leon.swagger" "org.shaneking.leon.test")
  for sk_leon_module in ${sk_leon_modules[@]}; do
    if [ -d ${sk_leon__sonar_dir}/${sk_leon_module} ]; then
      cd ${sk_leon__sonar_dir}/${sk_leon_module}
      mvn sonar:sonar -Dsonar.host.url=http://sonar.shaneking.org -Dsonar.login=${SK_EXP__SONAR__TOKEN}
    else
      echo "${sk_leon__sonar_dir}/${sk_leon_module} not exist."
    fi
  done
else
  echo 'SK_EXP__SONAR__TOKEN not exist.'
fi
cd ${sk_leon__sonar_dir}
