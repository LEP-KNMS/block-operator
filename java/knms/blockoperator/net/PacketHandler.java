package knms.blockoperator.net;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import knms.blockoperator.BlockOperator;
import knms.blockoperator.entity.BlockEntity;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;

public class PacketHandler {

	//このMOD用のSimpleNetworkWrapperを生成。チャンネルの文字列は固有であれば何でも良い。MODIDの利用を推奨。
	//チャンネル名は20文字以内の文字数制限があるので注意。
	public static SimpleNetworkWrapper INSTANCE = null;
	
	public static IEntitySelector collidingSelector = new IEntitySelector(){
		public boolean isEntityApplicable(Entity entity){
			return entity.isDead ? false: entity instanceof BlockEntity;
		}
	};


	public static void init() {

		/**IMesssageHandlerクラスとMessageクラスの登録。
		 *第三引数：MessageクラスのMOD内での登録ID。256個登録できる
		 *第四引数：送り先指定。クライアントかサーバーか、Side.CLIENT Side.SERVER*/
		//PacketHandler.INSTANCE.sendToServer(new MessageSample(data));
		//PacketHandler.INSTANCE.sendToAll(new MessageSurface());
		INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(BlockOperator.MODID);
		INSTANCE.registerMessage(MessageGuiDataHandler.class, MessageGuiData.class, 0, Side.SERVER);
		INSTANCE.registerMessage(MessageBlockUpdateHandler.class, MessageBlockUpdate.class, 1, Side.SERVER);
	}
}