package de.hjg.hugojunkersapp.activities.vertretung;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.hjg.hugojunkersapp.R;
import de.hjg.hugojunkersapp.SQLite.VertretungData;

public class InfoPopup {

	private static DateFormat df = new SimpleDateFormat("EEEE, dd MMMM yyyy");
	private static String shareBody;
	private static String shareSubject;

	public static void showData(VertretungData vert, View view, Context con) {
	
		LayoutInflater inflater = ((Activity) con).getLayoutInflater();
		ViewGroup slidingView = (ViewGroup) view.findViewById(R.id.content_slide_view); 
		View content = inflater.inflate(R.layout.info_view, slidingView);
		TextView classTV = (TextView) content.findViewById(R.id.tvClass);
		classTV.setText(vert.getKlasse()); 
		TextView teacherTV = (TextView)	content.findViewById(R.id.tvTeacher);
		teacherTV.setText(vert.getLehrer()); 
		TextView dateTV = (TextView) content.findViewById(R.id.tvDate);
		dateTV.setText(df.format(vert.getDatum())); 
		TextView lessonTV =	(TextView) content.findViewById(R.id.tvLesson);
		lessonTV.setText(vert.getStunde());
		TextView subjectTV = (TextView)	content.findViewById(R.id.tvSubject);
		subjectTV.setText(vert.getFach()); 
		TextView roomTV = (TextView) content.findViewById(R.id.tvRoom); roomTV.setText(vert.getRaum());
		TextView vertTV = (TextView) content.findViewById(R.id.tvVertreter);
		vertTV.setText(vert.getVertreter()); 
		TextView infoTV = (TextView) content.findViewById(R.id.tvInfo); infoTV.setText(vert.getInfo());

	}

	public static String getShareBody(VertretungData vert) {
		shareBody = "Klasse" + vert.getKlasse() + " hat am "
				+ df.format(vert.getDatum()) + " in der " + vert.getStunde()
				+ ". für " + vert.getLehrer() + " Vertretung mit "
				+ vert.getVertreter() + " in " + vert.getRaum() + ".";
		String shareSubject = "Vertretung";
		if (vert.getInfo().equals("Klausur")) {
			shareBody = "Die " + vert.getKlasse() + " schreibt am "
					+ df.format(vert.getDatum()) + " in der "
					+ vert.getStunde() + ". Stunde Klausur in "
					+ vert.getRaum() + ". Aufsicht führt "
					+ vert.getVertreter() + ".";
			shareSubject = "Klausur";
		}
		if (vert.getVertreter().equals("+")
				|| vert.getVertreter().equals("---")) {
			shareBody = "Die " + vert.getKlasse() + ", die am "
					+ df.format(vert.getDatum()) + " in der "
					+ vert.getStunde() + ". Stunde mit " + vert.getLehrer()
					+ " Unterricht hätte, hat dort frei.";
			shareSubject = "Entfall";
		}
		if (vert.getLehrer().equals(vert.getVertreter())) {
			shareBody = "Die " + vert.getStunde() + ". Stunde am "
					+ df.format(vert.getDatum()) + " mit " + vert.getLehrer()
					+ " findet in Raum " + vert.getRaum() + " statt.";
			shareSubject = "Raumvertretung";
		}

		return shareBody;
	}

	public static String getShareSubject(VertretungData vert) {
		getShareBody(vert);
		return shareSubject;
	}

}
