package bs23.com.calculatorcloneagain;

import android.animation.StateListAnimator;
import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;

import org.nfunk.jep.JEP;

import java.util.ArrayList;
import java.util.Stack;

import codetail.graphics.drawables.DrawableHotspotTouch;
import codetail.graphics.drawables.LollipopDrawable;
import codetail.graphics.drawables.LollipopDrawablesCompat;

public class MainActivity extends AppCompatActivity {

    private String[] buttonsText={"1","2","3","4","5","6","7","8","9",".","0"};
    ArrayList<Button> buttons=new ArrayList<Button>();
    TextView rawTextView,resultTextView;
    Runnable pressed,notpressed;
    Stack<String> scientificOperator=new Stack<String>();
    private float x1,x2;
    MyDbHelper myDbHelper;
    HistoryListAdapter historyListAdapter;
    ArrayList<History> histories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M)
        {
            setContentView(R.layout.activity_main);
        }
        else {
            setContentView(R.layout.activity_main);
        }


        setButtons();
        setListeners();

        getSupportActionBar().hide();



/*
        stackTest();
*/

/*        RippleView rippleView=(RippleView) findViewById(R.id.rippling);
        rippleView.animateRipple(100,100);
        rippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                Toast.makeText(getApplicationContext(),"Hoiche",Toast.LENGTH_LONG).show();
            }
        });*/

        listInitialization();

    }

    public void listInitialization()
    {

        myDbHelper=new MyDbHelper(this);
        final ArrayList<History> histories=myDbHelper.chexk();
        ListView listView=(ListView) findViewById(R.id.historyList);
        historyListAdapter=new HistoryListAdapter(this,histories,rawTextView,resultTextView);
        listView.setAdapter(historyListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                rawTextView.setText(histories.get(position).getEqaution());
                resultTextView.setText(histories.get(position).getResult());
            }
        });
    }

    public void entryUpdate(String equation,String result)
    {
        myDbHelper.update(equation, result);
        histories=myDbHelper.chexk();
        historyListAdapter.changeData(histories);
        historyListAdapter.notifyDataSetChanged();
        ListView listView=(ListView) findViewById(R.id.historyList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                rawTextView.setText(histories.get(position).getEqaution());
                resultTextView.setText(histories.get(position).getResult());
            }
        });
    }

    private void setListeners() {
        for (int i = 0; i < 11; i++) {
            buttons.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    numberListerner((Button) v);
                    v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.elevation_selector));
                }
            });
        }

        buttons.get(11).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                equalListener();
            }
        });

        for (int i = 12; i < 16; i++) {
            buttons.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    operatorListener((Button) v);
                }
            });
        }

        buttons.get(16).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delClickListener();
            }
        });

        buttons.get(16).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
                    delOnLongClickListener();
                } else {
                    PercentRelativeLayout percentRelativeLayout = (PercentRelativeLayout) findViewById(R.id.numberLayout);
/*                    notpressed=new Runnable() {
                        @Override
                        public void run() {
                            percentRelativeLayout.setPressed(false);
                        }
                    };
                    pressed=new Runnable() {
                        @Override
                        public void run() {
                            percentRelativeLayout.setPressed(true);
                            percentRelativeLayout.postOnAnimation(notpressed);
                        }
                    };
                    pressed.run();
                    Toast.makeText(getApplicationContext(),"Ripple",Toast.LENGTH_LONG).show();*/
                    RippleView rippleView = (RippleView) findViewById(R.id.rippling);
                    rippleView.animateRipple(percentRelativeLayout.getX() + percentRelativeLayout.getWidth(), percentRelativeLayout.getY() + (percentRelativeLayout.getHeight() / 2));
                    delOnLongClickListenerAlternative();
                }
                return true;
            }
        });

        for (int i = 17; i < 22; i++) {
            buttons.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button button=(Button) v;
                    scientificButtonsListener(((Button) v).getText().toString());
                }
            });
        }

        for (int i = 22; i < 28; i++) {
            buttons.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    numberListerner((Button) v);
                }
            });
        }

        buttons.get(28).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                scientificButtonsListener(((Button) v).getText().toString());/*                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
                drawerLayout.closeDrawer(Gravity.RIGHT);
                Toast.makeText(getApplicationContext(),"JAdkjahfg",Toast.LENGTH_LONG).show();*/
            }
        });

        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            x1 = event.getX();
                            break;
                        case MotionEvent.ACTION_UP:
                            x2 = event.getX();
                            float deltaX = x2 - x1;
                            if (Math.abs(deltaX) > 256) {
                                if (x2 < x1) {
                                    DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
                                    drawerLayout.openDrawer(Gravity.RIGHT);
                                }else if(x1 < x2)
                                {
                                    DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
                                    drawerLayout.openDrawer(Gravity.LEFT);
                                }
                            } else {
                                // consider as something else - a screen tap for example
                            }
                            break;

                    }
                    return false;
                }
            });
        }

    }

    public void setButtons()
    {
        buttons.add((Button) findViewById(R.id.b0));
        buttons.add((Button) findViewById(R.id.b1));
        buttons.add((Button) findViewById(R.id.b2));
        buttons.add((Button) findViewById(R.id.b3));
        buttons.add((Button) findViewById(R.id.b4));
        buttons.add((Button) findViewById(R.id.b5));
        buttons.add((Button) findViewById(R.id.b6));
        buttons.add((Button) findViewById(R.id.b7));
        buttons.add((Button) findViewById(R.id.b8));
        buttons.add((Button) findViewById(R.id.b9));
        buttons.add((Button) findViewById(R.id.bdot));
        buttons.add((Button) findViewById(R.id.bequal));
        buttons.add((Button) findViewById(R.id.bdivition));
        buttons.add((Button) findViewById(R.id.bmultiplication));
        buttons.add((Button) findViewById(R.id.bminus));
        buttons.add((Button) findViewById(R.id.bplus));
        buttons.add((Button) findViewById(R.id.bdel));
        buttons.add((Button) findViewById(R.id.bsin));
        buttons.add((Button) findViewById(R.id.bcos));
        buttons.add((Button) findViewById(R.id.btan));
        buttons.add((Button) findViewById(R.id.bln));
        buttons.add((Button) findViewById(R.id.blog));
        buttons.add((Button) findViewById(R.id.bpow));
        buttons.add((Button) findViewById(R.id.bpi));
        buttons.add((Button) findViewById(R.id.bEE));
        buttons.add((Button) findViewById(R.id.bleftBrack));
        buttons.add((Button) findViewById(R.id.bRightBrack));
        buttons.add((Button) findViewById(R.id.bfactorial));
        buttons.add((Button) findViewById(R.id.broot));

        for(int i=0;i<buttons.size();i++)
        {
            if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M)
            {
                buttons.get(i).setBackground(getResources().getDrawable(R.drawable.button_selector));
            }
            else {
                buttons.get(i).setBackground(getResources().getDrawable(R.drawable.ripple));
            }
        }

        for(int i=0;i<12;i++)
        {
            buttons.get(i).setTextSize(30);
        }

        for(int i=12;i<17;i++)
        {
            buttons.get(i).setTextSize(20);
        }

        rawTextView=(TextView) findViewById(R.id.rawTextView);
        resultTextView=(TextView) findViewById(R.id.resultTextView);


    }

    public void numberListerner(Button button)
    {
        rawTextView.setText(rawTextView.getText()+button.getText().toString());
        if(evaluate(rawTextView.getText().toString()).equals("2147483647"))
        {
            resultTextView.setText("Out of bounds");
        }
        else {
            resultTextView.setText(evaluate(rawTextView.getText().toString()));
        }
    }

    public void equalListener()
    {
        entryUpdate(rawTextView.getText().toString(),resultTextView.getText().toString());
        rawTextView.setText(resultTextView.getText());
        resultTextView.setText("");
    }

    public void delClickListener()
    {
        if(!rawTextView.getText().toString().equals("")) {
            rawTextView.setText(rawTextView.getText().subSequence(0, rawTextView.getText().length() - 1));
            if(evaluate(rawTextView.getText().toString()).equals("2147483647"))
            {
                resultTextView.setText("Out of bounds");
            }
            else {
                resultTextView.setText(evaluate(rawTextView.getText().toString()));
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void delOnLongClickListener()
    {
        final PercentRelativeLayout percentRelativeLayout=(PercentRelativeLayout) findViewById(R.id.numberLayout);
        final RippleDrawable rippleDrawable=(RippleDrawable) percentRelativeLayout.getBackground();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            rippleDrawable.setColor(ColorStateList.valueOf(getResources().getColor(R.color.dark_blue)));
            rippleDrawable.setHotspot(percentRelativeLayout.getX() + percentRelativeLayout.getWidth(), percentRelativeLayout.getY() + (percentRelativeLayout.getHeight()/2));
            rippleDrawable.setRadius(RippleDrawable.RADIUS_AUTO);
        }
        notpressed=new Runnable() {
            @Override
            public void run() {
                percentRelativeLayout.setPressed(false);
            }
        };

        pressed=new Runnable() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                percentRelativeLayout.setPressed(true);
                percentRelativeLayout.postOnAnimationDelayed(notpressed, 0);
            }
        };
        pressed.run();
        rawTextView.setText("");
        resultTextView.setText("");
    }

    public void delOnLongClickListenerAlternative()
    {
        rawTextView.setText("");
        resultTextView.setText("");
    }


    public void operatorListener(Button button)
    {
        rawTextView.setText(rawTextView.getText() + button.getText().toString());
    }

    public void scientificButtonsListener(String button)
    {
        scientificOperator.push(button);
        if(rawTextView.getText().toString().equals("") || isOperator(rawTextView.getText().toString().charAt(rawTextView.getText().length()-1))) {
            rawTextView.setText(rawTextView.getText() + button+ "(");
        }
        else
        {
            rawTextView.setText(rawTextView.getText() + "*" + button + "(");
        }
    }


    public String evaluate(String expression)
    {

/*        Stack<String> results=new Stack<String>();
        String lastString;
        int i=0;
        while(true) {
            String main = "";
            results.push(main);
            for (; i < expression.length(); i++) {
                if (!isScientificFuction(expression.charAt(i))) {
                    main = main + expression.charAt(i);
                } else {
                    results.pop();
                    results.push(main);
                    break;
                }
            }

            String sci = "";
            for (; i < expression.length(); i++) {
                if (expression.charAt(i) == '(') {
                    break;
                } else {
                    sci = sci + expression.charAt(i);
                }
            }
            scientificOperator.push(sci);
            i++;

            if(i==expression.length())
            {
                break;
            }
        }*/


/*        try {
            return new Evaluator().evaluate(expression);
        } catch (EvaluationException e) {
            e.printStackTrace();
        }*/

        expression=expression.replaceAll(buttons.get(28).getText().toString(),"sqrt");
        expression=expression.replaceAll(buttons.get(23).getText().toString(),"pi");

        JEP jep=new JEP();
        jep.addStandardFunctions();
        jep.addStandardConstants();
        jep.parseExpression(expression);
        if(jep.hasError())
        {
            return "ERROR";
        }
        if((jep.getValue()%1.0)!=0.0) {
            return jep.getValue() + "";
        }else
        {
            return (int) jep.getValue()+"";
        }

/*
        return "ERROR";
*/
    }

    public boolean isOperator(char c)
    {
        switch (c)
        {
            case '*':
            case '/':
            case '+':
            case '-':
            case '(':
                return true;
        }

        return false;
    }

    public boolean isScientificFuction(char c)
    {
        switch (c)
        {
            case 's':
            case 'c':
            case 't':
            case 'l':
            case 'r':
                return true;
        }

        return false;
    }
    public void stackTest()
    {
        Stack<String> test=new Stack<String>();
        String a="ad";
        test.push(a);
        a="baad";
        Toast.makeText(this,test.pop(),Toast.LENGTH_LONG).show();
    }

    public Drawable getDrawable2(int id)
    {
        return LollipopDrawablesCompat.getDrawable(getResources(),id,getTheme());
    }


}
