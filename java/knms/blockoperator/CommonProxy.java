package knms.blockoperator;

import cpw.mods.fml.common.registry.GameRegistry;
import knms.blockoperator.tileentity.TileBlockOperator;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class CommonProxy {

	protected IIcon[] destroyBlockIcons;

	public void registerTileEntity() {
		GameRegistry.registerTileEntity(TileBlockOperator.class, "blockoperator");
	}
	public void registerEntityRenderer(){}
	
	public World getWorld(){
		return null;
	}
	
	public void registerIcons(){}
	public IIcon[] getDestroyIcons(){
		return null;
	}

}
