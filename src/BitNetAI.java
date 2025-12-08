
import aiinterface.AIInterface;
import aiinterface.CommandCenter;
import struct.FrameData;
import struct.GameData;
import struct.Key;
// サーバー実装クラスをインポート
import serverForBitNet.FightingIceGrpcServer;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import gamescene.NewSystemVeil;

import javax.management.RuntimeErrorException;

public class BitNetAI implements AIInterface {
    private Key inputKey;
    private CommandCenter commandCenter;
    private boolean serverStarted = false;

    private boolean playerNumber;
    private int i = 100;

    @Override
    public int initialize(GameData gd, boolean playerNumber) {
        Logger.getAnonymousLogger().log(Level.WARNING, "★DEBUG: AIの初期化が始まりました");
        this.inputKey = new Key();
        this.commandCenter = new CommandCenter();
        this.playerNumber = playerNumber;

        if (!this.serverStarted) {
            try {
                FightingIceGrpcServer server = new FightingIceGrpcServer();
                server.start(); // サーバー開始
                this.serverStarted = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return 1;
    }

    @Override
    public void getInformation(FrameData fb, boolean isControl) {
        if (fb == null || fb.getCharacter(true) == null || fb.getCharacter(false) == null) {
            return;
        }
        //System.out.println("==========" + String.valueOf(fb.getCharacter(isControl).getCenterX() - fb.getCharacter(!isControl).getCenterX()));
        FightingIceGrpcServer.currentDistance = String.valueOf(fb.getCharacter(isControl).getCenterX() - fb.getCharacter(!isControl).getCenterX());
        FightingIceGrpcServer.currentLightPositions = String.valueOf(fb.getCharacter(isControl).getHp());
        FightingIceGrpcServer.currentPlayerState = fb.getCharacter(this.playerNumber).getState().toString();
        if (this.commandCenter.getSkillFlag()) {
            FightingIceGrpcServer.isBusy ="True";
        }
        else {
            FightingIceGrpcServer.isBusy = "False";
        }

        commandCenter.setFrameData(fb, this.playerNumber);
    }

    @Override
    public void processing() {
        String rawCommand = FightingIceGrpcServer.latestCommand;
        System.err.println(rawCommand);
        if (rawCommand.contains("#")) {
            System.out.println(rawCommand);
            String[] axis = rawCommand.split("_");
            axis[0] = axis[1];
            axis[1] = axis[2];
            //System.out.println((float)Float.parseFloat(axis[0]));
            NewSystemVeil.setCoordinates((float)Float.parseFloat(axis[0]), (float)Float.parseFloat(axis[1]));
        }
        if (!"NEUTRAL".equals(rawCommand)) {
            //System.err.println("DEBUG: Busy=" + commandCenter.getSkillFlag() + ", Cmd=" + rawCommand+", skillflag"+commandCenter.getSkillFlag());
        }
        // コマンドが入力済みの場合は、コマンドの内容を実行します。
        if (this.commandCenter.getSkillFlag()) {
            this.inputKey = commandCenter.getSkillKey();

            return;
        }
        if(!commandCenter.getSkillFlag()) {
            inputKey.empty();
            //commandCenter.skillCancel();

            String command = FightingIceGrpcServer.latestCommand;

            if (command != null && !command.equals("NEUTRAL")) {
                commandCenter.commandCall(command);
                this.inputKey = this.commandCenter.getSkillKey();
                //System.out.println(commandCenter.getSkillKey().toString());
            }
        }
    }

    @Override
	public Key input() {
		return this.inputKey;
	}

    // その他必須メソッド（空でOK）
    @Override public void close() {}
    @Override public void roundEnd(int p1Hp, int p2Hp, int frames) {}
}
