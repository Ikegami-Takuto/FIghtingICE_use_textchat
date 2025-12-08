import socket
import re
import time

# --- 認証情報と接続先の設定 ---
# Twitch IRCサーバーの情報
class ConnectionTwich:
    ERVER = "irc.chat.twitch.tv"
    PORT = 6667

    # あなたのTwitchアカウント名
    NICK = "Twich_playgame" #Goのアカウント
    # Twitch OAuthトークン (必ず "oauth:" から始まる形式)
    PASS = "oauth:d0lox459q85poxbogyc8rsqyoaq4gr" 
    # コメントを取得したいチャンネル名 (小文字で '#' を付ける)
    CHANNEL = "#20251205iitt" 
    
    def __init__(self):
        pass
    # --- IRC接続 ---
    def connect_irc(self):
        s = self.Ssocket.socket()
        s.connect((self.SERVER, self.PORT))
        
        # 認証情報を送信
        s.send(f"PASS {self.PASS}\r\n".encode("utf-8"))
        s.send(f"NICK {self.NICK}\r\n".encode("utf-8"))
        
        # チャンネルに参加
        s.send(f"JOIN {self.CHANNEL}\r\n".encode("utf-8"))
        print(f"[{self.CHANNEL}] チャンネルに接続中...")
        
        return s

    # --- メインループ ---
    def get_comments(self):
        s = self.connect_irc()
        
        # 受信したデータをパースするための正規表現
        # 例: :user!user@user.tmi.twitch.tv PRIVMSG #channel :message content
        CHAT_REGEX = re.compile(r"^:\w+!\w+@\w+\.tmi\.twitch\.tv PRIVMSG #\w+ :(.+)$")
        try:
            # サーバーからデータを受信 (1024バイト)
            response = s.recv(1024).decode("utf-8")
        except socket.timeout:
            return None
        except ConnectionResetError:
            print("接続がリセットされました。再接続を試みます...")
            s.close()
            s = self.connect_irc()
            time.sleep(5)
            return None

        # サーバーからのPING要求に応答 (接続維持のため必須)
        if response.startswith("PING"):
            s.send("PONG :tmi.twitch.tv\r\n".encode("utf-8"))
            return None

        # 受信したデータを行ごとに処理
        lines = response.split('\r\n')
        for line in lines:
            if not line:
                continue

            # ユーザー名とメッセージを抽出
            if "PRIVMSG" in line:
                try:
                    # 例: :user!user@user.tmi.twitch.tv PRIVMSG #channel :message content
                    user_match = re.search(r"^:(\w+)!", line)
                    message_match = CHAT_REGEX.search(line)
                    
                    if user_match and message_match:
                        username = user_match.group(1)
                        content = message_match.group(1)
                        print(f"[{self.CHANNEL}] <{username}>: {content}")
                        return username, content
                        
                except Exception as e:
                    # パースエラーなどの例外処理
                    print(f"エラー: {e}, 行: {line}")
            
            # その他のIRCメッセージを表示 (デバッグ用)
            elif "001" in line: # 接続成功メッセージ
                print(f"[{self.CHANNEL}] 接続成功。")
            else:
                # print(f"IRC: {line}") # 必要に応じてコメント解除
                pass