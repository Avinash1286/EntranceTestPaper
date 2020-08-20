package com.entranceaptitude;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    MeowBottomNavigation bottomNavigation;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        bottomNavigation = (MeowBottomNavigation) findViewById(R.id.bottomnav);
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_theee));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_finaleng));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_finalsic));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_finalbank));
        bottomNavigation.add(new MeowBottomNavigation.Model(5, R.drawable.ic_finalmodel));
        getSupportFragmentManager().beginTransaction().replace(R.id.hostFrag, new Science()).commit();
        getSupportActionBar().setTitle("Science's MCQs 2020 AD");
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {

                Fragment fragment = null;
                switch (item.getId()) {
                    case 1:
                        fragment = new Maths();
                        title="Math's MCQs 2020 AD";
                        break;
                    case 2:
                        fragment = new English();
                        title="English's MCQs 2020 AD";
                        break;
                    case 3:
                        fragment = new Science();
                        title="Science's MCQs 2020 AD";
                        break;
                    case 4:
                        fragment = new QuestionBank();
                        title="Past Question Collection";
                        break;
                    case 5:
                        fragment = new ModelQuestion();
                        title="Entrance Test Paper 2020";
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.hostFrag, fragment).commit();
                getSupportActionBar().setTitle(title);
            }
        });

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {

                // Toast.makeText(MainActivity.this, "Click", Toast.LENGTH_SHORT).show();
            }
        });
        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {

            }
        });
        bottomNavigation.show(3, true);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_result:
                startActivity(new Intent(MainActivity.this,SEEResult.class));
                break;
            case R.id.nav_share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                String shareMessage= "\nEntrance Test Paper 2020 \n- Developed by TheAvi\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "Choose one"));
                break;
           /* case R.id.nav_upload:
                startActivity(new Intent(MainActivity.this,UploadPdf.class));
                break; */
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}