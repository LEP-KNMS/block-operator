package knms.blockoperator;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import knms.blockoperator.client.renderer.TileBlockOperatorRenderer;
import knms.blockoperator.tileentity.TileBlockOperator;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ClientProxy extends CommonProxy{
	
	public void registerTileEntity() {
		GameRegistry.registerTileEntity(TileBlockOperator.class, "blockoperator");
	}
	
	public void registerEntityRenderer(){
		ClientRegistry.bindTileEntitySpecialRenderer(TileBlockOperator.class, new TileBlockOperatorRenderer());
		//RenderingRegistry.registerEntityRenderingHandler(BlockEntity.class, new EntityNullRenderer());
	}
	
	public World getWorld(){
		return Minecraft.getMinecraft().theWorld;
	}
	
	public void registerIcons(){
		this.destroyBlockIcons = new IIcon[10];
		for (int i = 0; i < 10; i++){
			this.destroyBlockIcons[i] =Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry("destroy_stage_" + i);
		}
	}
	
	public IIcon[] getDestroyIcons(){
		return destroyBlockIcons;
	}

}
