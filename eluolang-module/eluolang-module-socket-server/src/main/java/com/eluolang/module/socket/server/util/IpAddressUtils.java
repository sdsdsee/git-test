package com.eluolang.module.socket.server.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * 获取ip地址工具类
 */
public class IpAddressUtils {

    private static Logger logger = LoggerFactory.getLogger(IpAddressUtils.class.getName());

    /**
     * Get host IP address
     *
     * @return IP Address
     */
    public static InetAddress getIpAddress() {
        InetAddress addr = null;
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                //System.out.println(netInterface.getName());
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress ipTmp = (InetAddress) addresses.nextElement();
                    if (ipTmp != null && ipTmp instanceof Inet4Address
                            && ipTmp.isSiteLocalAddress()
                            && !ipTmp.isLoopbackAddress()
                            && ipTmp.getHostAddress().indexOf(":") == -1) {
                        addr = ipTmp;
                    }
                }
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }
        return addr;
    }

}
