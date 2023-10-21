package findNumber;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class TimSoClient extends JFrame implements MouseListener, Runnable{
	
	int n = 5;
	int s = 50;
	int off = 50;
	int score = 0;
	int turn = 0;
	List<Point> dachon = new ArrayList<Point>();
	List<Integer> chon = new ArrayList<Integer>();
	List<Integer> listNumber = new ArrayList<Integer>();
	String thbao, nums, clr;
	Socket socket;

	public static void main(String[] args) {
		new TimSoClient();
	}

	public TimSoClient() {
		
		try {
			this.socket = new Socket("localhost", 5555);
			
			
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			String str = dis.readUTF();
			System.out.println(str);
			
			if (str.equals("waiting for other players...")) {
				thbao = str;
			}
			else {
				String[] listNum = str.split(",");
				nums = str;
				for (int i=0; i<listNum.length; i++) {
					System.out.print(listNum[i] + "  ");
				}
				
				for (int i=0; i<listNum.length; i++) {
					int num = Integer.parseInt(listNum[i]);
					listNumber.add(num);
				}
				
				for (int i=0; i<listNumber.size(); i++) {
					System.out.print(listNumber.get(i) + "  ");
				}
				turn = Integer.parseInt(dis.readUTF());
			}
			
			Thread t = new Thread(this);
			t.start();
		} catch (Exception e) {
			System.out.println(e.getMessage() + "\tLoi 7");
		}
		
		this.setSize(n*s+2*off, n*s+2*off);
		this.setTitle("Tim so Client");
		
		this.setDefaultCloseOperation(3);
		this.addMouseListener(this);
		
		this.setVisible(true);
	}
	
	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		g.setColor(Color.BLACK);
		for (int i=0; i<=n; i++) {
			g.drawLine(off, off+s*i, off+s*n, off+s*i);
			g.drawLine(off+s*i, off, off+s*i, off+s*n);
		}
		
		for (int i=0; i<chon.size(); i++) {
			Color c = Color.BLUE;
			if (chon.get(i) == 2) {
				c = Color.RED;
			}
			if (chon.get(i) == 3) {
				c = Color.GREEN;
			}
			if (chon.get(i) == 4) {
				c = Color.YELLOW;
			}
			g.setColor(c);
			int rx = off + s*dachon.get(i).x;
			int ry = off + s*dachon.get(i).y;
			g.fillRect(rx, ry, s, s);
		}
		
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("arial", Font.BOLD, 20));
		String str = "";
		int index = 0;
		if (listNumber.size() == 0) {
			str = thbao;
			g.drawString(str, off, off+s*2);
		}
		else {
			for (int i=0; i<n; i++) {
				for (int j=0; j<n; j++) {
					str = listNumber.get(index) + "";
					int x = off + s*i + s - s/2 - s/4;
					int y = off + s*j + s - s/2 - s/4 + 7*s/20 + 7*s/100;
					g.drawString(str, x, y);
					index++;
				}
			}
		}
		
		String scr = "Score: " + score;
		String team = "Team: ";
		g.setFont(new Font("arial", Font.PLAIN, 15));
		g.drawString(scr, off, off+s*5+30);
		g.drawString(team, off+130, off+s*5+30);
		Color cl = Color.BLACK;
		if (dachon.size() == 0) {
			clr = "none";
		}
		else {
//			clr = "";
		}
		if ((turn == 1 && clr.equals("none")) || clr.equals("blue")) {
			cl = Color.BLUE;
			clr = "blue";
		}
		if ((turn == 2 && clr.equals("none")) || clr.equals("red")) {
			cl = Color.RED;
			clr = "red";
		}
		if ((turn == 3 && clr.equals("none")) || clr.equals("green")) {
			cl = Color.GREEN;
			clr = "green";
		}
		if ((turn == 4 && clr.equals("none")) || clr.equals("yellow")) {
			cl = Color.YELLOW;
			clr = "yellow";
		}
		g.setColor(cl);
//		g.fillRect(off+s*3+40, off+s*5+25, 50, 5);
		g.drawString(clr, off+s*3+40, off+s*5+30);
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if (x<off || x>off+s*n) return;
		if (y<off || y>off+s*n) return;
		int ix = (x-off)/s;
		int iy = (y-off)/s;
		
		// Gui toa do cho server xu ly
		try {
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			dos.writeUTF(ix+"");
			dos.writeUTF(iy+"");
		} catch (Exception e2) {
			System.out.println(e2.getMessage() + "\tLoi 6");
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		try {
			if (nums == null) {
				DataInputStream dis = new DataInputStream(socket.getInputStream());
				String str = dis.readUTF();
				nums = str;
				turn = Integer.parseInt(dis.readUTF());
			}
			
			String[] listNum = nums.split(",");
			for (int i=0; i<listNum.length; i++) {
				System.out.print(listNum[i] + "  ");
			}
			
			for (int i=0; i<listNum.length; i++) {
				int num = Integer.parseInt(listNum[i]);
				listNumber.add(num);
			}
			
			for (int i=0; i<listNumber.size(); i++) {
				System.out.print(listNumber.get(i) + "  ");
			}
			this.repaint();
		} catch (Exception e) {
			System.out.println("Hehe\t" + e.getMessage());
		}
		
		while (true) {
			try {
				DataInputStream dis = new DataInputStream(socket.getInputStream());
				int ix = Integer.parseInt(dis.readUTF());
				int iy = Integer.parseInt(dis.readUTF());
				turn = Integer.parseInt(dis.readUTF());
				
				dachon.add(new Point(ix, iy));
				chon.add(turn);
				System.out.println(dachon);
				this.repaint();
			} catch (Exception e) {
				try {
					Thread.sleep(100);
					
				} catch (Exception e2) {
					System.out.println(e2.getMessage() + "\tLoi 5");
				}
			}
		}
	}
}
