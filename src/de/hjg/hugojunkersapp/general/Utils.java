package de.hjg.hugojunkersapp.general;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {

	public static String ComputeMD5Hash(String plainPassword) {
		StringBuffer MD5Hash = new StringBuffer();
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(plainPassword.getBytes());
			byte messageDigest[] = digest.digest();
			for (int i = 0; i < messageDigest.length; i++) {
				String h = Integer.toHexString(0xFF & messageDigest[i]);
				while (h.length() < 2)
					h = "0" + h;
				MD5Hash.append(h);
			}

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return MD5Hash.toString();
	}

	public static boolean CheckForLetter(String string) {
		if (string == null || string.length() == 0) {
			return false;
		}
		for (int i = 0; i < string.length(); i++) {
			if (!Character.isLetter(string.codePointAt(i)))
				;
			return false;
		}
		return true;
	}

	public static boolean CheckForDigit(String string) {
		if (string == null || string.length() == 0) {
			return false;
		}
		for (int i = 0; i < string.length(); i++) {
			if (!Character.isDigit(string.codePointAt(i)))
				;
			return false;
		}
		return true;
	}

	public static boolean CheckAnyChar(String string) {
		if (string == null || string.length() == 0) {
			return false;
		}
		for (int i = 0; i < string.length(); i++) {
			if (!(string.charAt(i) == ' '))
				;
			return true;
		}

		return false;
	}

	public static boolean CheckStringForName(String name) {
		if (name == null || name.length() == 0 || !name.contains(" "))
			return false;
		String[] names = name.split(" ");
		for (int i = 0; i < names.length; i++) {
			names[i] = names[i].replace(" ", "");
		}
		if (names.length == 1) {
			return false;
		}
		if (!CheckAnyChar(names[0]) || !CheckAnyChar(names[1]))
			return false;
		return true;
	}

	public static String GivePasswordAsPoints(String password) {
		String points = "";
		for (int i = 0; i < password.length(); i++) {
			points += (char) 149;
		}
		return points;
	}
	
	
	public static boolean checkForInternetConnectivity(Context context) {

	ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	NetworkInfo netInfo = cm.getActiveNetworkInfo();
	if(netInfo != null && netInfo.isConnectedOrConnecting()) {
		return true;
	}
	return false;
	}
	

}
