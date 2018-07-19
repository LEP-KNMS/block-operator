package knms.blockoperator.client.renderer;

import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.opengl.GL11;

import knms.blockoperator.entity.BlockEntity;
import knms.blockoperator.tileentity.TileBlockOperator;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

public class TileBlockOperatorRenderer  extends TileEntitySpecialRenderer{

	protected RenderBlocks renderer = new RenderBlocks();
	
	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z,float time) {
		renderTileEntityAt((TileBlockOperator)tile, x, y, z, time);
	}
	/**
	 * 
	 * @param tile
	 * @param x
	 * @param y
	 * @param z
	 * @param time 前回Tickから何Tick進んだかをfloat精度で渡す
	 */
	public void renderTileEntityAt(TileBlockOperator tile, double x, double y, double z,float time) {
		if(!tile.isOperating)return;
		GL11.glPushMatrix();
		RenderHelper.disableStandardItemLighting();
		Tessellator tessellator = Tessellator.instance;
		bindTexture(TextureMap.locationBlocksTexture);
		renderer.blockAccess = tile.tinyWorld;
		tessellator.setTranslation(-0.5, -0.5, -0.5);
		tessellator.startDrawingQuads();
		Map<Integer, BlockEntity> map = tile.tinyWorld.getBlockMap();
		for(Entry<Integer, BlockEntity> entry: map.entrySet()){
			BlockEntity bd = entry.getValue();
			renderer.renderBlockByRenderType(bd.block, bd.localX, bd.localY, bd.localZ);
		}
		//drawDestroyLayer((BlockObj) arg0);
		GL11.glTranslated(x+0.5, y+0.5, z+0.5);
		int meta = tile.getBlockMetadata()&7;
		GL11.glRotated(
				tile.getTheta(),
				ForgeDirection.VALID_DIRECTIONS[meta].offsetX,
				ForgeDirection.VALID_DIRECTIONS[meta].offsetY,
				ForgeDirection.VALID_DIRECTIONS[meta].offsetZ);
		GL11.glTranslated(tile.getIntegratedX(), tile.getIntegratedY(), tile.getIntegratedZ());
		tessellator.draw();
		tessellator.setTranslation(0, 0, 0);
		/*MovingObjectPosition mop = Minecraft.getMinecraft().objectMouseOver;
		if(mop != null){
			if(mop.entityHit != null && mop.entityHit == arg0.moveableWorld.selectedBlock){
				BlockPos pos = arg0.moveableWorld.selectedBlock.getPosition();
				renderSelectionBox(arg0.moveableWorld.selectedBlock.getBlockType().getSelectedBoundingBoxFromPool((World) field_147909_c.blockAccess, pos.x, pos.y, pos.z));
			}
		}*/
		
		//GL11.glLoadIdentity();
		RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();
	}
	
	private static void renderSelectionBox(AxisAlignedBB aabb){
		GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
		GL11.glLineWidth(2.0F);
		GL11.glDisable(3553);
		GL11.glDepthMask(false);
		
		float f1 = 0.002F;
		aabb = aabb.expand(f1, f1, f1);
		
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawing(GL11.GL_LINE_STRIP);
		tessellator.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
		tessellator.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
		tessellator.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
		tessellator.addVertex(aabb.minX, aabb.minY, aabb.minZ);
		tessellator.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
		
		tessellator.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
		tessellator.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
		tessellator.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
		tessellator.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
		tessellator.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
		tessellator.draw();
		
		tessellator.startDrawing(GL11.GL_LINES);
		tessellator.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
		tessellator.addVertex(aabb.minX, aabb.minY, aabb.minZ);
		
		tessellator.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
		tessellator.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
		
		tessellator.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
		tessellator.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
		tessellator.draw();
		
		GL11.glDepthMask(true);
		GL11.glEnable(3553);
	}
	
	private void drawDestroyLayer(BlockEntity entity){
		//if(9 < entity.damage)entity.damage = 9;
		//renderer.renderBlockUsingTexture(Blocks.stone, 0, 0, 0, RedMove.proxy.getDestroyIcons()[MathHelper.floor_float(entity.damage)]);
	}

}
