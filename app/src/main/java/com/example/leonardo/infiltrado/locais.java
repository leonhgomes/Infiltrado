package com.example.leonardo.infiltrado;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Locais extends AppCompatActivity {

    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locais);

        listView = (ExpandableListView)findViewById(R.id.lista);
        initData();
        listAdapter = new ExpandableListAdapter(this,listDataHeader,listHash);
        listView.setAdapter(listAdapter);

    }

    private void initData() {

        String pre_lista[] = getResources().getStringArray(R.array.locais);
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();
        String func[];
        for (int i = 0; i < pre_lista.length; i++) {
            func=pre_lista[i].split(";");
            listDataHeader.add(func[0]);

            listHash.put(listDataHeader.get(i),Arrays.asList(Arrays.copyOfRange(func, 1, func.length)));
        }
   }

}
