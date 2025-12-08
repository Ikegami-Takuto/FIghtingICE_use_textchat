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
