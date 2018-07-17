echo
echo "************** BUILDING FOR LATEST SPRING BOOT 2.x ************** "
mvn clean install
echo
echo
echo "************** BUILDING FOR SPRING BOOT 1.x ************** "
mvn clean install -Dspring-boot.version=1.5.8.RELEASE
