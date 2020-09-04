package kr.ac.ssu.myrecipe.RefrigerRatorDB;

import java.util.ArrayList;

public class ThreadTask implements Runnable{
    private ArrayList<RefrigeratorData> mArgument;
    private RefrigeratorDao dao;
    private OnTaskCompleted listener;
    private int flag;
    public final static int RECIEPT = 0;
    public final static int INITIALIZE = 1;

    public ThreadTask(RefrigeratorDao dao, OnTaskCompleted listener, int flag) {
        this.listener = listener;
        this.dao = dao;
        this.flag = flag;
    }
    public interface OnTaskCompleted {
        void onTaskCompleted(String str);
        void onTaskFailure(String str);
    }

    final public void execute(final ArrayList<RefrigeratorData> arg) {
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
        if (listener != null)
            listener.onTaskCompleted("Success");
    }
}
