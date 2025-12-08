rm -r myai_bin
mkdir myai_bin

javac -d myai_bin -cp "FightingICE.jar:lib/*" src/gamescene/NewSystemVeil.java src/serverForBitNet/*.java src/BitNetAI.java
jar cvf data/ai/BitNetAI.jar -C myai_bin .
               