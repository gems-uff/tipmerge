/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.assignmerge;

/**
 *
 * @author catarinacosta
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StatisticsUtil has statistics utility methods
 * @author Michael
 * @link
 * @since 7/21/12 7:30 PM
 */
public class StatisticsUtil {

    public static double getMean(List<CommitAuthor> commiters) {
        double mean = 0.0;
        if ((commiters != null) && (commiters.size() > 0)) {
            for (CommitAuthor cmt : commiters){
                mean += cmt.getCommits();
            }
            mean /= commiters.size();
        }
        return mean;
    } 
    
    /*    public static double getMean(List<Double> values) {
    double mean = 0.0;
    if ((values != null) && (values.size() > 0)) {
    for (double value : values) {
    mean += value;
    }
    mean /= values.size();
    }
    return mean;
    }*/
    
    public static double getSum(List<Double> values) {
        double sum = 0.0;
        if ((values != null) && (values.size() > 0)) {
            for (double value : values) {
                sum += value;
            }
            //sum /= values.size();
        }
        return sum;
    }
    public static double getStandardDeviation(List<CommitAuthor> committers) {
        double deviation = 0.0;
        if ((committers != null) && (committers.size() > 1)) {
            double mean = getMean(committers);
            for (CommitAuthor cmt : committers){
                double delta = cmt.getCommits()-mean;
                deviation += delta*delta;
            }
            deviation = Math.sqrt(deviation/committers.size());
        }
        return deviation;
    }

    /*    
    public static double getStandardDeviation(List<Double> values) {
        double deviation = 0.0;
            if ((values != null) && (values.size() > 1)) {
            double mean = getMean(values);
            for (double value : values) {
                double delta = value-mean;
                deviation += delta*delta;
            }
            deviation = Math.sqrt(deviation/values.size());
        }
        return deviation;
    }*/

    public static double getMedian(List<Double> values) {
        double median = 0.0;
        if (values != null) {
            int numValues = values.size();
            if (numValues > 0) {
                Collections.sort(values);
                if ((numValues%2) == 0) {
                    median = (values.get((numValues/2)-1)+values.get(numValues/2))/2.0;
                } else {
                    median = values.get(numValues/2);
                }
            }
        }
        return median;
    }

    public static double getMean(double [] values) {
        double mean = 0.0;
        if ((values != null) && (values.length > 0)) {
            for (double value : values) {
                mean += value;
            }
            mean /= values.length;
        }
        return mean;
    }

    public static double getStandardDeviation(double [] values) {
        double deviation = 0.0;
        if ((values != null) && (values.length > 1)) {
            double mean = getMean(values);
            for (double value : values) {
                double delta = value-mean;
                deviation += delta*delta;
            }
            deviation = Math.sqrt(deviation/values.length);
        }
        return deviation;
    }

    public static double getMedian(double [] values) {
        double median = 0.0;
        if (values != null) {
            int numValues = values.length;
            if (numValues > 0) {
                Arrays.sort(values);
                if ((numValues%2) == 0) {
                    median = (values[(numValues/2)-1]+values[numValues/2])/2.0;
                } else {
                    median = values[numValues/2];
                }
            }
        }
        return median;
    }
}
