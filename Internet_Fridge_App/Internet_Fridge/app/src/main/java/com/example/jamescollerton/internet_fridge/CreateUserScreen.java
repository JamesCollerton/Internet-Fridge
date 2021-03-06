package com.example.jamescollerton.internet_fridge;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 *
 * CreateUserScreen
 *
 * This class is used to hold and control the functionality and appearance of the create user
 * screen that is launched at the start of the app when it is detected that no user profile on
 * that device exists.
 *
 */
public class CreateUserScreen extends AppCompatActivity {

    /**
     *
     * The screen dimensions map is used to store how big the device screen is so that components
     * can be dynamically spaced on the page. The dictionaryKeysList is used to hold all of the
     * dictionary keys used across the application. The screen command class is used so we can pass
     * open screen commands as arguments in functions. Finally the screen dimensions list is a list
     * of all of the percentage borders for the app (for example if we want the title 50% of the
     * way down the screen, that will be stored here).
     *
     */
    private HashMap<String, Integer> screenDimensionsMap;
    private DictionaryKeysList dictionaryKeysList = new DictionaryKeysList();
    private ScreenCommandClasses screenCommandClasses = new ScreenCommandClasses();
    private ScreenDimensionsList screenDimensionsList = new ScreenDimensionsList();
    private StringConstantsList stringConstantsList = new StringConstantsList();

    /**
     *
     * This class is used to create the user screen. It sets up the toolbar at the top of the
     * page, then it sets the screen dimensions and moves all of the text and buttons around the
     * page so they are properly proportioned.
     *
     * @param savedInstanceState
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Setting up the top toolbar.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setPasswordEditTextFont();
        setScreenDimensions();

        initialiseText();
        initialiseTextViews();
        initialiseButtons();

    }

    /**
     *
     * This is used to set the dimensions of the screen according to the Activity input. It takes
     * them from the extras set on the intent when the intent was started.
     *
     */
    private void setScreenDimensions(){

        Intent intent = getIntent();
        screenDimensionsMap = (HashMap<String, Integer>)intent.getSerializableExtra(dictionaryKeysList.screenDimensionsMapIntentKey);

    }

    /**
     *
     * This function is used to alter the password hint appearance. For some reason by default the
     * password hint is set to some weird font, this is used to undo that.
     *
     */
    private void setPasswordEditTextFont(){

        EditText password = (EditText) findViewById(R.id.createUserScreenPasswordTextFieldID);
        password.setTypeface(Typeface.DEFAULT);
        password.setTransformationMethod(new PasswordTransformationMethod());

    }

    /**
     *
     * This is used to initialise the positioning of the text on screen. It gets the two text views
     * from the xml, then sets the text view margins according to top and side positioning percentages
     * from the screenDimensionsList class.
     *
     */
    private void initialiseText(){

        TextView helloTextView = (TextView) findViewById(R.id.createUserScreenTextViewHelloID);
        TextView descriptionTextView = (TextView) findViewById(R.id.createUserScreenTextViewDescriptionID);

        setTextViewMargins(helloTextView,
                           screenDimensionsList.createUserScreenTextViewHelloTopPercentageMargin,
                           screenDimensionsList.createUserScreenTextViewSidePercentageMargin);
        setTextViewMargins(descriptionTextView,
                           screenDimensionsList.createUserScreenTextViewDescriptionTopPercentageMargin,
                           screenDimensionsList.createUserScreenTextViewSidePercentageMargin);

    }

