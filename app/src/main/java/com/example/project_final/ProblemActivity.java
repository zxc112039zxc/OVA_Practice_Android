package com.example.project_final;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ProblemActivity extends AppCompatActivity {

    TextView tvShowProblemNumber, tvShowProblemName;
    WebView webViewProblem;
    DBHelper dbHelper;
    Problem problem;
    ImageView btnFavoriteShow;
    FloatingActionButton btnAddNote;
    String note;
    String url = "https://onlinejudge.org/external/1/146.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);

        Bundle bundle = getIntent().getExtras();
        String problemName = bundle.getString("problemName");
        tvShowProblemNumber = (TextView) findViewById(R.id.tvShowProblemNumber);
        tvShowProblemName = (TextView) findViewById(R.id.tvShowProblemName);
        webViewProblem = (WebView) findViewById(R.id.webViewProblem);
        btnFavoriteShow = (ImageView) findViewById(R.id.btnFavoriteShow);
        btnAddNote = (FloatingActionButton) findViewById(R.id.btnAddNote);
        dbHelper = new DBHelper(this);
        problem = dbHelper.getProblemToProblem(problemName);
        url = dbHelper.getProblemURL(problemName);
        dbHelper.insertHistory(problem.getProblemName());

        tvShowProblemNumber.setText(problem.getProblemNumber());
        tvShowProblemName.setText(problem.getProblemName());
        if (problem.isFavorite()) {
            btnFavoriteShow.setImageResource(R.drawable.heart_red);
        } else {
            btnFavoriteShow.setImageResource(R.drawable.heart_white);
        }

        webViewProblem.getSettings().setJavaScriptEnabled(true);
        webViewProblem.getSettings().setSupportZoom(true);
        webViewProblem.getSettings().setBuiltInZoomControls(false);
        webViewProblem.loadUrl("https://docs.google.com/gview?embedded=true&url=" + url);

        btnFavoriteShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (problem.isFavorite()) {
                    btnFavoriteShow.setImageResource(R.drawable.heart_white);
                    problem.setFavorite(false);
                } else {
                    btnFavoriteShow.setImageResource(R.drawable.heart_red);
                    problem.setFavorite(true);
                }
                dbHelper.updateProblemFavorite(problem.getProblemName(), problem.isFavorite());
            }
        });

        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(ProblemActivity.this);
                View note_layout = inflater.inflate(R.layout.note_layout,null);
                final EditText editTextNote = (EditText) note_layout.findViewById(R.id.editTextNote);
                note = dbHelper.getNoteToString(problem.getProblemName());
                AlertDialog.Builder ad = new AlertDialog.Builder(ProblemActivity.this);
                editTextNote.setText(note);

                ad.setCancelable(true);
                ad.setTitle("NOTE");
                ad.setView(note_layout);
                ad.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                ad.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String noteTXT = editTextNote.getText().toString();

                        dbHelper.updateNote(problem.getProblemName(), noteTXT);
                    }
                });
                ad.show();
            }
        });
    }
}