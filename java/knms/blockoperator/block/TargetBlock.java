package knms.blockoperator.block;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import knms.blockoperator.BlockOperator;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class TargetBlock extends BlockForBO{
	
	public TargetBlock() {
		super(Material.iron);
		setHardness(1.0f);
		setBlockName("target_block");
	}
	@Override
	public void registerBlockIcons(IIconRegister register){
		this.baseIcon=register.registerIcon(BlockOperator.MODID +":state_0");
		this.blockIcon=register.registerIcon(BlockOperator.MODID +":state_1");
	}
	
	@Override
	public IIcon getIcon(int side, int meta){
		return (meta==0)?baseIcon:blockIcon;
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_) { ;
		p_149666_3_.add(new ItemStack(p_149666_1_, 1, 0));
		p_149666_3_.add(new ItemStack(p_149666_1_, 1, 1));
	}

}
