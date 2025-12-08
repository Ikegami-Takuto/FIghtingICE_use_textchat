package serverForBitNet;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class FightingIceServerGrpc {

  private FightingIceServerGrpc() {}

  public static final java.lang.String SERVICE_NAME = "serverForBitNet.FightingIceServer";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<serverForBitNet.BitNetToFightingICE.OutputOfBitNet,
      serverForBitNet.BitNetToFightingICE.GameState> getGameStreamMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GameStream",
      requestType = serverForBitNet.BitNetToFightingICE.OutputOfBitNet.class,
      responseType = serverForBitNet.BitNetToFightingICE.GameState.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<serverForBitNet.BitNetToFightingICE.OutputOfBitNet,
      serverForBitNet.BitNetToFightingICE.GameState> getGameStreamMethod() {
    io.grpc.MethodDescriptor<serverForBitNet.BitNetToFightingICE.OutputOfBitNet, serverForBitNet.BitNetToFightingICE.GameState> getGameStreamMethod;
    if ((getGameStreamMethod = FightingIceServerGrpc.getGameStreamMethod) == null) {
      synchronized (FightingIceServerGrpc.class) {
        if ((getGameStreamMethod = FightingIceServerGrpc.getGameStreamMethod) == null) {
          FightingIceServerGrpc.getGameStreamMethod = getGameStreamMethod =
              io.grpc.MethodDescriptor.<serverForBitNet.BitNetToFightingICE.OutputOfBitNet, serverForBitNet.BitNetToFightingICE.GameState>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GameStream"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  serverForBitNet.BitNetToFightingICE.OutputOfBitNet.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  serverForBitNet.BitNetToFightingICE.GameState.getDefaultInstance()))
              .setSchemaDescriptor(new FightingIceServerMethodDescriptorSupplier("GameStream"))
              .build();
        }
      }
    }
    return getGameStreamMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static FightingIceServerStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FightingIceServerStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FightingIceServerStub>() {
        @java.lang.Override
        public FightingIceServerStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FightingIceServerStub(channel, callOptions);
        }
      };
    return FightingIceServerStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static FightingIceServerBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FightingIceServerBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FightingIceServerBlockingV2Stub>() {
        @java.lang.Override
        public FightingIceServerBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FightingIceServerBlockingV2Stub(channel, callOptions);
        }
      };
    return FightingIceServerBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static FightingIceServerBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FightingIceServerBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FightingIceServerBlockingStub>() {
        @java.lang.Override
        public FightingIceServerBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FightingIceServerBlockingStub(channel, callOptions);
        }
      };
    return FightingIceServerBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static FightingIceServerFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FightingIceServerFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FightingIceServerFutureStub>() {
        @java.lang.Override
        public FightingIceServerFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FightingIceServerFutureStub(channel, callOptions);
        }
      };
    return FightingIceServerFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void gameStream(serverForBitNet.BitNetToFightingICE.OutputOfBitNet request,
        io.grpc.stub.StreamObserver<serverForBitNet.BitNetToFightingICE.GameState> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGameStreamMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service FightingIceServer.
   */
  public static abstract class FightingIceServerImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return FightingIceServerGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service FightingIceServer.
   */
  public static final class FightingIceServerStub
      extends io.grpc.stub.AbstractAsyncStub<FightingIceServerStub> {
    private FightingIceServerStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FightingIceServerStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FightingIceServerStub(channel, callOptions);
    }

    /**
     */
    public void gameStream(serverForBitNet.BitNetToFightingICE.OutputOfBitNet request,
        io.grpc.stub.StreamObserver<serverForBitNet.BitNetToFightingICE.GameState> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGameStreamMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service FightingIceServer.
   */
  public static final class FightingIceServerBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<FightingIceServerBlockingV2Stub> {
    private FightingIceServerBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FightingIceServerBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FightingIceServerBlockingV2Stub(channel, callOptions);
    }

    /**
     */
    public serverForBitNet.BitNetToFightingICE.GameState gameStream(serverForBitNet.BitNetToFightingICE.OutputOfBitNet request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getGameStreamMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service FightingIceServer.
   */
  public static final class FightingIceServerBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<FightingIceServerBlockingStub> {
    private FightingIceServerBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FightingIceServerBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FightingIceServerBlockingStub(channel, callOptions);
    }

    /**
     */
    public serverForBitNet.BitNetToFightingICE.GameState gameStream(serverForBitNet.BitNetToFightingICE.OutputOfBitNet request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGameStreamMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service FightingIceServer.
   */
  public static final class FightingIceServerFutureStub
      extends io.grpc.stub.AbstractFutureStub<FightingIceServerFutureStub> {
    private FightingIceServerFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FightingIceServerFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FightingIceServerFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<serverForBitNet.BitNetToFightingICE.GameState> gameStream(
        serverForBitNet.BitNetToFightingICE.OutputOfBitNet request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGameStreamMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GAME_STREAM = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GAME_STREAM:
          serviceImpl.gameStream((serverForBitNet.BitNetToFightingICE.OutputOfBitNet) request,
              (io.grpc.stub.StreamObserver<serverForBitNet.BitNetToFightingICE.GameState>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getGameStreamMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              serverForBitNet.BitNetToFightingICE.OutputOfBitNet,
              serverForBitNet.BitNetToFightingICE.GameState>(
                service, METHODID_GAME_STREAM)))
        .build();
  }

  private static abstract class FightingIceServerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    FightingIceServerBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return serverForBitNet.BitNetToFightingICE.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("FightingIceServer");
    }
  }

  private static final class FightingIceServerFileDescriptorSupplier
      extends FightingIceServerBaseDescriptorSupplier {
    FightingIceServerFileDescriptorSupplier() {}
  }

  private static final class FightingIceServerMethodDescriptorSupplier
      extends FightingIceServerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    FightingIceServerMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (FightingIceServerGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new FightingIceServerFileDescriptorSupplier())
              .addMethod(getGameStreamMethod())
              .build();
        }
      }
    }
    return result;
  }
}
