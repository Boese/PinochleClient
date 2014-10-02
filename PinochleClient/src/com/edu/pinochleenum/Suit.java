package com.edu.pinochleenum;

public enum Suit {
	Hearts,
	Spades,
	Diamonds,
	Clubs;
	public Suit getNext(int i) {
		return values()[(ordinal()+i) % values().length];
	}
}
