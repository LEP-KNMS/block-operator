package knms.blockoperator.net;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class MessageGuiData implements IMessage {
	
	private float speed;
	private int x, y, z;
	
	@Override//IMessageのメソッド。ByteBufからデータを読み取る。
	public void fromBytes(ByteBuf buf) {
		this.speed = buf.readFloat();
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
	}
 
	@Override//IMessageのメソッド。ByteBufにデータを書き込む。
	public void toBytes(ByteBuf buf) {
		buf.writeFloat(this.speed);
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
	}
	
	public MessageGuiData setSpeed(float speed){
		this.speed = speed;
		return this;
	}
	
	public float getSpeed(){
		return this.speed;
	}
	
	public MessageGuiData setPosition(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	public int getX(){
		return this.x;
	}
	public int getY(){
		return this.y;
	}
	public int getZ(){
		return this.z;
	}
}