package knms.blockoperator.client.gui;

import knms.blockoperator.inventory.ContainerTheta;
import knms.blockoperator.net.MessageGuiData;
import knms.blockoperator.net.PacketHandler;
import knms.blockoperator.tileentity.TileBlockOperator;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class GuiBlockOperator extends GuiContainer {

	private static final ResourceLocation TEXTURE = new ResourceLocation("blockoperator", "textures/gui/gui_texture.png");
	private TileBlockOperator tile;
	private float speed=0.001f;
	
	public GuiBlockOperator(TileEntity tileEntity) {
		super(new ContainerTheta());
		if(tileEntity instanceof TileBlockOperator)this.tile=(TileBlockOperator)tileEntity;
		xSize = 128;
		ySize = 128;
	}

	public void initGui(){
		super.initGui();
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(0, guiLeft+31, guiTop+95, 66, 20,"Done"));
		this.buttonList.add(new GuiButton(1, guiLeft+10, guiTop+23, 20, 20,"-"));
		this.buttonList.add(new GuiButton(2, guiLeft+98, guiTop+23, 20, 20,"+"));
	}
	
    /*GUIの背景の描画処理*/
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseZ) {
        this.mc.renderEngine.bindTexture(TEXTURE);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 72, xSize, ySize);
        fontRendererObj.drawString("SPEED", guiLeft+7, guiTop+30, 0x000000);
        fontRendererObj.drawString(Float.toString(speed), guiLeft+57, guiTop+30, 0xffffff);
    }
 
    /*GUIが開いている時にゲームの処理を止めるかどうか。*/
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    protected void actionPerformed(GuiButton button){
    	if(button.id==0){
    		this.mc.thePlayer.closeScreen();
    	}else if(button.id==1){
    		speed-=0.001;
    	}else if(button.id==2){
    		speed+=0.001;
    	}
    	if(speed<0){
    		speed=0;
    	}else if(speed>10){
    		speed=10;
    	}
    }
    
    public void onGuiClosed(){
    	if(tile!=null){
    		tile.speed = speed;
    		MessageGuiData message = new MessageGuiData();
    		message.setPosition(tile.xCoord, tile.yCoord, tile.zCoord);
    		message.setSpeed(speed);
    		PacketHandler.INSTANCE.sendToServer(message);
    	}
    }

}