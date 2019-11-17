package com.test.transportapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.test.transportapp.notificaciones.APIService;
import com.test.transportapp.notificaciones.Client;
import com.test.transportapp.notificaciones.Token;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth autenticacion;
    APIService apiService;
    String mUID;
    boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        apiService = Client.getRetrofit("https://fcm.googleapis.com/").create(APIService.class);
        autenticacion=FirebaseAuth.getInstance();
        mUID=autenticacion.getCurrentUser().getUid();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //NavigationView navigationView =findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        updateToken(FirebaseInstanceId.getInstance().getToken());

    }

    public void updateToken (String tken){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tokens");
        Token mtoken = new Token(tken);
        ref.child(mUID).setValue(mtoken);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if(id == R.id.nav_gallery) {
            Intent intento = new Intent(MainActivity.this, RegistroActivity.class);
            startActivity(intento);
        }
        else if(id == R.id.nav_slideshow) {
            Intent intento = new Intent(MainActivity.this, VehiculoActivity.class);
            startActivity(intento);
        }
        else if(id == R.id.nav_tools) {
            Intent intento = new Intent(MainActivity.this, ActualizarActivity.class);
            startActivity(intento);
        }
        else if(id == R.id.nav_send) {
            Intent intento = new Intent(MainActivity.this, SolicitudActivity.class);
            startActivity(intento);
        }
        else if(id == R.id.agendar) {
            notify=true;
            Intent intento = new Intent(MainActivity.this, InicioActivity.class);
            startActivity(intento);
        }
        else if(id== R.id.nav_home){
            Intent intento = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intento);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
