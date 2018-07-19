package knms.blockoperator.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemTargetBlock extends ItemBlock{

	public ItemTargetBlock(Block p_i45328_1_) {
		super(p_i45328_1_);
		setMaxDamage(0);
		setHasSubtypes(true);
	}
	@Override
	public int getMetadata(int par1) {
		return par1;
	}
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		String blockName=field_150939_a.getUnlocalizedName();
		switch(stack.getItemDamage()){
		case 0:
			blockName+="_0";
			break;
		case 1:
			blockName+="_1";
			break;
		}
	    return blockName;
	}

}
