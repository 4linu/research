package biz.bokhorst.xprivacy;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class ActivityMoreOptions extends AppCompatActivity {
    public static final String cUid = "Uid";
    public static final String cRestrictionName = "RestrictionName";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_policy_main);

        Bundle extras = getIntent().getExtras();
        int uid = extras.getInt(cUid);
        String restrictionName = extras.getString(cRestrictionName);
        TextView tv1 = (TextView) findViewById(R.id.textViewMoreOptionsId);
        tv1.setText("Uid=" + uid + "  restriction name=" + restrictionName);
    }

}
