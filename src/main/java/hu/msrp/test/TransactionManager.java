package hu.msrp.test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransactionManager {
	
	private List<Transaction> transactions = new ArrayList<Transaction>();
	
	
	public Transaction createNewTransaction() {
		Transaction t = new Transaction(genRandomString(16));
		transactions.add(t);
		return t;
	}
	
	public List<Transaction> getTransactions() {
		return transactions;
	}

	private String genRandomString(int length) {
		double divisor = length / 32.0; //32 karakter egy UUID '-' jelek nélkül
		System.out.println(divisor);
		int cycle = (int) Math.round(divisor);
		cycle++; //Növeljük eggyel, hogy a pl. 4.2 --> 4 kerekítés után is jó legyen 
		
		String random = new String();
		for(int i = 0; i < cycle; i++) {
			random += UUID.randomUUID().toString().replaceAll("-", "");
		}
		
		return random.substring(0, length);
	}
}
