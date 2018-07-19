package knms.blockoperator;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import knms.blockoperator.block.BOBlocks;
import knms.blockoperator.client.gui.BOGUI;
import knms.blockoperator.entity.BlockEntity;
import knms.blockoperator.net.PacketHandler;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

@Mod(modid = BlockOperator.MODID,name=BlockOperator.MODID, version = BlockOperator.VERSION)
public class BlockOperator
{
	public static final String MODID = "blockoperator";
	public static final String VERSION = "alpha";
	public static int size = 1024;
	
	@Mod.Instance(MODID)
	public static BlockOperator instance;
    
	@SidedProxy(clientSide = "knms.blockoperator.ClientProxy", serverSide = "knms.blockoperator.CommonProxy")
	public static CommonProxy proxy;
	
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
		try{
			cfg.load();
			Property Prop2 = cfg.get("Setting", "Size", 1024);
			Prop2.comment  = "The maximum amount of MOVE-Block:can be placed in Minecraft World.(DEFAULT:1024)";
			size = Prop2.getInt();
		}finally{
			cfg.save();
		}
	}
	@EventHandler
	public void init(FMLInitializationEvent event){

		BOBlocks.registerBlocks();
		PacketHandler.init();
		EntityRegistry.registerModEntity(BlockEntity.class, "BlockEntity",0, this, 64, 1, true);
		proxy.registerTileEntity();
		proxy.registerEntityRenderer();
		NetworkRegistry.INSTANCE.registerGuiHandler(this.instance, new BOGUI());
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		proxy.registerIcons();
	}
}
