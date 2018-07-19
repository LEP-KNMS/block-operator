package knms.blockoperator.entity;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.registry.GameData;
import knms.blockoperator.net.PacketHandler;
import knms.blockoperator.tileentity.TileBlockOperator;
import knms.blockoperator.util.Int3DCoordHelper;
import knms.blockoperator.world.BlockAccess;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockEntity extends Entity{
	
	public float rotationRoll;
	public float prevRotationRoll;
	
	public BlockAccess tinyWorld;
	
	public Block block;
	public TileEntity tile;
	public int meta;
	public int localX, localY, localZ;
	private int ownerX,ownerY,ownerZ;
	boolean initialized=false;

	public BlockEntity(World p_i1582_1_) {
		super(p_i1582_1_);
		this.ignoreFrustumCheck = true;
		this.isImmuneToFire = true;
		this.forceSpawn=true;
        this.yOffset = 0F;
        this.stepHeight = 0.0F;
        setSize(1,1);
	}
	public void moveEntity(double x, double y, double z) {
		setPosition(this.posX + x, this.posY + y, this.posZ + z);
	}
	protected boolean canTriggerWalking(){
		return false;
	}
	
	protected void entityInit(){
		this.dataWatcher.addObject(3,new Integer(0));//blockId
		this.dataWatcher.addObject(4,new Integer(0));//3d coordinations
		this.dataWatcher.addObject(5,new Integer(0));
		this.dataWatcher.addObject(6,new Integer(0));
		this.dataWatcher.addObject(7,new Byte((byte) 0));//meta
		this.dataWatcher.addObject(8,new Integer(0));
		this.dataWatcher.addObject(9,new Integer(0));
		this.dataWatcher.addObject(10,new Integer(0));
	}
	
	public void updateProperties(){
		this.block = Block.getBlockById(this.dataWatcher.getWatchableObjectInt(3));
		this.localX = this.dataWatcher.getWatchableObjectInt(4);
		this.localY = this.dataWatcher.getWatchableObjectInt(5);
		this.localZ = this.dataWatcher.getWatchableObjectInt(6);
		this.meta = this.dataWatcher.getWatchableObjectByte(7);
		this.ownerX = this.dataWatcher.getWatchableObjectInt(8);
		this.ownerY = this.dataWatcher.getWatchableObjectInt(9);
		this.ownerZ = this.dataWatcher.getWatchableObjectInt(10);
		setPosition(ownerX + localX + 0.5D, ownerY + localY, ownerZ + localZ + 0.5D);
	}
	
	public void setArgs(Block b,int x, int y, int z, byte meta, int px, int py, int pz){
		block = b;
		this.localX = x;
		this.localY = y;
		this.localZ = z;
		this.meta = meta;
		this.ownerX = px;
		this.ownerY = py;
		this.ownerZ = pz;
		this.dataWatcher.updateObject(3,new Integer(GameData.getBlockRegistry().getId(block)));//blockId
		this.dataWatcher.updateObject(4,new Integer(x));//coordination
		this.dataWatcher.updateObject(5,new Integer(y));
		this.dataWatcher.updateObject(6,new Integer(z));
		this.dataWatcher.updateObject(7,new Byte(meta));//meta
		this.dataWatcher.updateObject(8,new Integer(px));//coordination
		this.dataWatcher.updateObject(9,new Integer(py));
		this.dataWatcher.updateObject(10,new Integer(pz));
	}
	
	public AxisAlignedBB getBoundingBox(){
		return boundingBox;
	}

	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_){
		return true;
	}
	public boolean interactFirst(EntityPlayer par1EntityPlayer) {
		return false;
	}
	
	public boolean canBeCollidedWith(){
		return !this.isDead;
	}
	
	public void onUpdate(){
		
		if(!initialized){
			if(this.worldObj.isRemote){
				this.updateProperties();
				TileEntity tile = worldObj.getTileEntity(ownerX, ownerY, ownerZ);
				if(tile instanceof TileBlockOperator){
					tinyWorld = ((TileBlockOperator)tile).tinyWorld;
					tinyWorld.getBlockMap().put(Int3DCoordHelper.make3DCoord(localX, localY, localZ), this);
				}
			}
			this.initialized = tinyWorld != null;
		}
		
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		if (this.posY < -16.0D){
			this.setDead();
		}
		if (this.worldObj.isRemote){
			/*if (this.turnProgress > 0){
				double var19 = this.posX + (this.newPosX - this.posX) / (double)this.turnProgress;
				double var21 = this.posY + (this.newPosY - this.posY) / (double)this.turnProgress;
				double var5 = this.posZ + (this.newPosZ - this.posZ) / (double)this.turnProgress;
				--this.turnProgress;
				this.setPosition(var19, var21, var5);
			}else{
				this.setPosition(this.posX, this.posY, this.posZ);
			}*/
		}else{
			this.func_145775_I();
		}
		
		onEntityUpdate();
	}

	public void onEntityUpdate(){}

	public float getShadowSize(){
		return 0.0F;
	}
	
	public void setVelocity(double p_70016_1_, double p_70016_3_, double p_70016_5_){
		this.motionX = p_70016_1_;
		this.motionY = p_70016_3_;
		this.motionZ = p_70016_5_;
	}
	
	public void rot(double[][] mat){
		double newX = mat[0][0]*localX + mat[0][1]*localY + mat[0][2]*localZ;
		double newY = mat[1][0]*localX + mat[1][1]*localY + mat[1][2]*localZ;
		double newZ = mat[2][0]*localX + mat[2][1]*localY + mat[2][2]*localZ;
		setPosition(ownerX + newX + 0.5D, ownerY + newY, ownerZ + newZ + 0.5D);
	}
	
	public void trans(double x, double y, double z){
		setPosition(ownerX + localX + x + 0.5D, ownerY + localY + y, ownerZ + localZ + z + 0.5D);
	}

	public boolean handleWaterMovement(){return false;}
	public boolean handleLavaMovement(){return false;}
	public void moveFlying(float p_70060_1_, float p_70060_2_, float p_70060_3_) {}
	public void setPositionAndRotation(double posX, double posY, double posZ, float yaw, float pitch) {
		//setNewPosition(posX, posY, posZ, 3);
	}
	public void setPositionAndRotation2(double x, double y, double z, float p_70056_7_, float p_70056_8_, int turn){
		//setNewPosition(x, y, z, turn);
	}
	public void setLocationAndAngles(double posX, double posY, double posZ, float yaw, float pitch) {
		//setNewPosition(posX, posY, posZ, 3);
	}
	public void tryUpdatePos(double x, double y, double z){
		//setNewPosition(x, y, z, 3);
		this.worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox, PacketHandler.collidingSelector);
	}
	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		int coords = nbt.getInteger("local_coords");
		localX = Int3DCoordHelper.readX(coords);
		localY = Int3DCoordHelper.readY(coords);
		localZ = Int3DCoordHelper.readZ(coords);
		
		ownerX = nbt.getInteger("ownerX");
		ownerY = nbt.getInteger("ownerY");
		ownerZ = nbt.getInteger("ownerZ");
		
		block = Block.getBlockById(nbt.getInteger("block_id"));
		meta = nbt.getInteger("meta");
		
		tile.readFromNBT(nbt);
	}
	@Override
	protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		
	}
	
	public List getCollidingBoxes(AxisAlignedBB aabb){
		List result = new ArrayList();
		int i = MathHelper.floor_double(aabb.minX);
		int j = MathHelper.floor_double(aabb.maxX + 1.0D);
		int k = MathHelper.floor_double(aabb.minY);
		int l = MathHelper.floor_double(aabb.maxY + 1.0D);
		int i1 = MathHelper.floor_double(aabb.minZ);
		int j1 = MathHelper.floor_double(aabb.maxZ + 1.0D);
		
		for (int k1 = i; k1 < j; k1++){
			for (int l1 = i1; l1 < j1; l1++){
				if (worldObj.blockExists(k1, 64, l1)){
					
					for (int i2 = k - 1; i2 < l; i2++){
						Block block = Blocks.stone;;
						if ((k1 >= -30000000) && (k1 < 30000000) && (l1 >= -30000000) && (l1 < 30000000)){
							block = worldObj.getBlock(k1, i2, l1);
						}
						
						block.addCollisionBoxesToList(this.worldObj, k1, i2, l1, aabb, result, this);
					}
				}
			}
		}
		List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, aabb, PacketHandler.collidingSelector);
		
		for (int j2 = 0; j2 < list.size(); j2++){
			Entity entity = (Entity)list.get(j2);
			if(entity != this){
				AxisAlignedBB axisalignedbb1 = entity.getBoundingBox();
				if ((axisalignedbb1 != null) && (axisalignedbb1.intersectsWith(aabb))){
					result.add(axisalignedbb1);
				}
			}
		}
		return result;
	}
	public Block getBlockType() {
		return block;
	}
	public void putTheBlock() {
		this.worldObj.setBlock(MathHelper.floor_double(posX),MathHelper.floor_double(posY),MathHelper.floor_double(posZ),block,meta,2);
		this.setDead();
	}
}
