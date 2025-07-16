package com.josh.utils;

import com.jcraft.jsch.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

@Component
@RequiredArgsConstructor
public class SSHUtil {

    @Value("${file-storage.ssh.username}")
    private String sshUser;

    @Value("${file-storage.ssh.private-key-path}")
    private String privateKeyPath;

    private Session setupSession(String host, int port) throws Exception {
        System.out.println("Using private key: " + privateKeyPath);
        File keyFile = new File(privateKeyPath);
        if (!keyFile.exists()) {
            throw new RuntimeException("Private key not found at: " + privateKeyPath);
        }

        JSch jsch = new JSch();
        try {
            jsch.addIdentity(keyFile.getAbsolutePath());
        } catch (JSchException e) {
            throw new RuntimeException("Failed to add identity: " + e.getMessage(), e);
        }

        Session session = jsch.getSession(sshUser, host, port);
        session.setConfig("StrictHostKeyChecking", "no");
        session.setConfig("PreferredAuthentications", "publickey");
        session.connect(10000);
        return session;
    }


    public void sendFileToContainer(byte[] content, String host, int port, String remotePath) throws Exception {
        Session session = null;
        Channel channel = null;
        OutputStream out = null;

        try {
            session = setupSession(host, port);
            channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand("cat > " + remotePath);

            out = channel.getOutputStream();
            channel.connect();

            out.write(content);
            out.flush();
        } finally {
            if (out != null) out.close();
            if (channel != null) channel.disconnect();
            if (session != null) session.disconnect();
        }
    }


    public byte[] fetchFileFromContainer(String host, int port, String remotePath) throws Exception {
        Session session = null;
        Channel channel = null;
        InputStream in = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            session = setupSession(host, port);
            channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand("cat " + remotePath);
            in = channel.getInputStream();
            channel.connect();

            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }

            return baos.toByteArray();
        } finally {
            if (in != null) in.close();
            if (channel != null) channel.disconnect();
            if (session != null) session.disconnect();
        }
    }

    private static class SilentUserInfo implements UserInfo {
        public String getPassphrase() { return null; }
        public String getPassword() { return null; }
        public boolean promptPassphrase(String message) { return true; }
        public boolean promptPassword(String message) { return true; }
        public boolean promptYesNo(String message) { return true; }
        public void showMessage(String message) { }
    }
}
