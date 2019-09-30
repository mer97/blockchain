package top.leemer.blockchain;

import com.alibaba.fastjson.JSON;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.leemer.blockchain.model.*;
import top.leemer.blockchain.utils.CryptoUtil;
import top.leemer.blockchain.utils.RSACoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlockchainApplicationTests {

    @Test
    public void contextLoads() {
        //创建一个空的区块链
        List<Block> blocks = new ArrayList<>();
        //生成创世区块
        Block beginBlock = new Block(1, "1", System.currentTimeMillis(), new ArrayList<Transaction>(), 1, "1");
        //加入创世区块到区块链
        blocks.add(beginBlock);

        //交易奖励
        List<Transaction> transactions = new ArrayList<>();
        Transaction tx1 = new Transaction();

        transactions.add(tx1);

        //交易发起方
        Wallet walletSender = Wallet.generateWallet();
        //交易接收方
        Wallet walletReceiver = Wallet.generateWallet();
        TransactionOutput txOut1 = new TransactionOutput(10, walletSender.getHashPubKey());
        Transaction tx2 = new Transaction(CryptoUtil.UUID(), null, txOut1);
        transactions.add(tx2);

        TransactionInput txIn = new TransactionInput(tx2.getId(), 10, null, walletSender.getHashPubKey());
        TransactionOutput txOut = new TransactionOutput(10, walletReceiver.getHashPubKey());
        Transaction tx3 = new Transaction(CryptoUtil.UUID(), txIn, txOut);

        //假定tx2之前已经被打包进区块，也就是已经被记录进账本了
        tx3.sign(walletSender.getPrivateKey(), tx2);
        transactions.add(tx3);

        //加入系统奖励的交易
        Transaction sysTx = new Transaction();
        transactions.add(sysTx);

        Block lastBlock = blocks.get(blocks.size() - 1);

        int nonce = 1;
        String sha256;
        while (true){
            sha256 = CryptoUtil.SHA256(lastBlock.getHash() + JSON.toJSONString(transactions) + nonce);

            if (sha256.startsWith("0000")){
                System.out.println("正确的挖矿结果：" + sha256 + "\r\n计算次数：" + nonce);
                break;
            }

            System.out.println("错误的挖矿结果：" + sha256);
            nonce++;
        }

        //创建新区块
        Block newBlock = new Block(lastBlock.getIndex() + 1, sha256, System.currentTimeMillis(), transactions, nonce, lastBlock.getHash());
        blocks.add(newBlock);

        System.out.println(JSON.toJSON(blocks));
    }

    private String publicKey;
    private String privateKey;

    @Before
    public void setUp() throws Exception {
        Map<String, Object> mapKey = RSACoder.initKey();
        publicKey = RSACoder.getPublicKey(mapKey);
        privateKey = RSACoder.getPrivateKey(mapKey);
        System.err.println("公钥：\r\n" + publicKey);
        System.err.println("私钥：\r\n" + privateKey);
    }

    @Test
    public void testEncrypt() throws Exception {
        System.out.println("========非对称加密（公钥加密，私钥解密）========");
        String dataString = String.valueOf(System.currentTimeMillis());

        byte[] data = dataString.getBytes();
        //发送方使用接收方公钥加密。
        byte[] encryptData = RSACoder.encryptByPublicKey(data, publicKey);

       //接收方使用接收方私钥解密。
        byte[] decryptData = RSACoder.decryptByPrivateKey(encryptData, privateKey);

        System.out.println("加密前：" + dataString + "\r\n解密后：" + new String(decryptData));
    }

    @Test
    public void testSign() throws Exception {
        System.out.println("========数字签名（私钥签名，公钥验证签名）========");
        String dataString = String.valueOf(System.currentTimeMillis());

        byte[] data = dataString.getBytes();
        //发送方利用发送方私钥签名。
        String sign = RSACoder.sign(data, privateKey);
        System.err.println("签名：\r\n" + sign);

        //接收方利用发送方公钥验证签名。
        boolean status = RSACoder.verify(data, publicKey, sign);

        System.err.println("验证结果：" + status);
    }

}
