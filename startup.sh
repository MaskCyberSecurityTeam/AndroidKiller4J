CLASS_PATH=./
for jar in ./libs/*.jar; do
    CLASS_PATH=$CLASS_PATH:$jar
done
./jre/bin/java -cp $CLASS_PATH com.richardtang.androidkiller4j.Application