package com.eluolang.physical.util;

import com.alibaba.nacos.client.identify.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterOutputStream;

public class ZlibDecompressionUtil {
    public static String compressData(String data) {

        ByteArrayOutputStream bos;

        DeflaterOutputStream zos;

        try {

            bos = new ByteArrayOutputStream();

            zos = new DeflaterOutputStream(bos);

            zos.write(data.getBytes());

            zos.close();

            return new String(Base64.encodeBase64(bos.toByteArray()));

        } catch (IOException e) {

            e.printStackTrace();

        }

        return null;

    }

    /**

     * zlib解压+base64

     */

    public static String decompressData(String encdata) {

        try {

            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            InflaterOutputStream zos = new InflaterOutputStream(bos);

            zos.write(Base64.decodeBase64(encdata.getBytes()));

            zos.close();

            return new String(bos.toByteArray());

        } catch (Exception ex) {

            ex.printStackTrace();

        }

        return null;

    }
}
