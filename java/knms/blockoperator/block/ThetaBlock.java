package knms.blockoperator.block;

import knms.blockoperator.BlockOperator;
import knms.blockoperator.client.gui.BOGUI;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ThetaBlock extends BlockForBO {

	protected ThetaBlock() {
		super(Material.iron );
		setHardness(1.0f);
		setBlockName("theta_block");
	}
	@Override
	public void registerBlockIcons(IIconRegister register){
		this.blockIcon=register.registerIcon(BlockOperator.MODID +":theta_block");
	}
	
	@Override
	public IIcon getIcon(int side, int meta){
		return blockIcon;
	}
	
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float posX, float posY, float posZ){
		if(!world.isRemote)player.openGui(BlockOperator.instance, BOGUI.GUI_THETA, world, x, y, z);
		return true;
	}

}
