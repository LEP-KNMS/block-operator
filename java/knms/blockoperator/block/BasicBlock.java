package knms.blockoperator.block;

import java.util.Random;

import knms.blockoperator.BlockOperator;
import knms.blockoperator.client.gui.BOGUI;
import knms.blockoperator.tileentity.TileBlockOperator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public abstract class BasicBlock extends BlockForBO implements ITileEntityProvider{
	
	public BasicBlock() {
		super(Material.iron);
		setHardness(1.0F);
	}
	
	public void registerBlockIcons(IIconRegister register){
		this.baseIcon = register.registerIcon(BlockOperator.MODID +":default");
		this.blockIcon = register.registerIcon(BlockOperator.MODID +":"+getTextureName());
	}
	
	public IIcon getIcon(int side,int meta){
		if(side==(meta&0x7))return blockIcon;
		return baseIcon;
	}
	
	@Override
	public void onBlockPlacedBy(World world, int p_x, int p_y, int p_z, EntityLivingBase entity, ItemStack p_149689_6_) {
		if (!world.isRemote){
			int l = BlockPistonBase.determineOrientation(world, p_x, p_y, p_z, entity);
			if (world.isBlockIndirectlyGettingPowered(p_x, p_y, p_z)){
				world.setBlockMetadataWithNotify(p_x, p_y, p_z, l|0x8, 3);
				onReceivingPower(world, p_x, p_y, p_z);
			}else {
				world.setBlockMetadataWithNotify(p_x, p_y, p_z, l, 3);
			}
		}
	}
	
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entity, int side, float posX, float posY, float posZ){
		if(!world.isRemote)entity.openGui(BlockOperator.instance, BOGUI.GUI_BLOCKOPERATOR, world, x, y, z);
		return true;
	}
	
	public void onNeighborBlockChange(World world, int x, int y, int z, Block p_149695_5_)
	{
		if (!world.isRemote)
		{
			int meta = world.getBlockMetadata(x, y, z);
			boolean flag = (meta & 0x8) != 0;
			
			if (!world.isBlockIndirectlyGettingPowered(x, y, z) && flag)
			{
				world.scheduleBlockUpdate(x, y, z, this, 4);
			}
			else if (world.isBlockIndirectlyGettingPowered(x, y, z) && !flag)
			{
				world.setBlockMetadataWithNotify(x, y, z, meta|0x8, 3);
				onReceivingPower(world, x, y, z);
			}
		}
	}
	
	public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_, int p_149674_4_, Random p_149674_5_)
	{
		int meta = p_149674_1_.getBlockMetadata(p_149674_2_, p_149674_3_, p_149674_4_);
		boolean flag = (meta & 0x8) != 0;
		if (!p_149674_1_.isRemote && !p_149674_1_.isBlockIndirectlyGettingPowered(p_149674_2_, p_149674_3_, p_149674_4_) && flag)
		{
			onBreakingUpPower(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_);
			p_149674_1_.setBlockMetadataWithNotify(p_149674_2_, p_149674_3_, p_149674_4_, meta&0x7, 3);
		}
	}
	
	public int tickRate(World world){
		return 1;
	}
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		TileEntity tile = new TileBlockOperator();
		tile.setWorldObj(p_149915_1_);
		return tile;
	}
	
	public abstract void onReceivingPower(World world, int x, int y, int z);
	public abstract void onBreakingUpPower(World world, int x, int y, int z);
}
