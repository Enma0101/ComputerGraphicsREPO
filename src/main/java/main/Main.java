package main;

import java.awt.Font;

import ui.GarageView;

public class Main {
	
	static GarageView frame;
	public static final Font GLOBAL_FONT = new Font("Arial", Font.BOLD, 50);
	public static final Font GLOBAL_FONT_botton = new Font("Arial", Font.BOLD, 20);
	
	public static void main(String[] args) {
		
		frame = new	GarageView();
		frame.setVisible(true);
		
		
	}
}