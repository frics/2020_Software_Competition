package kr.ac.ssu.billysrecipe.ShoppingListDB;

import java.util.ArrayList;

public class ThreadTask2 implements Runnable{
    private ArrayList<ShoppingListData> mArgument;
    private ShoppingListDao dao;
    private OnTaskCompleted listener;

    public ThreadTask2(ShoppingListDao dao, OnTaskCompleted listener) {
        this.listener = listener;
        this.dao = dao;
    }
    public interface OnTaskCompleted {
        void onTaskCompleted(String str);
        void onTaskFailure(String str);
    }

    final public void execute(final ArrayList<ShoppingListData> arg) {
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
    protected void doInBackground(ArrayList<ShoppingListData> arg)
    {
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
