# FIghtingICE_use_textchat

Now, you can only use text chat.

##Compile FightingICE
``
mkdir bin
``
``javac -cp "./lib/*:./lib/lwjgl/*:./lib/grpc/*" -d bin src/**/*.java|jar cvf FightingICE.jar -C bin .``
``jar cvf FightingICE.jar -C bin . ``

##Compile AI
``mkdir myai_bin``
``javac -d myai_bin -cp "FightingICE.jar:lib/*" src/gamescene/NewSystemVeil.java src/serverForBitNet/*.java src/BitNetAI.java``
``jar cvf data/ai/BitNetAI.jar -C myai_bin .``
