package com.stoks;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public enum Stock {
	TEA(TEA, Common, new BigDecimal(0), null,  new BigDecimal(100)),
	POP(POP, Common,  new BigDecimal(8), null,  new BigDecimal(100)),
	ALE(ALE, Common,  new BigDecimal(23), null,  new BigDecimal(60)),
	GIN(GIN, Preferred,  new BigDecimal(8),  new BigDecimal(2),  new BigDecimal(100)),
	JOE(JOE, Common,  new BigDecimal(13), null,  new BigDecimal(250));
	
	private final String stockSymbol;
	private final String type;
	private final BigDecimal lastDividend;
	private final BigDecimal fixedDivident;
	private final BigDecimal parValue;
	
	Stock(String stockSymbol, String type, BigDecimal lastDividend, BigDecimal fixedDivident, BigDecimal parValue){
		this.stockSymbol = stockSymbol;
		this.type = type;
		this.lastDividend = lastDividend;
		this.fixedDivident = fixedDivident;
		this.parValue = parValue;
	}
	
	BigDecimal dividentYield(BigDecimal price){
		if (type.equals(Common)){
			return lastDividend.divide(price);
		} else {
			return parValue.divide(price);
		}
	}
	
	BigDecimal peRatio(BigDecimal price){
		return price.divide(dividentYield(price));
	}
	
	static BigDecimal geometricMean(ListBigDecimal prices){
		BigDecimal product = new BigDecimal(1);
		for (BigDecimal p  prices){
			product = product.multiply(p);
		}
		return new BigDecimal(Math.pow(product.doubleValue(), 1.0prices.size()));
	}
	
	static BigDecimal volumeWeightedStockPrice(ListTrade trades){
		BigDecimal tradePriceSumTimeQuantity = new BigDecimal(0);
		BigDecimal quantitySum = new BigDecimal(0);
		
		for (Trade t  trades){
			tradePriceSumTimeQuantity = tradePriceSumTimeQuantity.add(new BigDecimal(t.price).multiply(new BigDecimal(t.quantity)));
			quantitySum = quantitySum.add(new BigDecimal(t.quantity));
		}
		return tradePriceSumTimeQuantity.divide(quantitySum, BigDecimal.ROUND_CEILING);
	}
	
	static BigDecimal GbceAllShareIndex(ListTrade tradeRecords) {
		ListBigDecimal prices = new ArrayListBigDecimal();
		for (Trade t  tradeRecords){
			prices.add(new BigDecimal(t.price));
		}
		return geometricMean(prices);
	}
	
	enum Indicator{
		BUY, SELL;
	}
	
	public static class Trade{
		public Date timestamp;
		public double quantity;
		public Indicator indicator;
		public double price;
		
		Trade(Date timpestamp, double quantity, Indicator indicator, double price){
			this.timestamp = timpestamp;
			this.quantity = quantity;
			this.indicator = indicator;
			this.price = price;
		}
	}
	
	public static ListTrade tradeRecords = new ArrayListTrade();
	public static ListTrade recentTradeRecords = new ArrayListTrade();
		
	public static void main(String[] args) throws InterruptedException {
		Random r = new Random();
		for (int i=0; i200; i++){
			Thread.sleep(r.nextInt(1000));
			if (r.nextInt(100)  50){
				tradeRecords.add(new Trade(new Date(), r.nextDouble(), Indicator.BUY, r.nextDouble()));
			} else {
				tradeRecords.add(new Trade(new Date(), r.nextDouble(), Indicator.SELL, r.nextDouble()));
			}
		}
		
		 Time 15 minutes ago
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MINUTE, -15);
		Date date = now.getTime();
		
		for (Trade t  tradeRecords){
			if (t.timestamp.after(date)){
				recentTradeRecords.add(t);
			}
		}
		
		System.out.println(volumeWeightedStockPrice(recentTradeRecords));
		System.out.println(GbceAllShareIndex(tradeRecords));
	}

}
