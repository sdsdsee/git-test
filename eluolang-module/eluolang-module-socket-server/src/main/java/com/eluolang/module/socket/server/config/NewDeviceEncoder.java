package com.eluolang.module.socket.server.config;

import com.eluolang.common.core.constant.HardwareStatus;
import com.eluolang.common.core.util.StringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.math.BigInteger;
import java.util.Locale;

/**
 * 编码器
 *
 * @author renzhixing
 * BASE_LENGTH = 2+2+2+2;
 */
@Component
public class NewDeviceEncoder extends MessageToByteEncoder<NewDeviceProtocol> {

    private static Logger logger = LoggerFactory.getLogger(NewDeviceEncoder.class.getName());

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, NewDeviceProtocol deviceProtocol, ByteBuf byteBuf) throws Exception {
        try {
            if (deviceProtocol.getCmd() != HardwareStatus.DOWNLOAD_FILE_INFO) {
                logger.info("DeviceEncoder---> data Not Null" + StringUtils.byteToString(deviceProtocol.getData()));
            } else {
                byte[] b1 = StringUtils.byteMerger(deviceProtocol.getHead(), StringUtils.shortToByte(deviceProtocol.getCmd()));
                byte[] b2 = StringUtils.byteMerger(StringUtils.shortToByte(deviceProtocol.getLength()), deviceProtocol.getData());
                byte[] b3 = StringUtils.byteMerger(b1, b2);
//                byte[] b4 = StringUtils.byteMerger(b3, StringUtils.shortToByte(deviceProtocol.getSumCheck()));
                short sumCheck = StringUtils.sumCheck(b3, b3.length);
                //write("/home/file/" + deviceProtocol.getSumCheck() + ".bin", StringUtils.subBytes(b4, 0, b4.length));
                System.out.println(StringUtils.bytesToHexString(b3) + "\n");
            }
            byteBuf.writeBytes(deviceProtocol.getHead());
            byteBuf.writeShort(deviceProtocol.getCmd());
            byteBuf.writeShort(deviceProtocol.getLength());
            byteBuf.writeBytes(deviceProtocol.getData());
//            byteBuf.writeShort(deviceProtocol.getSumCheck());
//            System.out.println("deviceProtocol-sumCheck--->" + deviceProtocol.getSumCheck());
        } catch (Exception e) {
            logger.error("DeviceEncoder error!");
            e.printStackTrace();
        }
    }

    private static String binary(byte[] bytes) {
        return new BigInteger(1, bytes).toString(2);
    }


    /**
     * byte[]数组转换为16进制的字符串
     *
     * @param data 要转换的字节数组
     * @return 转换后的结果
     */
    public static final String byteArrayToHexString(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        for (byte b : data) {
            int v = b & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.getDefault());
    }

    /**
     * 字节数组转成16进制表示格式的字符串
     *
     * @param byteArray 要转换的字节数组
     * @return 16进制表示格式的字符串
     **/
    public static String toHexString(byte[] byteArray) {
        if (byteArray == null || byteArray.length < 1)
            throw new IllegalArgumentException("this byteArray must not be null or empty");

        final StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < byteArray.length; i++) {
            if ((byteArray[i] & 0xff) < 0x10)//0~F前面不零
                hexString.append("0");
            hexString.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return hexString.toString().toLowerCase();
    }

    public static void write(String fileName, byte[] data) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            fileOutputStream.write(data);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
