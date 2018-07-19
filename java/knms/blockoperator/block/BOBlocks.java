package knms.blockoperator.block;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import knms.blockoperator.BlockOperator;
import knms.blockoperator.item.ItemTargetBlock;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BOBlocks {
	public static final CreativeTabs tabMove = new CreativeTabs(BlockOperator.MODID) {
	    @Override
	    @SideOnly(Side.CLIENT)
	    public Item getTabIconItem(){
	        ItemStack iStack = new ItemStack(theta_block);
	        return iStack.getItem();
	    }
	    @Override
	    @SideOnly(Side.CLIENT)
	    public int func_151243_f() { return 0; }
	};
	public static final Block theta_block = new ThetaBlock().setCreativeTab(tabMove);
	public static final Block target_block = new TargetBlock().setCreativeTab(tabMove);
	public static final Block translater = new Translater().setCreativeTab(tabMove);
	
	
	public static void registerBlocks(){
		GameRegistry.registerBlock(theta_block,theta_block.getUnlocalizedName());
		GameRegistry.registerBlock(target_block,ItemTargetBlock.class,target_block.getUnlocalizedName());
		GameRegistry.registerBlock(translater,translater.getUnlocalizedName());
	}
}
