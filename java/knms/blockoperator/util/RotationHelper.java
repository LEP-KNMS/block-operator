package knms.blockoperator.util;

import net.minecraftforge.common.util.ForgeDirection;

public class RotationHelper {
	
	private static double sin,cos;
	public static final double[][] MATRIX = new double[3][3];
	
	public static void genMatrix(double deg,int meta){
		double rad = (Math.PI * deg)/180;
		sin = Math.sin(rad);
		cos = Math.cos(rad);
		double x = ForgeDirection.VALID_DIRECTIONS[meta].offsetX;
		double y = ForgeDirection.VALID_DIRECTIONS[meta].offsetY;
		double z = ForgeDirection.VALID_DIRECTIONS[meta].offsetZ;
		MATRIX[0][0] = x*x + (1-x*x)*cos;//x!=0?1:cos;
		MATRIX[0][1] = x*y*(1-cos) - z*sin;
		MATRIX[0][2] = x*z*(1-cos) + y*sin;
		MATRIX[1][0] = x*y*(1-cos) + z*sin;
		MATRIX[1][1] = y*y + (1-y*y)*cos;
		MATRIX[1][2] = z*y*(1-cos) - x*sin;
		MATRIX[2][0] = x*z*(1-cos) - y*sin;
		MATRIX[2][1] = z*y*(1-cos) + x*sin;
		MATRIX[2][2] = z*z + (1-z*z)*cos;
	}
	

}
