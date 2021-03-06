package com.example.jamescollerton.internet_fridge;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;

import android.widget.Button;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.Serializable;
import java.util.*;

import android.util.DisplayMetrics;

/**
 *
 * HomeScreen
 *
 * This is the initial HomeScreen class that is ran on startup. It is responsible for holding all
 * of the buttons that are contained on the screen, the menu bar and keeping the screen dimensions.
 *
 */
public class HomeScreen extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    /**
     *
     * These are the custom generated variables. The screenDimensionsMap is used to
     * hold the dimensions of the screen. The dictionaryKeysList holds all of the
     * keys for any of the dictionaries across the whole application. screenCommandClass
     * is used to pass the methods for launching the screens as arguments to the
     * button creators. screenDimensionsList is used to hold all of the percentages we want
     * to use as margins (for example if we want the title 50% of the way down the page).
     *
     */
    private HashMap<String, Integer> screenDimensionsMap = new HashMap<String, Integer>();
    private DictionaryKeysList dictionaryKeysList = new DictionaryKeysList();
    private ScreenDimensionsList screenDimensionsList = new ScreenDimensionsList();
    private ScreenCommandClasses screenCommandClasses = new ScreenCommandClasses();
    private Boolean userCreated;

    /**
     *
     * Loads up its latest instance (presumably from when its been sleeping).
     * Then sets the content view and the toolbar from the XML. Sets all of the
     * setting buttons on the toolbar and the floating action buttons at the bottom
     * of the page. Then initialises all of the buttons on the page. If the user has been
     * detected to already have created an account then we do not launch the create user screen,
     * otherwise that screen is launched.
     *
     * @param savedInstanceState
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Creating the top toolbar.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setUserCreated();
        setScreenDimensions();
        setSupportActionBar(toolbar);
        initialiseHomeScreenFloatingActionButtons();
        initialiseHomeScreenButtons();

//        FOR TESTING THE API CONNECTION.
//        MyFridgeAPIConnection test = new MyFridgeAPIConnection(this);
//        test.execute("https://192.168.1.69:8080/api/MyFridge");

        // FOR TESTING THE CREATE USER SCREEN.
        if(!userCreated) { launchCreateUserScreen(); }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     *
     * This initialises all of the floating action buttons at the bottom of the page. It creates
     * the buttons and then passes to the custom floating action button class in order to format
     * them.
     *
     */
    public void initialiseHomeScreenFloatingActionButtons() {

        FloatingActionButton emailFloatingActionButton =
                (FloatingActionButton) findViewById(R.id.homeScreenEmailFloatingActionButton);
        FloatingActionButton refreshFloatingActionButton =
                (FloatingActionButton) findViewById(R.id.homeScreenRefreshFloatingActionButton);

        HomeScreenFloatingActionButton homeScreenEmailFloatingActionButton =
                new HomeScreenFloatingActionButton(emailFloatingActionButton, this);
        HomeScreenFloatingActionButton homeScreenRefreshFloatingActionButton =
                new HomeScreenFloatingActionButton(refreshFloatingActionButton, this);

    }

    /**
     *
     * This creates the option menu in the toolbar which can be dropped down to change
     * settings and to access other options.
     *
     * The inflater part inflates the menu; this adds items to the action bar if it is present.
     *
     * @return true
     *
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    /**
     *
     * Handle action bar item clicks here. The action bar will
     * automatically handle clicks on the Home/Up button, so long
     * as you specify a parent activity in AndroidManifest.xml.
     *
     * The bottom part is a noinspection SimplifiableIfStatement
     *
     * @return selectedItem (An item that has been selected from the options menu.)
     *
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            launchSettingsScreen();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     *
     * This function is used for launching the settings screen for the app. It adds
     * the screen dimensions map to the intent so the settings screen knows how big
     * it is.
     *
     */
    public void launchSettingsScreen(){

        Intent settingsIntent = new Intent(this, SettingsScreen.class);
        settingsIntent.putExtra(dictionaryKeysList.screenDimensionsMapIntentKey, screenDimensionsMap);
        startActivity(settingsIntent);

    }

    /**
     *
     * This function is used for launching the user's My Fridge screen for the app.
     *
     */
    public void launchUserFridgeScreen(){

        Intent userFridgeIntent = new Intent(this, UserFridgeScreen.class);
        startActivity(userFridgeIntent);

    }

    /**
     *
     * This function is used for launching the create user screen if a create user screen is needed.
     * The screen dimensions are added to the intent so they can be retrieved from the other side
     * to relatively position the components of the screen.
     *
     */
    private void launchCreateUserScreen(){

        userCreated = true;

        Intent createUserIntent = new Intent(this, CreateUserScreen.class);
        createUserIntent.putExtra(dictionaryKeysList.screenDimensionsMapIntentKey, screenDimensionsMap);
        startActivity(createUserIntent);

    }

    /**
     *
     * This is used to set the user created flag. If a user has already been created then the flag
     * needs to be set to true so that we don't launch the create user screen again by accident. The
     * user created flag is taken from the intent. If this is the first time the app has been launched
     * then this will return null. Otherwise it will return a usable value.
     *
     */
    private void setUserCreated(){

        Intent intent = getIntent();
        Boolean temporaryUserCreated = (Boolean)intent.getSerializableExtra(dictionaryKeysList.userCreatedIntentKey);
        if(temporaryUserCreated == null){ userCreated = false; }
        else{ userCreated = temporaryUserCreated; }

    }

    /**
     *
     * This is used to set the screen dimensions array. It puts them into a dictionary which
     * can be accessed publicly.
     *
     */
    private void setScreenDimensions(){

        DisplayMetrics screenMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(screenMetrics);
        int screenWidth = screenMetrics.widthPixels;
        int screenHeight = screenMetrics.heightPixels;
        screenDimensionsMap.put(dictionaryKeysList.screenDimensionsMapScreenWidth, screenWidth);
        screenDimensionsMap.put(dictionaryKeysList.screenDimensionsMapScreenHeight, screenHeight);

    }

    /**
     *
     * This is used to initialise each of the four buttons on the home screen: Scan, Recipes,
     * Deals and Friends. It finds the dimensions of the screen, then the four buttons from
     * the xml file. Next it gets a command from the screenCommandClasses file in order for
     * it to be passed as a parameter to tell the button what to do. Next it finds all of the fonts
     * and icons from the values file, and the margins from the screenDimensionList class, which
     * it assigns to some maps in order for them to be iterated through in the button class and
     * set the margins.
     *
     */
    private void initialiseHomeScreenButtons(){

        Map<String, Integer> homeScreenDimensionsMap = this.screenDimensionsMap;

        Button scanButton = (Button) findViewById(R.id.homeScreenButtonScanID);
        Button dealsButton = (Button) findViewById(R.id.homeScreenButtonDealsID);
        Button myFridgeButton = (Button) findViewById(R.id.homeScreenButtonMyFridgeID);
        Button friendsButton = (Button) findViewById(R.id.homeScreenButtonFriendsID);
        Button recipesButton = (Button) findViewById(R.id.homeScreenButtonRecipesID);

        ScreenCommandClasses.UserFridgeScreenCommand scanButtonAction = screenCommandClasses.new UserFridgeScreenCommand(this);
        ScreenCommandClasses.UserFridgeScreenCommand dealsButtonAction = screenCommandClasses.new UserFridgeScreenCommand(this);
        ScreenCommandClasses.UserFridgeScreenCommand myFridgeButtonAction = screenCommandClasses.new UserFridgeScreenCommand(this);
        ScreenCommandClasses.UserFridgeScreenCommand friendsButtonAction = screenCommandClasses.new UserFridgeScreenCommand(this);
        ScreenCommandClasses.UserFridgeScreenCommand recipesButtonAction = screenCommandClasses.new UserFridgeScreenCommand(this);

        String scanButtonFontLocation = this.getResources().getString(R.string.appDefaultFontLocation);
        String dealsButtonFontLocation = this.getResources().getString(R.string.appDefaultFontLocation);
        String myFridgeButtonFontLocation = this.getResources().getString(R.string.appDefaultFontLocation);
        String friendsButtonFontLocation = this.getResources().getString(R.string.appDefaultFontLocation);
        String recipesButtonFontLocation = this.getResources().getString(R.string.appDefaultFontLocation);

        String scanButtonIconFileLocation = this.getResources().getString(R.string.homeScreenButtonScanIconLocation);
        String dealsButtonIconFileLocation = this.getResources().getString(R.string.homeScreenButtonDealsIconLocation);
        String myFridgeButtonIconFileLocation = this.getResources().getString(R.string.homeScreenButtonMyFridgeIconLocation);
        String friendsButtonIconFileLocation = this.getResources().getString(R.string.homeScreenButtonFriendsIconLocation);
        String recipesButtonIconFileLocation = this.getResources().getString(R.string.homeScreenButtonRecipesIconLocation);

        Map<String, Double> scanButtonMargins =  new HashMap<String, Double>();
        Map<String, Double> dealsButtonMargins =  new HashMap<String, Double>();
        Map<String, Double> myFridgeButtonMargins =  new HashMap<String, Double>();
        Map<String, Double> friendsButtonMargins =  new HashMap<String, Double>();
        Map<String, Double> recipesButtonMargins =  new HashMap<String, Double>();

        scanButtonMargins.put(dictionaryKeysList.buttonBottomMarginPercentage, screenDimensionsList.homeScreenButtonScanBottomPercentageMargin);
        scanButtonMargins.put(dictionaryKeysList.buttonTopMarginPercentage, screenDimensionsList.homeScreenButtonScanTopPercentageMargin);
        scanButtonMargins.put(dictionaryKeysList.buttonLeftMarginPercentage, screenDimensionsList.homeScreenButtonScanLeftPercentageMargin);
        scanButtonMargins.put(dictionaryKeysList.buttonRightMarginPercentage, screenDimensionsList.homeScreenButtonScanRightPercentageMargin);

        dealsButtonMargins.put(dictionaryKeysList.buttonBottomMarginPercentage, screenDimensionsList.homeScreenButtonDealsBottomPercentageMargin);
        dealsButtonMargins.put(dictionaryKeysList.buttonTopMarginPercentage, screenDimensionsList.homeScreenButtonDealsTopPercentageMargin);
        dealsButtonMargins.put(dictionaryKeysList.buttonLeftMarginPercentage, screenDimensionsList.homeScreenButtonDealsLeftPercentageMargin);
        dealsButtonMargins.put(dictionaryKeysList.buttonRightMarginPercentage, screenDimensionsList.homeScreenButtonDealsRightPercentageMargin);

        myFridgeButtonMargins.put(dictionaryKeysList.buttonBottomMarginPercentage, screenDimensionsList.homeScreenButtonMyFridgeBottomPercentageMargin);
        myFridgeButtonMargins.put(dictionaryKeysList.buttonTopMarginPercentage, screenDimensionsList.homeScreenButtonMyFridgeTopPercentageMargin);
        myFridgeButtonMargins.put(dictionaryKeysList.buttonLeftMarginPercentage, screenDimensionsList.homeScreenButtonMyFridgeLeftPercentageMargin);
        myFridgeButtonMargins.put(dictionaryKeysList.buttonRightMarginPercentage, screenDimensionsList.homeScreenButtonMyFridgeRightPercentageMargin);

        friendsButtonMargins.put(dictionaryKeysList.buttonBottomMarginPercentage, screenDimensionsList.homeScreenButtonFriendsBottomPercentageMargin);
        friendsButtonMargins.put(dictionaryKeysList.buttonTopMarginPercentage, screenDimensionsList.homeScreenButtonFriendsTopPercentageMargin);
        friendsButtonMargins.put(dictionaryKeysList.buttonLeftMarginPercentage, screenDimensionsList.homeScreenButtonFriendsLeftPercentageMargin);
        friendsButtonMargins.put(dictionaryKeysList.buttonRightMarginPercentage, screenDimensionsList.homeScreenButtonFriendsRightPercentageMargin);

        recipesButtonMargins.put(dictionaryKeysList.buttonBottomMarginPercentage, screenDimensionsList.homeScreenButtonRecipesBottomPercentageMargin);
        recipesButtonMargins.put(dictionaryKeysList.buttonTopMarginPercentage, screenDimensionsList.homeScreenButtonRecipesTopPercentageMargin);
        recipesButtonMargins.put(dictionaryKeysList.buttonLeftMarginPercentage, screenDimensionsList.homeScreenButtonRecipesLeftPercentageMargin);
        recipesButtonMargins.put(dictionaryKeysList.buttonRightMarginPercentage, screenDimensionsList.homeScreenButtonRecipesRightPercentageMargin);

        MyFridgeButton homeScreenButtonScan = new MyFridgeButton(scanButton, scanButtonAction, scanButtonFontLocation, scanButtonIconFileLocation, homeScreenDimensionsMap, scanButtonMargins, this);
        MyFridgeButton homeScreenButtonRecipes = new MyFridgeButton(recipesButton, recipesButtonAction, recipesButtonFontLocation, recipesButtonIconFileLocation, homeScreenDimensionsMap, recipesButtonMargins, this);
        MyFridgeButton homeScreenButtonDeals = new MyFridgeButton(dealsButton, dealsButtonAction, dealsButtonFontLocation, dealsButtonIconFileLocation, homeScreenDimensionsMap, dealsButtonMargins, this);
        MyFridgeButton homeScreenButtonMyFridge = new MyFridgeButton(myFridgeButton, myFridgeButtonAction, myFridgeButtonFontLocation, myFridgeButtonIconFileLocation, homeScreenDimensionsMap, myFridgeButtonMargins, this);
        MyFridgeButton homeScreenButtonFriends = new MyFridgeButton(friendsButton, friendsButtonAction, friendsButtonFontLocation, friendsButtonIconFileLocation, homeScreenDimensionsMap, friendsButtonMargins, this);

    }

//  ------------------------------------------------------------------------------------------------

//  ------------------------------------------------------------------------------------------------
//  Google API Automatically Generated Functions.

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,       // TODO: choose an action type.
                "HomeScreen Page",      // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.jamescollerton.internet_fridge/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,       // TODO: choose an action type.
                "HomeScreen Page",      // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.jamescollerton.internet_fridge/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}

//    ----------------------------------------------------------------------------------------------
