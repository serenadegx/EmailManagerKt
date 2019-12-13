package com.fantaike.emailmanagerkt.data.source.remote;

import android.os.Environment;
import android.util.Log;
import com.fantaike.emailmanager.data.Email;
import com.fantaike.emailmanager.data.source.EmailDataSource;
import com.fantaike.emailmanagerkt.data.Account;
import com.fantaike.emailmanagerkt.data.Attachment;
import com.fantaike.emailmanagerkt.data.FolderType;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.smtp.SMTPTransport;
import org.jetbrains.annotations.NotNull;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

public class EmailRemoteDataSource implements EmailDataSource {

    private static EmailRemoteDataSource INSTANCE;

    static boolean showStructure = true;
    static int level = 0;

    private EmailRemoteDataSource() {
    }

    public static EmailRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EmailRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getEmails(FolderType type, final Account account, final GetEmailsCallback callBack) {
        final List<Email> data = new ArrayList<>();
        Properties props = System.getProperties();
        props.put(account.getConfig().getReceiveHostKey(), account.getConfig().getReceiveHostValue());
        props.put(account.getConfig().getReceivePortKey(), account.getConfig().getReceivePortValue());
        props.put(account.getConfig().getReceiveEncryptKey(), account.getConfig().getReceiveEncryptValue());
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        account.getAccount(), account.getPwd());
            }
        });
        session.setDebug(true);
        Store store = null;
        IMAPFolder folder = null;
        try {
            store = session.getStore(account.getConfig().getReceiveProtocol());
            store.connect();
            Folder[] list = store.getDefaultFolder().list();
            for (Folder f : list) {
                Log.i("mango", "folderName:" + f.getName() + "URL_Name:" + f.getURLName());
            }
            switch (type) {
                case INBOX:
                    folder = (IMAPFolder) store.getFolder("INBOX");
                    break;
                case SENT:
                    folder = (IMAPFolder) store.getFolder("Sent Messages");
                    break;
                case DRAFTS:
                    folder = (IMAPFolder) store.getFolder("Drafts");
                    break;
                case DELETED:
                    folder = (IMAPFolder) store.getFolder("Deleted Messages");
                    break;
                default:
                    folder = (IMAPFolder) store.getFolder("INBOX");
                    break;
            }
            //网易163邮箱配置
            if (account.getConfigId() == 3) {
                final Map<String, String> clientParams = new HashMap<String, String>();
                clientParams.put("name", "my-imap");
                clientParams.put("version", "1.0");
                folder.doOptionalCommand("ID not supported",
                        p -> p.id(clientParams));
            }
            folder.open(Folder.READ_WRITE);
            Message[] messages = folder.getMessages();
            for (Message message : messages) {
                Email emailDetail = new Email();
                //仅支持imap
                emailDetail.setRead(message.getFlags().contains(Flags.Flag.SEEN));
                dumpPart(message, emailDetail);
                data.add(emailDetail);
            }
            callBack.onEmailsLoaded(data, type);
        } catch (NoSuchProviderException e) {
            callBack.onDataNotAvailable();
            e.printStackTrace();
        } catch (MessagingException e) {
            callBack.onDataNotAvailable();
            e.printStackTrace();
        } catch (Exception e) {
            callBack.onDataNotAvailable();
            e.printStackTrace();
        } finally {
            try {
                if (folder != null && folder.isOpen())
                    folder.close();
                if (store != null)
                    store.close();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void getEmailById(long id, @NotNull FolderType type, @NotNull Account account, @NotNull GetEmailCallback callback) {
        showStructure = true;
        Email data;
        Properties props = System.getProperties();
        props.put(account.getConfig().getReceiveHostKey(), account.getConfig().getReceiveHostValue());
        props.put(account.getConfig().getReceivePortKey(), account.getConfig().getReceivePortValue());
        props.put(account.getConfig().getReceiveEncryptKey(), account.getConfig().getReceiveEncryptValue());
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        account.getAccount(), account.getPwd());
            }
        });
