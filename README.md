# FIghtingICE_use_textchat

Now, you can only use text chat.

##Compile FightingICE
```bash
mkdir bin
```
```bash
javac -cp "./lib/*:./lib/lwjgl/*:./lib/grpc/*" -d bin src/**/*.java|jar cvf FightingICE.jar -C bin .
```
```bash
jar cvf FightingICE.jar -C bin . 
```

##Compile AI
```bash
mkdir myai_bin
```
```bash
javac -d myai_bin -cp "FightingICE.jar:lib/*" src/gamescene/NewSystemVeil.java src/serverForBitNet/*.java src/BitNetAI.java
```
```bash
jar cvf data/ai/BitNetAI.jar -C myai_bin .
```
#install LLM(Qwen2.5-0.5B)
```bash
cd client
```
```bash
cd model
```
```bash
curl -L -o qwen2.5-0.5b-instruct-q4_k_m.gguf https://huggingface.co/Qwen/Qwen2.5-0.5B-Instruct-GGUF/resolve/main/qwen2.5-0.5b-instruct-q4_k_m.gguf
```
URL: https://huggingface.co/Qwen/Qwen2.5-0.5B


excution
Excution of FightingICE : please check official. https://github.com/TeamFightingICE/FightingICE\\
If you don't know how to contoroll FightingICE, Please ask me.

Excute new terminal.
```bash
cd client
```
```bash
python client_test.py
```

