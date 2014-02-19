package de.hjg.hugojunkersapp.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	// Table Vertretung
	// prepare Table
	public static final String NAME_VERTRETUNG = "Vertretung ";
	public static final String[][] COLUMNS_VERTRETUNG = {
			{ "id ", "INTEGER PRIMARY KEY AUTOINCREMENT, " },
			{ "Klasse ", "TEXT NOT NULL, " }, { "Lehrer ", "TEXT NOT NULL, " },
			{ "Tag ", "TEXT NOT NULL, " }, { "Stunde ", "TEXT NOT NULL, " },
			{ "Fach ", "TEXT, " }, { "Raum ", "TEXT, " },
			{ "Vertreter ", "TEXT NOT NULL, " }, { "Info ", "TEXT" } };

	public MySQLiteHelper(Context context) {
		super(context, "HugoApp", null, 1);
	}

	// SQL Command to create Table
	private static final String VERTRETUNG_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ NAME_VERTRETUNG
			+ "("
			+ COLUMNS_VERTRETUNG[0][0]
			+ COLUMNS_VERTRETUNG[0][1]
			+ COLUMNS_VERTRETUNG[1][0]
			+ COLUMNS_VERTRETUNG[1][1]
			+ COLUMNS_VERTRETUNG[2][0]
			+ COLUMNS_VERTRETUNG[2][1]
			+ COLUMNS_VERTRETUNG[3][0]
			+ COLUMNS_VERTRETUNG[3][1]
			+ COLUMNS_VERTRETUNG[4][0]
			+ COLUMNS_VERTRETUNG[4][1]
			+ COLUMNS_VERTRETUNG[5][0]
			+ COLUMNS_VERTRETUNG[5][1]
			+ COLUMNS_VERTRETUNG[6][0]
			+ COLUMNS_VERTRETUNG[6][1]
			+ COLUMNS_VERTRETUNG[7][0]
			+ COLUMNS_VERTRETUNG[7][1]
			+ COLUMNS_VERTRETUNG[8][0]
			+ COLUMNS_VERTRETUNG[8][1] + ");";

	@Override
	public void onCreate(SQLiteDatabase database) {
		// Execute Creation
		Log.i("Creation", "Database created.");
		database.execSQL(VERTRETUNG_CREATE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Upgrade
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + NAME_VERTRETUNG);

		onCreate(db);
	}

	@Override
	public void onOpen(SQLiteDatabase db) {

		super.onOpen(db);
	}

	public void empty(SQLiteDatabase db) {
		db.execSQL("DELETE FROM " + NAME_VERTRETUNG);
	}

}