    /**
     *
     * This is used to set borders of the text views so that we can space them relatively on the
     * page. It takes in arguments for the percentage from the top we want a variable to be, and
     * the percentage from the side we want it to be, and then uses them along with the screen dimensions
     * to calculate the absolute position.
     *
     * @param editingTextView Whichever text view we want to edit the margins of.
     * @param topPercentageMargin The percentage from the top of the page we would like to make it.
     * @param sidePercentageMargin The percentage from the side of the page we would like to make it.
     *
     * @return The absolute top and side positions to be used to set other elements relative to.
     *
     */
    private HashMap<String, Double> setTextViewMargins(TextView editingTextView, double topPercentageMargin, double sidePercentageMargin){

        CoordinatorLayout.LayoutParams editingTextViewLayoutParams = (CoordinatorLayout.LayoutParams) editingTextView.getLayoutParams();

        double topEditTextFieldTopMargin = topPercentageMargin * (double) screenDimensionsMap.get(dictionaryKeysList.screenDimensionsMapScreenHeight);

        double topEditTextFieldSideMargin = sidePercentageMargin * (double) screenDimensionsMap.get(dictionaryKeysList.screenDimensionsMapScreenWidth);

        editingTextViewLayoutParams.setMargins((int) topEditTextFieldSideMargin, (int) topEditTextFieldTopMargin, (int) topEditTextFieldSideMargin, 0);

        editingTextView.setLayoutParams(editingTextViewLayoutParams);

        HashMap<String, Double> editTextFieldPositions = new HashMap<>();
        editTextFieldPositions.put(dictionaryKeysList.createUserScreenEditTextFieldTopMargin, topEditTextFieldTopMargin);
        editTextFieldPositions.put(dictionaryKeysList.createUserScreenEditTextFieldSideMargin, topEditTextFieldSideMargin);

        return (editTextFieldPositions);

    }

    /**
     *
     * This is used to initialise the positioning of the editable text on screen. There are three
     * editable text views. The email address, then the username and the password. The top field is
     * set a certain percentage of the way down the screen, and then the other fields are spaced
     * equidistantly from there.
     *
     * In this function all of the text fields are taken from the page, and all but the top one
     * are added to a HashMap. The top text field and the HashMap are then passed to the next function.
     *
     */
    private void initialiseTextViews(){

        EditText emailEditText = (EditText) findViewById(R.id.createUserScreenEmailTextFieldID);
        EditText usernameEditText = (EditText) findViewById(R.id.createUserScreenUsernameTextFieldID);
        EditText passwordEditText = (EditText) findViewById(R.id.createUserScreenPasswordTextFieldID);

        Map<String, EditText> editTextFields = new HashMap<>();
        editTextFields.put(dictionaryKeysList.createUserScreenUsernameEditTextFieldKey, usernameEditText);
        editTextFields.put(dictionaryKeysList.createUserScreenPasswordEditTextFieldKey, passwordEditText);

        setEditTextFieldMargins(emailEditText, editTextFields);

    }

    /**
     *
     * This function spaces all of the components on the page. It gets the first text field and then
     * sets that on the page. Then for the remaining text fields it spaces them equally after the
     * top one.
     *
     * It reads the percentage from the top and the side that we want the top text field in the
     * screens dimensions list, then uses those and the screen dimensions list to find the absolute
     * positions of the components. Then it loops through the list of remaining text fields and spaces
     * them equidistantly from the top text field.
     *
     * @param topEditTextField
     * @param remainingEditTextFields
     */
    private void setEditTextFieldMargins(EditText topEditTextField, Map<String, EditText> remainingEditTextFields){

        HashMap<String, Double> editTextFieldPositions = setTextViewMargins(topEditTextField,
                                                         screenDimensionsList.createUserScreenEditTextEmailTopPercentageMargin,
                                                         screenDimensionsList.createUserScreenEditTextEmailSidePercentageMargin);

        double topEditTextFieldTopMargin = editTextFieldPositions.get(dictionaryKeysList.createUserScreenEditTextFieldTopMargin);
        double topEditTextFieldSideMargin = editTextFieldPositions.get(dictionaryKeysList.createUserScreenEditTextFieldSideMargin);

        int i = 0;

        for (Map.Entry<String, EditText> editTextField: remainingEditTextFields.entrySet()) {

            CoordinatorLayout.LayoutParams remainingEditTextFieldParams = (CoordinatorLayout.LayoutParams) editTextField.getValue().getLayoutParams();

            double editTextFieldTopMargin = topEditTextFieldTopMargin + (++i * screenDimensionsList.createUserScreenScreenEditTextLineSize);

            remainingEditTextFieldParams.setMargins((int) topEditTextFieldSideMargin, (int) editTextFieldTopMargin, (int) topEditTextFieldSideMargin, 0);

            editTextField.getValue().setLayoutParams(remainingEditTextFieldParams);

        }

    }

