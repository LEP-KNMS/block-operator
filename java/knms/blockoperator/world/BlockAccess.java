package knms.blockoperator.world;

import java.util.HashMap;
import java.util.Map;

import knms.blockoperator.block.BOBlocks;
import knms.blockoperator.entity.BlockEntity;
import knms.blockoperator.tileentity.TileBlockOperator;
import knms.blockoperator.util.Int3DCoordHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockAccess implements IBlockAccess{

	private Map<Integer,BlockEntity>blockMap;
	private World worldObj;
	/**
	 * Coordinations of destination
	 */
	public int xDest=0, yDest=0, zDest=0;
	private int tempX=0, tempY=0, tempZ=0;
	public int destDistanceSq = 0;
	
	private TileBlockOperator tileEntity;//owner
	
	
	public BlockAccess(TileBlockOperator tile){
		blockMap = new HashMap<Integer,BlockEntity>();
		tileEntity = tile;
	}
	
	public void setWorldObj(World world){
		this.worldObj = world;
	}
	
	@Override
	public Block getBlock(int x, int y, int z) {
		if(-128<=y&&y<=127&&-2048<=x&&x<=2047&&-2048<=z&&z<=2047){
			BlockEntity block = getBlockMap().get(Int3DCoordHelper.make3DCoord(x, y, z));
			if(block!=null)return block.block;
		}
		return Blocks.air;
	}

	@Override
	public TileEntity getTileEntity(int p_147438_1_, int p_147438_2_, int p_147438_3_) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public int getLightBrightnessForSkyBlocks(int p_72802_1_, int p_72802_2_, int p_72802_3_, int p_72802_4_) {
		// TODO 自動生成されたメソッド・スタブ
		return 0xffffff;
	}

	@Override
	public int getBlockMetadata(int x, int y, int z) {
		if(-128<=y&&y<=127&&-2048<=x&&x<=2047&&-2048<=z&&z<=2047){
			BlockEntity block = getBlockMap().get(Int3DCoordHelper.make3DCoord(x, y, z));
			if(block!=null)return block.meta;
		}
		return 0;
	}

	@Override
	public int isBlockProvidingPowerTo(int p_72879_1_, int p_72879_2_, int p_72879_3_, int p_72879_4_) {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public boolean isAirBlock(int x, int y, int z) {
		// TODO 自動生成されたメソッド・スタブ
		return getBlock(x, y, z).isAir(this, x, y, z);
	}

	@Override
	public BiomeGenBase getBiomeGenForCoords(int p_72807_1_, int p_72807_2_) {
		// TODO 自動生成されたメソッド・スタブ
		return BiomeGenBase.plains;
	}

	@Override
	public int getHeight() {
		// TODO 自動生成されたメソッド・スタブ
		return 10;
	}

	@Override
	public boolean extendedLevelsInChunkCache() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public boolean isSideSolid(int x, int y, int z, ForgeDirection side, boolean _default) {
		// TODO 自動生成されたメソッド・スタブ
		if (x < -2048 || z < -2048 || x >= 2047 || z >= 2047){
			return _default;
		}
		return getBlock(x, y, z).isSideSolid(this, x, y, z, side);
	}

	public boolean generate(int xCoord, int yCoord, int zCoord, int relatedX, int relatedY, int relatedZ, int meta) {
		if(relatedY<-128&&127<relatedY&&relatedX<-2048&&2047<relatedX&&2047<relatedZ&&relatedZ<-2048)return false;
		int coords = Int3DCoordHelper.make3DCoord(relatedX, relatedY, relatedZ);
		if(coords!=0 && !getBlockMap().containsKey(coords)){
			Block block = worldObj.getBlock(xCoord, yCoord, zCoord);
			int metadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
			if(canReplaceBlockToEntity(block, xCoord, yCoord, zCoord)){
				
				BlockEntity blockEntity = new BlockEntity(worldObj);
				blockEntity.setPosition(((double)xCoord)+0.5D, (double)yCoord, ((double)zCoord)+0.5D);
				blockEntity.setArgs(block, relatedX, relatedY, relatedZ, (byte) metadata, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
				blockEntity.tile = worldObj.getTileEntity(xCoord, yCoord, zCoord);
				blockEntity.tinyWorld = this;
				getBlockMap().put(coords,blockEntity);
				
				worldObj.setBlockToAir(xCoord, yCoord, zCoord);
				
				worldObj.spawnEntityInWorld(blockEntity);
				
				if(block==BOBlocks.target_block){
					if(tileEntity.getBlockType()==BOBlocks.translater){
						if(metadata==0^tileEntity.goBack){
							metadata = tileEntity.getBlockMetadata()&0x7;
							int distanceSquare;
							if((metadata==0&&relatedY==-1)||(metadata==1&&relatedY==1)){
								distanceSquare = (relatedX*relatedX)+(relatedZ*relatedZ);
								if(destDistanceSq==0||distanceSquare<destDistanceSq){
									destDistanceSq = distanceSquare;
									xDest = -relatedX;
									zDest = -relatedZ;
								}
							}else if((metadata==2&&relatedZ==-1)||(metadata==3&&relatedZ==1)){
								distanceSquare = (relatedX*relatedX)+(relatedY*relatedY);
								if(destDistanceSq==0||distanceSquare<destDistanceSq){
									destDistanceSq = distanceSquare;
									xDest = -relatedX;
									yDest = -relatedY;
								}
							}else if((metadata==4&&relatedX==-1)||(metadata==5&&relatedX==1)){
								distanceSquare = (relatedZ*relatedZ)+(relatedY*relatedY);
								if(destDistanceSq==0||distanceSquare<destDistanceSq){
									destDistanceSq = distanceSquare;
									zDest = -relatedZ;
									yDest = -relatedY;
								}
							}
						}
					}
				}else if(block == BOBlocks.theta_block){
					//rotator
				}
				
				for(int i=0;i<6;i++){
					if(i==ForgeDirection.OPPOSITES [meta])continue;
					generate(
							xCoord+ForgeDirection.VALID_DIRECTIONS[i].offsetX,
							yCoord+ForgeDirection.VALID_DIRECTIONS[i].offsetY,
							zCoord+ForgeDirection.VALID_DIRECTIONS[i].offsetZ,
							relatedX+ForgeDirection.VALID_DIRECTIONS[i].offsetX,
							relatedY+ForgeDirection.VALID_DIRECTIONS[i].offsetY,
							relatedZ+ForgeDirection.VALID_DIRECTIONS[i].offsetZ,
							i);
				}
				return true;
			}
		}
		return false;
	}

	private boolean canReplaceBlockToEntity(Block target, int x, int y, int z){
		return !target.isAir(worldObj, x, y, z);
	}

	public Map<Integer,BlockEntity> getBlockMap() {
		return blockMap;
	}
	
	public void readFromNBT(NBTTagCompound nbt) {
		int coordination = nbt.getInteger("dest");
		this.xDest = Int3DCoordHelper.readX(coordination);
		this.yDest = Int3DCoordHelper.readY(coordination);
		this.zDest = Int3DCoordHelper.readZ(coordination);
		coordination = nbt.getInteger("tmp");
		this.tempX = Int3DCoordHelper.readX(coordination);
		this.tempY = Int3DCoordHelper.readY(coordination);
		this.tempZ = Int3DCoordHelper.readZ(coordination);
		this.destDistanceSq = nbt.getInteger("dds");
	}
	
	public void writeToNBT(NBTTagCompound nbt){
		int destination = Int3DCoordHelper.make3DCoord(xDest, yDest, zDest);
		int tmpCoord = Int3DCoordHelper.make3DCoord(tempX, tempY, tempZ);
		nbt.setInteger("dest", destination);
		nbt.setInteger("tmp", tmpCoord);
		nbt.setInteger("dds", destDistanceSq);
	}
	
	public void dispose(){
		blockMap.clear();
		xDest = yDest = zDest = destDistanceSq = 0;
		tempX = tempY = tempZ = 0;
	}
	
	public void swap(){
		int i = xDest;
		xDest = tempX;
		tempX = i;
		i = yDest;
		yDest = tempY;
		tempY = i;
		i = zDest;
		zDest = tempZ;
		tempZ = i;
	}
}
