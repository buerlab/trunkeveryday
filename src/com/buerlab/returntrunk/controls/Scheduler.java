package com.buerlab.returntrunk.controls;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhongqiling on 14-7-15.
 */
public class Scheduler implements Runnable{


    private boolean mLoop = true;
    private List<ScheduleJob> mJobs = null;

    public Scheduler(){
        mJobs = new ArrayList<ScheduleJob>();
    }

    public void addJobs(ScheduleJob job){
        mJobs.add(job);
    }

    public void run(){
        while (mLoop){
            try{
                Thread.sleep(60*1000);

                for(ScheduleJob job : mJobs)
                    job.execute();

            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
    }

    private void overDueBillsCheck(){

    }

}
