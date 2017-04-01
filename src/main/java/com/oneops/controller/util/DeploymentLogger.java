package com.oneops.controller.util;

import com.oneops.cms.simple.domain.CmsWorkOrderSimple;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by glall on 3/29/17.
 */
public class DeploymentLogger {



    private ConcurrentHashMap<Long, List<TreeMap<String,String>>> deploymentMetadata = new ConcurrentHashMap<>();

    private static final DeploymentLogger DLOGGER = new DeploymentLogger();

    public static  DeploymentLogger getInstance(){
        return  DLOGGER;
    }
    public ConcurrentHashMap<Long, List<TreeMap<String, String>>> getDeploymentMetadata() {
        return deploymentMetadata;
    }

    public void setDeploymentMetadata(ConcurrentHashMap<Long, List<TreeMap<String, String>>> deploymentMetadata) {
        this.deploymentMetadata = deploymentMetadata;
    }


    public void put(long deploymentId, CmsWorkOrderSimple workOrderSimple ) {
        TreeMap<String, String> tag = new TreeMap<>(workOrderSimple.getSearchTags());
        tag.put("woId",String.valueOf(workOrderSimple.getDpmtRecordId()));

    }

    public void put(CmsWorkOrderSimple workOrderSimple ) {
        TreeMap<String, String> tag = new TreeMap<>(workOrderSimple.getSearchTags());
        tag.put("rfcId",String.valueOf(workOrderSimple.getRfcId()));
        deploymentMetadata.computeIfPresent(workOrderSimple.getDeploymentId(),(k,v)->{
                    deploymentMetadata.get(k).add(tag);
                    return deploymentMetadata.get(k);
                }
        );
        deploymentMetadata.computeIfAbsent(workOrderSimple.getDeploymentId(),(k)->{
            List<TreeMap<String, String>> v1 = new ArrayList<>(250);
                v1.add(tag);
            return v1;
                }
        );
    }
    /**
     *
     * executionTime
     1.0
     requestDequeTS
     2017-03-30T00:13:48.484
     rfcCount
     1
     iWoCrtTime
     6
     totalTime
     17.046
     closeTime
     16.008
     responseDequeTS
     2017-03-30T00:14:05.493
     rfcAction
     add
     woCrtTime
     6747
     responseEnqueTS
     2017-03-30T00:13:49.485
     requestEnqueTS
     2017-03-30T00:13:48.447
     queueTime
     0.037
     *
     *
     *
     *
     *
     * @param deploymentId
     */
    public void print(long deploymentId) {
        //did|woid|wocrttime|totaltime
        StringBuilder sb = new StringBuilder("\n");
        sb.append("dId").append("|").append("totalTime").append("|").append("woId").append("|").append("rfcId").append("|").append("woCrtTime").append("|").append("queueTime");
        sb.append("\n");
        deploymentMetadata.forEach(
                (k,v)->{
                       v.forEach(deploymentStats->{
                        sb.append(deploymentId).append("|").append(deploymentStats.get("totalTime")).append("|").append(deploymentStats.get("rfcId")).append("|").append(deploymentStats.get("woCrtTime")).append("|").append(deploymentStats.get("queueTime"));
                        sb.append("\n");
                       });
                }
        );

        System.out.println(sb.toString());

    }
}
