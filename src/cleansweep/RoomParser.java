package cleansweep;

import java.io.*;

public class RoomParser {
	//TODO turn into interface? factory?
	//This class takes in a filename (parseFile) and spits out a Room object.
	
	private static int byteLEtoInt(byte[] b){
		int val = 0;
        for (int i=0; i<4; i++) {
            val=val<<8;
            val=val|(b[i] & 0xFF);
        }
        return val;
	}
	
	public static Room parseFile(String filename) throws IOException {
		try{
			FileInputStream f = new FileInputStream(filename);
			f.skip(4);
			byte[] buffer = new byte[4];
			f.read(buffer);
			int fsize = byteLEtoInt(buffer);
			f.skip(12);
			
			System.out.println(fsize);
			//TODO parse the file here. 
			
			Room r = new Room(10,10);
			
			f.close();
			return r;
		} catch(IOException e){
			System.out.println(e.getMessage());
			throw e;
		}
	}
}
