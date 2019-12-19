package com.fantaike.emailmanagerkt.data.source.remote;


import com.fantaike.emailmanager.data.source.AccountDataSource;
import com.fantaike.emailmanagerkt.data.Account;
import com.fantaike.emailmanagerkt.utils.AppExecutors;
import org.jetbrains.annotations.NotNull;

import javax.mail.*;
import java.util.Properties;

public class AccountRemoteDataSource implements AccountDataSource {
    private static AccountRemoteDataSource INSTANCE;
    private AppExecutors mExecutors;

    private AccountRemoteDataSource(AppExecutors executors) {
        this.mExecutors = executors;
    }

    @Override
    public void add(@NotNull final Account account, @NotNull final CallBack callback) {
        mExecutors.getNetworkIO().execute(() -> {
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
            try (Store store = session.getStore(account.getConfig().getReceiveProtocol());) {
                store.connect();
                callback.onSuccess();
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
                callback.onError("未知错误");
            } catch (MessagingException e) {
                e.printStackTrace();
                callback.onError("账号或密码错误");
            }
        });
    }

    public static AccountRemoteDataSource getInstance(AppExecutors executors) {
        if (INSTANCE == null) {
            synchronized (AccountRemoteDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AccountRemoteDataSource(executors);
                }
            }
        }
        return INSTANCE;
    }
}
