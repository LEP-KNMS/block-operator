package knms.blockoperator.net;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class MessageBlockUpdate implements IMessage{

	int x,y,z,meta;
	
	public MessageBlockUpdate setPosition(int x,int y,int z){
		this.x=x;
		this.y=y;
		this.z=z;
		return this;
	}
	
	public MessageBlockUpdate setMetadata(int meta){
		this.meta=meta;
		return this;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		x=buf.readInt();
		y=buf.readInt();
		z=buf.readInt();
		meta=buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x).writeInt(y).writeInt(z).writeInt(meta);
	}

}
