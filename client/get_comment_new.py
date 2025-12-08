import socket
import re
import time
from collections import deque
import sys


class ConnectionTwich:
    # ==========================================================
    # --- ユーザー設定 ---
    # 接続情報を必ずあなたのTwitchアカウントとチャンネルに合わせて変更してください
    # ==========================================================
    SERVER = "irc.chat.twitch.tv"
    PORT = 6667
    NICK = "Twich_playgame" # あなたのTwitchアカウント名
    PASS = "oauth:d0lox459q85poxbogyc8rsqyoaq4gr" # あなたのOAuthトークン (必ず "oauth:" から始まる)
    CHANNEL = "#daylyplate"  # コメントを取得したいチャンネル名 (小文字で '#' を付ける)

    # --- 移動平均の設定 ---
    WINDOW_SIZE_SEC = 20.0 # 移動平均を計算する期間（秒）
    OUTPUT_INTERVAL_SEC = 2 # 移動平均を出力する間隔（秒）
    # ==========================================================


    # --- グローバル変数 ---
    # データ構造: {'x': float, 'y': float, 't': float(timestamp)} の辞書を保持
    coordinate_history = deque()
    s = None # ソケット接続
    last_output_time = time.time() # 最後の出力時刻

    # --- 正規表現 ---
    # Twitch IRCメッセージからユーザー名とコメント本文を抽出
    CHAT_REGEX = re.compile(r"^:(\w+)!\w+@\w+\.tmi\.twitch\.tv PRIVMSG #\w+ :(.+)$")
    # コメント本文から座標を抽出（負の数も対応）
    COORDINATE_REGEX = re.compile(r"x=(-?\d+\.?\d*),y=(-?\d+\.?\d*)")


    def connect_irc(self):
        """Twitch IRCサーバーに接続し、認証・チャンネル参加を行う"""
        try:
            self.s = socket.socket()
            # 接続のタイムアウトを設定 (接続維持チェックと移動平均計算を確実に行うため)
            s.settimeout(1.0) 
            s.connect((self.SERVER, self.PORT))
            
            # 認証情報を送信
            s.send(f"PASS {self.PASS}\r\n".encode("utf-8"))
            s.send(f"NICK {self.NICK}\r\n".encode("utf-8"))
            
            # チャンネルに参加
            s.send(f"JOIN {self.CHANNEL}\r\n".encode("utf-8"))
            print(f"[{self.CHANNEL}] チャンネルに接続中...")
            return s
        except Exception as e:
            print(f"接続エラー: {e}")
            time.sleep(5)
            return None

    def update_and_calculate_ma(self):
        """
        1. ウィンドウ外の古いデータを削除する
        2. 現在の履歴に基づき移動平均を計算し、必要であればご要望の形式で出力する
        """
        global last_output_time
        current_time = time.time()

        # 1. n秒前の古いデータを削除
        # 現在時刻 - WINDOW_SIZE_SEC よりも古いデータは捨てる
        while self.coordinate_history and self.coordinate_history[0]['t'] < current_time - self.WINDOW_SIZE_SEC:
            self.coordinate_history.popleft()

        # 移動平均の出力間隔チェック
        if current_time - last_output_time < self.OUTPUT_INTERVAL_SEC:
            return

        # 2. 移動平均の計算
        if not self.coordinate_history:
            # データがない場合は、出力しない
            # 必要であれば、print(f"[@ N/A N/A]") のような出力をここで追加できます
            # 現状では、データがない場合は沈黙します
            last_output_time = current_time
            return
            
        count = len(self.coordinate_history)
        sum_x = sum(d['x'] for d in self.coordinate_history)
        sum_y = sum(d['y'] for d in self.coordinate_history)
        
        avg_x = sum_x / count
        avg_y = sum_y / count
        
        # 3. 結果の出力 (ご要望の形式: [@ x y])
        print(f"[@ {avg_x:.4f} {avg_y:.4f}]")
        
        # (デバッグ用: 取得データ数も確認したい場合)
        # print(f"[@ {avg_x:.4f} {avg_y:.4f}] (Count: {count})")
        
        last_output_time = current_time
        return "# {avg_x:.4f} {avg_y:.4f}"


    def get_comments_and_calculate(self):
        global s
        s = self.connect_irc()
        
        if not s:
            print("IRC接続に失敗しました。")
        try:
            # サーバーからデータを受信 (1024バイト)
            response = s.recv(1024).decode("utf-8")
        except socket.timeout:
            # タイムアウトしたら、接続維持とMA計算をトリガー
            self.update_and_calculate_ma() 
            pass
        except ConnectionResetError:
            print("接続がリセットされました。再接続を試みます...")
            s.close()
            s = self.connect_irc()
            time.sleep(5)
            pass
        except Exception as e:
            print(f"予期せぬ受信エラー: {e}", file=sys.stderr)
            time.sleep(1)
            pass

        # サーバーからのPING要求に応答 (接続維持のため必須)
        if response.startswith("PING"):
            s.send("PONG :tmi.twitch.tv\r\n".encode("utf-8"))
            pass

        # 受信したデータを行ごとに処理
        lines = response.split('\r\n')
        for line in lines:
            if not line:
                continue

            # チャットメッセージ (PRIVMSG) の処理
            if "PRIVMSG" in line:
                message_match = self.CHAT_REGEX.search(line)
                
                if message_match:
                    username = message_match.group(1)
                    content = message_match.group(2).strip()
                    
                    # コメント本文から座標を抽出
                    coord_match = self.COORDINATE_REGEX.search(content)
                    
                    if coord_match:
                        # === 1. 座標コメントの場合: 移動平均に追加 ===
                        try:
                            # xとyの数値を抽出し、floatに変換
                            x_coord = float(coord_match.group(1))
                            y_coord = float(coord_match.group(2))
                            current_time = time.time()
                            
                            # 履歴に追加
                            new_data = {'x': x_coord, 'y': y_coord, 't': current_time}
                            self.coordinate_history.append(new_data)
                            
                        except ValueError:
                            # 数値変換エラー
                            pass
                            
                    else:
                        # === 2. その他のコメントの場合: 小文字に変換して出力 ===
                        lowercased_content = content.lower()
                        print(f"[CHAT] <{username}>: {lowercased_content}")
                        return lowercased_content
                        

            # 接続成功メッセージ
            elif "001" in line:
                print(f"[{self.CHANNEL}] 接続成功。")

        # コメント受信後、移動平均の計算をトリガー
        return self.update_and_calculate_ma()

if __name__ == "__main__":
    try:
        ConnectionTwich.get_comments_and_calculate()
    except KeyboardInterrupt:
        print("\nプログラムを終了します。")
        if s:
            s.close()