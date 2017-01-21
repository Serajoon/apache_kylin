package com.serajoon.po;

public class KylinSalesPo {
	private String part_dt;
	private int sellers;

	public String getPart_dt() {
		return part_dt;
	}

	public void setPart_dt(String part_dt) {
		this.part_dt = part_dt;
	}

	public int getSellers() {
		return sellers;
	}

	public KylinSalesPo(){
		
	}
	public KylinSalesPo(String part_dt, int sellers) {
		super();
		this.part_dt = part_dt;
		this.sellers = sellers;
	}

	public void setSellers(int sellers) {
		this.sellers = sellers;
	}

}
