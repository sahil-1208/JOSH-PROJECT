package com.josh.utils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


@Component
@RequiredArgsConstructor
public class SSHUtil {

    @Value("${file-storage.ssh.username}")
    private String sshUser;

    @Value("${file-storage.ssh.private-key-path}")
    private String privateKeyPath;

    private Session setupSession(String host) throws Exception {
        JSch jsch = new JSch();
        jsch.addIdentity(privateKeyPath);
        Session session = jsch.getSession(sshUser, host, 22);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
        return session;
    }

    public void sendFileToContainer(byte[] content, String container, String path) throws Exception {
        String host = container;
        Session session = setupSession(host);
        Channel channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand("cat > " + path);

        OutputStream out = channel.getOutputStream();
        channel.connect();
        out.write(content);
        out.flush();
        out.close();

        channel.disconnect();
        session.disconnect();
    }

    public byte[] fetchFileFromContainer(String container, String path) throws Exception {
        String host = container;
        Session session = setupSession(host);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Channel channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand("cat " + path);
        channel.setInputStream(null);
        InputStream in = channel.getInputStream();
        channel.connect();

        byte[] tmp = new byte[1024];
        int len;
        while ((len = in.read(tmp)) != -1) {
            baos.write(tmp, 0, len);
        }

        channel.disconnect();
        session.disconnect();
        return baos.toByteArray();
    }
}
