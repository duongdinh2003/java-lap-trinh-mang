package findNumber;

import java.awt.Point;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class TimSoServer {
	
	int n = 5;
	List<Point> dachon = new ArrayList<Point>();
	List<Integer> listNumber = new ArrayList<Integer>();
	Vector<XuLyClient> listPlayer = new Vector<XuLyClient>();
	int[][] matrix = new int[n][n];
	String listNum;
	
	public TimSoServer() {
		try {
			ServerSocket server = new ServerSocket(5555);
			
			for (int i=1; i<=n*n; i++) {
				listNumber.add(i);
			}
			
			Collections.shuffle(listNumber);
			
			int index = 0;
			listNum = "";
			for (int i=0; i<n; i++) {
				for (int j=0; j<n; j++) {
					matrix[i][j] = listNumber.get(index);
					listNum += listNumber.get(index) + ",";
					index++;
				}
			}
			
			for (int i=0; i<n; i++) {
				for (int j=0; j<n; j++) {
					System.out.print(matrix[i][j] + "\t");
				}
				System.out.println();
			}
			
			while (true) {
				try {
					Socket socket = server.accept();
					XuLyClient client = new XuLyClient(socket, this);
					listPlayer.add(client);
					client.start();
					
					if (listPlayer.size() < 4) {
						DataOutputStream dos = new DataOutputStream(client.socket.getOutputStream());
						dos.writeUTF("waiting for other players...");
					}
					
					if (listPlayer.size() == 4) {
						System.out.println(listNum);
						DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
						dos.writeUTF(listNum);
						dos.writeUTF(4+"");
						for (int i=0; i<listPlayer.size() - 1; i++) {
							DataOutputStream dto = new DataOutputStream(listPlayer.get(i).socket.getOutputStream());
							dto.writeUTF(i+1+"");
						}
						
					}
					
					if (listPlayer.size() > 4) {
						DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
						dos.writeUTF(listNum);
						
					}
					
					
//					index = 0;
//					for (int i=0; i<n; i++) {
//						for (int j=0; j<n; j++) {
//							dos.writeUTF(listNumber.get(index) + "");
//							index++;
//						}
//					}
				} catch (Exception e) {
					System.out.println(e.getMessage() + "\tLoi 1");
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage() + "\tLoi 2");
		}
	}

	public static void main(String[] args) {
		new TimSoServer();
	}

}

class XuLyClient extends Thread {
	Socket socket;
	TimSoServer server;
	DataInputStream dis;
	DataOutputStream dos;
	
	public XuLyClient(Socket socket, TimSoServer server) {
		this.socket = socket;
		this.server = server;
	}
	
	public void run() {
		if (server.listPlayer.size() >= 4) {
			for (int i=0; i<server.listPlayer.size(); i++) {
				try {
					DataOutputStream dos = new DataOutputStream(server.listPlayer.get(i).socket.getOutputStream());
					dos.writeUTF(server.listNum);
				} catch (Exception e) {
					System.out.println("haha");
				}
			}
			
			for (int i=0; i<server.dachon.size(); i++) {
				try {
					dos = new DataOutputStream(socket.getOutputStream());
					dos.writeUTF(server.dachon.get(i).x + "");
					dos.writeUTF(server.dachon.get(i).y + "");
//					System.out.println("\n" + server.dachon.get(i).x + ", " + server.dachon.get(i).y);
				} catch (Exception e) {
					System.out.println("hihi");
				}
			}
		}
		
		
		
		while (true) {
			try {
				dis = new DataInputStream(socket.getInputStream());
				int ix = Integer.parseInt(dis.readUTF());
				int iy = Integer.parseInt(dis.readUTF());
				
				// Kiem tra xem da du 4 nguoi choi hay chua
				if (server.listPlayer.size() < 4) {
					continue;
				}
				
				// Neu la nguoi thu 5 tro di thi chi duoc xem
				if (server.listPlayer.size() >= 5) {
					if (this != server.listPlayer.get(0) && this != server.listPlayer.get(1) &&
							this != server.listPlayer.get(2) && this != server.listPlayer.get(3))
						continue;
				}
				
				// Kiem tra luot danh cua tung nguoi co hop le hay khong
				if (server.dachon.size() < 1) {
					if (numberAt(ix, iy) != 1)
						continue;
				} else {
					if (numberAt(ix, iy) != (server.dachon.size()+1))
						continue;
				}
				
				
				// Kiem tra o chon co hop le hay khong
				boolean ok = true;
				for (Point p : server.dachon) {
					if (ix == p.x && iy == p.y) {
						ok = false;
						break;
					}
				}
				
				if (ok) {
					server.dachon.add(new Point(ix, iy));
					for (int i=0; i<server.listPlayer.size(); i++) {
						try {
							dos = new DataOutputStream(server.listPlayer.get(i).socket.getOutputStream());
							dos.writeUTF(ix + "");
							dos.writeUTF(iy + "");
							if (this == server.listPlayer.get(0)) {
								dos.writeUTF(1+"");
							}
							if (this == server.listPlayer.get(1)) {
								dos.writeUTF(2+"");
							}
							if (this == server.listPlayer.get(2)) {
								dos.writeUTF(3+"");
							}
							if (this == server.listPlayer.get(3)) {
								dos.writeUTF(4+"");
							}
						} catch (Exception e) {
							System.out.println(e.getMessage() + "\tLoi 3");
						}
					}
				}
			} catch (Exception e) {
				System.out.println(e.getMessage() + "\tLoi 4");
			}
		}
	}
	
	public int numberAt(int col, int row) {
		for (Integer itg : server.listNumber) {
			if (itg == server.matrix[col][row]) {
				return itg;
			}
		}
		return 0;
	}
}
