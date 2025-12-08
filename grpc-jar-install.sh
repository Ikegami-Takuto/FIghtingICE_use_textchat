# libフォルダがなければ作る
mkdir -p lib

# メインのダウンロード先URL
BASE_URL="https://repo1.maven.org/maven2"

# 必要なJARファイルのリスト (gRPC 1.68.0 / Protobuf 3.25.5)
# これらをすべて入れないと「NoClassDefFoundError」で動かなくなります
files=(
  "io/grpc/grpc-api/1.68.0/grpc-api-1.68.0.jar"
  "io/grpc/grpc-context/1.68.0/grpc-context-1.68.0.jar"
  "io/grpc/grpc-core/1.68.0/grpc-core-1.68.0.jar"
  "io/grpc/grpc-netty-shaded/1.68.0/grpc-netty-shaded-1.68.0.jar"
  "io/grpc/grpc-protobuf/1.68.0/grpc-protobuf-1.68.0.jar"
  "io/grpc/grpc-protobuf-lite/1.68.0/grpc-protobuf-lite-1.68.0.jar"
  "io/grpc/grpc-stub/1.68.0/grpc-stub-1.68.0.jar"
  "io/grpc/grpc-util/1.68.0/grpc-util-1.68.0.jar"
  "com/google/protobuf/protobuf-java/3.25.5/protobuf-java-3.25.5.jar"
  "com/google/guava/guava/33.3.1-android/guava-33.3.1-android.jar"
  "com/google/guava/failureaccess/1.0.2/failureaccess-1.0.2.jar"
  "com/google/api/grpc/proto-google-common-protos/2.29.0/proto-google-common-protos-2.29.0.jar"
  "com/google/errorprone/error_prone_annotations/2.23.0/error_prone_annotations-2.23.0.jar"
  "io/perfmark/perfmark-api/0.26.0/perfmark-api-0.26.0.jar"
  "javax/annotation/javax.annotation-api/1.3.2/javax.annotation-api-1.3.2.jar"
  "com/google/code/gson/gson/2.10.1/gson-2.10.1.jar"
  "com/google/android/annotations/4.1.1.4/annotations-4.1.1.4.jar"
)

echo "Downloading JARs to ./lib..."
for file in "${files[@]}"; do
    url="${BASE_URL}/${file}"
    filename=$(basename "$file")
    echo "Fetching: $filename"
    curl -L -o "lib/$filename" "$url" --silent
done

echo "完了しました！ libフォルダを確認してください。"