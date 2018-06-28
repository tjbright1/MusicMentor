package com.example.musicmentor.musicmentor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> cricket = new ArrayList<String>();
        cricket.add("Video1: Slowly");
        cricket.add("Video2: Fast");

        List<String> football = new ArrayList<String>();
        football.add("Video1");
        football.add("Video2");

        List<String> basketball = new ArrayList<String>();
        basketball.add("Video1");
        basketball.add("Video2");
        basketball.add("Video3");

        List<String> testtask = new ArrayList<String>();
        testtask.add("VIDDD");

        expandableListDetail.put("Task1: Play C major Scale", cricket);
        expandableListDetail.put("Task2: Play Etude", football);
        expandableListDetail.put("Task3: Play Lip Slurs", basketball);
        return expandableListDetail;
    }
}