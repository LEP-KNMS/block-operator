package knms.blockoperator.tileentity;

import java.util.Map.Entry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import knms.blockoperator.block.BOBlocks;
import knms.blockoperator.entity.BlockEntity;
import knms.blockoperator.util.RotationHelper;
import knms.blockoperator.world.BlockAccess;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class TileBlockOperator extends TileEntity{
	
	public boolean goBack = false;
	public float speed = 0.101F;//degrees or distance
	
	public boolean isOperating = false;
	private boolean doSetBlock = false;
	public BlockAccess tinyWorld;
	
	public double dx, dy, dz;
	private double sx, sy, sz;
	private double distance = 0.0f;
	private double theta;
	
	
	public TileBlockOperator(){
		tinyWorld = new BlockAccess(this);
	}
	
	public void setWorldObj(World world){
		super.setWorldObj(world);
		tinyWorld.setWorldObj(world);
	}
	
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.speed = nbt.getFloat("speed");
		this.isOperating = nbt.getBoolean("isOperating");
		this.dx = nbt.getDouble("dx");
		this.dy = nbt.getDouble("dy");
		this.dz = nbt.getDouble("dz");
		this.sx = nbt.getDouble("sx");
		this.sy = nbt.getDouble("sy");
		this.sz = nbt.getDouble("sz");
		this.setTheta(nbt.getDouble("theta"));//go back distance
		this.goBack = nbt.getBoolean("back");
		this.distance = nbt.getFloat("distance");
		
		tinyWorld.readFromNBT(nbt);
	}
	
	public void writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		nbt.setFloat("speed", this.speed);
		nbt.setBoolean("isOperating", this.isOperating);
		nbt.setDouble("dx", this.dx);
		nbt.setDouble("dy", this.dy);
		nbt.setDouble("dz", this.dz);
		nbt.setDouble("sx", this.sx);
		nbt.setDouble("sy", this.sy);
		nbt.setDouble("sz", this.sz);
		nbt.setDouble("theta", this.getTheta());
		nbt.setBoolean("back", this.goBack);
		nbt.setFloat("distance", (float) this.distance);
		
		tinyWorld.writeToNBT(nbt);
	}
	
	public boolean generate(BlockAccess tinyWorld) {
		if(getWorldObj().isRemote)return false;//On client side,it doesn't work.
		int meta = getBlockMetadata() & 0x7;//Picking up Operator's direction.
		
		return tinyWorld.generate(
				xCoord +ForgeDirection.VALID_DIRECTIONS[meta].offsetX,
				yCoord +ForgeDirection.VALID_DIRECTIONS[meta].offsetY,
				zCoord +ForgeDirection.VALID_DIRECTIONS[meta].offsetZ,
				ForgeDirection.VALID_DIRECTIONS[meta].offsetX,
				ForgeDirection.VALID_DIRECTIONS[meta].offsetY,
				ForgeDirection.VALID_DIRECTIONS[meta].offsetZ,
				meta);
	}
	
	public void setSpeed(float param){
		this.speed = param;
	}
	
	public void updateEntity() {
		if(isOperating){
			if(doSetBlock){
				setBlocks();
				isOperating = false;
				doSetBlock = false;
				tinyWorld.dispose();
				goBack = !goBack;
				if(speed < 0){
					speed *= -1;
				}
			}
			if(getBlockType() == BOBlocks.translater){
				if(tinyWorld.destDistanceSq <= 0){
					setTheta(getTheta() + speed);
					if(theta > 360.0)setTheta(theta - 360.0);
					if(theta < 0.0)theta += 360.0;
					rot(theta, getBlockMetadata()&7);
				}else{
					sx += dx;
					sy += dy;
					sz += dz;
					distance += speed;
					if((sx*sx)+(sy*sy)+(sz*sz) > tinyWorld.destDistanceSq || distance <= 0){
						sx = tinyWorld.xDest;
						sy = tinyWorld.yDest;
						sz = tinyWorld.zDest;
						doSetBlock = true;
					}
					trans(sx, sy, sz);
				}
			}else{
				//rotate
			}
		}
	}
	private void rot(double deg, int dir){
		RotationHelper.genMatrix(deg, dir);
		for(Entry<Integer, BlockEntity> entry: tinyWorld.getBlockMap().entrySet()){
			BlockEntity bd = entry.getValue();
			bd.rot(RotationHelper.MATRIX);
		}
	}
	private void trans(double x, double y, double z){
		for(Entry<Integer, BlockEntity> entry: tinyWorld.getBlockMap().entrySet()){
			BlockEntity bd = entry.getValue();
			bd.trans(x, y, z);
		}
	}
	private void setBlocks(){
		for(Entry<Integer, BlockEntity> entry: tinyWorld.getBlockMap().entrySet()){
			BlockEntity bd = entry.getValue();
			bd.putTheBlock();
		}
	}

	/**
	 * 
	 * @param back Is this method called when braking up power.
	 * @return
	 */
	public boolean initialize() {
		if(isOperating){
			goBack = !goBack;
			speed *= -1;
			dx *= -1;
			dy *= -1;
			dz *= -1;
			tinyWorld.swap();
		}else{
			if(this.generate(tinyWorld)){
				if(tinyWorld.destDistanceSq > 0){
					double ratio = (double)speed/Math.sqrt(tinyWorld.destDistanceSq);
					dx = tinyWorld.xDest * ratio;
					dy = tinyWorld.yDest * ratio;
					dz = tinyWorld.zDest * ratio;
				}
				distance = sx = sy = sz = setTheta(0);
				isOperating = true;
				return true;
			}
		}
		return false;
	}
	
	public Packet getDescriptionPacket(){
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		this.writeToNBT(nbttagcompound);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, nbttagcompound);
	}
	//RedStone input will trigger TileEntity updating.
	@SideOnly(Side.CLIENT)
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt){
		readFromNBT(pkt.func_148857_g());
	}
	
	public boolean shouldRefresh(Block oldBlock, Block newBlock, int oldMeta, int newMeta, World world, int x, int y, int z){
		return oldBlock != newBlock;
	}

	public double getTheta() {
		return theta;
	}

	public double setTheta(double theta) {
		this.theta = theta;
		return theta;
	}
	
	public double getIntegratedX(){
		return sx;
	}
	public double getIntegratedY(){
		return sy;
	}
	public double getIntegratedZ(){
		return sz;
	}
}
