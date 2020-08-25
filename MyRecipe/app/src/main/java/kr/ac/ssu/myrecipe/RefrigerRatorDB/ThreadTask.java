package kr.ac.ssu.myrecipe.RefrigerRatorDB;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AppCompatDialog;

import java.util.ArrayList;

public class ThreadTask implements Runnable{
    private ArrayList<RefrigeratorData> mArgument;
    private AppCompatDialog dialog;
    private Context context;
    private RefrigeratorDao dao;
    private int flag;
    public final static int RECIEPT = 0;
    public final static int INITIALIZE = 1;

    public ThreadTask(Context context, RefrigeratorDao dao, AppCompatDialog dialog, int flag) {
        this.context = context;
        this.dialog = dialog;
        this.dao = dao;
        this.flag = flag;
    }

    final public void execute(final ArrayList<RefrigeratorData> arg) {
        // Store the argument
        mArgument = arg;
        // Call onPreExecute
        //onPreExecute();

        // Begin thread work
        Thread thread = new Thread(this);
        thread.start();

        // Wait for the thread work
        try {
            thread.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            onPostExecute();
            return;
        }

        // Call onPostExecute
        onPostExecute();
    }

    @Override
    public void run() {
        doInBackground(mArgument);
    }

    // onPreExecute
    protected void onPreExecute()
    {

    }

    // doInBackground
    protected void doInBackground(ArrayList<RefrigeratorData> arg)
    {
        if(flag == INITIALIZE)
            dao.deleteAll();

        for(int i = 0; i < arg.size(); i++) {
            if(dao.findData(arg.get(i).getName()) == null) {
                dao.insert(arg.get(i));
            }
        }
    };

    // onPostExecute
    protected void onPostExecute()
    {
        dialog.dismiss();
        ((Activity)context).finish();
    }
}
