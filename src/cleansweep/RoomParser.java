package cleansweep;

import java.io.*;

public class RoomParser {
	//TODO turn into interface? factory?
	//This class takes in a filename (parseFile) and spits out a Room object.
	
	private static int byteLEtoInt(byte[] b){
		int val = 0;
        for (int i=3; i>=0; i--) {
        	val=val*256;
        	val+=b[i];
        }
        return val;
	}
	
	private static Tile intsToTile(int[] is){
		int obstacle, carpet, dirt;
		obstacle=Tile.OBSTACLE_NONE;
		carpet=Tile.CARPET_BARE;
		if (is[0]==0x00) obstacle = Tile.OBSTACLE_BLOCK;
		else if (is[0]==0x55) obstacle = Tile.OBSTACLE_STAIRS;
		else if (is[0]==0xCC) carpet=Tile.CARPET_LOW;
		else if (is[0]==0x99) carpet=Tile.CARPET_HIGH;
		
		if (is[1]<0x65) dirt=(int) is[1];
		else dirt=0;
		System.out.println("CARPET:"+carpet);
		return new Tile(carpet, obstacle, dirt);
	}
	
	private static int intsToWall(int[] is){
		if (is[0]==0) 	return Room.WALL_WALL;
		if (is[0]==0x55) return Room.WALL_DOORCLOSED;
		if (is[0]==0xAA) return Room.WALL_DOOROPEN;
		if (is[0]==0xFF) return Room.WALL_NONE;
		return 0;
	}
	
	public static Room parseFile(String filename) throws IOException {
		try{
			FileInputStream f = new FileInputStream(filename);
			f.skip(2);
			byte[] buffer = new byte[4];
			f.read(buffer);
			int fsize = byteLEtoInt(buffer);
			f.skip(12);			
			f.read(buffer);
			int xSize = byteLEtoInt(buffer);
			f.read(buffer);
			int ySize = byteLEtoInt(buffer);
			f.skip(28);
			
			Room r = new Room((xSize-1)/2,(ySize-1)/2);
			Tile[] tilequeue = new Tile[((xSize-1)/2)*((ySize-1)/2)];
			
			buffer = new byte[3];
			int[] pixel = new int[3];
			for (int y=ySize-1; y>=0;y--){
				for(int x=0; x<xSize; x++){
					f.read(buffer);
					for(int i=0;i<3;i++){
						pixel[i] = buffer[2-i];
						if (pixel[i]<0){
							pixel[i]+=256;
						}
						System.out.print(pixel[i]+" ");
					}
					System.out.print("("+(y)+","+x+")");
					
					if (y%2==0 && (x)%2==1){
						System.out.print("Wall H");
						r.addWall(x/2, y/2, Room.DIR_N, intsToWall(pixel));
					} else if (y%2==1 && (x)%2==1){
						System.out.print("Tile" + ((y/2*(xSize-1)/2)+x/2));
						tilequeue[((y/2*(xSize-1)/2)+x/2)]=intsToTile(pixel);
					} else if (y%2==1){
						System.out.print("Wall V");
						r.addWall(x/2, y/2, Room.DIR_W, intsToWall(pixel));
					}
					System.out.println();
				}
				f.skip(4-((xSize*3)%4));
			}

			for(Tile t:tilequeue){
				r.addTile(t);
			}
			
			f.close();
			return r;
		} catch(IOException e){
			System.out.println(e.getMessage());
			throw e;
		}
	}
}
