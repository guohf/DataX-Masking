/**
 * Created by LabUser on 2018/4/22.
 */
import com.alibaba.datax.core.transport.transformer.maskingMethods.differentialPrivacy.EpsilonDifferentialPrivacyImpl;
import com.alibaba.datax.core.transport.transformer.maskingMethods.cryptology.RSAEncryptionImpl;
import com.alibaba.datax.core.transport.transformer.maskingMethods.cryptology.AESEncryptionImpl;
import com.alibaba.datax.core.transport.transformer.maskingMethods.irreversibleInterference.MD5EncryptionImpl;
import com.alibaba.datax.core.transport.transformer.maskingMethods.cryptology.FormatPreservingEncryptionImpl;
import org.junit.Test;
import org.bouncycastle.asn1.eac.RSAPublicKey;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

public class TestMasking {

    private String originStr = "HelloWorld";
    private double originDouble = 1.234;

    @Test
    public void testEDP(){

        try{
            double epsilon = 100;
            EpsilonDifferentialPrivacyImpl masker = new EpsilonDifferentialPrivacyImpl();
            String result = masker.maskOne(originDouble, epsilon);
            System.out.println(result);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void testRSA(){
        RSAEncryptionImpl masker = new RSAEncryptionImpl();
        try{
            String content = new String("asdjl12");
            RSAEncryptionImpl rsatest = new RSAEncryptionImpl();
            System.out.println("RSA加密解密\n数据加密前：" + content);
            System.out.println("将原始数据转换为16进制表示的字串：" + rsatest.changeBytesToString(content.getBytes()));
            String masked = rsatest.executeWithPrivateEncrypt(content);
            System.out.println("私钥加密后：" + masked);
            String decoded = rsatest.executeWithPublicDecrypt(masked);
            System.out.println("解密后：" + decoded);
            masked = rsatest.executeWithPublicEncrypt(content);
            System.out.println("公钥加密后：" + masked);
            decoded = rsatest.executeWithPrivateDecrypt(masked);
            System.out.println("私钥解密后：" + decoded);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void testAES(){
        AESEncryptionImpl masker = new AESEncryptionImpl();
        try{
            String result = masker.execute(originStr);
            System.out.println(result);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void testMD5(){
        MD5EncryptionImpl masker = new MD5EncryptionImpl();
        try{
            String result = masker.execute(originStr);
            System.out.println(result);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void testFPE(){
        try{
            FormatPreservingEncryptionImpl masker = new FormatPreservingEncryptionImpl();
            String result = masker.execute("abcdefg");
            System.out.println(result);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

}
