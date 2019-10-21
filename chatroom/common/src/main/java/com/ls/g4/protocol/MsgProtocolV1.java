package com.ls.g4.protocol;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 消息协议版本1
 * head + body
 * version + msgType + dataLength + 预留   = head
 * 1字节       + 1字节       + 2字节             + 4字节 = 8字节
 */
public enum MsgProtocolV1 {
  /**
   * 版本1 的单例实例
   */
  protocol;

  public int getVersion() {
    return 1;
  }

  public DataPacket readDataPacketFromStream(InputStream in) throws IOException {
    DataInputStream din = new DataInputStream(in);
    DataPacket dataPacket = new DataPacket();
    din.readFully(dataPacket.head);
    dataPacket.setHead();
    dataPacket.data = new byte[dataPacket.getDataLength()];
    din.readFully(dataPacket.data);
    return dataPacket;
  }

  public class DataPacket {
    private final int version = 1;

    private int msgType;

    private final int headLength = 8;

    private int dataLength = 0;

    private byte[] data;

    private byte[] head;

    public DataPacket() {
      head = new byte[headLength];
      head[0] = (byte) version;
    }

    private void setHead() {
      msgType = head[1];
      dataLength = getDataLength();
    }

    public int getMsgType() {
      return msgType;
    }

    private int getDataLength() {
      dataLength = ((head[2] & 0xff) << 8) | (head[3] & 0xff);
      return dataLength;
    }

    public DataPacket setMsgType(int type) {
      msgType = type;
      head[1] = (byte) msgType;
      return this;
    }

    public DataPacket setData(byte[] data) {
      this.data = data;
      dataLength = data.length;
      head[2] = (byte) (dataLength >> 8);
      head[3] = (byte) dataLength;
      return this;
    }

    public byte[] getData() {
      return data;
    }

    public byte[] toBytes() {
      int length = headLength;
      if (data != null) {
        length += data.length;
      }
      byte[] packet = new byte[length];
      System.arraycopy(head, 0, packet, 0, headLength);
      if (data != null) {
        System.arraycopy(data, 0, packet, headLength, data.length);
      }
      return packet;
    }
  }

}
