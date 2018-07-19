package knms.blockoperator.util;

public class Int3DCoordHelper {
	
	public static int make3DCoord(int x,int y,int z){
		return (x<<20)|((y&0xff)<<12)|(z&0xfff);
	}
	
	public static int readX(int data){
		return data>>20;
	}

	public static int readY(int data){
		return(data<<12)>>24;
	}
	
	public static int readZ(int data){
		return(data<<20)>>20;
	}
}
