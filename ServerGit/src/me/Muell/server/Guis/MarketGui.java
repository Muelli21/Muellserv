package me.Muell.server.Guis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Muell.server.Main;
import me.Muell.server.types.Items;
import me.Muell.server.types.Offer;

public class MarketGui extends AbstractGui {

    private HashMap<Integer, Offer> marketoffers = new HashMap<>();
    private int offers;
    private int site;

    public MarketGui(Player p) {
	super(p, 54, "Market", true);

	setOffers();
	p.openInventory(getInv());
    }

    public MarketGui(Player p, Offer offer) {
	super(p, 9, "Offer details", true);

	for (int i = 0; i < getInv().getSize(); i++) {

	    setItem(i, Items.createGlassPane(DyeColor.GRAY, "*", null), null, null);
	}

	setItemLeftAction(0, Items.createDye(DyeColor.GREEN, "Buy this offer", null), new AbstractAction() {

	    public void click(Player player) {

		if (offer == null) {

		    getP().sendMessage(Main.pre + ChatColor.RED + "This offer has already been sold!");
		    delete();

		}

		offer.buyOffer(player);
		delete();
	    }
	});

	setItemLeftAction(8, Items.createGlassPane(DyeColor.WHITE, "Back to the market", null), new AbstractAction() {

	    public void click(Player player) {

		new MarketGui(p);
	    }
	});
	p.openInventory(getInv());

    }

    public void setOffers() {

	clearGui();

	int amount = 0;
	for (Offer offer : Offer.offers) {

	    amount++;
	    marketoffers.put(amount, offer);
	    offers = 1;
	    site = 1;
	}

	showSite(getP());
    }

    public void showSite(Player p) {

	clearGui();
	for (int slot = 0; slot < 54; slot++) {

	    if (slot % 9 == 0) {
		setBackNext(p, slot);
		continue;
	    }

	    if ((slot + 1) % 9 == 0)
		continue;

	    addOffer(slot);
	    offers++;
	}
    }

    public void setBackNext(Player p, int slot) {

	setItemLeftAction(slot, Items.createitem(Material.STAINED_GLASS_PANE, "Back", Arrays.asList(ChatColor.GRAY + "Go back")), new AbstractAction() {

	    public void click(Player player) {
		site--;
		setOffers(getOffers() - 84);

		if (site < 1) {
		    delete();
		    return;
		}

		showSite(p);
	    }
	});

	setItemLeftAction(slot + 8, Items.createitem(Material.STAINED_GLASS_PANE, "Next", Arrays.asList(ChatColor.GRAY + "Go to the next side")), new AbstractAction() {

	    public void click(Player player) {
		site++;
		showSite(p);
	    }
	});
    }

    public void addOffer(int slot) {

	Offer offer = marketoffers.get(offers);

	if (offer != null) {

	    ArrayList<String> lore = new ArrayList<>();
	    lore.add(ChatColor.WHITE + "Ability: " + offer.getAbility().getName());
	    lore.add(ChatColor.WHITE + "Player: " + offer.getP().getName());
	    lore.add(ChatColor.WHITE + "Price: " + offer.getPrice());
	    lore.add(ChatColor.GRAY + "Left click to see futher details!");
	    lore.add(ChatColor.GRAY + "Right click to buy the offer immediately!");

	    ItemStack item = offer.getAbility().getInvItem().clone();
	    ItemMeta meta = item.getItemMeta();
	    meta.setLore(lore);
	    item.setItemMeta(meta);

	    setItem(slot, item, new AbstractAction() {

		public void click(Player player) {

		    new MarketGui(getP(), offer);

		}
	    }, new AbstractAction() {

		public void click(Player player) {

		    offer.buyOffer(player);
		    delete();
		}
	    });

	} else {

	    setItem(slot, Items.createGlassPane(DyeColor.GRAY, "*", null), null, null);
	}

    }

    public int getOffers() {

	return this.offers;
    }

    public void setOffers(int offers) {

	this.offers = offers;
    }

}
