package com.example.animalcrash;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.animalcrash.util.Model;

import java.util.ArrayList;
import java.util.List;

public class SwipeViewer extends AppCompatActivity {

    private static final String TAG = "SwipeViewer";

    private ViewPager viewPager;
    private Adapter adapter;
    private List<Model> models;
    private Integer[] colors = null;
    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    private int chosenAnimalID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager);

        this.chosenAnimalID = getIntent().getIntExtra("Animal ID", 0);

        models = new ArrayList<>();
        models.add(new Model(R.drawable.echteleeuw, "Leeuw", "De meest bekende leeuwensoort is " +
                "de Afrikaanse leeuw. Hun leefgebied bestaat voornamelijk uit delen van Centraal- " +
                "en Oost-Afrika in landen als Tanzania, Botswana en Zuid-Afrika.", 1));
        models.add(new Model(R.drawable.echtevos, "Vos", "Er bestaan veel verschillende " +
                "soorten vossen. De vos is de meest verspreide wilde hondensoort ter wereld. " +
                "Vossen komen bijna overal voor, ze leven gemakkelijk in de stad, op het platteland," +
                " in bossen en bergen.", 2));
        models.add(new Model(R.drawable.eechtepaard, "Paard", "Het wilde paard leefde vroeger" +
                " van West-Europa tot in Centraal Azië. Alleen het oostelijke wilde steppepaard " +
                "Przewalskipaard, leeft mogelijk nog in kleine restpopulaties in Mongolië en West-China.", 3));
        models.add(new Model(R.drawable.echterodepanda, "Rode Panda", "De rode panda is ongeveer " +
                "zo groot als een flinke huiskat. Ze leven in de beboste gedeelten van de Himalaya’s, " +
                "mits er een dikke onderlaag van bamboe is.", 4));
        models.add(new Model(R.drawable.echteneushoorn, "Neushoorn", "In de verre oertijd waren " +
                "er minstens 165 soorten neushoorns. Maar nu zijn er nog maar vijf soorten. In Afrika" +
                " leven de witte en de zwarte neushoorn in de savanne. De Javaanse en de Sumatraanse neushoorn " +
                "leven ook in Azië. Zij zitten diep in het regenwoud. Die zijn erg zeldzaam", 5));

        adapter = new Adapter(models, this);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130, 0, 130, 0);

        Integer[] colors_temp = {
                getResources().getColor(R.color.color1),
                getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color3),
                getResources().getColor(R.color.color4),
                getResources().getColor(R.color.color5),
        };

        colors = colors_temp;

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int i1) {
                if (position < (adapter.getCount() - 1) && position < (colors.length - 1)) {
                    viewPager.setBackgroundColor(
                            (Integer) argbEvaluator.evaluate(
                                    positionOffset,
                                    colors[position],
                                    colors[position + 1]
                            )
                    );
                } else {
                    viewPager.setBackgroundColor(colors[colors.length - 1]);
                }
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        getIncomingIntent();

        // Setting starter pager
        viewPager.setCurrentItem(chosenAnimalID);
    }

    private void getIncomingIntent() {
        Log.d(TAG, "getIncomingIntent: checking for incoming intents");
                    if (getIntent().hasExtra("Image_beeld") &&
                            getIntent().hasExtra("Image_name")) {
            }
        }
    public void goBack(View view){
        this.finish();
    }
}