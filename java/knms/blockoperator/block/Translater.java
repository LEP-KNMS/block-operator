package knms.blockoperator.block;

import knms.blockoperator.tileentity.TileBlockOperator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class Translater extends BasicBlock {

	public Translater() {
		super();
		setBlockName("translater");
		setBlockTextureName("translater");
	}

	@Override
	public void onReceivingPower(World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(x,y,z);
		if(!(tile instanceof TileBlockOperator))return;
		TileBlockOperator rmb = (TileBlockOperator)tile;
		rmb.initialize();
	}

	@Override
	public void onBreakingUpPower(World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(x,y,z);
		if(!(tile instanceof TileBlockOperator))return;
		TileBlockOperator rmb = (TileBlockOperator)tile;
		rmb.initialize();
	}

}
