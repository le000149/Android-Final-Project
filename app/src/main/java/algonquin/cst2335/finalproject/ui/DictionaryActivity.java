package algonquin.cst2335.finalproject.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.databinding.ActivityDictionaryBinding;
import algonquin.cst2335.finalproject.R;

public class DictionaryActivity extends AppCompatActivity {

    private ActivityDictionaryBinding binding;
    private DefinitionAdapter definitionAdapter;
    private List<DictionaryEntry> definitions;
    private RequestQueue requestQueue;
    private DefinitionMessageDAO dDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDictionaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();
        dDAO = db.dmDAO();

        definitions = new ArrayList<>();
        definitionAdapter = new DefinitionAdapter(definitions);
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        binding.recycleView.setAdapter(definitionAdapter);

        requestQueue = Volley.newRequestQueue(this);

        binding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = binding.addword.getText().toString().trim();
                if (!word.isEmpty()) {
                    fetchDefinitions(word);
                }
            }
        });

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the currently displayed item position in the RecyclerView
                int position = ((LinearLayoutManager) binding.recycleView.getLayoutManager()).findFirstVisibleItemPosition();
                // Retrieve the word and definition from the RecyclerView
                String word = definitions.get(position).getWord();
                String definition = definitions.get(position).getDefinitions().toString();
                // Call the addMessage method to save the word and definition to the database
                addMessage(word, definition);
            }
        });
    }

    private void addMessage(String word, String definition) {
        // Create a new DefinitionMessage object
        DefinitionMessage newMessage = new DefinitionMessage(word, definition);

        // Insert the new message into the Room database and get its ID
        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> {
            long messageId = dDAO.insertMessage(newMessage);
            newMessage.setId((int) messageId);

            runOnUiThread(() -> {
                Toast.makeText(DictionaryActivity.this, "Word and definition saved!", Toast.LENGTH_SHORT).show();
            });
        });
    }


    private void fetchDefinitions(String word) {
        String url = String.format("https://api.dictionaryapi.dev/api/v2/entries/en/%s", word);

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            List<DictionaryEntry> newDefinitions = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject wordObject = jsonArray.getJSONObject(i);
                                String word = wordObject.getString("word");

                                JSONArray meaningsArray = wordObject.getJSONArray("meanings");
                                List<String> definitions = new ArrayList<>();
                                for (int j = 0; j < meaningsArray.length(); j++) {
                                    JSONObject meaningObject = meaningsArray.getJSONObject(j);
                                    JSONArray definitionsArray = meaningObject.getJSONArray("definitions");
                                    for (int k = 0; k < definitionsArray.length(); k++) {
                                        definitions.add(definitionsArray.getJSONObject(k).getString("definition"));
                                    }
                                }

                                newDefinitions.add(new DictionaryEntry(word, definitions));
                            }

                            definitions.clear();
                            definitions.addAll(newDefinitions);
                            definitionAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DictionaryActivity.this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(DictionaryActivity.this, "Error fetching data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(request);
    }


    private static class DefinitionAdapter extends RecyclerView.Adapter<DefinitionAdapter.DefinitionViewHolder> {

        private List<DictionaryEntry> entries;

        public DefinitionAdapter(List<DictionaryEntry> entries) {
            this.entries = entries;
        }

        @Override
        public DefinitionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.definition_item, parent, false);
            return new DefinitionViewHolder(view);
        }

        @Override
        public void onBindViewHolder(DefinitionViewHolder holder, int position) {
            DictionaryEntry entry = entries.get(position);
            StringBuilder definitionsText = new StringBuilder();
            for (String definition : entry.getDefinitions()) {
                definitionsText.append(definition).append("\n"); // Add a newline between each definition
            }
            holder.definitionTextView.setText(definitionsText.toString());
        }

        @Override
        public int getItemCount() {
            return entries.size();
        }

        public static class DefinitionViewHolder extends RecyclerView.ViewHolder {
            TextView definitionTextView;

            public DefinitionViewHolder(View itemView) {
                super(itemView);
                definitionTextView = itemView.findViewById(R.id.definition_text);
            }
        }

    }
}
