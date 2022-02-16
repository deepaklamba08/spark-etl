PROJECT_DIR=$PWD

echo "starting maven build ..."
mvn clean compile install -DskipTests

BUILD_STATUS=$?
echo "maven build status is - $BUILD_STATUS"

if [ "$BUILD_STATUS" -ne 0 ]
then
  echo "maven build failed"
  exit 1
fi

echo "maven build success, copying jar file..."
cp $PROJECT_DIR/etl-app/target/etl-app-1.0-SNAPSHOT.jar $PROJECT_DIR/app/lib/