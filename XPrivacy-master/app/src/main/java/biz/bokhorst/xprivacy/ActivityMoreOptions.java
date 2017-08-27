package biz.bokhorst.xprivacy;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class ActivityMoreOptions extends AppCompatActivity {
    public static final String cUid = "Uid";
    public static final String cRestrictionName = "RestrictionName";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.policy_main);

        Bundle extras = getIntent().getExtras();
        int uid = extras.getInt(cUid);
        String restrictionName = extras.getString(cRestrictionName);
        TextView tv1 = (TextView) findViewById(R.id.textViewPolicyContextId);
        tv1.setText("Uid=" + uid + "  restriction name=" + restrictionName);
        Spinner sp = (Spinner) findViewById(R.id.options_spinner);
        ArrayAdapter<CharSequence> spAdapter = ArrayAdapter.createFromResource(this, R.array.options_spinner, android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(spAdapter);
        Button save = (Button) findViewById(R.id.buttonSavePolicyId);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText ed1 = (EditText) findViewById(R.id.editText1Id);
                Spinner sp1 = (Spinner)  findViewById(R.id.options_spinner);
                RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroupId);
                RadioButton rb = (RadioButton) findViewById(rg.getCheckedRadioButtonId());
                String result = sp1.getSelectedItem().toString() + "  " + ed1.getText().toString() + "  " + (rb.getText().toString());
                TextView tvs = (TextView) findViewById(R.id.textViewPolicyContextId);
                tvs.setText(result);
            }
        });
    }

}
