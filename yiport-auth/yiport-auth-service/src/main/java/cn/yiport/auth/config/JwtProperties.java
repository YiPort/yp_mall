//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.yiport.auth.config;

import cn.yiport.auth.utils.RsaUtils;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
    prefix = "yiport.jwt"
)
public class JwtProperties {
    private String secret;
    private String pubKeyPath;
    private String priKeyPath;
    private int expire;
    private String cookieName;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private static final Logger logger = LoggerFactory.getLogger(JwtProperties.class);

    public JwtProperties() {
    }

    @PostConstruct
    public void init() {
        try {
            File pubKey = new File(this.pubKeyPath);
            File priKey = new File(this.priKeyPath);
            if (!pubKey.exists() || !priKey.exists()) {
                RsaUtils.generateKey(this.pubKeyPath, this.priKeyPath, this.secret);
            }

            this.publicKey = RsaUtils.getPublicKey(this.pubKeyPath);
            this.privateKey = RsaUtils.getPrivateKey(this.priKeyPath);
        } catch (Exception var3) {
            logger.error("初始化公钥和私钥失败！", var3);
            throw new RuntimeException();
        }
    }

    public String getSecret() {
        return this.secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getPubKeyPath() {
        return this.pubKeyPath;
    }

    public void setPubKeyPath(String pubKeyPath) {
        this.pubKeyPath = pubKeyPath;
    }

    public String getPriKeyPath() {
        return this.priKeyPath;
    }

    public void setPriKeyPath(String priKeyPath) {
        this.priKeyPath = priKeyPath;
    }

    public int getExpire() {
        return this.expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public String getCookieName() {
        return this.cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }
}