//        session.setDebug(true);
        Store store = null;
        Folder folder = null;
        try {
            store = session.getStore(account.getConfig().getReceiveProtocol());
            store.connect();
            switch (type) {
                case INBOX:
                    folder = (IMAPFolder) store.getFolder("INBOX");
                    break;
                case SENT:
                    folder = (IMAPFolder) store.getFolder("Sent Messages");
                    break;
                case DRAFTS:
                    folder = (IMAPFolder) store.getFolder("Drafts");
                    break;
                case DELETED:
                    folder = (IMAPFolder) store.getFolder("Deleted Messages");
                    break;
                default:
                    folder = (IMAPFolder) store.getFolder("INBOX");
                    break;
            }
            folder.open(Folder.READ_ONLY);
            Message message = folder.getMessage((int) id);
            //标记已读
            message.setFlag(Flags.Flag.SEEN, true);
            data = new Email();
//            data.setAttachments(new ArrayList<Attachment>());
            dumpPart(message, data);
            showStructure = false;
            callback.onEmailLoaded(data);
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            callback.onDataNotAvailable();
        } catch (Exception e) {
            e.printStackTrace();
            callback.onDataNotAvailable();
        } finally {
            try {
                if (folder != null)
                    folder.close();
                if (store != null)
                    store.close();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void send(@NotNull Account account, @NotNull Email email, boolean saveSent, @NotNull Callback callback) {
        Properties props = System.getProperties();
        props.put(account.getConfig().getSendHostKey(), account.getConfig().getSendHostValue());
        props.put(account.getConfig().getSendPortKey(), account.getConfig().getSendPortValue());
        props.put(account.getConfig().getSendEncryptKey(), account.getConfig().getSendEncryptValue());
        props.put(account.getConfig().getAuthKey(), account.getConfig().getAuthValue());
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        account.getAccount(), account.getPwd());
            }
        });
        SMTPTransport t = null;
        try {
            Message msg = new MimeMessage(session);
            if (email.getFrom() != null) {
                try {
                    msg.setFrom(new InternetAddress(email.getFrom(), account.getPersonal()));
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email.getTo(), false));
            if (email.getCc() != null)
                //抄送人
                msg.setRecipients(Message.RecipientType.CC,
                        InternetAddress.parse(email.getCc(), false));
            if (email.getBcc() != null)
                //秘密抄送人
                msg.setRecipients(Message.RecipientType.BCC,
                        InternetAddress.parse(email.getBcc(), false));

            msg.setSubject(email.getSubject());

            MimeMultipart mp = new MimeMultipart();
            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setText(email.getContent());
            mp.addBodyPart(mbp1);
            if (email.getAttachments() != null && email.getAttachments().size() > 0) {
                for (Attachment detail1 : email.getAttachments()) {
                    MimeBodyPart mbp2 = new MimeBodyPart();
                    mbp2.attachFile(detail1.getPath());
                    mp.addBodyPart(mbp2);
                }
            }
            msg.setContent(mp);
            msg.setSentDate(new Date());
            t = (SMTPTransport) session.getTransport(account.getConfig().getSendProtocol());
            t.connect();
            t.sendMessage(msg, msg.getAllRecipients());
            if (saveSent) {
                save2Sent(account, msg);
            }
            callback.onSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            callback.onError();
        } finally {
            try {
                if (t != null)
                    t.close();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void reply(@NotNull Account account, @NotNull Email email, boolean saveSent, @NotNull Callback callback) {
        Properties props = System.getProperties();
        props.put(account.getConfig().getSendHostKey(), account.getConfig().getSendHostValue());
        props.put(account.getConfig().getSendPortKey(), account.getConfig().getSendPortValue());
        props.put(account.getConfig().getSendEncryptKey(), account.getConfig().getSendEncryptValue());
        props.put(account.getConfig().getAuthKey(), account.getConfig().getAuthValue());
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        account.getAccount(), account.getPwd());
            }
        });
        Folder folder = null;
        Store store = null;
        SMTPTransport t = null;
        try {
            store = session.getStore(account.getConfig().getReceiveProtocol());
            store.connect();
            folder = store.getFolder("inbox");
            folder.open(Folder.READ_ONLY);
            Message message = folder.getMessage((int) email.getId());
            Message forward = new MimeMessage(session);
            if (email.getFrom() != null) {
                forward.setFrom(new InternetAddress(email.getFrom(), account.getPersonal()));
            }

            forward.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email.getTo(), false));
            if (email.getCc() != null)
                //抄送人
                forward.setRecipients(Message.RecipientType.CC,
                        InternetAddress.parse(email.getCc(), false));
            if (email.getBcc() != null)
                //秘密抄送人
                forward.setRecipients(Message.RecipientType.BCC,
                        InternetAddress.parse(email.getBcc(), false));

            forward.setSubject(email.getSubject());

            MimeMultipart mp = new MimeMultipart();
            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setDataHandler(collect(email, message));
            mp.addBodyPart(mbp1);
            if (email.getAttachments() != null && email.getAttachments().size() > 0) {
                for (Attachment detail1 : email.getAttachments()) {
                    MimeBodyPart mbp2 = new MimeBodyPart();
                    mbp2.attachFile(detail1.getPath());
                    mp.addBodyPart(mbp2);
                }
            }
            forward.setContent(mp);
            forward.saveChanges();
            forward.setSentDate(new Date());
            t = (SMTPTransport) session.getTransport(account.getConfig().getSendProtocol());
            t.connect();
            t.sendMessage(forward, forward.getAllRecipients());
            if (saveSent) {
                save2Sent(account, forward);
            }
            callback.onSuccess();
        } catch (NoSuchProviderException e) {
            callback.onError();
            e.printStackTrace();
        } catch (MessagingException e) {
            callback.onError();
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            callback.onError();
            e.printStackTrace();
        } catch (IOException e) {
            callback.onError();
            e.printStackTrace();
        } finally {
            try {
                if (t != null)
                    t.close();
                if (folder != null)
                    folder.close();
                if (store != null)
                    store.close();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void forward(@NotNull Account account, @NotNull Email email, boolean saveSent, @NotNull Callback callback) {
        reply(account, email, saveSent, callback);
    }

    @Override
    public void delete(long id, @NotNull FolderType type, @NotNull Account account, @NotNull Callback callback) {
        Properties props = System.getProperties();
        props.put(account.getConfig().getReceiveHostKey(), account.getConfig().getReceiveHostValue());
        props.put(account.getConfig().getReceivePortKey(), account.getConfig().getReceivePortValue());
        props.put(account.getConfig().getReceiveEncryptKey(), account.getConfig().getReceiveEncryptValue());
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        account.getAccount(), account.getPwd());
            }
        });
