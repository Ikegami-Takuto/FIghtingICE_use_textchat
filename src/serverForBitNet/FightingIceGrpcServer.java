package serverForBitNet;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import serverForBitNet.BitNetToFightingICE.GameState;
import serverForBitNet.BitNetToFightingICE.OutputOfBitNet;
import serverForBitNet.FightingIceServerGrpc.FightingIceServerImplBase;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.protobuf.Empty;

public class FightingIceGrpcServer extends FightingIceServerImplBase {
    private Server server;
    // AI本体から書き込んでもらうための「最新のゲーム状況」置き場
    // (AIInterface側で、毎フレームここに値をセットします)
    public static volatile String currentDistance = "5";
    public static volatile String currentLightPositions = "None";
    public static volatile String currentPlayerState = "STAND";
    public static volatile String isBusy = "False";

    // AI本体へ渡すための「受信したコマンド」置き場
    public static volatile String latestCommand = "NEUTRAL";

    public void start() throws IOException {
        int port = 50051;
        server = ServerBuilder.forPort(port).addService(this).build().start();
        Logger.getAnonymousLogger().log(Level.WARNING, "Java Server started, listening onon" + port);
        System.out.println("Java Server started, listening on " + port);

        // 終了時の処理
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            FightingIceGrpcServer.this.stop();
            System.err.println("*** server shut down");
        }));
    }
    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    @Override
    public void gameStream(OutputOfBitNet request, StreamObserver<GameState> responseObserver) {
        String command = request.getOutputOfBitNet();
        latestCommand = command;

        GameState responce = GameState.newBuilder()
                        .setDistance(currentDistance)
                        .setLightPositions(currentLightPositions)
                        .setPlayerState(currentPlayerState)
                        .setIsbusy(isBusy).build();
        responseObserver.onNext(responce);
        responseObserver.onCompleted();
    }


    
}
