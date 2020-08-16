package kr.ac.ssu.myrecipe;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import kr.ac.ssu.myrecipe.adapter.AlertDialogImageAdapter;

public class AddActivity extends AppCompatActivity {
    private EditText category;
    private static final String TAG_TEXT = "text";
    private static final String TAG_IMAGE = "image";
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        context = this;
        ImageButton close = (ImageButton)findViewById(R.id.add_close);
        category = (EditText)findViewById(R.id.add_category);
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog(context);
                //Dialog();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void showAlertDialog(Context context) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_dialog, null);
        final GridView gridView = (GridView) view.findViewById(R.id.gridview_alterdialog_list);
        final Button button = (Button) view.findViewById(R.id.button_alerdialog_button);
        final AlertDialogImageAdapter adapter = new AlertDialogImageAdapter(context,false);
        gridView.setAdapter(adapter);
        gridView.setNumColumns(4);
        gridView.setGravity(Gravity.CENTER);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: Implement
                //Toast.makeText(view.getContext(), "Clicked position is: " + position, Toast.LENGTH_LONG).show();
                category.setText((String)adapter.getText(position));
                dialog.dismiss();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}