//        session.setDebug(true);
        Store store = null;
        Folder folder = null;
        try {
            store = session.getStore(account.getConfig().getReceiveProtocol());
            store.connect();
            switch (type) {
                case INBOX:
                    folder = (IMAPFolder) store.getFolder("INBOX");
                    break;
                case SENT:
                    folder = (IMAPFolder) store.getFolder("Sent Messages");
                    break;
                case DRAFTS:
                    folder = (IMAPFolder) store.getFolder("Drafts");
                    break;
                case DELETED:
                    folder = (IMAPFolder) store.getFolder("Deleted Messages");
                    break;
                default:
                    folder = (IMAPFolder) store.getFolder("INBOX");
                    break;
            }
            folder.open(Folder.READ_WRITE);
            Message message = folder.getMessage((int) id);
            message.setFlag(Flags.Flag.DELETED, true);
            callback.onSuccess();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            callback.onError();
            e.printStackTrace();
        } finally {
            try {
                if (folder != null)
                    folder.close();
                if (store != null)
                    store.close();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    private static void save2Sent(final Account account, Message message) {
        Properties props = System.getProperties();
        props.put(account.getConfig().getReceiveHostKey(), account.getConfig().getReceiveHostValue());
        props.put(account.getConfig().getReceivePortKey(), account.getConfig().getReceivePortValue());
        props.put(account.getConfig().getReceiveEncryptKey(), account.getConfig().getReceiveEncryptValue());
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        account.getAccount(), account.getPwd());
            }
        });
        session.setDebug(true);
        Store store = null;
        Folder sent = null;
        try {
            store = session.getStore(account.getConfig().getReceiveProtocol());
            store.connect();
            sent = store.getFolder("Sent Messages");
            sent.open(Folder.READ_WRITE);
            message.setFlag(Flags.Flag.SEEN, true); // 设置已读标志
            sent.appendMessages(new Message[]{message});
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (sent != null)
                    sent.close();
                if (store != null)
                    store.close();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    public static void dumpPart(Part p, Email data) {
        try {
            if (p instanceof Message) {
                dumpEnvelope((Message) p, data);
            }
            if (p.isMimeType("text/plain")) {
//            This is plain text
                data.setContent((String) p.getContent());
            } else if (p.isMimeType("multipart/*")) {
//            This is a Multipart
                Multipart mp = (Multipart) p.getContent();
                level++;
                int count = mp.getCount();
                for (int i = 0; i < count; i++)
                    dumpPart(mp.getBodyPart(i), data);
                level--;
            } else if (p.isMimeType("message/rfc822")) {
//            This is a Nested Message
                level++;
                dumpPart((Part) p.getContent(), data);
                level--;
            } else {
                if (showStructure) {
                    /*
                     * If we actually want to see the data, and it's not a
                     * MIME type we know, fetch it and check its Java type.
                     */
                    Object o = p.getContent();
                    if (o instanceof String) {
//                    This is a string
                        System.out.println((String) o);
                        data.setContent((String) p.getContent());
                    } else if (o instanceof InputStream) {
//                    This is just an input stream
                        InputStream is = (InputStream) o;
                    } else {
//                    "This is an unknown type"
                    }
                } else {
                    // other
                }
            }
            if (level != 0 && p instanceof MimeBodyPart &&
                    !p.isMimeType("multipart/*")) {
                String disp = p.getDisposition();
                // many mailers don't include a Content-Disposition
                if (disp != null && disp.equalsIgnoreCase(Part.ATTACHMENT)) {
                    String filename = p.getFileName();
                    if (filename != null) {
                        data.setHasAttach(true);
                        if (showStructure) {
                            String fileName = MimeUtility.decodeText(filename);
                            data.getAttachments().add(new Attachment(fileName,
                                    Environment.getExternalStorageDirectory().getAbsolutePath()
                                            + "/EmailManager/" + fileName, p.getSize(), getPrintSize(p.getSize())));
                        }
                    }
                }
            }
        } catch (MessagingException e) {
            try {
                MimeMessage cmsg = new MimeMessage((MimeMessage) p);
                Multipart mp = (Multipart) cmsg.getContent();
                level++;
                int count = mp.getCount();
                for (int i = 0; i < count; i++)
                    dumpPart(mp.getBodyPart(i), data);
                level--;
            } catch (MessagingException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void dumpEnvelope(Message message, Email data) throws MessagingException, UnsupportedEncodingException {
        data.setId(message.getMessageNumber());
        Address[] recipients = message.getRecipients(Message.RecipientType.TO);
        if (recipients != null) {
            StringBuffer sb = new StringBuffer();
            for (Address recipient : recipients) {
                sb.append(((InternetAddress) recipient).getAddress() + ";");
            }
            data.setTo(sb.toString());
        }
        Address[] ccs = message.getRecipients(Message.RecipientType.CC);
        if (ccs != null) {
            StringBuffer sbCc = new StringBuffer();
            for (Address recipient : ccs) {
                sbCc.append(((InternetAddress) recipient).getAddress() + ";");
            }
            data.setCc(sbCc.toString());
        }
        Address[] bccs = message.getRecipients(Message.RecipientType.BCC);
        if (bccs != null) {
            StringBuffer sbBcc = new StringBuffer();
            for (Address recipient : bccs) {
                sbBcc.append(((InternetAddress) recipient).getAddress() + ";");
            }
            data.setBcc(sbBcc.toString());
        }
        InternetAddress address = (InternetAddress) message.getFrom()[0];
        data.setFrom(address.getAddress());
        data.setPersonal(address.getPersonal());
        data.setSubject(message.getHeader("subject") == null || message
                .getHeader("subject").length == 0 ? message.getSubject()
                : MimeUtility.decodeText(message.getHeader("subject")[0]));
        data.setDate(dateFormat(message.getReceivedDate() == null ? message.getSentDate() : message.getReceivedDate()));
    }

    private static String getPrintSize(long size) {

        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return String.valueOf(size) + " B";
        } else {
            size = size / 1024;
        }
        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        //因为还没有到达要使用另一个单位的时候
        //接下去以此类推
        if (size < 1024) {
            return String.valueOf(size) + " KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            //因为如果以MB为单位的话，要保留最后1位小数，
            //因此，把此数乘以100之后再取余
            size = size * 100;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size * 100 / 1024 % 100)) + "MB";
        } else {

            //否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + " GB";
        }
    }

    private static String dateFormat(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    private static DataHandler collect(Email data, Message msg) throws MessagingException, IOException {
        return new DataHandler(
                new ByteArrayDataSource(data.getContent(), "text/html"));
    }
}
