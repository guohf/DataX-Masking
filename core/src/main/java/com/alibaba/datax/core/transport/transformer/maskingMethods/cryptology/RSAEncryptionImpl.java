package com.alibaba.datax.core.transport.transformer.maskingMethods.cryptology;

import java.io.*;
import java.security.*;
import java.security.interfaces.*;
import java.math.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.encodings.PKCS1Encoding;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.datax.core.transport.transformer.maskingConfigure.RSAKey;

/**
 * @author liujiaye
 * RSA
 */

/*
* @author Liu Kun
* 实现了私钥加密和公钥解密操作  2018-04-24
*
*
* RSA加密解密
数据加密前：asdjl12
将原始数据转换为16进制表示的字串：6173646a6c3132
私钥加密后：19bb286a60ce386fcc43099754fd966a976025ce55688570fabbae293ed47940b71373f0dd715e5797348f198139404a24534b72bfedf261a49094d63babba63a29391758dd1273f8d0076dc91e86f23161be5ea2ccf63cec1286e8c056ce9d4dd65c273e55967eb81a44e3fc54b977342289ba9a1cf3f257a583c3f240d52b9
解密后：asdjl12
* */

public class RSAEncryptionImpl extends CryptologyMasking{
	// 填充模式
	final public static int RAW = 2;
	final public static int PKCS1 = 1;

	private RSAPublicKey publicKey;
	private RSAPrivateKey privateKey;
	private KeyPair pair;
	private int keyLength = 1024;

    private static Logger logger = LoggerFactory.getLogger(RSAEncryptionImpl.class);

	/**
	 * constructor
	 * parameter keyLength
	 */
	public RSAEncryptionImpl(int keyLength) {
		// generate RSA Key Pair
		this.keyLength = keyLength;
		generateRSAKeyPair();
	}

	/**
	 * constructor
	 * no parameter
	 */
	public RSAEncryptionImpl() {
		// generate RSA Key Pair
		generateRSAKeyPair();
	}

	private void generateRSAKeyPair() {
		try {
			publicKey = RSAKey.getPublicKey();
			privateKey = RSAKey.getPrivateKey();
		} catch (Exception e) {
			System.out.println("Exception in keypair generation. Reason: " + e);
		}
	}

	public void printRSAKeyPair(){
        try {
            KeyPairGenerator rsaKeyGen = KeyPairGenerator.getInstance("RSA");
            // setKeyLength 1024,setCertaintyOfPrime
            rsaKeyGen.initialize(keyLength, new SecureRandom());
            KeyPair Pair = rsaKeyGen.genKeyPair();
            System.out.println((RSAPublicKey) Pair.getPublic());
            RSAPrivateKey priKey = (RSAPrivateKey) Pair.getPrivate();
            RSAPublicKey pubKey = (RSAPublicKey) Pair.getPublic();
        } catch (Exception e) {
            System.out.println("Exception in keypair generation. Reason: " + e);
        }
    }

	/**
	 * 使用公钥加密
	 * @param clearBytes
	 * @param type
	 * @return
	 */
	public byte[] publicEncrypt(byte[] clearBytes, int type) {
		BigInteger mod = publicKey.getModulus();
		// 指数
		BigInteger publicExponent = publicKey.getPublicExponent();
		RSAKeyParameters para = new RSAKeyParameters(false, mod, publicExponent);

		AsymmetricBlockCipher engine = new RSAEngine();
		if (type == PKCS1)
			engine = new PKCS1Encoding(engine);
		engine.init(true, para);
		try {
			byte[] data = engine.processBlock(clearBytes, 0, clearBytes.length);
			return data;
		} catch (InvalidCipherTextException e) {
			e.printStackTrace();
			logger.error("publicEncrypt engine.processBlock error");
		}
		return null;
	}

	/**
	 * 使用公钥解密
	 * @param clearBytes
	 * @param type
	 * @return
	 */
	public byte[] publicDecrypt(byte[] clearBytes, int type) {
		BigInteger mod = publicKey.getModulus();
		BigInteger pubExp = publicKey.getPublicExponent();

		RSAKeyParameters para = new RSAKeyParameters(false, mod, pubExp);
		AsymmetricBlockCipher engine = new RSAEngine();
		if (type == PKCS1)
			engine = new PKCS1Encoding(engine);
		engine.init(false, para);
		try {
			byte[] data = engine.processBlock(clearBytes, 0, clearBytes.length);
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("publicDecrypt engine.processBlock exception");
		}
		return null;
	}

	/**
	 * 使用私钥加密
	 * @param encodedBytes
	 * @param type
	 * @return
	 */
	public byte[] privateDecrypt(byte[] encodedBytes, int type) {
		RSAPrivateCrtKey prvCrtKey = (RSAPrivateCrtKey) privateKey;
		BigInteger mod = prvCrtKey.getModulus();
		BigInteger pubExp = prvCrtKey.getPublicExponent();
		BigInteger privExp = prvCrtKey.getPrivateExponent();
		BigInteger pExp = prvCrtKey.getPrimeExponentP();
		BigInteger qExp = prvCrtKey.getPrimeExponentQ();
		BigInteger p = prvCrtKey.getPrimeP();
		BigInteger q = prvCrtKey.getPrimeQ();
		BigInteger crtCoef = prvCrtKey.getCrtCoefficient();

		RSAKeyParameters para = new RSAPrivateCrtKeyParameters(mod, pubExp, privExp, p, q, pExp, qExp, crtCoef);

		AsymmetricBlockCipher engine = new RSAEngine();
		if (type == PKCS1)
			engine = new PKCS1Encoding(engine);

		engine.init(false, para);
		try {
			byte[] data = engine.processBlock(encodedBytes, 0, encodedBytes.length);
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("privateDecrypt engine.processBlock error");
		}
		return null;
	}

