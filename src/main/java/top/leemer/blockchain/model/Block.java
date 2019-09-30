package top.leemer.blockchain.model;

import java.util.List;

/**
 * 区块
 * @author LEEMER
 * Create Date: 2019-04-14
 */
public class Block {

    /**
     * 区块索引
     */
    private int index;
    /**
     * 当前区块哈希值
     */
    private String hash;

    /**
     * 创建时间
     */
    private long timestamp;

    /**
     * 当前区块交易集合
     */
    private List<Transaction> transactions;

    /**
     * 工作量证明，计算正确hash值次数
     */
    private int nonce;

    /**
     * 前一个区块的哈希值
     */
    private String previousHash;

    public Block() {
    }

    public Block(int index, String hash, long timestamp, List<Transaction> transactions, int nonce, String previousHash) {
        this.index = index;
        this.hash = hash;
        this.timestamp = timestamp;
        this.transactions = transactions;
        this.nonce = nonce;
        this.previousHash = previousHash;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public int getNonce() {
        return nonce;
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    @Override
    public String toString() {
        return "Block{" +
                "index=" + index +
                ", hash='" + hash + '\'' +
                ", timestamp=" + timestamp +
                ", transactions=" + transactions +
                ", nonce=" + nonce +
                ", previousHash='" + previousHash + '\'' +
                '}';
    }
}
