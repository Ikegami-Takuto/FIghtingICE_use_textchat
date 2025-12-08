import socket
import re
import time
from collections import deque
import sys

class ConnectionTwich:
    # ==========================================================
    # --- ユーザー設定 ---
    # ==========================================================
    SERVER = "irc.chat.twitch.tv"
    PORT = 6667
    NICK = "Twich_playgame" 
    # 【注意】実際のコードではここに正しいトークンを入れてください
    PASS = "oauth:d0lox459q85poxbogyc8rsqyoaq4gr" 
    CHANNEL = "#20251205iitt" 

    # --- 移動平均の設定 ---
    WINDOW_SIZE_SEC = 20.0
    OUTPUT_INTERVAL_SEC = 2 
    # ==========================================================

    # --- グローバル変数 ---
    coordinate_history = deque()
    s = None
    last_output_time = time.time()

    CHAT_REGEX = re.compile(r"^:(\w+)!\w+@\w+\.tmi\.twitch\.tv PRIVMSG #\w+ :(.+)$")
    COORDINATE_REGEX = re.compile(r"x=(-?\d+\.?\d*),y=(-?\d+\.?\d*)")
    
    # 【修正1】 __inin__ を __init__ に修正
    def __init__(self):
        self.s = self.connect_irc()

    def connect_irc(self):
        """Twitch IRCサーバーに接続し、認証・チャンネル参加を行う"""
        try:
            new_socket = socket.socket()
            # 接続時は少し長めに待つ（ネットワーク遅延対策）
            new_socket.settimeout(5.0) 
            new_socket.connect((self.SERVER, self.PORT))
            
            # 接続できたら、受信待ち受け用の短いタイムアウトに設定変更
            new_socket.settimeout(1.0)
            
            new_socket.send(f"PASS {self.PASS}\r\n".encode("utf-8"))
            new_socket.send(f"NICK {self.NICK}\r\n".encode("utf-8"))
            new_socket.send(f"JOIN {self.CHANNEL}\r\n".encode("utf-8"))
            
            print(f"[{self.CHANNEL}] チャンネルに接続成功。")
            return new_socket
        except Exception as e:
            print(f"接続エラー: {e}")
            return None

    def update_and_calculate_ma(self):
        """移動平均を計算する"""
        current_time = time.time()

        while self.coordinate_history and self.coordinate_history[0]['t'] < current_time - self.WINDOW_SIZE_SEC:
            self.coordinate_history.popleft()

        if current_time - self.last_output_time < self.OUTPUT_INTERVAL_SEC:
            return None

        if not self.coordinate_history:
            self.last_output_time = current_time
            return None
            
        count = len(self.coordinate_history)
        sum_x = sum(d['x'] for d in self.coordinate_history)
        sum_y = sum(d['y'] for d in self.coordinate_history)
        
        avg_x = sum_x / count
        avg_y = sum_y / count
        
        # 結果を出力して返す
        result_str = "#"+"_" + f"{avg_x:.4f}" + "_" + f"{avg_y:.4f}"
        print(result_str)
        
        self.last_output_time = current_time
        return result_str

    def get_comments_and_calculate(self):
        # 【修正2】接続がない場合は即座に再接続を試み、ダメならリターンする
        if self.s is None:
            print("再接続を試みます...")
            self.s = self.connect_irc()
            if self.s is None:
                return None # 接続失敗時はここで終了

        response = None # 【修正3】変数を初期化しておく

        try:
            response = self.s.recv(1024).decode("utf-8")
        except socket.timeout:
            # タイムアウトは正常動作（データが来ていないだけ）なのでMA計算へ
            pass
        except (ConnectionResetError, OSError):
            print("接続が切断されました。")
            self.s.close()
            self.s = None
            return None
        except Exception as e:
            print(f"予期せぬ受信エラー: {e}", file=sys.stderr)
            return None

        # 受信データがない（タイムアウトなど）場合
        if not response:
            return self.update_and_calculate_ma()

        # PING応答処理
        if response.startswith("PING"):
            try:
                self.s.send("PONG :tmi.twitch.tv\r\n".encode("utf-8"))
            except:
                pass
            # PINGの時もMA計算は更新チェックする
            return self.update_and_calculate_ma()

        # データ処理
        lines = response.split('\r\n')
        for line in lines:
            if not line:
                continue

            if "PRIVMSG" in line:
                message_match = self.CHAT_REGEX.search(line)
                if message_match:
                    username = message_match.group(1)
                    content = message_match.group(2).strip()
                    
                    coord_match = self.COORDINATE_REGEX.search(content)
                    if coord_match:
                        try:
                            x_coord = float(coord_match.group(1))
                            y_coord = float(coord_match.group(2))
                            self.coordinate_history.append({'x': x_coord, 'y': y_coord, 't': time.time()})
                        except ValueError:
                            pass
                    else:
                        lowercased_content = content.lower()
                        print(f"[CHAT] <{username}>: {lowercased_content}")
                        return lowercased_content

        return self.update_and_calculate_ma()

if __name__ == "__main__":
    # テスト実行用ループ
    twitch = ConnectionTwich()
    try:
        while True:
            twitch.get_comments_and_calculate()
            # ループが早すぎるとCPUを使うので少し待つ（timeoutがあるので必須ではないが安全策）
            time.sleep(0.01) 
    except KeyboardInterrupt:
        print("終了します")