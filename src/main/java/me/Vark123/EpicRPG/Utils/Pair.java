package me.Vark123.EpicRPG.Utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pair<K,V> {

	private K key;
	private V value;
	
	public Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}
}
