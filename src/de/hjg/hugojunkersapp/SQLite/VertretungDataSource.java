package de.hjg.hugojunkersapp.SQLite;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

//class to work with the classes VertretungData and MySQLiteHelper
public class VertretungDataSource {

	private static SQLiteDatabase Vertretung;
	private static MySQLiteHelper dbHelper;
	private static String[] allColumns = {
			MySQLiteHelper.COLUMNS_VERTRETUNG[1][0],
			MySQLiteHelper.COLUMNS_VERTRETUNG[2][0],
			MySQLiteHelper.COLUMNS_VERTRETUNG[3][0],
			MySQLiteHelper.COLUMNS_VERTRETUNG[4][0],
			MySQLiteHelper.COLUMNS_VERTRETUNG[5][0],
			MySQLiteHelper.COLUMNS_VERTRETUNG[6][0],
			MySQLiteHelper.COLUMNS_VERTRETUNG[7][0],
			MySQLiteHelper.COLUMNS_VERTRETUNG[8][0] };

	public VertretungDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	// Method to open the SQLite database
	public static void open() throws SQLException {
		Vertretung = dbHelper.getWritableDatabase();
	}

	// Method to close the database helper
	public static void close() {
		dbHelper.close();
	}

	// Method that inserts a VertretungData object into the database
	public static void createVertretungEntry(VertretungData vert) {
		ContentValues val = new ContentValues();
		if (vert == null)
			return;
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			val.put(MySQLiteHelper.COLUMNS_VERTRETUNG[1][0], vert.getKlasse());
			val.put(MySQLiteHelper.COLUMNS_VERTRETUNG[2][0], vert.getLehrer());
			val.put(MySQLiteHelper.COLUMNS_VERTRETUNG[3][0],
					df.format(vert.getDatum()));
			val.put(MySQLiteHelper.COLUMNS_VERTRETUNG[4][0], vert.getStunde());
			val.put(MySQLiteHelper.COLUMNS_VERTRETUNG[5][0], vert.getFach());
			val.put(MySQLiteHelper.COLUMNS_VERTRETUNG[6][0], vert.getRaum());
			val.put(MySQLiteHelper.COLUMNS_VERTRETUNG[7][0],
					vert.getVertreter());
			val.put(MySQLiteHelper.COLUMNS_VERTRETUNG[8][0], vert.getInfo());

			Vertretung.insert(MySQLiteHelper.NAME_VERTRETUNG, null, val);
		} catch (Exception e) {
			Log.e("Entry_Error",
					"Error in creating SQLite entry " + e.toString());
		}
	}

	// Method to fill the database from a list of VertretungenData objects
	public static void fillDatabase(List<VertretungData> Vertretungen) {
		open();
		dbHelper.empty(Vertretung);

		for (VertretungData vert : Vertretungen) {
			createVertretungEntry(vert);
		}
	}

	// Method to create a VertretungData object from Strings
	public static VertretungData createVertretung(String Klasse, String Lehrer,
			String Datum, String Stunde, String Fach, String Raum,
			String Vertreter, String Info) {
		VertretungData vert = new VertretungData();

		vert.setKlasse(Klasse);
		vert.setLehrer(Lehrer);
		vert.setDatum(Datum);
		vert.setStunde(Stunde);
		vert.setFach(Fach);
		vert.setRaum(Raum);
		vert.setVertreter(Vertreter);
		vert.setInfo(Info);

		return vert;
	}

	// Method to get all VertretungenData objects from the SQLite database
	public static ArrayList<VertretungData> getAllVertretungen() {
		ArrayList<VertretungData> AllVertretungen = new ArrayList<VertretungData>();

		Cursor cursor = Vertretung.query(MySQLiteHelper.NAME_VERTRETUNG,
				allColumns, null, null, null, null, "Tag, Klasse, Stunde");

		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			VertretungData vert = cursorToVertretung(cursor);
			AllVertretungen.add(vert);
			cursor.moveToNext();
		}
		return AllVertretungen;
	}

	public static VertretungData getVertretungByID(int id) {
		Cursor cursor = Vertretung.query(MySQLiteHelper.NAME_VERTRETUNG,
				allColumns, null, null, null, null, "Tag, Klasse, Stunde");

		cursor.moveToPosition(id);

		return cursorToVertretung(cursor);
	}

	public static ArrayList<VertretungData> getVertretungenByClass(String Klasse) {
		ArrayList<VertretungData> ClassVertretungen = new ArrayList<VertretungData>();
		Cursor cursor = Vertretung.query(MySQLiteHelper.NAME_VERTRETUNG,
				allColumns, null, null, null, null, "Tag, Klasse, Stunde");

		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			VertretungData vert = cursorToVertretung(cursor);
			if (Klasse.length() == 1) {
				if (vert.getKlasse().contains(Klasse)) {
					ClassVertretungen.add(vert);
				}
			} else {
				if (vert.getKlasse().contains(Klasse.substring(0, 1))
						&& vert.getKlasse().contains(Klasse.substring(1, 2))) {
					ClassVertretungen.add(vert);
				}
			}
			cursor.moveToNext();
		}
		return ClassVertretungen;

	}

	public static ArrayList<VertretungData> getVertretungenByDay(Date day) {
		// TODO:
		return null;
	}

	// Method to create a VertretungData object from a specific row of the
	// database
	private static VertretungData cursorToVertretung(Cursor c) {
		VertretungData vert = new VertretungData();

		vert.setKlasse(c.getString(0));
		vert.setLehrer(c.getString(1));
		vert.setDatum(c.getString(2));
		vert.setStunde(c.getString(3));
		vert.setFach(c.getString(4));
		vert.setRaum(c.getString(5));
		vert.setVertreter(c.getString(6));
		vert.setInfo(c.getString(7));

		return vert;
	}

	public static List<VertretungData> getVertretungenFromJSON(String JSON) {
		List<VertretungData> Vertretungen = new ArrayList<VertretungData>();

		try {
			JSONArray JArray = new JSONArray(JSON);

			for (int i = 0; i < JArray.length(); i++) {
				VertretungData vert = new VertretungData();
				JSONObject JObject = JArray.getJSONObject(i);

				vert.setKlasse(JObject
						.getString(MySQLiteHelper.COLUMNS_VERTRETUNG[1][0]
								.trim()));
				vert.setLehrer(JObject
						.getString(MySQLiteHelper.COLUMNS_VERTRETUNG[2][0]
								.trim()));
				vert.setDatum(JObject
						.getString(MySQLiteHelper.COLUMNS_VERTRETUNG[3][0]
								.trim()));
				vert.setStunde(JObject
						.getString(MySQLiteHelper.COLUMNS_VERTRETUNG[4][0]
								.trim()));
				vert.setFach(JObject
						.getString(MySQLiteHelper.COLUMNS_VERTRETUNG[5][0]
								.trim()));
				vert.setRaum(JObject
						.getString(MySQLiteHelper.COLUMNS_VERTRETUNG[6][0]
								.trim()));
				vert.setVertreter(JObject
						.getString(MySQLiteHelper.COLUMNS_VERTRETUNG[7][0]
								.trim()));
				vert.setInfo(JObject
						.getString(MySQLiteHelper.COLUMNS_VERTRETUNG[8][0]
								.trim()));

				Vertretungen.add(vert);

			}
		}

		catch (Exception e) {
			// Error pasing data
			Log.e("log_tag", "Error in parsing data " + e.toString() + JSON);

		}

		return Vertretungen;
	}

	public static int getDatesCount() {
		int count = 0;
		Date oldday = null;
		for (VertretungData vert : getAllVertretungen()) {
			if (oldday == null || oldday != vert.getDatum()) {
				count++;
			}
			oldday = vert.getDatum();
		}

		return count;
	}

	public static ArrayList<String> GetDaysAsStrings(String format) {
		// Gives an list of strings of all days in the VertretungData back
		ArrayList<String> days = new ArrayList<String>();
		Date oldday = null;
		for (VertretungData vert : getAllVertretungen()) {
			if (oldday == null || oldday.compareTo(vert.getDatum()) != 0) {
				days.add(new SimpleDateFormat(format).format(vert.getDatum()));
			}
			oldday = vert.getDatum();
		}
		return days;
	}
}
