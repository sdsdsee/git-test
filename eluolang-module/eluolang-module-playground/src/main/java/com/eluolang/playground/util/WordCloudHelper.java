package com.eluolang.playground.util;

import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.word.cloud.support.background.Backgrounds;
import com.github.houbb.word.cloud.support.background.IBackground;

import java.util.List;

/**
 * @author zjl重写了github上word_cloud的增加了两种分词类型一种逗号隔开，一种用每个词语用不同list装
 * @since 1.0.0
 */
public final class WordCloudHelper {

    private WordCloudHelper() {
    }

    /**
     * 云图
     *
     * @param text                文本
     * @param outPath             输出路径
     * @param backgroundImagePath 背景图路径
     * @since 1.0.0
     */
    public static void wordCloud(final String text,
                                 final String outPath,
                                 final String backgroundImagePath) {
        IBackground background = Backgrounds.none();
        if (StringUtil.isNotEmptyTrim(backgroundImagePath)) {
            background = Backgrounds.image(backgroundImagePath);
        }

        WordCloudBs.newInstance()
                .text(text)
                .outPath(outPath)
                .background(background)
                .wordCloud();
    }

    /**
     * 云图
     *
     * @param textList            词语集合
     * @param outPath             输出路径
     * @param backgroundImagePath 背景图路径
     * @since 1.0.0
     */
    public static void wordCloudList(final List<String> textList,
                                     final String outPath,
                                     final String backgroundImagePath) {
        IBackground background = Backgrounds.none();
        if (StringUtil.isNotEmptyTrim(backgroundImagePath)) {
            background = Backgrounds.image(backgroundImagePath);
        }

        WordCloudBs.newInstance()
//                .text(text)
                .outPath(outPath)
                .background(background)
                .wordCloudList(textList);
    }

    /**
     * 云图
     *
     * @param text                词语逗号隔开
     * @param outPath             输出路径
     * @param backgroundImagePath 背景图路径
     * @since 1.0.0
     */
    public static void wordCloudComma(final String text,
                                      final String outPath,
                                      final String backgroundImagePath) {
        IBackground background = Backgrounds.none();
        if (StringUtil.isNotEmptyTrim(backgroundImagePath)) {
            background = Backgrounds.image(backgroundImagePath);
        }

        WordCloudBs.newInstance()
//                .text(text)
                .outPath(outPath)
                .background(background)
                .wordCloudComma(text);
    }

    /**
     * 云图
     *
     * @param text    文本
     * @param outPath 输出路径
     * @since 1.0.0
     */
    public static void wordCloud(final String text, final String outPath) {
        String imagePath = null;
        wordCloud(text, outPath, imagePath);
    }

    /**
     * 云图
     *
     * @param text 文本
     * @since 1.0.0
     */
    public static void wordCloud(final String text) {
        wordCloud(text, "out.png");
    }

}
