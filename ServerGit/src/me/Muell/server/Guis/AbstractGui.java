package me.Muell.server.Guis;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractGui {

    public static HashMap<Inventory, AbstractGui> Guis = new HashMap<>();

    private Inventory inv;
    private boolean closeable;
    private HashMap<Integer, AbstractAction> leftaction = new HashMap<>();
    private HashMap<Integer, AbstractAction> rightaction = new HashMap<>();
    private Player p;

    public AbstractGui(Player p, int slots, String title, boolean closeable) {

	Inventory inv = Bukkit.createInventory(null, slots, title);
	Guis.put(inv, this);
	this.p = p;
	this.closeable = closeable;
	this.inv = inv;
	// p.openInventory(inv);
    }

    public AbstractGui(Player p, boolean closeable) {

	Inventory inv = p.getInventory();
	Guis.put(inv, this);
	this.p = p;
	this.closeable = closeable;
	this.inv = inv;
	// p.openInventory(inv);
    }

    public void setItem(int slot, ItemStack item, AbstractAction leftaction, AbstractAction rightaction) {

	inv.setItem(slot, item);
	getLeftaction().remove(slot);
	getRightaction().remove(slot);
	getLeftaction().put(slot, leftaction);
	getRightaction().put(slot, rightaction);
	p.updateInventory();
    }

    public void setItemRightAction(int slot, ItemStack item, AbstractAction rightaction) {

	inv.setItem(slot, item);
	getLeftaction().remove(slot);
	getRightaction().remove(slot);
	getRightaction().put(slot, rightaction);
	p.updateInventory();
    }

    public void setItemLeftAction(int slot, ItemStack item, AbstractAction leftaction) {

	inv.setItem(slot, item);
	getLeftaction().remove(slot);
	getRightaction().remove(slot);
	getLeftaction().put(slot, leftaction);
	p.updateInventory();
    }

    public void clearGui() {

	inv.clear();
	leftaction.clear();
	rightaction.clear();
	p.updateInventory();
    }

    public void delete() {

	if (p.getOpenInventory() == inv)
	    p.closeInventory();

	Guis.remove(inv);
	leftaction.clear();
	rightaction.clear();
	setCloseable(true);
	p.closeInventory();
	this.inv = null;
	this.p = null;

    }

    public interface AbstractAction {
	void click(Player player);
    }

    public Inventory getInv() {
	return inv;
    }

    public void setInv(Inventory inv) {
	this.inv = inv;
    }

    public HashMap<Inventory, AbstractGui> getGuis() {
	return Guis;
    }

    public void setGuis(HashMap<Inventory, AbstractGui> guis) {
	Guis = guis;
    }

    public boolean isCloseable() {
	return closeable;
    }

    public void setCloseable(boolean closeable) {
	this.closeable = closeable;
    }

    public Player getP() {
	return p;
    }

    public void setP(Player p) {
	this.p = p;
    }

    public HashMap<Integer, AbstractAction> getRightaction() {
	return rightaction;
    }

    public void setRightaction(HashMap<Integer, AbstractAction> rightaction) {
	this.rightaction = rightaction;
    }

    public HashMap<Integer, AbstractAction> getLeftaction() {
	return leftaction;
    }

    public void setLeftaction(HashMap<Integer, AbstractAction> leftaction) {
	this.leftaction = leftaction;
    }

}
