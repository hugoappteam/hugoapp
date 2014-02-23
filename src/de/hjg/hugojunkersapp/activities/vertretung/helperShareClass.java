package de.hjg.hugojunkersapp.activities.vertretung;

public class helperShareClass {
	
	private static int selection = 0;
	private static boolean selected = false;
	
	public static void setSelection(int sel){
		selection=sel;
		selected = true;
	}
	
	public static int getSelection(){
		return selection;
	}
	
	public static boolean isSelected(){
		return selected;
	}
}
