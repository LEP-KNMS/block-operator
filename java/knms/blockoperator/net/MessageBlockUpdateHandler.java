package knms.blockoperator.net;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.world.World;

public class MessageBlockUpdateHandler implements IMessageHandler<MessageBlockUpdate, IMessage> {

	@Override
	public IMessage onMessage(MessageBlockUpdate data, MessageContext context) {
		World world = context.getServerHandler().playerEntity.worldObj;
		world.setBlockMetadataWithNotify(data.x, data.y, data.z, data.meta,2);
		return null;
	}

}