package com.example.twittersearches;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText tagEditText;
    private EditText queryEditText;
    private Button saveButton, clearTagsButton;
    private LinearLayout tagsContainer;

    private Map<String, String> searches = new LinkedHashMap<>();
    private String currentlyEditingTag = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tagEditText = findViewById(R.id.tagEditText);
        queryEditText = findViewById(R.id.queryEditText);
        saveButton = findViewById(R.id.saveButton);
        clearTagsButton = findViewById(R.id.clearTagsButton);
        tagsContainer = findViewById(R.id.tagsContainer);

        // Додавање на почетни тагови
        addTag("AndroidFP", "https://twitter.com/search?q=AndroidFP");
        addTag("Deitel", "https://twitter.com/search?q=Deitel");
        addTag("Google", "https://twitter.com/search?q=Google");
        addTag("iPhoneFP", "https://twitter.com/search?q=iPhoneFP");
        addTag("JavaFP", "https://twitter.com/search?q=JavaFP");
        addTag("JavaHTP", "https://twitter.com/search?q=JavaHTP");

        saveButton.setOnClickListener(view -> {
            String tag = tagEditText.getText().toString().trim();
            String query = queryEditText.getText().toString().trim();

            if (!tag.isEmpty() && !query.isEmpty()) {
                if (currentlyEditingTag != null) {
                    updateTag(currentlyEditingTag, tag, query);
                    currentlyEditingTag = null;
                } else {
                    addTag(tag, "https://twitter.com/search?q=" + query);
                }
                tagEditText.setText("");
                queryEditText.setText("");
            }
        });

        clearTagsButton.setOnClickListener(view -> {
            tagsContainer.removeAllViews();
            searches.clear();
        });
    }

    private void addTag(String tag, String queryUrl) {
        if (!searches.containsKey(tag)) {
            searches.put(tag, queryUrl);
            displayTags();
        }
    }

    private void updateTag(String oldTag, String newTag, String newQuery) {
        if (searches.containsKey(oldTag)) {
            searches.remove(oldTag);
            searches.put(newTag, "https://twitter.com/search?q=" + newQuery);
            displayTags();
        }
    }

    private void displayTags() {
        tagsContainer.removeAllViews();
        for (Map.Entry<String, String> entry : searches.entrySet()) {
            String tag = entry.getKey();
            String queryUrl = entry.getValue();

            LinearLayout tagLayout = new LinearLayout(this);
            tagLayout.setOrientation(LinearLayout.HORIZONTAL);
            tagLayout.setPadding(0, 5, 0, 5);

            Button tagButton = new Button(this);
            tagButton.setText(tag);
            tagButton.setTextColor(Color.WHITE);
            tagButton.setBackgroundColor(Color.GRAY);
            tagButton.setPadding(20, 8, 20, 8);
            tagButton.setOnClickListener(view -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(queryUrl));
                startActivity(intent);
            });

            Button editButton = new Button(this);
            editButton.setText("Edit");
            editButton.setBackgroundColor(Color.LTGRAY);
            editButton.setPadding(20, 8, 20, 8);
            editButton.setOnClickListener(view -> {
                tagEditText.setText(tag);
                queryEditText.setText(queryUrl.replace("https://twitter.com/search?q=", ""));
                currentlyEditingTag = tag;
            });

            LinearLayout.LayoutParams tagParams = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 2);
            tagButton.setLayoutParams(tagParams);

            LinearLayout.LayoutParams editParams = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            editButton.setLayoutParams(editParams);

            tagLayout.addView(tagButton);
            tagLayout.addView(editButton);
            tagsContainer.addView(tagLayout);
        }
    }
}
