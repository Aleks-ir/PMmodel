package by.alexeytisserand.pmmodel;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialize.holder.StringHolder;

import by.alexeytisserand.phmodels.R;

public class Cannon extends ToolBar {

    private Toolbar toolbar;
    private AccountHeader header = null;
    private Drawer drawer = null;
    private static final String TAG = Cannon.class.getCanonicalName();


    private final int[] arrayConstansName = {R.string.body_size, R.string.body_density,
            R.string.start_speed, R.string.height, R.string.shot_angle};

    private final IProfile castiron = new ProfileDrawerItem().withIcon(R.drawable.ic_castiron).withName(R.string.name_1);
    private final IProfile lead = new ProfileDrawerItem().withIcon(R.drawable.ic_lead).withName(R.string.name_2);
    private final IProfile stone = new ProfileDrawerItem().withIcon(R.drawable.ic_stone).withName(R.string.name_3);
    StringHolder currentNameProfile = castiron.getName();

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initToolbarAndView();
        initHeader();
        initDrawer(savedInstanceState);
        initStartSettings();
    }
    private void initToolbarAndView() {
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        toolbar = new Toolbar(this);
        setSupportActionBar(toolbar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mainLayout.addView(toolbar);
        mainLayout.addView(new CannonView(this));
        setContentView(mainLayout);

    }
    private void initHeader(){
        header = new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withHeaderBackground(R.drawable.header)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        changeProfile(profile);
                        return false;
                    }
                })
                .withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                        changeProfile(profile);
                        return false;
                    }

                    @Override
                    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
                        showInfoAboutMaterial(profile);
                        return false;
                    }
                })
                .addProfiles(castiron)
                .addProfiles(lead)
                .addProfiles(stone)
                .withProfileImagesClickable(true)
                .build();
    }
    @SuppressLint("ResourceAsColor")
    private void initDrawer(Bundle savedInstanceState){
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withAccountHeader(header)
                .addDrawerItems(

                        new PrimaryDrawerItem().withName(R.string.about_flight).withIcon(R.drawable.ic_info).withIdentifier(AppConstants.ID_ABOUT_FLIGHT).withSelectable(false),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.body_size).withIcon(R.drawable.ic_size).withIdentifier(AppConstants.ID_BODY_SIZE).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.body_density).withIcon(R.drawable.ic_density).withIdentifier(AppConstants.ID_BODY_DENSITY).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.start_speed).withIcon(R.drawable.ic_speed).withIdentifier(AppConstants.ID_START_SPEED).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.height).withIcon(R.drawable.ic_height).withIdentifier(AppConstants.ID_HEIGHT).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.shot_angle).withIcon(R.drawable.ic_angle).withIdentifier(AppConstants.ID_SHOT_ANGLE).withSelectable(false),
                        new DividerDrawerItem()
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        if (drawerItem != null) {
                            int id = (int) drawerItem.getIdentifier();
                            switch (id) {
                                case AppConstants.ID_ABOUT_FLIGHT:
                                    infoDialog();
                                    return false;
                                case AppConstants.ID_BODY_SIZE:
                                    customDialogSingleData(id);
                                    return false;
                                case AppConstants.ID_BODY_DENSITY:
                                    customDialogSingleData(id);
                                    return false;
                                case AppConstants.ID_START_SPEED:
                                    customDialogSingleData(id);
                                    return false;
                                case AppConstants.ID_HEIGHT:
                                    customDialogSingleData(id);
                                    return false;
                                case AppConstants.ID_SHOT_ANGLE:
                                    customDialogSingleData(id);
                                    return false;
                                default:
                                    Log.e(TAG, "MISSING ACTION: " + drawerItem.getIdentifier());
                            }
                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(false)
                .withShowDrawerUntilDraggedOpened(false)
                .withCloseOnClick(false)
                .withDrawerWidthDp(250)
                .build();
    }
    public void showInfoAboutMaterial(IProfile profile){
        if (castiron == profile) {
            Toast.makeText(this, getString(R.string.info_density, (int)AppConstants.D_BODY_D_CASTICON), Toast.LENGTH_SHORT).show();
        } else if (lead == profile) {
            Toast.makeText(this,  getString(R.string.info_density, (int)AppConstants.D_BODY_D_LEAD), Toast.LENGTH_SHORT).show();
        } else if (stone == profile) {
            Toast.makeText(this,  getString(R.string.info_density, (int)AppConstants.D_BODY_D_STONE), Toast.LENGTH_SHORT).show();
        }
    }

    public void changeProfile(IProfile profile){
        currentNameProfile = profile.getName();
        if (profile == castiron) {
            changeSettings(AppConstants.D_BODY_D_CASTICON, AppConstants.D_TYPE_CASTICON);
        }else
        if (profile == lead) {
            changeSettings(AppConstants.D_BODY_D_LEAD, AppConstants.D_TYPE_LEAD);
        }else
        if (profile == stone) {
            changeSettings(AppConstants.D_BODY_D_STONE, AppConstants.D_TYPE_STONE);
        }
    }

    public void changeSettings(double density, int type) {
        TrajectoryFly.bodyDensity = density;
        CannonView.cbType = type;
    }
    public void initStartSettings(){
        TrajectoryFly.bodySize = AppConstants.D_BODY_SIZE;
        TrajectoryFly.bodyDensity = AppConstants.D_BODY_D_CASTICON;
        TrajectoryFly.startSpeed =  AppConstants.D_START_SPEED;
        TrajectoryFly.height = AppConstants.D_HEIGHT;
        CannonView.getAndSetHeightTower();
        TrajectoryFly.shotAngleGr = AppConstants.D_SHOT_ANGLE;
        CannonView.getAndSetCannonFrame();
    }


    public void customDialogSingleData(final int id) {
        final View customView = getLayoutInflater().inflate(R.layout.dialog_single_data, null);
        final EditText etData = (EditText) customView.findViewById(R.id.etData);
        initHind(etData, id);
        new AlertDialog.Builder(this)
                .setView(customView)
                .setTitle(arrayConstansName[id])
                .setPositiveButton(R.string.fire, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        checkEmptyAndFillElement(etData, id);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .create()
                .show();
    }
    public void infoDialog() {
        String message = getString(R.string.info_message, String.valueOf(TrajectoryFly.bodySize),
                String.valueOf(TrajectoryFly.bodyDensity), String.valueOf(TrajectoryFly.startSpeed),
                String.valueOf(TrajectoryFly.height), String.valueOf(TrajectoryFly.shotAngleGr));
        AlertDialog.Builder a_builder = new AlertDialog.Builder(Cannon.this);
        a_builder.setTitle(R.string.info_title);
        a_builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.show();
    }


    private void checkEmptyAndFillElement(EditText etData, int id) {
        if(!etData.getText().toString().equals("")){
            String value = etData.getText().toString();
            if(checkData(id, value)){
                changeSetting(id, value);
            }
        }
    }
    private void initHind(EditText etData, int id){
        switch (id) {
            case AppConstants.ID_BODY_SIZE:
                etData.setHint(R.string.allowed_size);
                break;
            case AppConstants.ID_BODY_DENSITY:
                etData.setHint(R.string.allowed_density);
                break;
            case AppConstants.ID_START_SPEED:
                etData.setHint(R.string.allowed_speed);
                break;
            case AppConstants.ID_HEIGHT: ;
                etData.setHint(R.string.allowed_height);
                break;
            case AppConstants.ID_SHOT_ANGLE:
                etData.setHint(R.string.allowed_angle);
                break;
        }
    }
    private boolean checkData(int id, String value){
        switch (id) {
            case AppConstants.ID_BODY_SIZE:
                if(Double.valueOf(value) >= AppConstants.MIN_BODY_SIZE && Double.valueOf(value) <= AppConstants.MAX_BODY_SIZE) {
                    return true;
                }
                break;
            case AppConstants.ID_BODY_DENSITY:
                if(Double.valueOf(value) >= AppConstants.MIN_BODY_DENSITY && Double.valueOf(value) <= AppConstants.MAX_BODY_DENSITY) {
                    return true;
                }
                break;
            case AppConstants.ID_START_SPEED:
                if(Double.valueOf(value) >= AppConstants.MIN_START_SPEED && Double.valueOf(value) <= AppConstants.MAX_START_SPEED) {
                    return true;
                }
                break;
            case AppConstants.ID_HEIGHT: ;
                if(Double.valueOf(value) >= AppConstants.MIN_HEIGHT && Double.valueOf(value) <= AppConstants.MAX_HEIGHT) {
                    return true;
                }
                break;
            case AppConstants.ID_SHOT_ANGLE:
                if(Double.valueOf(value) >= AppConstants.MIN_SHOT_ANGLE && Double.valueOf(value) <= AppConstants.MAX_SHOT_ANGLE) {
                    return true;
                }
                break;
        }
        return false;
    }

    private void changeSetting(int id, String value){
        switch (id) {
            case AppConstants.ID_BODY_SIZE:
                TrajectoryFly.bodySize = Double.valueOf(value);
                break;
            case AppConstants.ID_BODY_DENSITY:
                TrajectoryFly.bodyDensity = Double.valueOf(value);
                break;
            case AppConstants.ID_START_SPEED:
                TrajectoryFly.startSpeed =  Double.valueOf(value);
                break;
            case AppConstants.ID_HEIGHT:
                if(CannonView.isEditHeight){
                    TrajectoryFly.height =  Double.valueOf(value);
                    CannonView.getAndSetHeightTower();
                }

                break;
            case AppConstants.ID_SHOT_ANGLE:
                if(CannonView.isEditAngle){
                    TrajectoryFly.shotAngleGr =  Double.valueOf(value);
                    CannonView.getAndSetCannonFrame();
                }

                break;
        }
    }



    @Override
    public void onBackPressed() {
        if (drawer != null && drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else {
            AppUtilities.tapPromtToExit(this);
        }
    }
}
