package com.example.demo.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class Receipt implements Serializable {

	private static final long serialVersionUID = 2974848584591598426L;

	private int userId;
	private int amountReceipt;
}