	/**
	 * 使用私钥解密
	 * @param encodedBytes
	 * @param type
	 * @return
	 */
	public byte[] privateEncrypt(byte[] encodedBytes, int type) {
		RSAPrivateCrtKey prvCrtKey = (RSAPrivateCrtKey) privateKey;
		BigInteger mod = prvCrtKey.getModulus();
		BigInteger pubExp = prvCrtKey.getPublicExponent();
		BigInteger privExp = prvCrtKey.getPrivateExponent();
		BigInteger pExp = prvCrtKey.getPrimeExponentP();
		BigInteger qExp = prvCrtKey.getPrimeExponentQ();
		BigInteger p = prvCrtKey.getPrimeP();
		BigInteger q = prvCrtKey.getPrimeQ();
		BigInteger crtCoef = prvCrtKey.getCrtCoefficient();
		RSAKeyParameters para = new RSAPrivateCrtKeyParameters(mod, pubExp, privExp, p, q, pExp, qExp, crtCoef);
		AsymmetricBlockCipher engine = new RSAEngine();
		if (type == PKCS1)
			engine = new PKCS1Encoding(engine);
		engine.init(true, para);
		try {
			byte[] data = engine.processBlock(encodedBytes, 0, encodedBytes.length);
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("privateEncrypt engine.processBlock error");
		}
		return null;
	}

	public String changeBytesToString(byte[] data) {
		return new String(Hex.encode(data));
	}

	//override from AbstractMasking
	public double execute(double epsilon) throws Exception {
		return -1;
	}

	public String execute(String originData, String type) throws NoSuchAlgorithmException {
        byte[] cipher = publicEncrypt(originData.getBytes(), 1);
        return changeBytesToString(cipher);
    }

    public List<String> execute(List<String> originData) throws Exception {
        List<String> cipherData = new ArrayList<String>();
        for (String str : originData) {
            String cipherStr = changeBytesToString(publicEncrypt(str.getBytes(),1));
            cipherData.add(cipherStr);
        }
        return cipherData;
    }


	public String executeWithPublicDecrypt(String originData) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		BigInteger origin = new BigInteger(originData, 16);
		byte[] cipher = publicDecrypt(origin.toByteArray(), RAW);
		String decoded = changeBytesToString(cipher);
		BigInteger raw_code = new BigInteger(decoded,16);
		String result = new String(raw_code.toByteArray(), "ascii");
		return result;
	}

	public String executeWithPublicEncrypt(String originData) throws NoSuchAlgorithmException {
		byte[] cipher = publicEncrypt(originData.getBytes(), RAW);
		return changeBytesToString(cipher);
	}

	public String executeWithPrivateDecrypt(String originData) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		BigInteger origin = new BigInteger(originData, 16);
		byte[] cipher = privateDecrypt(origin.toByteArray(), RAW);
		String decoded = changeBytesToString(cipher);
		BigInteger raw_code = new BigInteger(decoded,16);
		String result = new String(raw_code.toByteArray(), "ascii");
		return result;
	}

	public String executeWithPrivateEncrypt(String originData) throws NoSuchAlgorithmException {
		byte[] cipher = privateEncrypt(originData.getBytes(), RAW);
		return changeBytesToString(cipher);
	}

	//override from Masking
	public void mask() throws Exception{}

	//从文件读写，加密
	/*public String encryptToText(String filepath){
		try {
			BufferedReader inputFile=new BufferedReader(new FileReader(filepath));
			String plainText=new String();
			String temp=inputFile.readLine();
			while(null!=temp)
			{
				plainText+=temp;
				temp=inputFile.readLine();
			}
			inputFile.close();
			//获取时间作为输出文件的唯一名字
			Date day=new Date();
			SimpleDateFormat df = new SimpleDateFormat("MM-dd-HH-mm");
//            System.out.println(filepath.substring(0,filepath.length()-5)+"-AES-"+df.format(day));

			BufferedWriter outputFile=new BufferedWriter(new FileWriter(filepath.substring(0,filepath.length()-5)+"-RSA-"+df.format(day)+".txt"));

			outputFile.write("PublicKey:"+publicKey.toString()+"\n");
			outputFile.write("PrivateKey:"+privateKey.toString()+"\n");
			outputFile.write(changeBytesToString(publicEncrypt(plainText.getBytes(),1)));
//			System.out.println(changeBytesToString(publicEncrypt(plainText.getBytes(),1)));
			outputFile.close();
			return filepath.substring(0,filepath.length()-5)+"-RSA-"+df.format(day)+".txt";
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
*/
	public static void main(String[] args){
		String content = new String("asdjl12");
		RSAEncryptionImpl rsatest = new RSAEncryptionImpl();
		System.out.println("String加密前：asdjl12");
		System.out.println("字符串："+ rsatest.changeBytesToString(content.getBytes()));
		byte[] cipher=rsatest.publicEncrypt(content.getBytes(), 1);
		System.out.println("公钥加密后："+ rsatest.changeBytesToString(cipher));
		byte[] plain=rsatest.privateDecrypt(cipher, 1);
		System.out.println("解密后："+rsatest.changeBytesToString(plain));
	}
}