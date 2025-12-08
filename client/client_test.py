import grpc
import time
import json
import os
from pathlib import Path
from llama_cpp import Llama 
import bitNetToFightingICE_pb2 as pb2
import bitNetToFightingICE_pb2_grpc as pb2_grpc
from get_comment_new import ConnectionTwich

# ==========================================
connectionTwich = ConnectionTwich()

# ==========================================
# ★設定
MODEL_PATH = str(Path(os.getcwd(), "qwen2.5-0.5b-instruct-q4_k_m.gguf"))

# ★デバッグモード切替
# True : キーボードで「疑似音声入力」を行うモード
# False: 音声入力あり
DEBUG_VOICE_MODE = False 

action_labels =[
    "FORWARD_WALK",
    "DASH",
    "BACK_STEP",
    "CROUCH",
    "JUMP",
    "FOR_JUMP",
    "BACK_JUMP",
    "STAND_GUARD",
    "CROUCH_GUARD",
    "AIR_GUARD",
    "THROW_A",
    "THROW_B",
    "STAND_A",
    "STAND_B",
    "CROUCH_A",
    "CROUCH_B",
    "AIR_A",
    "AIR_B",
    "AIR_DA",
    "AIR_DB",
    "STAND_FA",
    "STAND_FB",
    "CROUCH_FA",
    "CROUCH_FB",
    "AIR_FA",
    "AIR_FB",
    "AIR_UA",
    "AIR_UB",
    "STAND_D_DF_FA",
    "STAND_D_DF_FB",
    "STAND_F_D_DFA",
    "STAND_F_D_DFB",
    "STAND_D_DB_BA",
    "STAND_D_DB_BB",
    "AIR_D_DF_FA",
    "AIR_D_DF_FB",
    "AIR_F_D_DFA",
    "AIR_F_D_DFB",
    "AIR_D_DB_BA",
    "AIR_D_DB_BB",
    "STAND_D_DF_FC"
]

check_list = []
for label in action_labels:
    check_list.append(label.lower().replace("_", ""))
print(check_list)
# ==========================================

print(f"Loading Model from: {MODEL_PATH}")
llm = Llama(
    model_path=MODEL_PATH,
    n_gpu_layers=-1,
    n_ctx=512,
    verbose=False
)
prompt = ""
with open("prompt.txt", "r") as p:
    prompt = p.read()
output = llm(prompt, max_tokens=100, stop=["]", "</s>"], echo=False)

def run():
    print("Connecting to Server (localhost:50051)...")
    with grpc.insecure_channel('localhost:50051') as channel:
        stub = pb2_grpc.FightingIceServerStub(channel)
        next_action = "NEUTRAL"
        
        # 疑似音声命令を保持する変数
        current_voice_order = "Defeat the enemy!" 

        while True:
            try:
                # 1. 現状確認
                request = pb2.OutputOfBitNet(OutputOfBitNet=next_action)
                response = stub.GameStream(request)
                llm.reset()
                # 2. 忙しい時はスキップ（入力もしない）
                if  "True" in response.isbusy:
                    next_action = "NEUTRAL"
                    #time.sleep(0.01)
                    continue

                # 3. ★入力パート：デバッグモードならキーボード入力を待つ
                if DEBUG_VOICE_MODE:
                    print("\n--- [VOICE DEBUG] ---")
                    # input() は入力を終えるまでプログラムを一時停止します（ゲームは止まります）
                    user_input = input("命令を入力 (Enterで前回と同じ / 'q'で終了): ")
                    
                    if user_input.lower() == 'q':
                        break
                    elif user_input != "":
                        current_voice_order = user_input
                    
                    print(f"Order: '{current_voice_order}'")
                else:
                    current_voice_order = connectionTwich.get_comments_and_calculate()


                # 4. プロンプト作成
                # 「音声命令」と「ゲーム状況」の両方を渡して判断させる
                situation = f"Distance is {response.distance}. State is {response.playerState}."
                
                if current_voice_order is None:
                    continue
                if "#" in current_voice_order:
                    next_action = current_voice_order
                    print(next_action)
                elif current_voice_order.replace(" ", "") in check_list:
                    next_action = current_voice_order.replace(" ", "")
                    next_action = convert_voice_to_command(next_action)
                else:
                    # 5. 推論
                    output = llm(prompt=prompt+"Input:"+current_voice_order+",Dist:"+response.distance+",Output:", max_tokens=100, echo=False, temperature=0)
                    text = output['choices'][0]['text'].strip()
                    full_text = '["' + text
                    print(f"★ AI Decided: {full_text}")
                    next_action = convert_voice_to_command(text)

                    

                print(f"-> Send: {next_action}")

            except grpc.RpcError:
                print("Waiting for server...")
                time.sleep(0.1)
            except KeyboardInterrupt:
                break
            except Exception as e:
                print(f"Error: {e}")
def convert_voice_to_command(text):
    # 6. コマンド決定ロジック
    text_lower = text.lower()
    
    if "stand_a" in text_lower or "punch" in text_lower:
        next_action = "STAND_A"
    elif "stand_b" in text_lower or "kick" in text_lower:
        next_action = "STAND_B"
    elif "crouch" in text_lower or "guard" in text_lower:
        next_action = "CROUCH" # ガードの意味合い
    elif "dash" in text_lower or "run" in text_lower:
        next_action = "DASH"
    elif "jump" in text_lower:
        next_action = "JUMP"
    elif "fire" in text_lower or "hadoken" in text_lower:
        next_action = "STAND_D" # もし波動拳があれば
    elif "go" in text_lower or "dash" in text_lower:
        next_action = "DASH"
    elif "walk" in text_lower:
        next_action = "FORWARD_WALK"
    elif "back" in text_lower or "gard" in text:
        next_action = "BACK_STEP"
    else:
        # 分からない場合は、とりあえず近づく
        next_action = "CROUCH_FB"
    return next_action
    

if __name__ == '__main__':
    run()