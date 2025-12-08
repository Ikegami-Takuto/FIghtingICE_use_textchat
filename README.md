# FIghtingICE_use_textchat

## 環境構築
はじめにgit cloneします.
```bash
git clone git@github.com:Ikegami-Takuto/FIghtingICE_use_textchat.git
```
次に, FightingICEの環境構築をしてください.

### pythonの環境構築をします.
もう一つterminalを起動して, 仮想環境を作成します. 
```bash
cd client
```
```bash
python -m venv [仮想環境の名前]
```
仮想環境の有効化
```bash
[仮想環境の名前]/bin/activate
```
必要なモジュールのインストール
```
pip install -r requirements.txt
```
### install LLM(Qwen2.5-0.5B)
LLMを"client"ディレクトリ内にインストールしてください
```bash
cd client
```
```bash
curl -L -o qwen2.5-0.5b-instruct-q4_k_m.gguf https://huggingface.co/Qwen/Qwen2.5-0.5B-Instruct-GGUF/resolve/main/qwen2.5-0.5b-instruct-q4_k_m.gguf
```
URL: https://huggingface.co/Qwen/Qwen2.5-0.5B
環境構築は完了です. 

## Compile FightingICE
```bash
mkdir bin
```
```bash
javac -cp "./lib/*:./lib/lwjgl/*:./lib/grpc/*" -d bin src/**/*.java|jar cvf FightingICE.jar -C bin .
```
```bash
jar cvf FightingICE.jar -C bin . 
```

## Compile AI
```bash
mkdir myai_bin
```
```bash
javac -d myai_bin -cp "FightingICE.jar:lib/*" src/gamescene/NewSystemVeil.java src/serverForBitNet/*.java src/BitNetAI.java
```
```bash
jar cvf data/ai/BitNetAI.jar -C myai_bin .
```


## 実行方法（excution）
FightingICEを起動してAIの選択時にBitNetAIを選択します. 
そのあと, 新しいターミナルでこのリポジトリのclientまで移動します. その後, client_text.pyを実行してください.

Excution of FightingICE : please check official. https://github.com/TeamFightingICE/FightingICE
If you don't know how to contoroll FightingICE, Please ask me.

Excute new terminal.
```bash
cd client
```
```bash
python client_test.py
```

