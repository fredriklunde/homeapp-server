#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

echo "Cleaning target folder..."
mvn -f ${DIR}/../pom.xml clean
if [ "$?" -ne 0 ]; then
    echo "Maven Clean Unsuccessful! Exiting."
    exit 1
fi

if [ ! -f ${DIR}/../src/main/resources/application.properties ]; then
    echo "Application properties not found"
    read -p 'Mirror host: ' mirrorhost
    read -p 'Mirror port: ' mirrorport

    read -p 'Razberry host: ' razhost
    read -p 'Razberry port: ' razport
    read -p 'Razberry username: ' razuser
    read -sp 'Razberry password: ' razpass

    FILE="${DIR}/../src/main/resources/application.properties"

    touch ${FILE}

    echo "server.port=28080" >> ${FILE}
    echo "mirror.server.host=${mirrorhost}" >> ${FILE}
    echo "mirror.server.port=${mirrorport}" >> ${FILE}
    echo "razberry.host=${razhost}" >> ${FILE}
    echo "razberry.port=${razport}" >> ${FILE}
    echo "razberry.port=${razuser}" >> ${FILE}
    echo "razberry.port=${razpass}" >> ${FILE}
    echo "lights.morning.cron.weekday=0 0 6 * * 1-5" >> ${FILE}
    echo "lights.morning.cron.weekend=0 45 7 * * 6-7" >> ${FILE}
fi

echo "Building jar..."
mvn -f ${DIR}/../pom.xml install
if [ "$?" -ne 0 ]; then
    echo "Maven Install Unsuccessful! Exiting."
    exit 1
fi

exit 1

ssh pi@rpi2 "mkdir -p ~/apps/homeapp"
/bin/bash upload.sh
scp start.sh pi@rpi2:~/apps/homeapp
scp stop.sh pi@rpi2:~/apps/homeapp
