package de.hjg.hugojunkersapp.general;

import java.util.ArrayList;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import de.hjg.hugojunkersapp.R;
import de.hjg.hugojunkersapp.SQLite.VertretungData;

public class SendNotification {

	private ArrayList<VertretungData> vertOld;
	private ArrayList<VertretungData> vertNew;
	private Context context;

	public SendNotification(Context context) {
		this.context = context;
		sendNotification();
	}

	public SendNotification(Context context, ArrayList<VertretungData> old) {
		this.context = context;
		this.vertOld = old;
	}

	public void setNew(ArrayList<VertretungData> vertNew) {
		this.vertNew = vertNew;
		if (!compare()) {
			sendNotification();
		}
	}

	public int getOldCount() {
		return vertOld.size();
	}

	private boolean compare() {
		VertretungData vert = new VertretungData();
		try {
			for (int i = 0; i < vertOld.size(); i++) {
				vert = vertOld.get(i);
				if (!vert.equal(vertNew.get(i))) {
					return false;
				}
			}
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
		return true;
	}

	private void sendNotification() {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context)
				.setSmallIcon(R.drawable.hugo_logo)
				.setContentTitle("Vertretungen")
				.setContentText(
						"Es gab eine Änderung bei den Vertretungen, die dich betreffen könnten.");
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(context, LogoScreen.class);
		resultIntent.setAction("android.intent.action.NOTIFICATION");
		// The stack builder object will contain an artificial back stack for
		// the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(LogoScreen.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(0, mBuilder.build());
	}

}
