package com.eluolang.module.socket.server.config;


import com.eluolang.common.core.hardware.ErrorFeedback;
import com.eluolang.common.core.util.NumberUtils;
import com.eluolang.common.core.util.StringUtils;
import com.eluolang.module.socket.server.util.Constant;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 解码器
 *
 * @author renzhixing
 */
@Component
public class NewDeviceDecoder extends ByteToMessageDecoder {

    private static Logger logger = LoggerFactory.getLogger(NewDeviceDecoder.class.getName());

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        try {
            logger.info("decoder字节:" + byteBuf.readableBytes());
            //如果读的字节数小于等于8,则不处理+
            if (byteBuf.readableBytes() <= Constant.BASICS_LENGTH) {
                byteBuf.resetReaderIndex();
                return;
            }
            byteBuf.markReaderIndex();
            //读取包头数据
            byte[] head = new byte[4];
            byteBuf.readBytes(head);
            //将数据包进行解析,判断数据包包头格式是否正确
            if (!Arrays.equals(Constant.HEAD, head)) {
                logger.error("协议头格式不正确");
                byteBuf.resetReaderIndex();
                return;
            }
            //读取控制命令
            short cmd = byteBuf.readShort();
            //如果cmd控制命令不在指定范围内,则不进行处理
            if (!NumberUtils.rangeInDefined(cmd, 0, 300)) {
                logger.error("协议控制命令cmd不在指定范围");
                byteBuf.resetReaderIndex();
                return;
            }
            //读取data长度信息
            short length = byteBuf.readShort();
            //如果读取到的消息长度小于发送过来的消息长度,则resetReadIndex,这个陪着markReaderIndex使用的,把readIndex重置到mark的地方
//            int packageLength = length + 2;
//            if (byteBuf.readableBytes() < packageLength) {
//                byteBuf.resetReaderIndex();
//                return;
//            }
            //读取data数据
            byte[] data = new byte[length];
            byteBuf.readBytes(data);
            //如果读出来的data长度和指定长度不一致,不进行处理
            if (data.length != length) {
                logger.error("协议发送的长度信息和指定长不一致");
                byteBuf.resetReaderIndex();
                return;
            }
//            short sumCheck = byteBuf.readShort();
//            System.out.println(sumCheck + "===" + checkSum(head, cmd, length, data) + "----cmd===" + cmd);
//            //将读出的数据,进行校验和是否正确
//            if (sumCheck != checkSum(head, cmd, length, data)) {
//                logger.error("协议校验和错误");
//                return;
//            }
            NewDeviceProtocol deviceProtocol = new NewDeviceProtocol(head, cmd, length, data);
            list.add(deviceProtocol);
        } catch (Exception e) {
            logger.error("Device decoder error");
            byteBuf.resetReaderIndex();
            e.printStackTrace();
        }
    }

    /**
     * 计算校验和
     *
     * @param head
     * @param cmd
     * @param length
     * @param data
     * @return
     */
    private Short checkSum(byte[] head, short cmd, short length, byte[] data) {
        short sum = 0;
        byte[] b1 = StringUtils.byteMerger(head, StringUtils.shortToByte(cmd));
        byte[] b2 = StringUtils.byteMerger(StringUtils.shortToByte(length), data);
        byte[] b3 = StringUtils.byteMerger(b1, b2);
        sum = StringUtils.sumCheck(b3, b3.length);
//        sum += StringUtils.sumCheck(head,head.length);
//        sum += StringUtils.sumCheck(StringUtils.shortToByte(cmd),StringUtils.shortToByte(cmd).length);
//        sum += StringUtils.sumCheck(StringUtils.shortToByte(length),StringUtils.shortToByte(length).length);
//        sum += StringUtils.sumCheck(data,data.length);
        // System.out.println("服务端计算检验和"+sum);
        return sum;
    }


    /**
     * 解码器发现无法识别的包,给出异常反馈响应给对方
     * @param channelHandlerContext
     * @param cmd
     * @param errorIdent
     * @param errorCmd
     * @param error
     */
    private void errorFeedback(ChannelHandlerContext channelHandlerContext, short cmd, short errorIdent, short errorCmd, int error) {
        ErrorFeedback errorFeedback = new ErrorFeedback();
        errorFeedback.setCmd(cmd);
        errorFeedback.setErr_cmd(errorCmd);
        errorFeedback.setErr_ident(errorIdent);
        errorFeedback.setError(error);
        channelHandlerContext.channel().writeAndFlush(errorFeedback);
    }

}