    /**
     *
     * This is used to initialise the buttons on screen. It takes the actual button object, the
     * action that we want to execute (opening a screen from the screen command classes), the font,
     * the icon file location is null as we don't want one, then sets all of the margins using the
     * screen dimensions list class. Finally all of these are instantiated using the MyFridgeButton
     * class.
     *
     */
    private void initialiseButtons(){

        Button signUpButton = (Button) findViewById(R.id.createUserScreenSignUpButton);

        ScreenCommandClasses.HomeScreenCommand signUpButtonAction = screenCommandClasses.new HomeScreenCommand(this);

        String signUpButtonFontLocation = this.getResources().getString(R.string.appDefaultFontLocation);

        String signUpButtonIconFileLocation = null;

        Map<String, Double> signUpButtonMargins =  new HashMap<String, Double>();

        signUpButtonMargins.put(dictionaryKeysList.buttonBottomMarginPercentage, screenDimensionsList.createUserScreenButtonSignUpBottomPercentageMargin);
        signUpButtonMargins.put(dictionaryKeysList.buttonTopMarginPercentage, screenDimensionsList.createUserScreenButtonSignUpTopPercentageMargin);
        signUpButtonMargins.put(dictionaryKeysList.buttonLeftMarginPercentage, screenDimensionsList.createUserScreenButtonSignUpLeftPercentageMargin);
        signUpButtonMargins.put(dictionaryKeysList.buttonRightMarginPercentage, screenDimensionsList.createUserScreenButtonSignUpRightPercentageMargin);

        MyFridgeButton createUserScreenButtonSignUp = new MyFridgeButton(signUpButton, signUpButtonAction, signUpButtonFontLocation, signUpButtonIconFileLocation, screenDimensionsMap, signUpButtonMargins, this);

    }

    /**
     *
     * This is used to launch the home screen when the user presses the sign me up button and
     * the user's details are registered and sent off to the server to be processed.
     *
     */
    public void launchHomeScreen(){

        registerUser();

        Intent homeScreenIntent = new Intent(this, HomeScreen.class);
        homeScreenIntent.putExtra(dictionaryKeysList.userCreatedIntentKey, true);
        startActivity(homeScreenIntent);

    }

    /**
     *
     * This is used to register the user on the server side. It takes their email address, their
     * password and their username and sends it over to the API using the https connection. The
     * API connection uses a callback so we can do something with the output.
     *
     */
    public void registerUser(){

        EditText usernameTextField = (EditText)findViewById(R.id.createUserScreenUsernameTextFieldID);
        String username = usernameTextField.getText().toString();

        EditText passwordTextField = (EditText)findViewById(R.id.createUserScreenPasswordTextFieldID);
        String password = passwordTextField.getText().toString();

        EditText emailAddressTextField = (EditText)findViewById(R.id.createUserScreenEmailTextFieldID);
        String emailAddress = emailAddressTextField.getText().toString();

        MyFridgeAPIConnection registerUserAPIConnection = new MyFridgeAPIConnection(this, new MyFridgeAPIConnection.AsyncResponse(){

            @Override
            public void processFinish(String output){

                System.out.println("Output: " + output);

            }

        });

        registerUserAPIConnection.execute(stringConstantsList.createUserScreenRegisterUserURLPrefix + username + "/" + password + "/" + emailAddress);

    }

}
