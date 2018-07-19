package knms.blockoperator.net;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import knms.blockoperator.tileentity.TileBlockOperator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class MessageGuiDataHandler implements IMessageHandler<MessageGuiData, IMessage> {

	@Override
	public IMessage onMessage(MessageGuiData data, MessageContext context) {
		
		World world = context.getServerHandler().playerEntity.worldObj;
		TileEntity tile = world.getTileEntity(data.getX(), data.getY(), data.getZ());
		if(tile instanceof TileBlockOperator){
			((TileBlockOperator)tile).speed=data.getSpeed();
		}
		return null;
	}

}