package knms.blockoperator.client.gui;

import cpw.mods.fml.common.network.IGuiHandler;
import knms.blockoperator.inventory.ContainerTheta;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class BOGUI implements IGuiHandler{

	public static final int GUI_THETA = 0;
	public static final int GUI_BLOCKOPERATOR = 1;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == GUI_THETA) return new ContainerTheta();
		if (ID == GUI_BLOCKOPERATOR) return new ContainerTheta();
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == GUI_THETA) return new GuiTheta(world, x, y, z);
		if (ID == GUI_BLOCKOPERATOR) return new GuiBlockOperator(world.getTileEntity( x, y, z));
		return null;
	}
}
