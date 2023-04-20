package com.eluolang.common.message;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import java.util.List;
import java.util.Properties;

/**
 * 邮箱发送
 *
 * @author suziwei
 * @date 2020/8/31
 */
public class MailUtil
{

    /**
     * 邮件服务器主机地址
     */
    private String host;
    /**
     * 账号
     */
    private String account;
    /**
     * 秘钥
     */
    private String password;

    public MailUtil(String host , String account , String password)
    {
        this.host = host;
        this.account = account;
        this.password = password;
    }

    /**
     * 群发（内容相同）
     *
     * @param toUserList   发送邮件给谁
     * @param title    邮件的标题
     * @param emailMsg 邮件信息
     */
    public void sendMails(List<String> toUserList, String title, String emailMsg) throws MessagingException
    {
        for (int i = 0; i < toUserList.size(); i++)
        {
            sendMail(toUserList.get(i), title, emailMsg);
        }
    }



    /**
     * 群发（内容不同）
     *
     * @param toUserList   发送邮件给谁
     * @param titleList    邮件的标题
     * @param emailMsgList 邮件信息
     */
    public void sendMails(List<String> toUserList, List<String> titleList, List<String> emailMsgList) throws MessagingException
    {
        for (int i = 0; i < toUserList.size(); i++)
        {
            sendMail(toUserList.get(i), titleList.get(i), emailMsgList.get(i));
        }
    }

    /**
     * 单发
     *
     * @param toUser   发送邮件给谁
     * @param title    邮件的标题
     * @param emailMsg 邮件信息
     */
    public void sendMail(String toUser, String title, String emailMsg) throws MessagingException 
    {
        Properties props = new Properties();
        //设置发送的协议
        props.setProperty("mail.transport.protocol", "SMTP");
        //设置发送邮件的服务器
        props.setProperty("mail.host", host);
        //指定验证为true
        props.setProperty("mail.smtp.auth", "true");

        // 创建验证器
        Authenticator auth = new Authenticator() 
        {
            @Override
            public PasswordAuthentication getPasswordAuthentication()
            {
                //设置发送人的帐号和秘钥
                return new PasswordAuthentication(account, password);
            }
        };

        Session session = Session.getInstance(props, auth);

        Message message = new MimeMessage(session);
        //设置发送者
        message.setFrom(new InternetAddress(account));
        //设置发送方式与接收者
        message.setRecipient(RecipientType.TO, new InternetAddress(toUser));
        //设置邮件标题
        message.setSubject(title);
        //设置邮件内容
        message.setContent(emailMsg, "text/html;charset=utf-8");

        Transport.send(message);
    }
}