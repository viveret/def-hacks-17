package com.viveret.safemessage.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.TextView;

import com.viveret.safemessage.R;

public class SettingsActivity extends AppCompatActivity {
    private SeekBar PRICEbar, DISTANCEbar, RATINGbar; // declare seekbar object variable
    // declare text label objects
    private TextView PRICEtextProgress, DISTANCEtextProgress, RATINGtextProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // load the layout
        setContentView(R.layout.activity_settings);
        PRICEbar = (SeekBar) findViewById(R.id.PRICEseekBarID); // make seekbar object
        PRICEbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                PRICEtextProgress = (TextView) findViewById(R.id.PRICEtextViewProgressID);
                PRICEtextProgress.setText("Price:: Rs " + progress);

            }
        });

    }

    // @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == PRICEbar)
            PRICEtextProgress.setText("Price:: Rs " + progress);
    }

    // @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    //  @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }


}

