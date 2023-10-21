package findNumber;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;

public class TimSoOffline extends JFrame implements MouseListener {
	
	int n = 5;
	int s = 50;
	int off = 50;
	List<Integer> listNumber = new ArrayList<Integer>();
	List<Point> dachon = new ArrayList<Point>();
	int[][] matrix = new int[n][n];
	int flag = 0;

	public static void main(String[] args) {
		new TimSoOffline();
	}

	public TimSoOffline() {
		this.setSize(n*s+2*off, n*s+2*off);
		this.setTitle("Tim So");
		
		this.setDefaultCloseOperation(3);
		
		this.setVisible(true);
		
		
		for (int i=1; i<=n*n; i++) {
			listNumber.add(i);
		}
		
		Collections.shuffle(listNumber);
		
		this.addMouseListener(this);
		
		int index = 0;
		for (int i=0; i<n; i++) {
			for (int j=0; j<n; j++) {
				matrix[i][j] = listNumber.get(index);
				index++;
			}
		}
		
		for (int i=0; i<n; i++) {
			for (int j=0; j<n; j++) {
				System.out.print(matrix[i][j] + "\t");
			}
			System.out.println();
		}
	}
	
	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		g.setColor(Color.BLACK);
		for (int i=0; i<=n; i++) {
			g.drawLine(off, off+s*i, off+s*n, off+s*i);
			g.drawLine(off+s*i, off, off+s*i, off+s*n);
		}
		
		
		
		for (int i=0; i<dachon.size(); i++) {
			Color c = Color.YELLOW;
			if (i%4==1) {
				c = Color.RED;
			}
			if (i%4==2) {
				c = Color.BLUE;
			}
			if (i%4==3) {
				c = Color.GREEN;
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
	
	public int numberAt(int col, int row) {
		for (Integer itg : listNumber) {
			if (itg == matrix[col][row]) {
				return itg;
			}
		}
		return 0;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if (x<off || x>off+s*n) return;
		if (y<off || y>off+s*n) return;
		int ix = (x-off)/s;
		int iy = (y-off)/s;
		
		for (Point p : dachon) {
			if (p.x == ix && p.y == iy) return;
		}
		
		flag++;
		if (numberAt(ix, iy) != flag) {
			flag--;
			return;
		}
		System.out.println(numberAt(ix, iy));
		System.out.println(flag);
		dachon.add(new Point(ix, iy));
		this.repaint();
		
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
}
