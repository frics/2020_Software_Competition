package kr.ac.ssu.billysrecipe.RefrigerRatorDB;

import android.os.AsyncTask;

public class RefrigeratorDelete extends AsyncTask<RefrigeratorData,Void, Void> {
    private RefrigeratorDao dao;

    public RefrigeratorDelete(RefrigeratorDao dao){
        this.dao = dao;
    }
    @Override
    protected Void doInBackground(RefrigeratorData... refrigeratorData) {
        RefrigeratorData result = dao.findData(refrigeratorData[0].getName());
        if(result != null)
            dao.delete(result);
        return null;
    }
}