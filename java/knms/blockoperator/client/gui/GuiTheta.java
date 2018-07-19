package knms.blockoperator.client.gui;

import knms.blockoperator.inventory.ContainerTheta;
import knms.blockoperator.net.MessageBlockUpdate;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiTheta extends GuiContainer {

	private static final ResourceLocation TEXTURE = new ResourceLocation("blockoperator", "textures/gui/gui_texture.png");
    private int degree=0;
    private World p_w;
	private int x;
	private int y;
	private int z;
	
	public GuiTheta(World world, int x, int y, int z) {
		super(new ContainerTheta());
		xSize = 128;
		ySize = 71;
		this.p_w=world;
		this.x=x;
		this.y=y;
		this.z=z;
		degree=world.getBlockMetadata(x,y,z)*90;
	}

	public void initGui(){
		super.initGui();
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(0, guiLeft+31, guiTop+46, 66, 20,"Done"));
		this.buttonList.add(new GuiButton(1, guiLeft+10, guiTop+23, 20, 20,"-"));
		this.buttonList.add(new GuiButton(2, guiLeft+98, guiTop+23, 20, 20,"+"));
	}
	
    /*GUIの背景の描画処理*/
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseZ) {
        this.mc.renderEngine.bindTexture(TEXTURE);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, xSize, ySize);
        fontRendererObj.drawString("Rotation Angle", guiLeft+26, guiTop+8, 0x000000);
        fontRendererObj.drawString(Integer.toString(degree), guiLeft+50, guiTop+30, 0xffffff);
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
    		degree-=90;
    	}else if(button.id==2){
    		degree+=90;
    	}
    	if(degree<0){
    		degree=0;
    	}else if(degree>1080){
    		degree=1080;
    	}
    }
    
    public void onGuiClosed(){
    	knms.blockoperator.net.PacketHandler.INSTANCE.sendToServer(new MessageBlockUpdate().setPosition(x,y,z).setMetadata(degree/90));
    }

}
