package net.handlers;

import constants.AccountHelper;
import client.Client;
import constants.ChatHelper;
import net.ByteBufReader;
import net.packet.ChatPacket;
import net.packet.InitializePacket;
import net.packet.LoginPacket;
import net.packet.opcode.COutOpcode;

import java.sql.SQLException;

public class CommonHandler {
    public static void handle(Client c, COutOpcode opcode, ByteBufReader b) throws InterruptedException, SQLException {
        switch(opcode) {
            case HandShakeRequest:
                c.writeAndFlush(InitializePacket.HandshakeResult());
                break;
            case LOGIN_REQUEST: {
                var id = b.readAsciiString();
                var pw = b.readAsciiString();
                var valid = AccountHelper.loginAccountCheck(id, pw);
                var uid = AccountHelper.getAccountUID(id);
                c.setAccountId(id);
                c.writeAndFlush(LoginPacket.loginResult(valid, id, uid));
            }
            break;
            case REGISTRATION_REQUEST: {
                var id = b.readAsciiString();
                var pw = b.readAsciiString();
                var isExist = AccountHelper.isExistAccount(id);
                if(!isExist) {
                    AccountHelper.createNewAccount(id, pw);
                    c.writeAndFlush(LoginPacket.registrationResult(true));
                } else {
                    c.writeAndFlush(LoginPacket.registrationResult(false));
                }
            }
            break;

            case FriendsListRequest:{
                var uid = b.readInt(); // uid compare check
                c.writeAndFlush(ChatPacket.SendFriendList(uid));
            }
            break;

            case ChatRoomRequest: {
                var toId = b.readInt();
                var fromId = b.readInt(); //나중에 비교처리안하면 패킷 하이재킹에 너무 취약해짐 조심할 것
                c.writeAndFlush(ChatPacket.SendChatRoomInfo(toId, fromId));
            }
            break;

            case SendChat: {
                var toId = b.readInt();
                var fromId = b.readInt(); //나중에 비교처리안하면 패킷 하이재킹에 너무 취약해짐 조심할 것
                var msg = b.readAsciiString();
                ChatHelper.WriteChatToDB(toId, fromId, msg);
                c.writeAndFlush(ChatPacket.SendChat(toId, fromId, msg));
            }
            break;
        }
    }
}
