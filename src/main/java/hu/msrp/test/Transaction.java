package hu.msrp.test;

public class Transaction {
	
	private String transactionId;

	public Transaction(String tId) {
		this.transactionId = tId;
	}
	
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getTransactionId() {
		return transactionId;
	}

}
