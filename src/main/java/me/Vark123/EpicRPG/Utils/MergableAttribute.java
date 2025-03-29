package me.Vark123.EpicRPG.Utils;

import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.inventory.EquipmentSlotGroup;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Builder
public class MergableAttribute {

	private Attribute attribute;
	private String name;
	@Setter
	private double amount;
	private Operation operation;
	private EquipmentSlotGroup slot;
	
	public MergableAttribute(Attribute attribute, AttributeModifier attributeModifier) {
		this.attribute = attribute;
		this.name = attributeModifier.getName();
		this.amount = attributeModifier.getAmount();
		this.operation = attributeModifier.getOperation();
		this.slot = attributeModifier.getSlotGroup();
	}
	
	public boolean isSimiliar(MergableAttribute ma) {
		return attribute.equals(ma.attribute)
				&& operation.equals(ma.operation)
				&& slot.equals(ma.slot);
	}
	
	public AttributeModifier getAttributeModifier() {
		return new AttributeModifier(
				NamespacedKey.fromString(name), 
				amount, 
				operation, 
				slot);
	}
	
	public void merge(MergableAttribute ma) {
		merge(ma.getAmount());
	}
	
	public void merge(AttributeModifier modifier) {
		merge(modifier.getAmount());
	}
	
	public void merge(double amount) {
		this.amount += amount;
	}
	
	public Entry<Attribute, AttributeModifier> getEntryAttribute(){
		return Map.entry(attribute, getAttributeModifier());
	}
	
}
