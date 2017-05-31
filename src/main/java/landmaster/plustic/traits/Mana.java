package landmaster.plustic.traits;

import baubles.api.*;
import baubles.api.cap.*;
import landmaster.plustic.api.*;
import mcjty.lib.tools.*;

import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.world.*;
import net.minecraftforge.items.*;
import slimeknights.tconstruct.library.traits.*;
import slimeknights.tconstruct.library.utils.*;
import vazkii.botania.api.mana.*;

public class Mana extends AbstractTrait {
	public static final int MANA_DRAW = 100;
	public static final Mana mana = new Mana();
	
	public Mana() {
		super("mana", 0x54E5FF);
		Toggle.toggleable.add(identifier);
	}
	
	@Override
	public void onUpdate(ItemStack tool, World world, Entity entity, int itemSlot, boolean isSelected) {
		if (!world.isRemote
				&& entity instanceof EntityPlayer
				&& ToolHelper.getCurrentDurability(tool) < ToolHelper.getMaxDurability(tool)
				&& Toggle.getToggleState(tool, identifier)
				&& drawMana((EntityPlayer)entity)) {
			ToolHelper.healTool(tool, MANA_DRAW, (EntityPlayer)entity);
		}
	}
	
	@Override
	public int onToolDamage(ItemStack tool, int damage, int newDamage, EntityLivingBase entity) {
		if (entity instanceof EntityPlayer
				&& Toggle.getToggleState(tool, identifier)
				&& drawMana((EntityPlayer)entity)) {
			--newDamage;
		}
		return super.onToolDamage(tool, damage, newDamage, entity);
	}
	
	private static boolean drawMana(EntityPlayer ent) {
		IItemHandler handler = ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		if (handler instanceof IItemHandlerModifiable) {
			for (int i=0; i<handler.getSlots(); ++i) {
				ItemStack is = ItemStackTools.safeCopy(handler.getStackInSlot(i));
				if (ManaItemHandler.requestManaExactForTool(is, ent, MANA_DRAW, true)) {
					((IItemHandlerModifiable) handler).setStackInSlot(i, is);
					return true;
				}
			}
		}
		
		IBaublesItemHandler ib = BaublesApi.getBaublesHandler(ent);
		for (int i=0; i<ib.getSlots(); ++i) {
			ItemStack is = ItemStackTools.safeCopy(ib.getStackInSlot(i));
			if (ManaItemHandler.requestManaExactForTool(is, ent, MANA_DRAW, true)) {
				ib.setStackInSlot(i, is);
				return true;
			}
		}
		
		return false;
 	}
}
